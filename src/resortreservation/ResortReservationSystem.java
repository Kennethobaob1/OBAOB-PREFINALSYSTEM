package resortreservation;

import java.util.Scanner;

public class ResortReservationSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n----------------------------Main Menu------------------------:");
            System.out.println("1.  Resorts");
            System.out.println("2.  Rooms");
            System.out.println("3.  Customers");
            System.out.println("4.  Reservations");
            System.out.println("5.  Reports");
            System.out.println("6.  Exit");
            System.out.println("-----------------------------------------------------------------");
            System.out.print("Enter choice: ");
            
           
            int choice = getIntInput(scanner);
            
            switch (choice) {
                case 1:
                    ResortCRUD.manageResorts();
                    break;
                case 2:
                    RoomCRUD.manageRooms();
                    break;
                case 3:
                    CustomerCRUD.manageCustomers();
                    break;
                case 4:
                    ReservationCRUD.manageReservations();
                    break;
                case 5:
                    Reports.generateReports();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

   
    private static int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();  
        }
        return scanner.nextInt();
    }
}
