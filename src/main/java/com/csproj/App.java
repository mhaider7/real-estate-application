package com.csproj;
import java.util.Scanner;
import java.math.BigDecimal;

public class App 
{
    public static void main( String[] args )
    {
        // Prompt the user for all application options
        while (true) {
            System.out.println("Options for real estate application:");
            System.out.println("To register for an account, enter (1).");
            System.out.println("To exit, enter (0).");
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
                    Registration.registerUser(email, firstName, lastName);
                    Registration.registerAgent(email, jobTitle, agency, phoneNum);
                } else if (user.equals("R") || user.equals("r")) {
                    // Call the class that will prompt the renter account
                    System.out.print("Enter your first name: "); System.out.flush(); String firstName = scanner.next(); scanner.nextLine();
                    System.out.print("Enter your last name: "); System.out.flush(); String lastName = scanner.next(); scanner.nextLine();
                    System.out.print("Enter email: "); System.out.flush(); String email = scanner.nextLine();
                    System.out.println("Here are the options for agents.");
                    Registration.agentOptions();
                    System.out.print("Enter your agent's ID: "); System.out.flush(); String agentID = scanner.nextLine();
                    System.out.print("Enter your agent's email: "); System.out.flush(); String agentEmail = scanner.nextLine();
                    System.out.print("Enter your desired property size: "); System.out.flush(); String size = scanner.nextLine();
                    System.out.print("Enter your desired maximum room number: "); System.out.flush(); String maxRoom = scanner.nextLine();
                    System.out.print("Enter your budget: "); System.out.flush(); String budget = scanner.nextLine();
                    System.out.print("Enter your desired move in date (YEAR-MONTH-DAY format): "); System.out.flush(); String date = scanner.nextLine();
                    Registration.registerUser(email, firstName, lastName);
                    Registration.registerRenter(email, agentID, agentEmail, size, maxRoom, budget, date);
                }
            } else if (input == 0) {
                scanner.close();
                System.exit(0);
            }
            System.out.println();
        }
    }
}
