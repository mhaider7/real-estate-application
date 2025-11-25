package com.csproj;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class App 
{
    public static void main( String[] args )
    {

        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/main/java/com/csproj/db.properties"));
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
