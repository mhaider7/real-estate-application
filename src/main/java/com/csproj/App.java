package com.csproj;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        // Prompt the user for all application options
        while (true) {
            System.out.println("REAL ESTATE APPLICATION");
            System.out.println(" - As an agent or a renter, to register for an account, enter (1).");
            System.out.println(" - As an existing renter, to add, modify, or delete payment and address information, enter (2).");
            System.out.print(" - To exit, enter (0).\n: ");
            Scanner scanner = new Scanner(System.in);
            Integer input = scanner.nextInt();
            if (input == 1) {
                // Prompt user if they are a Agent or Prospective renter
                System.out.print("\nFor agent, select (A).\nFor prospective renter, select (R).\n: ");
                String user = scanner.next();
                if (user.equals("A") || user.equals("a")) {
                    // Call the class that will prompt the agent account
                    System.out.print("\nEnter your first name: "); System.out.flush(); String firstName = scanner.next(); scanner.nextLine();
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
                    System.out.print("\nEnter your first name: "); System.out.flush(); String firstName = scanner.next(); scanner.nextLine();
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
                System.out.print("\nTo edit address, enter (A).\nTo edit credit card, enter (C).\n: "); System.out.flush(); String response = scanner.next(); scanner.nextLine();
                if (response.equals("A") || response.equals("a")) {
                    System.out.println("\n - To add an address, enter (1)");
                    System.out.println(" - To modify an address, enter (2)");
                    System.out.println(" - To delete an address, enter (3)");
                    Integer selection = scanner.nextInt(); scanner.nextLine(); // Check in case there is a problem here
                    if (selection == 1) {
                        PaymentAddressInfo.add();
                    } else if (selection == 2) {
                        PaymentAddressInfo.modify();
                    } else if (selection == 3) {
                        PaymentAddressInfo.delete();
                    }
                } else {
                    System.out.println(" - To add a credit card, enter (1)");
                    System.out.println(" - To modify a credit card, enter (2)");
                    System.out.println(" - To delete a credit card, enter (3)");
                    Integer selection = scanner.nextInt(); scanner.nextLine(); // Check in case there is a problem here

                }
            }
            else if (input == 0) {
                scanner.close();
                System.exit(0);
            }
            System.out.println();
        }
    }
}
