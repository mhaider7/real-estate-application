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

            // Validate Dates
            if (end.isBefore(start)) {
                System.out.println("\nError: End date cannot be before start date.");
                return;
            }

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

    public static boolean viewRentersBookings(String renterEmail) {
        Properties props = new Properties();
        String propPath = "src/main/java/com/csproj/dbproperties";

        try {
            props.load(new FileInputStream(propPath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        // Join Booking and Property to show useful info (Address, Dates)
        String query = "SELECT b.booking_id, b.start_date, b.end_date, p.street, p.city, p.state, p.price " +
                "FROM booking b " +
                "JOIN property p ON b.property_id = p.property_id " +
                "WHERE b.renter_email = '" + renterEmail + "' " +
                "ORDER BY b.start_date;";

        boolean hasBookings = false;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n--- Your Current Bookings ---");
            while (rs.next()) {
                hasBookings = true;
                int id = rs.getInt("booking_id");
                String start = rs.getString("start_date");
                String end = rs.getString("end_date");
                String street = rs.getString("street");
                String city = rs.getString("city");

                System.out.println("ID: " + id + " | " + start + " to " + end + " | " + street + ", " + city);
            }

            if (!hasBookings) {
                System.out.println("You have no active bookings.");
            }

        } catch (Exception e) {
            System.out.println("Error viewing bookings: " + e.getMessage());
        }
        return hasBookings;
    }

    public static void cancelBooking(int bookingId) {
        Properties props = new Properties();
        String propPath = "src/main/java/com/csproj/dbproperties";

        try {
            props.load(new FileInputStream(propPath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        // Fetch details first to calculate refund amount
        String fetchQuery = "SELECT b.start_date, b.end_date, b.cc_number, p.price " +
                "FROM booking b " +
                "JOIN property p ON b.property_id = p.property_id " +
                "WHERE b.booking_id = " + bookingId + ";";

        String deleteQuery = "DELETE FROM booking WHERE booking_id = " + bookingId + ";";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            // Calculate Refund
            ResultSet rs = stmt.executeQuery(fetchQuery);
            if (rs.next()) {
                LocalDate start = LocalDate.parse(rs.getString("start_date"));
                LocalDate end = LocalDate.parse(rs.getString("end_date"));
                double price = rs.getDouble("price");
                long ccNum = rs.getLong("cc_number");

                long days = ChronoUnit.DAYS.between(start, end);
                if (days < 1) days = 1;
                double refund = price * days;

                // Delete
                stmt.executeUpdate(deleteQuery);
                conn.commit();

                System.out.printf("\nSuccess! Booking %d cancelled.\n", bookingId);
                System.out.printf("Refund of $%.2f processed to card ending in %d.\n", refund, ccNum % 10000); // Last 4 digits
            } else {
                System.out.println("Booking ID not found.");
            }

        } catch (Exception e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }
}