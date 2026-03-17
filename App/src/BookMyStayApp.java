import java.util.*;
import java.util.stream.Collectors;

// Room entity (immutable)
class Room {
    private final String type;
    private final double pricePerNight;
    private final String description;

    public Room(String type, double pricePerNight, String description) {
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Room Type: " + type +
                ", Price: " + pricePerNight +
                ", Description: " + description;
    }
}

// Inventory Service (read-only)
class InventoryService {
    private final Map<String, Integer> availabilityMap;

    public InventoryService(Map<String, Integer> availabilityMap) {
        this.availabilityMap = Collections.unmodifiableMap(availabilityMap);
    }

    public int getAvailableCount(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }
}

// Search Service (core logic)
class SearchService {
    private final InventoryService inventoryService;
    private final List<Room> rooms;

    public SearchService(InventoryService inventoryService, List<Room> rooms) {
        this.inventoryService = inventoryService;
        this.rooms = Collections.unmodifiableList(rooms);
    }

    public List<Room> searchAvailableRooms() {
        return rooms.stream()
                .filter(room -> inventoryService.getAvailableCount(room.getType()) > 0)
                .collect(Collectors.toList());
    }
}

// Guest (actor)
class Guest {
    private final SearchService searchService;

    public Guest(SearchService searchService) {
        this.searchService = searchService;
    }

    public void viewAvailableRooms() {
        List<Room> availableRooms = searchService.searchAvailableRooms();

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            System.out.println("Available Rooms:");
            availableRooms.forEach(System.out::println);
        }
    }
}

// Main class (simulation)
public class BookMyStayApp {
    public static void main(String[] args) {

        // Sample room data
        List<Room> rooms = Arrays.asList(
                new Room("Single", 1000, "Single bed room"),
                new Room("Double", 1800, "Double bed room"),
                new Room("Suite", 3500, "Luxury suite")
        );

        // Availability data (roomType -> count)
        Map<String, Integer> availability = new HashMap<>();
        availability.put("Single", 3);
        availability.put("Double", 0); // unavailable
        availability.put("Suite", 2);

        InventoryService inventoryService = new InventoryService(availability);
        SearchService searchService = new SearchService(inventoryService, rooms);

        Guest guest = new Guest(searchService);

        // Guest performs search
        guest.viewAvailableRooms();
    }
}