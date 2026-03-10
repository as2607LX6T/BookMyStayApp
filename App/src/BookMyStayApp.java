// Abstract class representing a generic Room
abstract class Room {

    protected String roomType;
    protected double pricePerNight;
    protected int availableRooms;

    public Room(String roomType, double pricePerNight, int availableRooms) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.availableRooms = availableRooms;
    }

    // Abstract method
    public abstract void displayRoomDetails();
}


// Single Room class
class SingleRoom extends Room {

    public SingleRoom(double pricePerNight, int availableRooms) {
        super("Single Room", pricePerNight, availableRooms);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Price per Night: $" + pricePerNight);
        System.out.println("Available Rooms: " + availableRooms);
        System.out.println("---------------------------");
    }
}


// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom(double pricePerNight, int availableRooms) {
        super("Double Room", pricePerNight, availableRooms);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Price per Night: $" + pricePerNight);
        System.out.println("Available Rooms: " + availableRooms);
        System.out.println("---------------------------");
    }
}


// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom(double pricePerNight, int availableRooms) {
        super("Suite Room", pricePerNight, availableRooms);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Price per Night: $" + pricePerNight);
        System.out.println("Available Rooms: " + availableRooms);
        System.out.println("---------------------------");
    }
}


// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking System");
        System.out.println("Available Room Types:");
        System.out.println("===============================");

        // Creating room objects
        SingleRoom single = new SingleRoom(80.0, 5);
        DoubleRoom doubleRoom = new DoubleRoom(120.0, 3);
        SuiteRoom suite = new SuiteRoom(250.0, 2);

        // Display room information
        single.displayRoomDetails();
        doubleRoom.displayRoomDetails();
        suite.displayRoomDetails();

        System.out.println("Application Terminated.");
    }
}
