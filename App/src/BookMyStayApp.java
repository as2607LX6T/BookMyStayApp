import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Reservation (immutable request object)
class Reservation {
    private final String requestId;
    private final String guestName;
    private final String roomType;
    private final LocalDateTime requestTime;

    public Reservation(String guestName, String roomType) {
        this.requestId = UUID.randomUUID().toString();
        this.guestName = guestName;
        this.roomType = roomType;
        this.requestTime = LocalDateTime.now();
    }

    public String getRequestId() {
        return requestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "requestId='" + requestId + '\'' +
                ", guestName='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", requestTime=" + requestTime +
                '}';
    }
}

// Booking Request Queue (FIFO + thread-safe)
class BookingRequestQueue {
    private final BlockingQueue<Reservation> queue = new LinkedBlockingQueue<>();

    // Add request (producer)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation); // non-blocking add
        System.out.println("Request added to queue: " + reservation.getRequestId());
    }

    // Peek (for monitoring, no removal)
    public Reservation peek() {
        return queue.peek();
    }

    // Poll (used later by allocation system)
    public Reservation poll() {
        return queue.poll();
    }

    public int size() {
        return queue.size();
    }
}

// Booking Service (intake layer)
class BookingService {
    private final BookingRequestQueue requestQueue;

    public BookingService(BookingRequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    // Guest submits request
    public void submitRequest(String guestName, String roomType) {
        Reservation reservation = new Reservation(guestName, roomType);
        requestQueue.addRequest(reservation);
    }
}

// Demo
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(queue);

        // Simulating multiple guest requests
        bookingService.submitRequest("Alice", "Single");
        bookingService.submitRequest("Bob", "Double");
        bookingService.submitRequest("Charlie", "Suite");

        System.out.println("\nCurrent Queue Size: " + queue.size());

        System.out.println("\nNext request to process (peek):");
        System.out.println(queue.peek());

        // NOTE: No inventory changes happen here
    }
}
