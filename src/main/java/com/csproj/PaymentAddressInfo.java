package com.csproj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class PaymentAddressInfo {
    
    public static void addAddress(String uEmail, String[] address) {
        // Credentials to the db
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

        // Create a view to get the users first and last name
        String getNameView = "DROP VIEW IF EXISTS get_name; " +
                        "CREATE VIEW get_name AS " + //
                        "SELECT first_name, last_name " + //
                        "FROM \"user\" " + //
                        "WHERE email = '" + uEmail + "';";

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            conn.setAutoCommit(false);
            //System.out.println("Connected to the database!");
            stmt.executeUpdate(getNameView);
            ResultSet rset = stmt.executeQuery("SELECT * FROM get_name");
            rset.next();
            // Create the insert query to add the address
            String addAdd = "INSERT INTO user_address (\"number\", \"state\", zip_code, city, street, email, first_name, last_name) "
                    + "VALUES (" + address[0] + ", '" + address[1] + "', '" + address[2] + "', '" + address[3] + "', '" + address[4] 
                    + "', '" + uEmail + "', '" + rset.getString("first_name") + "', '" + rset.getString("last_name") + "');";
            stmt.executeUpdate(addAdd);
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Address added successfully.");
    }

    public static void modifyAddress(String uEmail, String[] address, String[] uAddress) {
        // Credentials to the db
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

        // Create a view to get the users first and last name
        String updatedAddress = "UPDATE user_address "
                            + "SET number = " + uAddress[0] + ", state = '" + uAddress[1] + "', zip_code = '" + uAddress[2] + "', city = '" + uAddress[3] + "', street = '" + uAddress[4] + "' "
                            + "WHERE email = '" + uEmail + "' AND number = " + address[0] + " AND state = '" + address[1] + "' AND zip_code = '" + address[2] + "' AND city = '" + address[3] + "' AND street = '" + address[4] + "';";

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            conn.setAutoCommit(false);
            //System.out.println("Connected to the database!");
            stmt.executeUpdate(updatedAddress);
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Address modified successfully.");
    }

    public static void deleteAddress(String uEmail, String[] address) {
        // Credentials to the db
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

        // Create a view to get the users first and last name
        String deleteAddress = "DELETE FROM user_address "
                            + "WHERE email = '" +  uEmail + "' AND number = " + address[0] 
                            + " AND state = '" + address[0] + "' AND zip_code = '" + address[2]
                            + "' AND city = '" + address[3] + "' AND street = '" + address[4] + "';";

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            conn.setAutoCommit(false);
            //System.out.println("Connected to the database!");
            stmt.executeUpdate(deleteAddress);
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Address deleted successfully.");
    }

    public static boolean showRenterAddresses(String uEmail) {
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

        String userAddresses = "SELECT number, state, zip_code, city, street "
                        + "FROM prospective_renter NATURAL JOIN user_address WHERE email = '" + uEmail + "';";

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            //System.out.println("Connected to the database!");
            ResultSet rset = stmt.executeQuery(userAddresses);
            System.out.println("\nHere are all addresses under " + uEmail + ":");
            if (rset.next()) {
                System.out.println(rset.getInt("number") 
                + ", " + rset.getString("state") + ", " + rset.getString("zip_code")
                + ", " + rset.getString("city") + ", " + rset.getString("street"));
                while (rset.next()) {
                    System.out.println(rset.getInt("number") 
                    + ", " + rset.getString("state") + ", " + rset.getString("zip_code")
                    + ", " + rset.getString("city") + ", " + rset.getString("street"));
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to make connection!");
            return false;
        }
    }

    public static boolean ccExists(String uEmail) {
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

        String creditCards = "SELECT * FROM credit_card, user_address "
                        + "WHERE credit_card.email = user_address.email AND credit_card.email = '" + uEmail + "';";

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            //System.out.println("Connected to the database!");
            ResultSet rset = stmt.executeQuery(creditCards);
            if (rset.next()) {
                System.out.println(rset.getString(1));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to make connection!");
            return true;
        }
    }

    public static void addCreditCard() {
        System.out.println();
    }

    public static void modifyCreditCard() {
        System.out.println();
    }

    public static void deleteCreditCard() {
        System.out.println();
    }
}
