package resortreservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import static resortreservation.Config.connectDB;

public class ResortCRUD {

    private static final Config config = new Config();
    private static final Scanner scanner = new Scanner(System.in);

    public static void manageResorts() {
        System.out.println("\n------------------------------Managing Resorts...----------------------------------");
        System.out.println("1. Add Resort");
        System.out.println("2. View Resorts");
        System.out.println("3. Update Resort");
        System.out.println("4. Delete Resort");
        System.out.println("5. Go back");
        System.out.println("--------------------------------------------------------------------------------------");
        int choice = getValidChoice();
        
        switch (choice) {
            case 1:
                addResort();
                break;
            case 2:
                viewResorts();
                break;
            case 3:
                viewResorts();
                updateResort();
                viewResorts();
                break;
            case 4:
                viewResorts();
                deleteResort();
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

    
    private static String getValidStringInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.next();
            if (input != null && !input.trim().isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please enter a valid value.");
            }
        }
    }

    private static void addResort() {
        String name = getValidStringInput("Enter Resort Name: ");
        String location = getValidStringInput("Enter Resort Location: ");
        config.addRecord("INSERT INTO Resorts (name, location) VALUES (?, ?)", name, location);
    }


    private static int getValidIdInput(String prompt) {
        int id;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                if (id > 0) {
                    return id;
                } else {
                    System.out.println("ID must be a positive integer.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid positive integer.");
                scanner.next();
            }
        }
    }

    public static void viewResorts() {
        String sql = "SELECT * FROM Resorts";
        String[] columnHeaders = {"ID", "Name", "Location"};
        String[] columnNames = {"id", "name", "location"};

        StringBuilder headerLine = new StringBuilder();
        headerLine.append("------------------------------------------------------------------\n| ");
        for (String header : columnHeaders) {
            headerLine.append(String.format("%-30s | ", header));
        }
        headerLine.append("\n------------------------------------------------------------------");

        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println(headerLine.toString());

            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-30s | ", value != null ? value : ""));
                }
                System.out.println(row.toString());
            }
            System.out.println("------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error retrieving resorts: " + e.getMessage());
        }
    }

    private static void updateResort() {
        int idToUpdate = getValidIdInput("Enter Resort ID to update: ");
        String newName = getValidStringInput("Enter new Resort Name: ");
        String newLocation = getValidStringInput("Enter new Resort Location: ");
        config.updateRecord("UPDATE Resorts SET name = ?, location = ? WHERE id = ?", newName, newLocation, idToUpdate);
    }

    private static void deleteResort() {
        int idToDelete = getValidIdInput("Enter Resort ID to delete: ");
        config.deleteRecord("DELETE FROM Resorts WHERE id = ?", idToDelete);
    }
}
