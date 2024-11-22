package resortreservation;

import java.util.Scanner;

public class CustomerCRUD {

    private static final Config config = new Config();
    private static final Scanner scanner = new Scanner(System.in);

    public static void manageCustomers() {
        System.out.println("\n----------------------------------Managing Customers...----------------------------------------------");
        System.out.println("1. Add Customer");
        System.out.println("2. View Customers");
        System.out.println("3. Update Customer");
        System.out.println("4. Delete Customer");
        System.out.println("5. Go back");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                addCustomer();
                break;
            case 2:
                viewCustomers();
                break;
            case 3:
                viewCustomers();
                updateCustomer();
                viewCustomers();
                break;
            case 4:
                viewCustomers();
                deleteCustomer();
                viewCustomers();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void addCustomer() {
        String firstName = getValidName("Enter First Name: ");
        String lastName = getValidName("Enter Last Name: ");
        String contactNumber = getValidContactNumber();

        config.addRecord("INSERT INTO Customers (first_name, last_name, contact_number) VALUES (?, ?, ?)", firstName, lastName, contactNumber);
    }

    static void viewCustomers() {
        config.viewRecords("SELECT * FROM Customers", 
            new String[]{"ID", "First Name", "Last Name", "Contact Number"}, 
            new String[]{"id", "first_name", "last_name", "contact_number"});
    }

    private static void updateCustomer() {
        int customerId = getValidCustomerId("Enter Customer ID to update: ");
        String newFirstName = getValidName("Enter new First Name: ");
        String newLastName = getValidName("Enter new Last Name: ");
        String newContact = getValidContactNumber();

        config.updateRecord("UPDATE Customers SET first_name = ?, last_name = ?, contact_number = ? WHERE id = ?", newFirstName, newLastName, newContact, customerId);
    }

    private static void deleteCustomer() {
        int customerToDelete = getValidCustomerId("Enter Customer ID to delete: ");
        config.deleteRecord("DELETE FROM Customers WHERE id = ?", customerToDelete);
    }

   
    private static int getValidCustomerId(String prompt) {
        int customerId;
        while (true) {
            System.out.print(prompt);
            customerId = scanner.nextInt();
            if (config.recordExists("SELECT id FROM Customers WHERE id = ?", customerId)) {
                return customerId;
            } else {
                System.out.println("Customer ID does not exist. Please enter a valid Customer ID.");
            }
        }
    }

    
    private static String getValidName(String prompt) {
        String name;
        while (true) {
            System.out.print(prompt);
            name = scanner.next();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter a valid name.");
            } else {
                return name;
            }
        }
    }

 
    private static String getValidContactNumber() {
        String contactNumber;
        while (true) {
            System.out.print("Enter Contact Number: ");
            contactNumber = scanner.next();
            if (contactNumber.isEmpty()) {
                System.out.println("Contact number cannot be empty.");
            } else if (!contactNumber.matches("[0-9]+") || contactNumber.length() < 10) {
                System.out.println("Please enter a valid contact number (only digits, at least 10 digits).");
            } else {
                return contactNumber;
            }
        }
    }
}
