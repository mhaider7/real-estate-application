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
            System.out.println("Address added successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            System.out.println("Address modified successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            System.out.println("Address deleted successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
                //System.out.println(rset.getString(1));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to make connection!");
            return true;
        }
    }

    public static void addCreditCard(String uEmail, String aEmail, String ccNumber, String expDate, String ccv) {
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

        String addCC = "INSERT INTO credit_card (email, renter_id, cc_number, exp_date, ccv) "
                    + "SELECT email, renter_id, " + ccNumber + ", '" + expDate + "', " + ccv + " "
                    + "FROM prospective_renter "
                    + "WHERE email = '" + uEmail + "' AND agent_email = '" + aEmail + "';";
        //System.out.println(addCC);

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            conn.setAutoCommit(false);
            //System.out.println("Connected to the database!");
            stmt.executeUpdate(addCC);
            conn.commit();
            System.out.println("Credit card successfully added.");
        } catch (Exception e) {
            System.out.println("Failed to add credit card!");
        }
    }

    public static void checkAgentEmail(String uEmail) {
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

        String listAgents = "SELECT a.email, u.first_name, u.last_name "
                + "FROM agent AS a, prospective_renter AS r, \"user\" AS u "
                + "WHERE a.email = r.agent_email AND a.agent_id = r.agent_id AND a.email = u.email AND r.email = '" + uEmail + "';";

        try (Connection conn = DriverManager.getConnection(url, user, password); 
                Statement stmt = conn.createStatement(); 
                ResultSet rset = stmt.executeQuery(listAgents);) {
            //System.out.println("Connected to the database!");
            System.out.println("Here are all the agents you are listed under: ");
            while (rset.next()) {
                System.out.println("Name: " + rset.getString("last_name") + ", " + rset.getString("first_name"));
                System.out.println("Email: " + rset.getString("email"));
            }
        } catch (Exception e) {
            System.out.println("Failed to list agents!");
        }
    }

    public static void modifyCreditCard(String uEmail, String ccNumber, String newCCNumber, String expDate, String ccv) {
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

        String updateCC = "UPDATE credit_card "
                        + "SET cc_number = " + newCCNumber + ", exp_date = '" + expDate + "', ccv = " + ccv + " "
                        + "WHERE email = '" + uEmail + "' AND cc_number = " + ccNumber + ";";
        //System.out.println(updateCC);

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            //System.out.println("Connected to the database!");
            conn.setAutoCommit(false);
            stmt.executeUpdate(updateCC);
            conn.commit();
            System.out.println("Credit card modification complete!");
        } catch (Exception e) {
            System.out.println("Failed to modify credit card!");
            System.out.println(e.getMessage());
        }
    }

    public static void listCC(String uEmail) {
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

        String listCCInfo = "SELECT cc_number, exp_date "
                + "FROM credit_card "
                + "WHERE email = '" + uEmail + "';";

        try (Connection conn = DriverManager.getConnection(url, user, password); 
                Statement stmt = conn.createStatement(); 
                ResultSet rset = stmt.executeQuery(listCCInfo);) {
            //System.out.println("Connected to the database!");
            System.out.println("\nHere are all your credits cards: ");
            while (rset.next()) {
                System.out.println("Number: " + rset.getString("cc_number") + ", Expiration Date: " + rset.getString("exp_date"));
            }
        } catch (Exception e) {
            System.out.println("Failed to list credit cards!");
        }
    }

    public static void deleteCreditCard(String uEmail, String ccNumber) {
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

        String deleteCC = "DELETE FROM credit_card "
                        + "WHERE email = '" + uEmail + "' AND cc_number = " + ccNumber + ";";

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement();) {
            //System.out.println("Connected to the database!");
            conn.setAutoCommit(false);
            stmt.executeUpdate(deleteCC);
            conn.commit();
            System.out.println("Credit card deleted.");
        } catch (Exception e) {
            System.out.println("Failed to delete credit card!");
            System.out.println(e.getMessage());
        }
    }
}
