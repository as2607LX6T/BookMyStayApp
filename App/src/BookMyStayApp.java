import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

// Reservation (same as before, simplified here)
class Reservation {
    private final String requestId;
    private final String guestName;
    private final String roomType;

    public Reservation(String requestId, String guestName, String roomType) {
        this.requestId = requestId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getRequestId() { return requestId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// Confirmed booking
class ConfirmedReservation {
    private final String requestId;
    private final String guestName;
    private final String roomType;
    private final String roomId;

    public ConfirmedReservation(String requestId, String guestName, String roomType, String roomId) {
        this.requestId = requestId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "CONFIRMED -> " + guestName +
                " | RoomType: " + roomType +
                " | RoomId: " + roomId;
    }
}

// Inventory Service (critical section)
class InventoryService {

    // roomType -> available count
    private final Map<String, AtomicInteger> availability = new ConcurrentHashMap<>();

    // roomType -> allocated room IDs
    private final Map<String, Set<String>> allocatedRooms = new ConcurrentHashMap<>();

    public InventoryService(Map<String, Integer> initialInventory) {
        initialInventory.forEach((type, count) -> {
            availability.put(type, new AtomicInteger(count));
            allocatedRooms.put(type, ConcurrentHashMap.newKeySet());
        });
    }

    // Atomic allocation method (core logic)
    public synchronized Optional<String> allocateRoom(String roomType) {
        AtomicInteger count = availability.get(roomType);

        if (count == null || count.get() <= 0) {
            return Optional.empty();
        }

        // Generate unique room ID
        String roomId = UUID.randomUUID().toString();

        // Record allocation
        allocatedRooms.get(roomType).add(roomId);

        // Decrement inventory
        count.decrementAndGet();

        return Optional.of(roomId);
    }
}

// Booking Request Queue
class BookingRequestQueue {
    private final BlockingQueue<Reservation> queue = new LinkedBlockingQueue<>();

    public void add(Reservation r) {
        queue.offer(r);
    }

    public Reservation take() throws InterruptedException {
        return queue.take(); // waits if empty
    }
}

// Booking Service (consumer/processor)
class BookingService implements Runnable {

    private final BookingRequestQueue queue;
    private final InventoryService inventoryService;

    public BookingService(BookingRequestQueue queue, InventoryService inventoryService) {
        this.queue = queue;
        this.inventoryService = inventoryService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Reservation request = queue.take();

                System.out.println("Processing request: " + request.getRequestId());

                Optional<String> roomIdOpt =
                        inventoryService.allocateRoom(request.getRoomType());

                if (roomIdOpt.isPresent()) {
                    ConfirmedReservation confirmed =
                            new ConfirmedReservation(
                                    request.getRequestId(),
                                    request.getGuestName(),
                                    request.getRoomType(),
                                    roomIdOpt.get()
                            );

                    System.out.println(confirmed);
                } else {
                    System.out.println("FAILED -> No rooms available for "
                            + request.getRoomType());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

// Demo
public class BookMyStayApp {
    public static void main(String[] args) throws InterruptedException {

        // Initial inventory
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);

        InventoryService inventoryService = new InventoryService(inventory);
        BookingRequestQueue queue = new BookingRequestQueue();

        // Start booking processor thread
        Thread worker = new Thread(new BookingService(queue, inventoryService));
        worker.start();

        // Simulate incoming requests
        queue.add(new Reservation("1", "Alice", "Single"));
        queue.add(new Reservation("2", "Bob", "Single"));
        queue.add(new Reservation("3", "Charlie", "Single")); // should fail
        queue.add(new Reservation("4", "David", "Double"));

        Thread.sleep(2000);
        worker.interrupt();
    }
}
