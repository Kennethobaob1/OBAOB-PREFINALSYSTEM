
package resortreservation;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Config {

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Obaob.db"); // Connection to the SQLite DB
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    // Add record to the database
    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    // View records from the database
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames, Object... values) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = connectDB(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder headerLine = new StringBuilder();
                headerLine.append("-------------------------------------------------------------------------------------------------------------------------------------\n| ");
                for (String header : columnHeaders) {
                    headerLine.append(String.format("%-20s | ", header));
                }
                headerLine.append("\n-------------------------------------------------------------------------------------------------------------------------------------");

                System.out.println(headerLine.toString());

                while (rs.next()) {
                    StringBuilder row = new StringBuilder("| ");
                    for (String colName : columnNames) {
                        String value = rs.getString(colName);
                        row.append(String.format("%-20s | ", value != null ? value : ""));
                    }
                    System.out.println(row.toString());
                }
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    // Update record in the database
   public void updateRecord(String sql, Object... params) {
    try (Connection conn = this.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Record updated successfully!");
        } else {
            System.out.println("No record was updated.");
        }
    } catch (SQLException e) {
        System.out.println("Error updating record: " + e.getMessage());
    }
}


    // Delete record from the database
 public void deleteRecord(String sql, Object... params) {
    try (Connection conn = this.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Record deleted successfully!");
        } else {
            System.out.println("No record found to delete.");
        }
    } catch (SQLException e) {
        System.out.println("Error deleting record: " + e.getMessage());
    }
}

   public boolean recordExists(String query, Object... params) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameters for the PreparedStatement
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Execute the query and check if any results exist
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // If a row is returned, it means the record exists
            }
        } catch (SQLException e) {
            System.out.println("Error checking record existence: " + e.getMessage());
            return false;
        }
    }
     public int getCount(String query, Object... params) {
        int count = 0;

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters dynamically based on the passed arguments
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);  // Get the count from the first column
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving count: " + e.getMessage());
        }

        return count;
    }
    }


