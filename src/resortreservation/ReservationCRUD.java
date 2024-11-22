package resortreservation;

import java.util.Scanner;
import static resortreservation.CustomerCRUD.viewCustomers;
import static resortreservation.RoomCRUD.viewRooms;

public class ReservationCRUD {

    private static final Config config = new Config();
    private static final Scanner scanner = new Scanner(System.in);

    public static void manageReservations() {
        System.out.println("\n-------------------------------Managing Reservations...-----------------------------------");
        System.out.println("1. Make Reservation");
        System.out.println("2. View Reservations");
        System.out.println("3. Update Reservation");
        System.out.println("4. Cancel Reservation");
        System.out.println("5. Back");
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.print("Enter choice: ");
        
        int choice = getIntInput(scanner);
        
        switch (choice) {
            case 1:
                makeReservation();
                break;
            case 2:
                viewReservations();
                break;
            case 3:
                viewReservations();
                updateReservation();
                viewReservations();
                break;
            case 4:
                viewReservations();
                cancelReservation();
                viewReservations();
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

    private static void makeReservation() {
        viewCustomers();
        System.out.print("Enter Customer ID: ");
        int customerId = getIntInput(scanner);
        
       
        while (!config.recordExists("SELECT id FROM Customers WHERE id = ?", customerId)) {
            System.out.println("Customer ID does not exist. Please enter a valid Customer ID.");
            customerId = getIntInput(scanner);
        }
        
        viewRooms();
        System.out.print("Enter Room ID: ");
        int roomId = getIntInput(scanner);

       
        while (!config.recordExists("SELECT id FROM Rooms WHERE id = ?", roomId)) {
            System.out.println("Room ID does not exist. Please enter a valid Room ID.");
            roomId = getIntInput(scanner);
        }
        
        String startDate = getValidDate("Enter Start Date (YYYY-MM-DD): ");
        String endDate = getValidDate("Enter End Date (YYYY-MM-DD): ");

        
        while (!isRoomAvailable(roomId, startDate, endDate)) {
            System.out.println("The room is not available for the selected dates. Please choose different dates.");
            startDate = getValidDate("Enter Start Date (YYYY-MM-DD): ");
            endDate = getValidDate("Enter End Date (YYYY-MM-DD): ");
        }

        String status = getValidStatus(scanner);

        config.addRecord("INSERT INTO Reservations (customer_id, room_id, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)", 
                         customerId, roomId, startDate, endDate, status);
    }

    private static String getValidDate(String prompt) {
        String date = "";
        while (true) {
            System.out.print(prompt);
            date = scanner.next();
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Invalid date format. Please enter date in YYYY-MM-DD format.");
            } else if (isDateInPast(date)) {
                System.out.println("The date cannot be in the past. Please enter a valid date.");
            } else {
                return date;
            }
        }
    }

    private static boolean isDateInPast(String date) {
       
        return date.compareTo(java.time.LocalDate.now().toString()) < 0;
    }

    private static boolean isRoomAvailable(int roomId, String startDate, String endDate) {
        String query = "SELECT COUNT(*) FROM Reservations WHERE room_id = ? AND (start_date < ? AND end_date > ?)";
  
        return config.getCount(query, roomId, endDate, startDate) == 0;
    }

    private static String getValidStatus(Scanner scanner) {
        String status = scanner.next();
        while (!status.equalsIgnoreCase("Confirmed") && !status.equalsIgnoreCase("Pending") && !status.equalsIgnoreCase("Canceled")) {
            System.out.println("Invalid status. Please enter 'Confirmed', 'Pending', or 'Canceled'.");
            status = scanner.next();
        }
        return status;
    }

    static void viewReservations() {
        config.viewRecords("SELECT r.id, c.first_name, c.last_name, ro.room_type, r.start_date, r.end_date, r.status FROM Reservations r INNER JOIN Customers c ON r.customer_id = c.id INNER JOIN Rooms ro ON r.room_id = ro.id",
                new String[]{"Reservation ID", "Customer Name", "Room Type", "Start Date", "End Date", "Status"},
                new String[]{"id", "first_name", "last_name", "room_type", "start_date", "end_date", "status"});
    }

    private static void updateReservation() {
        System.out.print("Enter Reservation ID to update: ");
        int reservationId = getIntInput(scanner);

       
        while (!config.recordExists("SELECT id FROM Reservations WHERE id = ?", reservationId)) {
            System.out.println("Reservation ID does not exist. Please enter a valid Reservation ID.");
            reservationId = getIntInput(scanner);
        }

        System.out.print("Enter new Status: ");
        String newStatus = getValidStatus(scanner);

        config.updateRecord("UPDATE Reservations SET status = ? WHERE id = ?", newStatus, reservationId);
    }

    private static void cancelReservation() {
        System.out.print("Enter Reservation ID to cancel: ");
        int reservationId = getIntInput(scanner);

       
        while (!config.recordExists("SELECT id FROM Reservations WHERE id = ?", reservationId)) {
            System.out.println("Reservation ID does not exist. Please enter a valid Reservation ID.");
            reservationId = getIntInput(scanner);
        }

        config.deleteRecord("DELETE FROM Reservations WHERE id = ?", reservationId);
    }
}
