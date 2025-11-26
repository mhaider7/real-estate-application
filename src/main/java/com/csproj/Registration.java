package com.csproj;
import java.sql.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Registration {

    public static void registerAgent(String uEmail, String firstName, String lastName, String jobTitle, String agency, String phoneNum) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("/Users/maz/Desktop/CS425/real-estate-application/real-estate-app/src/main/java/com/csproj/dbproperties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        String insertUser = "INSERT INTO \"user\" (email, first_name, last_name) " 
                            + "VALUES ('" + uEmail + "', '" + firstName + "', '" + lastName + "');";
        //System.out.println(insertUser);
        String insertAgent = "INSERT INTO agent (email, job_title, agency, phone_number) "
                            + "VALUES ('" + uEmail + "', '" + jobTitle + "', '" + agency + "', " + phoneNum + ");";
        //System.out.println(insertAgent);

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            conn.setAutoCommit(false);
            //System.out.println("Connected to the database!");
            stmt.executeUpdate(insertUser);
            stmt.executeUpdate(insertAgent);
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Agent record created successfully.");
    }

    public static void registerRenter(String uEmail, String firstName, String lastName, String agentID, String agentEmail, String size, String maxRoom, String budget, String date) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("/Users/maz/Desktop/CS425/real-estate-application/real-estate-app/src/main/java/com/csproj/dbproperties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        String insertUser = "INSERT INTO \"user\" (email, first_name, last_name) " 
                            + "VALUES ('" + uEmail + "', '" + firstName + "', '" + lastName + "');";
        //System.out.println(insertUser);
        String userRenter = "INSERT INTO prospective_renter (email, agent_id, agent_email, desired_size, max_room_number, budget, desired_movein_date) "
                        + "VALUES ('" + uEmail + "', " + agentID + ", '" + agentEmail + "', " + size + ", " + maxRoom + ", " + budget + ", '" + date + "');";
        //System.out.println(userRenter);

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            conn.setAutoCommit(false);
            //System.out.println("Connected to the database!");
            stmt.executeUpdate(insertUser);
            stmt.executeUpdate(userRenter);
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Renter update successful.");
    }

    public static void agentOptions() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("/Users/maz/Desktop/CS425/real-estate-application/real-estate-app/src/main/java/com/csproj/dbproperties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        String possibleAgents = "SELECT agent_id, email FROM agent";

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            //System.out.println("Connected to the database!");
            ResultSet rset = stmt.executeQuery(possibleAgents);
            while (rset.next()) {
                System.out.println("Agent ID: " + rset.getString("agent_id") 
                + ", Agent email: " + rset.getString("email"));
            }
        } catch (Exception e) {
            System.out.println("Failed to make connection!");
        }
    }
}
