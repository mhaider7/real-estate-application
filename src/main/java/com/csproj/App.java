package com.csproj;
import java.sql.Connection;
import java.sql.DriverManager;

public class App 
{
    public static void main( String[] args )
    {
        String url = "jdbc:postgresql://localhost:5432/RealEstateDB";
        String user = "postgres";
        String password = "cs425class";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database!");
        } catch (Exception e) {
            System.out.println("Failed to make connection!");
        }
    }
}
