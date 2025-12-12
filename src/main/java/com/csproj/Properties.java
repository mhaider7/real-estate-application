package com.csproj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Properties {
    
    // Database connection method
    private static Connection getConnection() {
        java.util.Properties props = new java.util.Properties();
        try {
            props.load(new FileInputStream("src/main/java/com/csproj/dbproperties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
            return null;
        }
    }
    
    // Verify admin access
    private static boolean isAdmin(String email) {
        // For demo purposes, we'll check against a simple list
        // In a real system, you would check against an admin table
        String[] adminEmails = {
            "admin@realestate.com",
            "system.admin@realestate.com",
            "admin@example.com"
        };
        
        for (String adminEmail : adminEmails) {
            if (adminEmail.equalsIgnoreCase(email)) {
                return true;
            }
        }
        
        System.out.println("Access denied: '" + email + "' is not an admin email.");
        return false;
    }
    
    // Direct database property modification (Admin only)
    public static void modifyProperty(String adminEmail, Scanner scanner) {
        if (!isAdmin(adminEmail)) {
            System.out.println("Access denied: Admin privileges required.");
            return;
        }
        
        System.out.println("\n--- Direct Property Modification (Admin Only) ---");
        
        System.out.print("Enter property ID to modify: ");
        int propertyId = scanner.nextInt();
        scanner.nextLine();
        
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null) return;
            
            // Show current property details
            String currentQuery = "SELECT * FROM property WHERE property_id = ?";
            PreparedStatement currentStmt = conn.prepareStatement(currentQuery);
            currentStmt.setInt(1, propertyId);
            ResultSet currentRs = currentStmt.executeQuery();
            
            if (!currentRs.next()) {
                System.out.println("Property not found.");
                return;
            }
            
            System.out.println("\nCurrent Property Details:");
            System.out.println("ID: " + currentRs.getInt("property_id"));
            System.out.println("Address: " + currentRs.getInt("number") + " " + currentRs.getString("street") + 
                             ", " + currentRs.getString("city") + ", " + currentRs.getString("state"));
            System.out.println("Base Price: $" + currentRs.getDouble("price"));
            System.out.println("Square Footage: " + currentRs.getInt("sqr_footage"));
            System.out.println("Availability: " + currentRs.getBoolean("availability"));
            System.out.println("Crime Rate: " + currentRs.getFloat("crime_rate"));
            
            System.out.println("\nWhat would you like to modify?");
            System.out.println("1. Base Price");
            System.out.println("2. Availability");
            System.out.println("3. Crime Rate");
            System.out.println("4. Square Footage");
            System.out.println("5. All of the above");
            System.out.print("Enter choice (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice.");
                return;
            }
            
            Double newPrice = null;
            Boolean newAvailability = null;
            Float newCrimeRate = null;
            Integer newSqft = null;
            
            if (choice == 1 || choice == 5) {
                System.out.print("Enter new base price: $");
                newPrice = scanner.nextDouble();
                scanner.nextLine();
            }
            
            if (choice == 2 || choice == 5) {
                System.out.print("Enter new availability (true/false): ");
                newAvailability = scanner.nextBoolean();
                scanner.nextLine();
            }
            
            if (choice == 3 || choice == 5) {
                System.out.print("Enter new crime rate (0.0-10.0): ");
                newCrimeRate = scanner.nextFloat();
                scanner.nextLine();
            }
            
            if (choice == 4 || choice == 5) {
                System.out.print("Enter new square footage: ");
                newSqft = scanner.nextInt();
                scanner.nextLine();
            }
            
            // Build update query
            StringBuilder updateQuery = new StringBuilder("UPDATE property SET ");
            boolean first = true;
            
            if (newPrice != null) {
                if (!first) updateQuery.append(", ");
                updateQuery.append("price = ?");
                first = false;
            }
            
            if (newAvailability != null) {
                if (!first) updateQuery.append(", ");
                updateQuery.append("availability = ?");
                first = false;
            }
            
            if (newCrimeRate != null) {
                if (!first) updateQuery.append(", ");
                updateQuery.append("crime_rate = ?");
                first = false;
            }
            
            if (newSqft != null) {
                if (!first) updateQuery.append(", ");
                updateQuery.append("sqr_footage = ?");
                first = false;
            }
            
            updateQuery.append(" WHERE property_id = ?");
            
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery.toString());
            int paramIndex = 1;
            
            if (newPrice != null) {
                updateStmt.setDouble(paramIndex++, newPrice);
            }
            
            if (newAvailability != null) {
                updateStmt.setBoolean(paramIndex++, newAvailability);
            }
            
            if (newCrimeRate != null) {
                updateStmt.setFloat(paramIndex++, newCrimeRate);
            }
            
            if (newSqft != null) {
                updateStmt.setInt(paramIndex++, newSqft);
            }
            
            updateStmt.setInt(paramIndex, propertyId);
            
            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("\n✓ Property updated successfully.");
                System.out.println("Note: This change may affect existing listings for this property.");
                System.out.println("Agents may need to adjust their listing prices if base price changed.");
            } else {
                System.out.println("Failed to update property.");
            }
            
        } catch (Exception e) {
            System.out.println("Error modifying property: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
    }
}