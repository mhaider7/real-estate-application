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
            System.out.println(" - To book a property, enter (3).");
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
                    System.out.print(" - To delete an address, enter (3)\n: ");
                    Integer selection = scanner.nextInt(); scanner.nextLine(); // Check in case there is a problem here
                    if (selection == 1) {
                        System.out.print("\nEnter your email: "); System.out.flush(); String email = scanner.nextLine();
                        // Check if email exists??
                        System.out.print("\nEnter your address in the format (building number, state (Ex: IL), zip code, city, street): "); System.out.flush(); String addr = scanner.nextLine();
                        String[] address = addr.split(",\\s+");
                        PaymentAddressInfo.addAddress(email, address);
                    } else if (selection == 2) {
                        System.out.print("\nEnter your email: "); System.out.flush(); String email = scanner.nextLine();
                        // Check if email exists
                        if (PaymentAddressInfo.showRenterAddresses(email) == false) {
                            System.out.println("\nNo addresses to delete.\n");
                            continue;
                        }
                        System.out.print("\nEnter the address you want to update in the format (building number, state (Ex: IL), zip code, city, street): "); System.out.flush(); String addr = scanner.nextLine();
                        String[] address = addr.split(",\\s+");
                        System.out.print("Enter your updated address in the format (building number, state (Ex: IL), zip code, city, street): "); System.out.flush(); String updtAddr = scanner.nextLine();
                        String[] updateAddress = updtAddr.split(",\\s+");
                        PaymentAddressInfo.modifyAddress(email, address, updateAddress);
                    } else if (selection == 3) {
                        System.out.print("\nEnter your email: "); System.out.flush(); String email = scanner.nextLine();
                        // Check if email exists
                        if (PaymentAddressInfo.showRenterAddresses(email) == false) {
                            System.out.println("\nNo addresses to delete.\n");
                            continue;
                        }
                        // Check if there are credit cards associated to the address that must be deleted first
                        if (PaymentAddressInfo.ccExists(email)) {
                            System.out.println("\nYou must delete your credit cards before deleting you billing address.\n");
                            continue;
                        } 
                        System.out.print("\nEnter the address you want to delete in the format (building number, state (Ex: IL), zip code, city, street): "); System.out.flush(); String addr = scanner.nextLine();
                        String[] address = addr.split(",\\s+");
                        PaymentAddressInfo.deleteAddress(email, address);
                    }
                } else {
                    System.out.println("\n - To add a credit card, enter (1)");
                    System.out.println(" - To modify a credit card, enter (2)");
                    System.out.print(" - To delete a credit card, enter (3)\n: ");
                    Integer selection = scanner.nextInt(); scanner.nextLine(); // Check in case there is a problem here
                    if (selection == 1) {
                        System.out.print("\nEnter your email: "); System.out.flush(); String email = scanner.nextLine();
                        // Check for the corresponding agent to get the correct renter ID
                        PaymentAddressInfo.checkAgentEmail(email);
                        System.out.print("\nEnter email of the agent you want this credit card under: "); String agentEmail = scanner.nextLine();
                        System.out.print("Enter your credit card number: "); System.out.flush(); String ccNumber = scanner.nextLine();
                        System.out.print("Enter your credit card expiration date in format (YEAR-MONTH-DAY): "); System.out.flush(); String expDate = scanner.nextLine();
                        System.out.print("Enter your credit card ccv number: "); System.out.flush(); String ccv = scanner.nextLine();
                        PaymentAddressInfo.addCreditCard(email, agentEmail, ccNumber, expDate, ccv);
                    } else if (selection == 2) {
                        // Enter email
                        System.out.print("\nEnter your email: "); System.out.flush(); String email = scanner.nextLine();
                        // Show the cc number and the date attached to the email
                        PaymentAddressInfo.listCC(email);
                        System.out.print("\nEnter the number of the credit card you want to update: "); System.out.flush(); String ccNumber = scanner.nextLine();
                        System.out.print("\nEnter the new credit card number: "); System.out.flush(); String newCCNumber = scanner.nextLine();
                        System.out.print("Enter the new expiration date: "); System.out.flush(); String expDate = scanner.nextLine();
                        System.out.print("Enter the new ccv number: "); System.out.flush(); String ccv = scanner.nextLine();
                        // Select which cc to modify by ccnumber
                        PaymentAddressInfo.modifyCreditCard(email, ccNumber, newCCNumber, expDate, ccv);
                    } else if (selection == 3) {
                        System.out.print("\nEnter your email: "); System.out.flush(); String email = scanner.nextLine();
                        // Show the cc numbers and the dates attached to the email
                        PaymentAddressInfo.listCC(email);
                        System.out.print("\nEnter the number of the credit card you want to delete: "); System.out.flush(); String ccNumber = scanner.nextLine();
                        PaymentAddressInfo.deleteCreditCard(email, ccNumber);
                    }
                }
            } else if (input == 3) {
                System.out.println("\n--- Book a Property ---");

                System.out.print("Enter your email: ");
                System.out.flush();
                String email = scanner.next();
                scanner.nextLine();

                // Show them their cards so they know which number to type
                PaymentAddressInfo.listCC(email);

                System.out.print("Enter the Credit Card Number to use: ");
                System.out.flush();
                String ccNumber = scanner.nextLine();

                System.out.print("Enter Property ID to book: ");
                System.out.flush();
                int propId = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter Start Date (YYYY-MM-DD): ");
                System.out.flush();
                String startDate = scanner.nextLine();

                System.out.print("Enter End Date (YYYY-MM-DD): ");
                System.out.flush();
                String endDate = scanner.nextLine();

                Booking.bookProperty(email, propId, startDate, endDate, ccNumber);
            }
            else if (input == 0) {
                scanner.close();
                System.exit(0);
            }
            System.out.println();
        }
    }
}
