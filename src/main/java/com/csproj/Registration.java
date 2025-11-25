package com.csproj;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Registration {
    
    public static void registerAgent() {
        Properties props = new Properties();


        
            try {
                props.load(new FileInputStream("/Users/maz/Desktop/CS425/real-estate-application/real-estate-app/src/main/java/com/csproj/dbproperties"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
