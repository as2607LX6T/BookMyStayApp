import java.util.HashMap;
import java.util.Map;

// RoomInventory manages centralized room availability
class RoomInventory {

    // HashMap stores roomType -> available count
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register a room type with initial availability
    public void registerRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Get current availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability for a room type (positive or negative)
    public void updateAvailability(String roomType, int delta) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + delta);
    }

    // Display current inventory state
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        System.out.println("======================");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
        System.out.println("======================");
    }
}


// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking System with Centralized Inventory");
        System.out.println();

        // Initialize RoomInventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.registerRoomType("Single Room", 5);
        inventory.registerRoomType("Double Room", 3);
        inventory.registerRoomType("Suite Room", 2);

        // Display initial inventory
        inventory.displayInventory();

        // Simulate a booking update
        System.out.println("\nBooking 1 Double Room...");
        inventory.updateAvailability("Double Room", -1);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("Application Terminated.");
    }
}
