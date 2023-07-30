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

        // Declare variables
        Logger logger = Logger.getAnonymousLogger();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection(CONNECTION, USER, PASS);

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
                    case 1 -> handleOption1(scanner, conn);
                    case 2 -> handleOption2(scanner, conn);
                    case 3 -> handleOption3(scanner, conn);
                    case 4 -> handleOption4(scanner, conn);
                    case 5 -> handleOption5(scanner, conn);
                    case 6 -> handleOption6(scanner, conn);
                    case 7 -> handleOption7(scanner, conn);
                    case 8 -> handleOption8(scanner, conn);
                    case 0 -> {
                        System.out.println("Exiting the application...");
                        exit = true;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
            // Close the scanner
            scanner.close();
            // Close the connection
            conn.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQLException: ", e);
        }
    }


    // Create a new user
    private static void handleOption1(Scanner scanner, Connection conn) throws SQLException {
        System.out.println("\n\n");

        System.out.println("Please enter a username (max 16 characters)*: ");
        String username = scanner.nextLine();
        // Check in database to see if username exists
        if (checkUsernameExists(username, conn)) {
            System.out.println("Username exists. Please try again.");
            printBackToMainMenu();
            return;
        }

        System.out.println("Please enter a password (max 16 characters): ");
        String password = scanner.nextLine();

        System.out.println("Please enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.println("Please enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.println("Please enter your address: ");
        String address = scanner.nextLine();

        System.out.println("Please enter your occupation: ");
        String occupation = scanner.nextLine();

        System.out.println("Please enter your SSN (no dashes): ");
        String ssn = scanner.nextLine();

        System.out.println("Please enter your credit card number (no dashes) [optional]: ");
        String creditCard = scanner.nextLine();

        System.out.println("Please enter your birth date (YYYY-MM-DD): ");
        String birthDate = scanner.nextLine();
        // Make sure that user is at least 18 years old
        if (LocalDate.parse(birthDate).isAfter(LocalDate.now().minusYears(18))) {
            System.out.println("You must be at least 18 years old to create an account.");
            printBackToMainMenu();
            return;
        }

        // Create a new user object
        User newUser = new User(username, password, firstName, lastName, address, occupation, ssn, creditCard, birthDate);
        // Do one final check to make sure that all fields are valid
        String validation = newUser.validateData();
        if (!validation.equals("pass")) {
            System.out.println(validation);
            printBackToMainMenu();
            return;
        }
        //Push the new user to the database
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users VALUES " + newUser.sqlInsertString());
        stmt.execute();
        System.out.println("New user '" + newUser.username + "' created successfully!");

        stmt.close();
        printBackToMainMenu();
    }


    // Book a listing
    private static void handleOption2(Scanner scanner, Connection conn) {
        System.out.println("\n\n");
        int choice;

        while (true) {
            System.out.println("Would you like to (1) search for a listing or (2) enter a listing ID to book it?");
            System.out.print("Enter your choice (Enter 0 to return to main menu): ");
            choice = scanner.nextInt();
            scanner.nextLine();

            // Make sure that the user input is an integer and is within the range of the menu
            if (choice < 0 || choice > 2) {
                System.out.println("Invalid choice. Please try again.\n");
            } else {
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
            // Ask user for username and make sure they have a credit card on file
                // If not, ask them to enter a credit card and update in database
            // Ask user for information, time, etc.
            // Create a new booking object
            // Push the new booking to the database
        } else if (choice == 2) {
            //Search for a listing using listing ID
            System.out.println("Please enter the listing ID: ");
            String listingID = scanner.nextLine();
            // Show the listing info and the available dates
            // Make sure that the user books one day or a row of days
            // Ask user for username and make sure they have a credit card on file
                // If not, ask them to enter a credit card and update in database
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
    private static void handleOption3(Scanner scanner, Connection conn) {
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
    private static void handleOption4(Scanner scanner, Connection conn) throws SQLException {
        System.out.println("\n\n");


        // Ask user for username
        System.out.println("Please enter a username (max 16 characters) : ");
        String username = scanner.nextLine();
        // Check in database to see if username exists
        if (!checkUsernameExists(username, conn)) {
            System.out.println("Username does not exist. Please try again.");
            printBackToMainMenu();
            return;
        }

        // Ask user for password
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        // Check to see if password matches with username
        if (!verifyLogin(username, password, conn)) {
            System.out.println("Password does not match. Please try again.");
            printBackToMainMenu();
            return;
        }

        // Ask user for listing type
        System.out.println("Please enter the listing type: ");
        System.out.println("(The valid listing types are: 'House', 'Apartment', 'Guesthouse', and 'Hotel')");
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
        System.out.println("Please enter the postal code (ex. A1A 1A1): ");
        String postalCode = scanner.nextLine();

        // Ask user for longitude
        System.out.println("Please enter the longitude (up to 6 decimal places): ");
        float longitude = scanner.nextFloat();

        // Ask user for latitude
        System.out.println("Please enter the latitude (up to 6 decimal places): ");
        float latitude = scanner.nextFloat();

        //Ask user for amenities
        int amenityCount = 0;
        String amenities = recordAmenities(scanner, conn);

        // Create a new listing object
        Listing newListing = new Listing(username, listingType, address, country, city, postalCode, 0,longitude, latitude, amenities);

        // Ask user for price
        // Run the function to estimate a price
        System.out.println("Based on the inputted information, we recommend this price: " + newListing.estimatePrice());
        System.out.println("Please enter your desired price per night: ");
        float price = scanner.nextFloat();

        // Push the new listing to the database
        String validation = newListing.validateData();
        if (!validation.equals("pass")) {
            System.out.println(validation);
            printBackToMainMenu();
            return;
        }
        //Push the new listing to the database
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Listings VALUES " + newListing.sqlInsertString());
        stmt.execute();

        // Get the auto generated listing ID from database
        int listingID = getListingId(username, listingType, address, country, city, postalCode, price, longitude, latitude, amenities, conn);
        newListing.setListingID(listingID);

        System.out.println("New listing with ID: '" + newListing.listingID + "' created successfully!\n");

        stmt.close();


        // Ask the user for dates
        System.out.println("Please enter the availability dates for your listing");
        System.out.println("Note that you must provide a time range, and must be a minimum of 1 night.");
        System.out.print("Enter the start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter the end date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        // TODO: Create the new Day object, create validate functions for them, figure out String to LocalDate,
        // TODO: Figure out LocalDate to SQL Date, push the Day to the database
        // TODO: Ask the user if they would like to add more availability dates



        // Return to main menu
        printBackToMainMenu();
    }


    // Remove a listing
    private static void handleOption5(Scanner scanner, Connection conn) {
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
    private static void handleOption6(Scanner scanner, Connection conn) {
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
    private static void handleOption7(Scanner scanner, Connection conn) {
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
        else if (choice == 2) {
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
    private static void handleOption8(Scanner scanner, Connection conn) {
        System.out.println("\n\n");


        System.out.println("Viewing Reports... (to be implemented)");


        printBackToMainMenu();

    }

    private static boolean checkUsernameExists(String username, Connection conn) throws SQLException {
        // Check in database to see if username exists
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            rs.close();
            stmt.close();
            return true;
        }
        rs.close();
        stmt.close();
        return false;
    }

    private static boolean verifyLogin(String username, String password, Connection conn) throws SQLException{
        // Check to see if password matches with username
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?;");
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        // If the username and password match, return true
        if (rs.next()) {
            rs.close();
            stmt.close();
            return true;
        }

        rs.close();
        stmt.close();
        return false;
    }

    public static String recordAmenities(Scanner scanner, Connection conn) throws SQLException {
        boolean exit = false;
        String amenities = "";
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Amenities WHERE amenity = ?;");

        while (!exit){
            System.out.println("Please enter the name of an amenity to include in your listing: ");
            String amenity = scanner.nextLine();

            // Check if amenity exists in the database
            stmt.setString(1, amenity);
            ResultSet rs = stmt.executeQuery();

            // If the amenity exists, concat the id to the string
            if (rs.next()){
                String amenityID = rs.getString("amenityID");
                //Concat amenityID and a comma to the string
                amenities = amenities.concat(amenityID + ",");
                System.out.println("Amenity " + amenity + " added successfully!");
            }
            else {
                System.out.println("Amenity does not exist. Please try again.");
                continue;
            }

            // Ask if the user wants to add another amenity
            System.out.println("Would you like to add another amenity? (y/n)");
            String answer = scanner.nextLine();

            if (answer.equals("n")){
                exit = true;
            }
        }
        stmt.close();

        return amenities;
    }

    private static int getListingId(String username, String listingType, String address, String country, String city, String postalCode, float price, float longitude, float latitude, String amenities, Connection conn) throws SQLException{
        int listingID = 0;

        PreparedStatement stmt = conn.prepareStatement("SELECT listingID FROM Listings WHERE hostID = ? AND listingType = ? AND address = ? AND country = ? AND city = ? AND postalCode = ? AND price = ? AND longitude = ? AND latitude = ? AND amenities = ?;");
        stmt.setString(1, username);
        stmt.setString(2, listingType);
        stmt.setString(3, address);
        stmt.setString(4, country);
        stmt.setString(5, city);
        stmt.setString(6, postalCode);
        stmt.setFloat(7, price);
        stmt.setFloat(8, longitude);
        stmt.setFloat(9, latitude);
        stmt.setString(10, amenities);

        ResultSet rs = stmt.executeQuery();
        rs.next();
        listingID = rs.getInt("listingID");

        rs.close();
        stmt.close();
        return listingID;
    }

    // Helper function for printing back to main menu message
    private static void printBackToMainMenu() {
        System.out.println("Bringing back to main menu...\n\n\n");
        System.out.println("------------------------------------------------------------");
    }
}

