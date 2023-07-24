import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Main {

    private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
    private static final String CONNECTION = "jdbc:mysql://127.0.0.1/projectdb";

    public static void main(String[] args) throws ClassNotFoundException {
        // Register JDBC driver
        Class.forName(dbClassName);
        final String USER = "root";
        final String PASS = "123";
        //System.out.println("Connecting to database...");

        // Declare variables
        Logger logger = Logger.getAnonymousLogger();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;



        // Start the application
        System.out.println("\n------------------------------------------------------------");
        System.out.println("Welcome to the MyBnB application!");

        // Display prompts
        while (!exit) {
            System.out.println("\n");
            System.out.println("Please enter the corresponding number to select an option.");
            System.out.println("1. Create a new user");
            System.out.println("2. Book a listing");
            System.out.println("3. Cancel a booking");
            System.out.println("4. Create a new listing");
            System.out.println("5. Remove a listing");
            System.out.println("6. Edit a listing");
            System.out.println("7. Write a review");
            System.out.println("8. Reports");
            System.out.println("0. Exit the application\n");

            // Get user input
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Make sure that the user input is an integer and is within the range of the menu
            if (choice < 0 || choice > 8) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            // Handle the user's choice
            switch (choice) {
                case 1 -> handleOption1(scanner);
                case 2 -> handleOption2(scanner);
                case 3 -> handleOption3(scanner);
                case 4 -> handleOption4(scanner);
                case 5 -> handleOption5(scanner);
                case 6 -> handleOption6(scanner);
                case 7 -> handleOption7(scanner);
                case 8 -> handleOption8(scanner);
                case 0 -> {
                    System.out.println("Exiting the application...");
                    exit = true;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        // Close the scanner
        scanner.close();
    }


    // Create a new user
    private static void handleOption1 (Scanner scanner){
        System.out.println("\n\n");

        System.out.println("Please enter a username");
        String username = scanner.nextLine();
        // Check in database to see if username exists

        System.out.println("Please enter a password");
        String password = scanner.nextLine();

        System.out.println("Please enter your first name");
        String firstName = scanner.nextLine();

        System.out.println("Please enter your last name");
        String lastName = scanner.nextLine();

        System.out.println("Please enter your address");
        String address = scanner.nextLine();

        System.out.println("Please enter your occupation");
        String occupation = scanner.nextLine();

        System.out.println("Please enter your SSN (no dashes)");
        String ssn = scanner.nextLine();

        System.out.println("Please enter your credit card number (no dashes)");
        String creditCard = scanner.nextLine();

        System.out.println("Please enter your birth date (YYYY-MM-DD)");
        String birthDate = scanner.nextLine();
        // Make sure that user is at least 18 years old
        if (LocalDate.parse(birthDate).isAfter(LocalDate.now().minusYears(18))) {
            System.out.println("You must be at least 18 years old to create an account.");
            printBackToMainMenu();
            return;
        }

        // Create a new user object
        User newUser = new User(username, password, firstName, lastName, address, occupation, ssn, creditCard, birthDate);
        //Push the new user to the database
        System.out.println("New user " + newUser.username + " created successfully!");

        printBackToMainMenu();
    }


    // Book a listing
    private static void handleOption2 (Scanner scanner){
        System.out.println("\n\n");

        int choice;

        while (true){
            System.out.println("Would you like to (1) search for a listing or (2) enter a listing ID to book it?");
            System.out.print("Enter your choice (Enter 0 to return to main menu): ");
            choice = scanner.nextInt();
            scanner.nextLine();

            // Make sure that the user input is an integer and is within the range of the menu
            if (choice < 0 || choice > 2) {
                System.out.println("Invalid choice. Please try again.\n");
            }
            else{
                break;
            }
        }

        // Handle the user's choice
        if (choice == 1) {
            //Search for a listing using location
            System.out.println("Please enter the longitude: ");
            float longitude = scanner.nextFloat();
            System.out.println("Please enter the latitude: ");
            float latitude = scanner.nextFloat();

            // Send information to the database to search for listings
            // Display the listings
            // Ask the user to select a listing
            // Show the listing info and the available dates
            // Make sure that the user books one day or a row of days
            // Ask user for information, time, etc.
            // Create a new booking object
            // Push the new booking to the database
        }
        else if (choice == 2) {
            //Search for a listing using listing ID
            System.out.println("Please enter the listing ID: ");
            String listingID = scanner.nextLine();
            // Show the listing info and the available dates
            // Make sure that the user books one day or a row of days
            // Ask user for information, time, etc.
            // Create a new booking object
            // Push the new booking to the database
        }

        // Ask if the user wants to book another listing
        System.out.println("Would you like to book another listing? (Y/N)");
        String answer = scanner.nextLine();
        // While loop the entire function

        printBackToMainMenu();
    }


    // Cancel a booking
    private static void handleOption3 (Scanner scanner){
        System.out.println("\n\n");


        // Ask user for booking ID
        System.out.println("Please enter the booking ID: ");
        String bookingID = scanner.nextLine();

        // Check if booking ID exists in the database
        // If it does, delete the booking
        // If it doesn't, print error message

        printBackToMainMenu();
    }


    // Create a new listing
    private static void handleOption4(Scanner scanner){
        System.out.println("\n\n");


        // Ask user for username
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        // Check if username exists in the database

        // Ask user for listing type
        System.out.println("Please enter the listing type: ");
        String listingType = scanner.nextLine();

        // Ask user for address
        System.out.println("Please enter the address: ");
        String address = scanner.nextLine();

        // Ask user for country
        System.out.println("Please enter the country: ");
        String country = scanner.nextLine();

        // Ask user for city
        System.out.println("Please enter the city: ");
        String city = scanner.nextLine();

        // Ask user for postal code
        System.out.println("Please enter the postal code: ");
        String postalCode = scanner.nextLine();

        // Ask user for longitude
        System.out.println("Please enter the longitude: ");
        float longitude = scanner.nextFloat();

        // Ask user for latitude
        System.out.println("Please enter the latitude: ");
        float latitude = scanner.nextFloat();

        // Ask user for price
        // Run the function to estimate a price
        System.out.println("Based on the inputted information, we recommend this price: ");
        System.out.println("Please enter your desired price per night: ");
        float price = scanner.nextFloat();

        // Create a new listing object


        // Ask the user for dates
        System.out.println("Please enter the availability dates for your listing");
        System.out.println("Do you want to (1) enter a time range or (2) enter specific dates?");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        // If 1, ask for start date and end date
        // If 2, repeatedly ask for dates until the user enters 0

        // Add the Days to the database with the associated listing ID

        // Return to main menu
        printBackToMainMenu();
    }


    // Remove a listing
    private static void handleOption5(Scanner scanner){
        System.out.println("\n\n");


        // Ask user for listing ID
        System.out.println("Please enter the listing ID: ");
        String listingID = scanner.nextLine();

        // Ask user for username
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        // Through the username, you will find out whether the user is renter or host
        // If user is renter, and they made the booking, they can delete the listing
        // If user is host, and they own the listing, they can delete the listing
        // If user is renter, and they did not make the booking, they cannot delete the listing

        // Check if listing ID exists in the database
        // Make sure that upcoming bookings are cancelled
        // Delete the listing

        printBackToMainMenu();
    }

    // Edit a listing
    private static void handleOption6(Scanner scanner){
        System.out.println("\n\n");


        // Ask user for listing ID
        System.out.println("Please enter the listing ID: ");
        String listingID = scanner.nextLine();
        // Check if listing ID exists in the database

        // Ask user for username
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        // Through the username, you will find out whether the user is renter or host
        // If they are not the host, they cannot edit the listing

        System.out.println("Would you like to (1) update price or (2) update availability?");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        // If 1, ask for when you want the price change to take effect
        // Make sure that the date entered is not already booked
        // Ask for the new price
        // Update the price in the database

        // If 2, ask for when you want the availability change to take effect
        // Make sure that the date entered is not already booked (if it is, tell host to cancel booking first)
        // Ask for the new availability
        // Update the availability in the database


        printBackToMainMenu();
    }


    // Write a review
    private static void handleOption7(Scanner scanner){
        System.out.println("\n\n");


        System.out.println("What type of review would you like to write?");
        System.out.println("1. Write a review for a listing");
        System.out.println("2. Write a review for a renter");
        System.out.println("0. Return to main menu\n");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        // Make sure that the user input is an integer and is within the range of the menu
        if (choice < 0 || choice > 2) {
            System.out.println("Invalid choice. Please try again.");
            printBackToMainMenu();
            return;
        }

        // If 1, write review for listing/host as renter
        if (choice == 1) {
            System.out.println("Please enter the listing ID: ");
            String listingID = scanner.nextLine();
            // Check if listing ID exists in the database

            // Ask user for username
            System.out.println("Please enter your username: ");
            String username = scanner.nextLine();
            //Make sure that the user has booked the listing within the past year

            // Ask user for listing rating
            System.out.println("Please rate your experience of the home (1-5): ");
            int listingRating = scanner.nextInt();
            scanner.nextLine();

            // Ask user for host rating
            System.out.println("Please rate your experience with the host (1-5): ");
            int hostRating = scanner.nextInt();
            scanner.nextLine();

            // Ask user for comment
            System.out.println("Please enter any comments: ");
            String comment = scanner.nextLine();

            // Create a new review object
            // Push the new review to the database
        }
        // If 2, write review for a renter as host
        else if (choice == 2){
            // Ask user for username
            System.out.println("Please enter your username: ");
            String username = scanner.nextLine();

            // Ask user for renter username
            System.out.println("Please enter the renter's username: ");
            String renterUsername = scanner.nextLine();
            // Make sure that the renter has booked a listing from the host before

            // Ask user for renter rating
            System.out.println("Please rate your experience with the renter (1-5): ");
            int renterRating = scanner.nextInt();
            scanner.nextLine();

            // Ask user for comment
            System.out.println("Please enter any comments: ");
            String comment = scanner.nextLine();

            // Create a new review object
            // Push the new review to the database
        }

        printBackToMainMenu();
    }


    // Reports
    private static void handleOption8(Scanner scanner){
        System.out.println("\n\n");


        System.out.println("Viewing Reports... (to be implemented)");


        printBackToMainMenu();

    }







    // Helper function for printing back to main menu message
    private static void printBackToMainMenu() {
        System.out.println("Bringing back to main menu...\n\n\n");
        System.out.println("------------------------------------------------------------");
    }
}

