package com.csproj;
import java.util.Scanner;
import java.util.ArrayList;

public class App 
{
    public static void main( String[] args )
    {
        // Prompt the user for all application options
        while (true) {
            System.out.println("REAL ESTATE APPLICATION:");
            System.out.println(" - As an agent or a renter, to register for an account, enter (1).");
            System.out.println(" - As an existing renter, to add, modify, or delete payment and address information, enter (2).");
            System.out.println(" - To exit, enter (0).");
            Scanner scanner = new Scanner(System.in);
            Integer input = scanner.nextInt();
            if (input == 1) {
                // Prompt user if they are a Agent or Prospective renter
                System.out.println("For agent, select (A)\nFor prospective renter, select (R).");
                String user = scanner.next();
                if (user.equals("A") || user.equals("a")) {
                    // Call the class that will prompt the agent account
                    System.out.print("Enter your first name: "); System.out.flush(); String firstName = scanner.next(); scanner.nextLine();
                    System.out.print("Enter your last name: "); System.out.flush(); String lastName = scanner.next(); scanner.nextLine();
                    System.out.print("Enter email: "); System.out.flush(); String email = scanner.nextLine();
                    System.out.print("Enter job title: "); System.out.flush(); String jobTitle = scanner.nextLine();
                    System.out.print("Enter agency name: "); System.out.flush(); String agency = scanner.nextLine();
                    System.out.print("Enter phone number: "); System.out.flush(); String phoneNum = scanner.nextLine();
                    System.out.print("Enter your address in the format (building number, state (Ex: IL), zip code, city, street): "); System.out.flush(); String addr = scanner.nextLine();
                    String[] address = addr.split(",\\s+");
                    Registration.registerAgent(email, firstName, lastName, jobTitle, agency, phoneNum, address);
                } else if (user.equals("R") || user.equals("r")) {
                    // Call the class that will prompt the renter account
                    System.out.print("Enter your first name: "); System.out.flush(); String firstName = scanner.next(); scanner.nextLine();
                    System.out.print("Enter your last name: "); System.out.flush(); String lastName = scanner.next(); scanner.nextLine();
                    System.out.print("Enter email: "); System.out.flush(); String email = scanner.nextLine();
                    System.out.println("Here is a list of our agents.");
                    Registration.agentOptions();
                    System.out.print("Enter your agent's ID: "); System.out.flush(); String agentID = scanner.nextLine();
                    System.out.print("Enter your agent's email: "); System.out.flush(); String agentEmail = scanner.nextLine();
                    System.out.print("Enter your desired property size: "); System.out.flush(); String size = scanner.nextLine();
                    System.out.print("Enter your desired maximum room number: "); System.out.flush(); String maxRoom = scanner.nextLine();
                    System.out.print("Enter your budget: "); System.out.flush(); String budget = scanner.nextLine();
                    System.out.print("Enter your desired move in date in the form YEAR-MONTH-DAY: "); System.out.flush(); String date = scanner.nextLine();
                    System.out.print("Enter your address in the format (building number, state (Ex: IL), zip code, city, street): "); System.out.flush(); String addr = scanner.nextLine();
                    String[] address = addr.split(",\\s+");
                    Registration.registerRenter(email, firstName, lastName, agentID, agentEmail, size, maxRoom, budget, date, address);
                }
            } else if (input == 2) {
                System.out.println();
            }
            else if (input == 0) {
                scanner.close();
                System.exit(0);
            }
            System.out.println();
        }
    }
}
