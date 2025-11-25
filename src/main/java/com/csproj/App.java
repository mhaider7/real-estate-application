package com.csproj;
import java.util.Scanner;

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
                    System.out.println("agent");
                    Registration.registerAgent();
                } else if (user.equals("R") || user.equals("r")) {
                    // Call the class that will prompt the renter account
                    System.out.println("renter");
                }
            } else if (input == 0) {
                scanner.close();
                System.exit(0);
            }
        }
    }
}
