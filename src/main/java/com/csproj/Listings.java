package com.csproj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Listings {
    
    private static Connection getConnection() {
        Properties props = new Properties();
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
    
    // Verify agent access
    private static boolean isAgent(String email) {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null) return false;
            
            String checkAgent = "SELECT 1 FROM agent WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(checkAgent);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("Error checking agent status: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
    }
    
    // Create listing for a property
    // If property doesn't exist, agent can create it as part of listing creation
    public static void createListing(String agentEmail, Scanner scanner) {
        if (!isAgent(agentEmail)) {
            System.out.println("Access denied: Agent privileges required.");
            return;
        }
        
        System.out.println("\n--- Create New Listing (Agent Only) ---");
        
        // First, check if property exists or needs to be created
        System.out.println("\nDoes the property already exist in our system?");
        System.out.println("1. Yes, I have the Property ID");
        System.out.println("2. No, I need to add a new property");
        System.out.print("Enter choice (1 or 2): ");
        int propertyChoice = scanner.nextInt();
        scanner.nextLine();
        
        int propertyId;
        
        if (propertyChoice == 1) {
            // Use existing property
            System.out.print("Enter existing Property ID: ");
            propertyId = scanner.nextInt();
            scanner.nextLine();
            
            // Verify property exists
            Connection conn = null;
            try {
                conn = getConnection();
                if (conn == null) return;
                
                String checkProperty = "SELECT 1 FROM property WHERE property_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkProperty);
                checkStmt.setInt(1, propertyId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    System.out.println("Property ID " + propertyId + " not found.");
                    return;
                }
                
            } catch (Exception e) {
                System.out.println("Error checking property: " + e.getMessage());
                return;
            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (Exception e) {}
                }
            }
            
        } else if (propertyChoice == 2) {
            // Create new property
            propertyId = createNewProperty(scanner);
            if (propertyId == -1) {
                System.out.println("Property creation failed. Listing cannot be created.");
                return;
            }
            System.out.println("New property created with ID: " + propertyId);
        } else {
            System.out.println("Invalid choice.");
            return;
        }
        createListingForProperty(agentEmail, propertyId, scanner);
    }
    
    // Helper method to create a new property (called when agent creates listing for new property)
    private static int createNewProperty(Scanner scanner) {
        System.out.println("\n--- Add New Property Details ---");
        
        // Get property type
        System.out.println("Select property type:");
        System.out.println("1. House");
        System.out.println("2. Apartment");
        System.out.println("3. Commercial Building");
        System.out.println("4. Other");
        System.out.print("Enter choice (1-4): ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();
        
        // Get common property details
        System.out.print("Enter square footage: ");
        int sqrFootage = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter base price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Is property available? (true/false): ");
        boolean availability = scanner.nextBoolean();
        scanner.nextLine();
        
        System.out.print("Enter crime rate (0.0-10.0): ");
        float crimeRate = scanner.nextFloat();
        scanner.nextLine();
        
        System.out.println("Enter address details:");
        System.out.print("Building number: ");
        int number = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("State (2-letter code): ");
        String state = scanner.nextLine();
        
        System.out.print("Zip code: ");
        String zipCode = scanner.nextLine();
        
        System.out.print("City: ");
        String city = scanner.nextLine();
        
        System.out.print("Street: ");
        String street = scanner.nextLine();
        
        // Get type-specific details
        String typeSpecificQuery = "";
        List<Object> typeParams = new ArrayList<>();
        
        switch (typeChoice) {
            case 1: // House
                System.out.print("Enter house number: ");
                int houseNumber = scanner.nextInt();
                scanner.nextLine();
                
                System.out.print("Enter number of rooms: ");
                int houseRooms = scanner.nextInt();
                scanner.nextLine();
                
                typeSpecificQuery = "INSERT INTO house (property_id, house_number, number_rooms) VALUES (?, ?, ?)";
                typeParams.add(houseNumber);
                typeParams.add(houseRooms);
                break;
                
            case 2: // Apartment
                System.out.print("Enter building number: ");
                int buildingNumber = scanner.nextInt();
                scanner.nextLine();
                
                System.out.print("Enter apartment number: ");
                String apartmentNumber = scanner.nextLine();
                
                System.out.print("Enter number of rooms: ");
                int apartmentRooms = scanner.nextInt();
                scanner.nextLine();
                
                typeSpecificQuery = "INSERT INTO apartment (property_id, building_number, apartment_number, number_rooms) VALUES (?, ?, ?, ?)";
                typeParams.add(buildingNumber);
                typeParams.add(apartmentNumber);
                typeParams.add(apartmentRooms);
                break;
                
            case 3: // Commercial Building
                System.out.print("Enter building type (e.g., Office Space, Retail Store): ");
                String buildingType = scanner.nextLine();
                
                typeSpecificQuery = "INSERT INTO commercial_building (property_id, building_type) VALUES (?, ?)";
                typeParams.add(buildingType);
                break;
                
            case 4: // Other
                System.out.print("Enter building utility (e.g., Storage Facility, Workshop): ");
                String buildingUtility = scanner.nextLine();
                
                System.out.print("Enter number of rooms: ");
                int otherRooms = scanner.nextInt();
                scanner.nextLine();
                
                typeSpecificQuery = "INSERT INTO other (property_id, building_utility, number_rooms) VALUES (?, ?, ?)";
                typeParams.add(buildingUtility);
                typeParams.add(otherRooms);
                break;
        }
        
        // Nearby schools
        List<String> schools = new ArrayList<>();
        System.out.println("\nEnter nearby schools (enter 'done' when finished):");
        while (true) {
            System.out.print("School name: ");
            String school = scanner.nextLine();
            if (school.equalsIgnoreCase("done")) {
                break;
            }
            schools.add(school);
        }
        
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null) return -1;
            
            conn.setAutoCommit(false);
            
            // Insert into property table
            String propertyQuery = "INSERT INTO property (crime_rate, sqr_footage, price, availability, " +
                    "\"number\", \"state\", zip_code, city, street) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING property_id";
            
            PreparedStatement pstmt = conn.prepareStatement(propertyQuery);
            pstmt.setFloat(1, crimeRate);
            pstmt.setInt(2, sqrFootage);
            pstmt.setDouble(3, price);
            pstmt.setBoolean(4, availability);
            pstmt.setInt(5, number);
            pstmt.setString(6, state);
            pstmt.setString(7, zipCode);
            pstmt.setString(8, city);
            pstmt.setString(9, street);
            
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int propertyId = rs.getInt(1);
            
            // Insert into type-specific table
            if (!typeSpecificQuery.isEmpty()) {
                PreparedStatement typeStmt = conn.prepareStatement(typeSpecificQuery);
                typeStmt.setInt(1, propertyId);
                
                for (int i = 0; i < typeParams.size(); i++) {
                    Object param = typeParams.get(i);
                    if (param instanceof String) {
                        typeStmt.setString(i + 2, (String) param);
                    } else if (param instanceof Integer) {
                        typeStmt.setInt(i + 2, (Integer) param);
                    }
                }
                typeStmt.executeUpdate();
            }
            
            // Insert nearby schools
            if (!schools.isEmpty()) {
                String schoolQuery = "INSERT INTO property_nearby_schools (school_name, property_id) VALUES (?, ?)";
                PreparedStatement schoolStmt = conn.prepareStatement(schoolQuery);
                
                for (String school : schools) {
                    schoolStmt.setString(1, school);
                    schoolStmt.setInt(2, propertyId);
                    schoolStmt.executeUpdate();
                }
            }
            
            conn.commit();
            return propertyId;
            
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) {}
            }
            System.out.println("Error creating property: " + e.getMessage());
            return -1;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
    }

    private static void createListingForProperty(String agentEmail, int propertyId, Scanner scanner) {
        System.out.println("\n--- Create Listing for Property ID: " + propertyId + " ---");
        
        // Get agent ID and agency
        String agentQuery = "SELECT agent_id, agency FROM agent WHERE email = ?";
        
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null) return;
            
            PreparedStatement agentStmt = conn.prepareStatement(agentQuery);
            agentStmt.setString(1, agentEmail);
            ResultSet rs = agentStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Agent not found!");
                return;
            }
            
            int agentId = rs.getInt("agent_id");
            String agency = rs.getString("agency");
            
            // Get property details to show base price
            String propertyCheck = "SELECT price, availability FROM property WHERE property_id = ?";
            PreparedStatement propCheckStmt = conn.prepareStatement(propertyCheck);
            propCheckStmt.setInt(1, propertyId);
            ResultSet propRs = propCheckStmt.executeQuery();
            
            if (!propRs.next()) {
                System.out.println("Property not found!");
                return;
            }
            
            double basePrice = propRs.getDouble("price");
            if (!propRs.getBoolean("availability")) {
                System.out.println("Property is not available for listing.");
                return;
            }
            
            System.out.println("Property base price: $" + basePrice);
            
            System.out.print("Enter your listing price (must be >= base price $" + basePrice + "): ");
            double listingPrice = scanner.nextDouble();
            scanner.nextLine();
            
            if (listingPrice < basePrice) {
                System.out.println("Listing price cannot be lower than property base price.");
                return;
            }
            
            System.out.print("Enter your commission percentage (e.g., 5 for 5%): ");
            double commissionPercent = scanner.nextDouble();
            scanner.nextLine();

            double finalPrice = listingPrice * (1 + commissionPercent / 100);
            
            // Check if listing already exists for this agent and property
            String existingCheck = "SELECT 1 FROM listing WHERE property_id = ? AND email = ? AND agent_id = ?";
            PreparedStatement existStmt = conn.prepareStatement(existingCheck);
            existStmt.setInt(1, propertyId);
            existStmt.setString(2, agentEmail);
            existStmt.setInt(3, agentId);
            ResultSet existRs = existStmt.executeQuery();
            
            if (existRs.next()) {
                System.out.println("You already have a listing for this property.");
                return;
            }
            
            // Create listing
            String listingQuery = "INSERT INTO listing (property_id, email, agent_id, agency, listing_price, commission_percent, final_price) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement listingStmt = conn.prepareStatement(listingQuery);
            listingStmt.setInt(1, propertyId);
            listingStmt.setString(2, agentEmail);
            listingStmt.setInt(3, agentId);
            listingStmt.setString(4, agency);
            listingStmt.setDouble(5, listingPrice);
            listingStmt.setDouble(6, commissionPercent);
            listingStmt.setDouble(7, finalPrice);
            
            int rows = listingStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("\n✓ Listing created successfully!");
                System.out.println("  Property ID: " + propertyId);
                System.out.println("  Your Listing Price: $" + listingPrice);
                System.out.println("  Your Commission: " + commissionPercent + "%");
                System.out.println("  Final Price to Renter: $" + finalPrice);
            } else {
                System.out.println("Failed to create listing.");
            }
            
        } catch (Exception e) {
            System.out.println("Error creating listing: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
    } 
}