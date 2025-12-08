package com.csproj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class Booking {

    public static void bookProperty(String renterEmail, int propertyId, String startDate, String endDate, String ccNumber) {
        Properties props = new Properties();
        String propPath = "src/main/java/com/csproj/dbproperties";

        try {
            props.load(new FileInputStream(propPath));
        } catch (FileNotFoundException e) {
            System.out.println("Error: dbproperties file not found.");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        // Get Property details to calculate cost and verify it exists
        String propertyQuery = "SELECT \"number\", state, zip_code, city, street, price " +
                "FROM property WHERE property_id = " + propertyId + ";";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            ResultSet rs = stmt.executeQuery(propertyQuery);

            if (!rs.next()) {
                System.out.println("Error: Property ID " + propertyId + " not found.");
                return;
            }

            int number = rs.getInt("number");
            String state = rs.getString("state");
            String zip = rs.getString("zip_code");
            String city = rs.getString("city");
            String street = rs.getString("street");
            double price = rs.getDouble("price");

            // Calculate Total Cost
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            long days = ChronoUnit.DAYS.between(start, end);
            if (days < 1) days = 1;
            double totalCost = price * days;

            // Insert the Booking
            String insertBooking = "INSERT INTO booking " +
                    "(\"number\", \"state\", zip_code, city, street, start_date, end_date, confirmation, property_id, renter_email, cc_number) " +
                    "VALUES (" + number + ", '" + state + "', '" + zip + "', '" + city + "', '" + street + "', " +
                    "'" + startDate + "', '" + endDate + "', TRUE, " + propertyId + ", '" + renterEmail + "', " + ccNumber + ");";

            stmt.executeUpdate(insertBooking);
            conn.commit();

            System.out.println("\n--- Booking Confirmed! ---");
            System.out.println("Address: " + number + " " + street + ", " + city + ", " + state);
            System.out.println("Total Cost: $" + totalCost);

        } catch (Exception e) {
            System.out.println("Booking Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}