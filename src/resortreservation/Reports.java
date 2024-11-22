package resortreservation;

import java.util.Scanner;

public class Reports {

    private static final Config config = new Config();
    private static final Scanner scanner = new Scanner(System.in);

    public static void generateReports() {
        System.out.println("\n------------------------ Reports ------------------------");
        System.out.println("1. View All Reservations");
        System.out.println("2. View Specific Reservation");
        System.out.println("3. View Available Rooms by Type");
        System.out.println("4. View Customer Reservation History");
        System.out.println("5. Back");
        System.out.println("---------------------------------------------------------");
        System.out.print("Enter choice: ");
        
       
        int choice = getIntInput(scanner);
        
        switch (choice) {
            case 1:
                viewAllReservations();
                break;
            case 2:
                viewSpecificReservation();
                break;
            case 3:
                viewAvailableRoomsByType();
                break;
            case 4:
                viewCustomerReservationHistory();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    
    private static int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();  
        }
        return scanner.nextInt();
    }

  
    private static void viewAllReservations() {
        config.viewRecords(
            "SELECT r.id, c.first_name, c.last_name, ro.room_type, r.start_date, r.end_date, r.status " +
            "FROM Reservations r " +
            "INNER JOIN Customers c ON r.customer_id = c.id " +
            "INNER JOIN Rooms ro ON r.room_id = ro.id",
            new String[]{"Reservation ID", "Customer Name", "Room Type", "Start Date", "End Date", "Status"}, // Column headers to show
            new String[]{"id", "first_name", "last_name", "room_type", "start_date", "end_date", "status"} // Field names returned by the SQL query
        );
    }

    private static void viewSpecificReservation() {
        int reservationId;
      
        do {
            System.out.print("Enter Reservation ID to view: ");
            reservationId = getIntInput(scanner);
        } while (reservationId <= 0);  

        config.viewRecords(
            "SELECT r.id, c.first_name, c.last_name, ro.room_type, r.start_date, r.end_date, r.status FROM Reservations r " +
            "INNER JOIN Customers c ON r.customer_id = c.id " +
            "INNER JOIN Rooms ro ON r.room_id = ro.id WHERE r.id = ?",
            new String[]{"Reservation ID", "Customer Name", "Room Type", "Start Date", "End Date", "Status"},
            new String[]{"id", "first_name", "last_name", "room_type", "start_date", "end_date", "status"},
            reservationId
        );
    }

    private static void viewAvailableRoomsByType() {
        System.out.print("Enter Room Type (e.g., Single, Double, Suite): ");
        String roomType = scanner.next();
        
        
        int availableRoomsCount = config.getCount(
            "SELECT COUNT(*) FROM Rooms ro " +
            "LEFT JOIN Reservations r ON ro.id = r.room_id AND r.status = 'Confirmed' " +
            "WHERE ro.room_type = ? AND r.id IS NULL", roomType);
        
        if (availableRoomsCount > 0) {
            config.viewRecords(
                "SELECT r.id, ro.room_type, ro.room_number FROM Rooms ro " +
                "LEFT JOIN Reservations r ON ro.id = r.room_id AND r.status = 'Confirmed' " +
                "WHERE ro.room_type = ? AND r.id IS NULL",
                new String[]{"Room ID", "Room Type", "Room Number"},
                new String[]{"id", "room_type", "room_number"},
                roomType
            );
        } else {
            System.out.println("No available rooms of the specified type.");
        }
    }

    private static void viewCustomerReservationHistory() {
        int customerId;
       
        do {
            System.out.print("Enter Customer ID to view reservation history: ");
            customerId = getIntInput(scanner);
        } while (customerId <= 0);

        config.viewRecords(
            "SELECT r.id, ro.room_type, r.start_date, r.end_date, r.status FROM Reservations r " +
            "INNER JOIN Rooms ro ON r.room_id = ro.id WHERE r.customer_id = ?",
            new String[]{"Reservation ID", "Room Type", "Start Date", "End Date", "Status"},
            new String[]{"id", "room_type", "start_date", "end_date", "status"},
            customerId
        );
    }
}
