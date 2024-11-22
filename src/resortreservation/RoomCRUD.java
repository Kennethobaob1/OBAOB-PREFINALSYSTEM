package resortreservation;

import java.util.Scanner;
import static resortreservation.ResortCRUD.viewResorts;

public class RoomCRUD {

    private static final Config config = new Config();
    private static final Scanner scanner = new Scanner(System.in);

    public static void manageRooms() {
        System.out.println("\n------------------------------Managing Rooms...-----------------------------------");
        System.out.println("1. Add Room");
        System.out.println("2. View Rooms");
        System.out.println("3. Update Room");
        System.out.println("4. Delete Room");
        System.out.println("5. Go back");
        System.out.println("------------------------------------------------------------------------------------");
        int choice = getValidChoice(); 
        
        switch (choice) {
            case 1:
                addRoom();
                break;
            case 2:
                viewRooms();
                break;
            case 3:
                viewRooms();
                updateRoom();
                viewRooms();
                break;
            case 4:
                viewRooms();
                deleteRoom();
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

  
    private static int getValidChoice() {
        int choice;
        while (true) {
            System.out.print("Enter choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 5) {
                    return choice;
                } else {
                    System.out.println("Please enter a valid choice between 1 and 5.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.next(); 
            }
        }
    }

   
    private static int getValidResortId() {
        int resortId;
        while (true) {
            System.out.print("Enter Resort ID: ");
            resortId = scanner.nextInt();
            
            if (config.recordExists("SELECT id FROM Resorts WHERE id = ?", resortId)) {
                return resortId;
            } else {
                System.out.println("Resort ID does not exist. Please enter a valid Resort ID.");
            }
        }
    }

    
    private static double getValidRoomPrice() {
        double roomPrice;
        while (true) {
            System.out.print("Enter Room Price: ");
            if (scanner.hasNextDouble()) {
                roomPrice = scanner.nextDouble();
                if (roomPrice > 0) {
                    return roomPrice;
                } else {
                    System.out.println("Room price must be a positive number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid room price.");
                scanner.next(); // Consume the invalid input
            }
        }
    }

    private static void addRoom() {
        viewResorts(); 
        int resortId = getValidResortId(); 
        System.out.print("Enter Room Type: ");
        String roomType = scanner.next();
        double roomPrice = getValidRoomPrice();
        config.addRecord("INSERT INTO Rooms (resort_id, room_type, price) VALUES (?, ?, ?)", resortId, roomType, roomPrice);
    }

    static void viewRooms() {
        config.viewRecords("SELECT r.id, r.room_type, r.price, res.name FROM Rooms r INNER JOIN Resorts res ON r.resort_id = res.id", 
            new String[]{"ID", "Room Type", "Price", "Resort Name"}, 
            new String[]{"id", "room_type", "price", "name"});
    }

   
    private static int getValidRoomId() {
        int roomId;
        while (true) {
            System.out.print("Enter Room ID to update: ");
            roomId = scanner.nextInt();
            // Check if the Room ID exists
            if (config.recordExists("SELECT id FROM Rooms WHERE id = ?", roomId)) {
                return roomId;
            } else {
                System.out.println("Room ID does not exist. Please enter a valid Room ID.");
            }
        }
    }

    private static void updateRoom() {
        int roomId = getValidRoomId(); 
        System.out.print("Enter new Room Type: ");
        String newRoomType = scanner.next();
        double newPrice = getValidRoomPrice(); 
        config.updateRecord("UPDATE Rooms SET room_type = ?, price = ? WHERE id = ?", newRoomType, newPrice, roomId);
    }

    private static void deleteRoom() {
        int roomToDelete = getValidRoomId();
        config.deleteRecord("DELETE FROM Rooms WHERE id = ?", roomToDelete);
    }
}
