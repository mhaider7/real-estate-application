package com.csproj;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Registration {
    
    public static void registerUser(String email, String firstName, String lastName) {
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

        // SQL insert query for inserting data
        String insertUser = "INSERT INTO user ";
        
        // Connect to the db and insert the user data
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("User record created successfully.");
    }

    public static void registerAgent(String email, String jobTitle, String agency, Integer phoneNum) {
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

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database!");
        } catch (Exception e) {
            System.out.println("Failed to make connection!");
        }
    }

    public static void registerRenter(String email, Integer agentID, String agentEmail, Integer size, Integer maxRoom, BigDecimal budget, String date) {
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

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database!");
        } catch (Exception e) {
            System.out.println("Failed to make connection!");
        }
    }
}
