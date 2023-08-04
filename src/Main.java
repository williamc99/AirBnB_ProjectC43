import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                System.out.println("8. Delete a user");
                System.out.println("9. Reports");
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
                    case 9 -> handleOption9(scanner, conn);
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
        stmt.setDate(1, Date.valueOf(newUser.getBirthDate()));
        stmt.execute();
        System.out.println("New user '" + newUser.username + "' created successfully!");

        stmt.close();
        printBackToMainMenu();
    }


    // Book a listing
    private static void handleOption2(Scanner scanner, Connection conn) throws SQLException {
        System.out.println("\n\n");
        int choice;

        while (true) {
            System.out.println("Would you like to: ");
            System.out.println("(1) Search for a listing using longitude/latitude");
            System.out.println("(2) Search for a listing using postal code");
            System.out.println("(3) Search for a listing using exact address");
            System.out.print("Enter your choice (Enter 0 to return to main menu): ");
            choice = scanner.nextInt();
            scanner.nextLine();

            // Make sure that the user input is an integer and is within the range of the menu
            if (choice < 0 || choice > 3) {
                System.out.println("Invalid choice. Please try again.\n");
            } else if (choice == 0) {
                printBackToMainMenu();
                return;
            } else {
                break;
            }
        }

        // Declare array list to store listings
        ArrayList<Listing> listings = new ArrayList<>();

        // Longitude/Latitude Search
        if (choice == 1) {
            // Ask user for longitude/latitude
            System.out.println("Please enter the longitude (up to 6 decimal places): ");
            float longitude = scanner.nextFloat();
            scanner.nextLine();
            System.out.println("Please enter the latitude (up to 6 decimal places): ");
            float latitude = scanner.nextFloat();
            scanner.nextLine();

            // Ask user for a distance radius
            int radius = 25;
            System.out.println("Please enter a distance radius (in km): ");
            System.out.println("(The default is 25 km)");
            radius = scanner.nextInt();
            scanner.nextLine();

            // Ask user how they would like to order the listings
            int orderChoice = 1;
            while (true) {
                System.out.println("How would you like to order the listings?");
                System.out.println("(1) Distance");
                System.out.println("(2) Price (ascending)");
                System.out.println("(3) Price (descending)");
                System.out.print("Enter your choice: ");
                orderChoice = scanner.nextInt();
                scanner.nextLine();

                if (orderChoice < 1 || orderChoice > 3) {
                    System.out.println("Invalid choice. Please try again.");
                } else {
                    break;
                }
            }

            // Get all listings and iterate through them
            // This query uses the Haversine formula to calculate distance between two latitude/longitude points
            // The 6371 constant is the radius of the Earth in km (makes sure that the distance is in km)
            String sql = "SELECT * FROM ( SELECT *, ( 6371 * acos(cos(radians(latitude)) * " +
                    "cos(radians(?)) * cos(radians(?) - radians(longitude) ) + sin(radians(latitude)) * " +
                    "sin(radians(?))) ) AS distance FROM Listings) AS x WHERE distance < ?";

            if (orderChoice == 1) {
                sql += " ORDER BY distance;";
            } else {
                sql += " ORDER BY ?;";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, latitude);
            stmt.setFloat(2, longitude);
            stmt.setFloat(3, latitude);
            stmt.setInt(4, radius);

            if (orderChoice == 2) {
                stmt.setString(5, "price");
            } else if (orderChoice == 3) {
                stmt.setString(5, "price DESC");
            }
            ResultSet rs = stmt.executeQuery();

            // Iterate through all listings, create listing objects from them and add to the array list
            while (rs.next()) {
                Listing listing = new Listing(rs.getString("hostID"),
                        rs.getString("listingType"), rs.getString("address"),
                        rs.getString("country"), rs.getString("city"),
                        rs.getString("postalCode"), rs.getFloat("price"),
                        rs.getFloat("longitude"), rs.getFloat("latitude"),
                        rs.getString("amenities"));
                listing.setListingID(rs.getInt("listingID"));
                listing.setDistance(rs.getFloat("distance"));
                listing.setAmenitiesList(getAmenities(conn, listing.amenities));
                listings.add(listing);
            }
            rs.close();
            stmt.close();
        }
        // Search by postal code
        else if (choice == 2) {
            // Ask user to enter postal code
            System.out.println("Please enter the postal code (ex. A1A 1A1): ");
            String postalCode = scanner.nextLine();

            // Search for exact postal code first
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Listings WHERE postalCode = ?;");
            stmt.setString(1, postalCode);
            ResultSet rs = stmt.executeQuery();

            // Iterate through all listings, create listing objects from them and add to the array list
            while (rs.next()) {
                Listing listing = new Listing(rs.getString("hostID"),
                        rs.getString("listingType"), rs.getString("address"),
                        rs.getString("country"), rs.getString("city"),
                        rs.getString("postalCode"), rs.getFloat("price"),
                        rs.getFloat("longitude"), rs.getFloat("latitude"),
                        rs.getString("amenities"));
                listing.setListingID(rs.getInt("listingID"));
                listing.setAmenitiesList(getAmenities(conn, listing.amenities));
                listings.add(listing);
            }

            // Ask user if they would like to search adjacent postal codes
            System.out.println("Would you like to include adjacent postal codes in your search? (y/n)");
            String answer = scanner.nextLine();

            if (answer.equals("y") || answer.equals("Y")) {
                // Get the first 3 characters of the postal code
                String firstThree = postalCode.substring(0, 3);

                // Get all listings with the same first 3 characters
                stmt = conn.prepareStatement("SELECT * FROM Listings WHERE postalCode LIKE ?;");
                stmt.setString(1, firstThree + "%");
                rs = stmt.executeQuery();

                // Store listings into array list
                storeListings(conn, rs, listings);

                rs.close();
                stmt.close();
            }
        }
        // Search by exact address
        else {
            // Ask user for address
            System.out.println("Please enter the EXACT address: ");
            String address = scanner.nextLine();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Listings WHERE address = ?;");
            stmt.setString(1, address);
            ResultSet rs = stmt.executeQuery();

            // Store listings into array list
            storeListings(conn, rs, listings);

            rs.close();
            stmt.close();
        }


        // Filters
        // Ask user if they would like to filter by amenities
        System.out.println("Would you like to filter by amenities? (y/n)");
        String answer = scanner.nextLine();

        if (answer.equals("y") || answer.equals("Y")) {
            // Ask user for amenities
            String amenitiesString = recordAmenities(scanner, conn);
            // Split amenitiesString by comma, convert each value to int and store in an int array
            String[] amenitiesArray = amenitiesString.split(",");
            int[] amenitiesIDArr = new int[amenitiesArray.length];
            for (int i = 0; i < amenitiesArray.length; i++) {
                amenitiesIDArr[i] = Integer.parseInt(amenitiesArray[i]);
            }

            // Iterate through ID array and create the SQL addon query
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < amenitiesIDArr.length; i++) {
                if (i == 0) {
                    builder.append("amenities LIKE '%").append(amenitiesIDArr[i]).append("%'");
                } else {
                    builder.append(" OR amenities LIKE '%").append(amenitiesIDArr[i]).append("%'");
                }
            }
            String sqlAddOn = builder.toString();
            String sql = "SELECT * FROM Listings WHERE listingID = ? AND (" + sqlAddOn + ");";

            // Filter the listings
            ArrayList<Listing> listingsToDelete = new ArrayList<>();
            for (Listing listing : listings) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, listing.listingID);
                ResultSet rs = stmt.executeQuery();
                if (!rs.isBeforeFirst()) {
                    listingsToDelete.add(listing);
                }
            }
            listings.removeAll(listingsToDelete);
        }

        // Ask user if they would like to filter by price
        System.out.println("Would you like to filter by price? (y/n)");
        answer = scanner.nextLine();

        if (answer.equals("y") || answer.equals("Y")) {
            // Ask user for price range
            System.out.println("Please enter the minimum price: ");
            float minPrice = scanner.nextFloat();
            scanner.nextLine();
            System.out.println("Please enter the maximum price: ");
            float maxPrice = scanner.nextFloat();
            scanner.nextLine();

            // Filter the listings
            ArrayList<Listing> listingsToDelete = new ArrayList<>();
            for (Listing listing : listings) {
                String sql = "SELECT * FROM Listings WHERE listingID = ? AND price BETWEEN ? AND ?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, listing.listingID);
                stmt.setFloat(2, minPrice);
                stmt.setFloat(3, maxPrice);
                ResultSet rs = stmt.executeQuery();
                if (!rs.isBeforeFirst()) {
                    listingsToDelete.add(listing);
                }
            }
            listings.removeAll(listingsToDelete);
        }

        // Ask user if they would like to filter by availability range
        System.out.println("Would you like to filter by availability range? (y/n)");
        answer = scanner.nextLine();

        if (answer.equals("y") || answer.equals("Y")) {
            // Ask user for start date
            System.out.println("Please enter the start date (YYYY-MM-DD): ");
            String startDate = scanner.nextLine();
            // Ask user for end date
            System.out.println("Please enter the end date (YYYY-MM-DD): ");
            String endDate = scanner.nextLine();

            // Filter the listings
            //TODO: Test this filter
            ArrayList<Listing> listingsToDelete = new ArrayList<>();
            for (Listing listing : listings) {
                // Queries for listings that are available in the date range
                String sql = "SELECT * FROM Listings WHERE listingID = ? AND listingID NOT IN " +
                        "(SELECT listingID FROM Bookings WHERE startDate <= ? AND endDate >= ?);";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, listing.listingID);
                stmt.setDate(2, Date.valueOf(endDate));
                stmt.setDate(3, Date.valueOf(startDate));
                ResultSet rs = stmt.executeQuery();
                // If our selected listing does not show up in this query, remove it from the list
                if (!rs.isBeforeFirst()) {
                    listingsToDelete.add(listing);
                }
            }
            listings.removeAll(listingsToDelete);
        }

        // If list is empty, print error message
        if (listings.isEmpty()) {
            System.out.println("No listings found with the specified search parameters.");
            printBackToMainMenu();
            return;
        }

        // Iterate through list and print out the listings
        for (Listing listing : listings) {
            System.out.println("\n");
            if (choice == 1) {
                displayListingWithDistance(listing);
            } else {
                displayListing(listing);
            }
        }


        // Choosing and booking a listing
        System.out.print("\n\n");
        boolean exit = false;
        int chosenID = 0;
        Listing chosenListing = new Listing();
        while (!exit) {
            System.out.println("Enter the listing ID of the listing you would like to book: ");
            chosenID = scanner.nextInt();
            scanner.nextLine();

            // Check if any of the listings correspond to the chosen ID
            for (Listing listing : listings) {
                if (listing.listingID == chosenID) {
                    chosenListing = listing;
                    exit = true;
                    break;
                }
            }
            if (!exit) {
                System.out.println("The listing ID you entered is invalid. Please try again.");
            }
        }

        // Ask user for username
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();

        if (loginUser(scanner, conn, username)) {
            printBackToMainMenu();
            return;
        }

        // Check if the renter is also the host of the listing
        if (username.equals(chosenListing.hostID)) {
            System.out.println("You cannot book your own listing!.");
            printBackToMainMenu();
            return;
        }

        // Check if user has a credit card on file
        checkCreditCard(scanner, conn, username);

        // Get rental dates from user
        exit = false;
        String startDate = "";
        String endDate = "";

        while (!exit) {
            System.out.print("Enter a rental start date (YYYY-MM-DD): ");
            startDate = scanner.nextLine();
            System.out.print("Enter a rental end date (YYYY-MM-DD): ");
            endDate = scanner.nextLine();

            // Check if the date range has already been booked
            if (!checkOverlapUnavailable(conn, LocalDate.parse(startDate), LocalDate.parse(endDate), chosenID)) {
                exit = true;
            }
        }

        // Create a new booking object
        Booking newBooking = new Booking(chosenID, username, "booked", LocalDate.parse(startDate), LocalDate.parse(endDate));
        newBooking.setPrice(chosenListing.getPrice());
        String validation = newBooking.validateData();
        if (!validation.equals("pass")) {
            System.out.println(validation);
            printBackToMainMenu();
            return;
        }

        //Push the new booking to the database
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Bookings VALUES(default, ?, ?, ?, NULL, ?, ?, ?)");
        stmt.setInt(1, newBooking.listingID);
        stmt.setString(2, newBooking.userID);
        stmt.setString(3, newBooking.status);
        stmt.setFloat(4, newBooking.price);
        stmt.setDate(5, Date.valueOf(newBooking.startDate));
        stmt.setDate(6, Date.valueOf(newBooking.endDate));
        stmt.execute();

        System.out.println("You have successfully booked listing with ID: " + chosenID + " from " + startDate + " to " + endDate + "\n");

        stmt.close();
        printBackToMainMenu();
    }


    // Cancel a booking
    private static void handleOption3(Scanner scanner, Connection conn) throws SQLException {
        System.out.println("\n\n");

        // Ask user for username
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();

        if (loginUser(scanner, conn, username)) {
            printBackToMainMenu();
            return;
        }

        int bookingID = 0;
        Booking booking = new Booking();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Bookings WHERE bookingID = ? AND status = 'booked';");
        while (true) {
            // Ask user for booking ID
            System.out.println("Please enter the booking ID : ");
            bookingID = scanner.nextInt();
            scanner.nextLine();

            // Check if booking ID exists in the database
            stmt.setInt(1, bookingID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                booking.setBookingID(rs.getInt("bookingID"));
                booking.setListingID(rs.getInt("listingID"));
                booking.setUserID(rs.getString("userID"));
                booking.setStatus(rs.getString("status"));
                booking.setPrice(rs.getFloat("price"));
                booking.setStartDate(rs.getDate("startDate").toLocalDate());
                booking.setEndDate(rs.getDate("endDate").toLocalDate());
                break;
            } else {
                System.out.println("The booking ID you entered is invalid. Please try again.");
            }
        }

        // There should be no way that a renter is also the host of a listing (handled in booking)
        // Check if user is a host
        String userType = "";
        stmt = conn.prepareStatement("SELECT * FROM Listings WHERE hostID = ? AND listingID = (SELECT listingID FROM bookings WHERE bookingID = ?);");
        stmt.setString(1, username);
        stmt.setInt(2, booking.bookingID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            userType = "host";
            System.out.println("You are the host of the booking with ID: " + booking.bookingID);
        }

        // Check if user is a renter
        stmt = conn.prepareStatement("SELECT * FROM Bookings WHERE userID = ? AND bookingID = ? AND status = 'booked';");
        stmt.setString(1, username);
        stmt.setInt(2, booking.bookingID);
        rs = stmt.executeQuery();

        if (rs.next()) {
            userType = "renter";
            System.out.println("You are a renter of the booking with ID: " + booking.bookingID);
        }

        if (userType.equals("")) {
            System.out.println("You are neither the host nor the renter of the booking with ID: " + booking.bookingID);
            printBackToMainMenu();
            return;
        }

        // Ask user if they would like to cancel the booking
        System.out.println("Are you sure you would like to cancel the booking with ID: " + booking.bookingID + "? (y/n)");
        String answer = scanner.nextLine();

        if (answer.equals("y") || answer.equals("Y")) {
            // Update the booking status to cancelled
            stmt = conn.prepareStatement("UPDATE Bookings SET status = 'cancelled', statusReason = ? WHERE bookingID = ?;");
            stmt.setString(1, userType);
            stmt.setInt(2, booking.bookingID);
            stmt.execute();

            System.out.println("The booking with ID: " + booking.bookingID + " has been cancelled.");
        }


        printBackToMainMenu();
    }


    // Create a new listing
    private static void handleOption4(Scanner scanner, Connection conn) throws SQLException {
        System.out.println("\n\n");

        // Ask user for username
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();

        if (loginUser(scanner, conn, username)) {
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
        scanner.nextLine();

        // Ask user for latitude
        System.out.println("Please enter the latitude (up to 6 decimal places): ");
        float latitude = scanner.nextFloat();
        scanner.nextLine();

        //Ask user for amenities
        String amenities = recordAmenities(scanner, conn);

        // Create a new listing object
        Listing newListing = new Listing(username, listingType, address, country, city, postalCode, 0, longitude, latitude, amenities);

        // Ask user for price
        // Run the function to estimate a price
        System.out.println("Based on the inputted information, we recommend this price: $" + newListing.estimatePrice());
        System.out.println("Please enter your desired price per night: ");
        float price = scanner.nextFloat();
        scanner.nextLine();

        newListing.setPrice(price);

        // Push the new listing to the database
        String validation = newListing.validateData();
        if (!validation.equals("pass")) {
            System.out.println(validation);
            printBackToMainMenu();
            return;
        }
        //Push the new listing to the database
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Listings VALUES(default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        stmt.setString(1, newListing.hostID);
        stmt.setString(2, newListing.listingType);
        stmt.setString(3, newListing.address);
        stmt.setString(4, newListing.country);
        stmt.setString(5, newListing.city);
        stmt.setString(6, newListing.postalCode);
        stmt.setFloat(7, newListing.price);
        stmt.setFloat(8, newListing.longitude);
        stmt.setFloat(9, newListing.latitude);
        stmt.setString(10, newListing.amenities);
        stmt.execute();

        // Get the auto generated listing ID from database
        int listingID = getListingId(username, listingType, address, country, city, postalCode, price, longitude, latitude, amenities, conn);
        newListing.setListingID(listingID);
        System.out.println("New listing with ID: '" + newListing.listingID + "' created successfully!\n");

        // Ask the user for dates that they are unavailable
        System.out.println("By default, we assume you are available to rent all year round.");
        System.out.println("Would you like to add a date range where you are unavailable? (y/n): ");
        String answer = scanner.nextLine();

        if (answer.equals("y")) {
            boolean exit = false;
            while (!exit) {
                System.out.print("Enter the start date (YYYY-MM-DD): ");
                String startDate = scanner.nextLine();
                System.out.print("Enter the end date (YYYY-MM-DD): ");
                String endDate = scanner.nextLine();

                // Check if the date range has already been set to unavailable
                if (checkOverlapUnavailable(conn, LocalDate.parse(startDate), LocalDate.parse(endDate), listingID)) {
                    continue;
                }

                // Create a new booking object
                Booking newBooking = new Booking(listingID, username, "unavailable", LocalDate.parse(startDate), LocalDate.parse(endDate));
                validation = newBooking.validateData();
                if (!validation.equals("pass")) {
                    System.out.println(validation);
                    printBackToMainMenu();
                    return;
                }

                //Push the new booking to the database
                stmt = conn.prepareStatement("INSERT INTO Bookings VALUES(default, ?, ?, ?, NULL, NULL, ?, ?)");
                stmt.setInt(1, newBooking.listingID);
                stmt.setString(2, newBooking.userID);
                stmt.setString(3, newBooking.status);
                stmt.setDate(4, Date.valueOf(newBooking.startDate));
                stmt.setDate(5, Date.valueOf(newBooking.endDate));
                stmt.execute();

                System.out.println("The date range: " + startDate + " to " + endDate +
                        " has been set to unavailable for listing with ID: " + listingID + "\n");

                System.out.println("Would you like to add another availability? (y/n): ");
                String choice = scanner.nextLine();
                if (choice.equals("n")) {
                    exit = true;
                }
                System.out.println("\n");
            }
            stmt.close();
        }
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
        // Ask for the new price
        // Ask for when you want this price to take effect
        // Make sure that the date entered is not already booked
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


    // Delete a user
    private static void handleOption8(Scanner scanner, Connection conn) throws SQLException {
        System.out.println("\n\n");

//        List<DistancePair> distancePairs = new ArrayList<>();
//        Listing l1 = new Listing();
//        distancePairs.add(new DistancePair(l1, 1.0));
//        distancePairs.add(new DistancePair(l1, 1.5));
//        distancePairs.add(new DistancePair(l1, 1.3333));
//        distancePairs.add(new DistancePair(l1, 1.3334));
//
//
//        distancePairs.sort(new Comparator<DistancePair>() {
//            @Override
//            public int compare(DistancePair o1, DistancePair o2) {
//                return Double.compare(o1.getDistance(), o2.getDistance());
//            }
//        });
//
//        for (DistancePair d : distancePairs){
//            System.out.println(d.getListing());
//            System.out.println(d.getDistance());
//        }

//        ArrayList<String> arr = new ArrayList<>();
//        arr.add("1");
//        arr.add("2");
//        arr.add("3");
//        arr.add("4");
//
//        System.out.println(arr);


        printBackToMainMenu();

    }

    // Reports
    private static void handleOption9(Scanner scanner, Connection conn) {

        //TODO: Make a sync function that updates all the booked listings to finished if today's date is past the end date

        System.out.println("\n\n");
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

    private static boolean verifyLogin(String username, String password, Connection conn) throws SQLException {
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
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Amenities WHERE amenityName = ?;");

        while (!exit) {
            System.out.println("Please enter the name of an amenity: ");
            String amenity = scanner.nextLine();

            // Check if amenity exists in the database
            stmt.setString(1, amenity);
            ResultSet rs = stmt.executeQuery();

            // If the amenity exists, concat the id to the string
            if (rs.next()) {
                int amenityID = rs.getInt("amenityID");
                //Concat amenityID and a comma to the string
                amenities = amenities.concat(String.valueOf(amenityID) + ",");
                System.out.println("Amenity: " + amenity + " added successfully!");
            } else {
                System.out.println("Amenity: " + amenity + "does not exist. Please try again.");
                continue;
            }

            // Ask if the user wants to add another amenity
            System.out.println("Would you like to add another amenity? (y/n)");
            String answer = scanner.nextLine();

            if (answer.equals("n")) {
                exit = true;
            }
        }
        stmt.close();

        return amenities;
    }

    private static int getListingId(String username, String listingType, String address, String country, String
            city, String postalCode, float price, float longitude, float latitude, String amenities, Connection conn) throws
            SQLException {
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

    public static boolean checkOverlapUnavailable(Connection conn, LocalDate startDate, LocalDate endDate,
                                                  int listingID) throws SQLException {
        // Given a start date and end date, check if there are any bookings that overlap with the given dates
        PreparedStatement stmt = conn.prepareStatement("SELECT * from Bookings WHERE listingID = ? " +
                "AND (status = 'unavailable' OR status = 'booked') AND (startDate <= ? AND endDate >= ?) ORDER BY startDate;");
        stmt.setInt(1, listingID);
        stmt.setDate(2, Date.valueOf(endDate));
        stmt.setDate(3, Date.valueOf(startDate));
        ResultSet rs = stmt.executeQuery();

        // If there is a booking that overlaps, return true
        if (rs.isBeforeFirst()) {
            System.out.println("There is a booking that overlaps with the given dates. ");
            System.out.println("Please consider the existing unavailable dates and try again: ");
            while (rs.next()) {
                System.out.println(rs.getString("status") + " from " + rs.getDate("startDate") + " to " + rs.getDate("endDate"));
            }
            System.out.println("\n");
            rs.close();
            stmt.close();
            return true;
        }

        rs.close();
        stmt.close();
        return false;
    }

    private static float calculateDistance(float startLongitude, float startLatitude, float targetLongitude,
                                           float targetLatitude, int radius) {
        //TODO: Remove this function

        // Given two longitude/latitude pairs, check if the target is within the radius of the start
        double AVERAGE_RADIUS_OF_EARTH = 6371;
        double latDistance = Math.toRadians(startLatitude - targetLatitude);
        double lngDistance = Math.toRadians(startLongitude - targetLongitude);

        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                (Math.cos(Math.toRadians(startLatitude))) *
                        (Math.cos(Math.toRadians(targetLatitude))) *
                        (Math.sin(lngDistance / 2)) *
                        (Math.sin(lngDistance / 2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        float distance = (float) (AVERAGE_RADIUS_OF_EARTH * c);

        if (distance > radius) {
            return -1;
        }

        return distance;
    }

    private static void displayListing(Listing listing) {
        System.out.println("Listing ID: " + listing.listingID);
        System.out.println("Listing Type: " + listing.listingType);
        System.out.println("Address: " + listing.address);
        System.out.println(listing.city + ", " + listing.country);
        System.out.println("Postal Code: " + listing.postalCode);
        System.out.println("Amenities: " + listing.amenitiesList);
        System.out.println("Host ID: " + listing.hostID);
        System.out.println("\nPrice: $" + String.format("%.2f", listing.price));
    }

    private static void displayListingWithDistance(Listing listing) {
        System.out.println("Listing ID: " + listing.listingID);
        System.out.println("Listing Type: " + listing.listingType);
        System.out.println("Address: " + listing.address);
        System.out.println(listing.city + ", " + listing.country);
        System.out.println("Postal Code: " + listing.postalCode);
        System.out.println("Amenities: " + listing.amenitiesList);
        System.out.println("Host ID: " + listing.hostID);
        System.out.println("Distance from you: " + String.format("%.2f", listing.getDistance()) + " km\n");
        System.out.println("Price: $" + String.format("%.2f", listing.price));
    }

    private static boolean loginUser(Scanner scanner, Connection conn, String username) throws SQLException {
        // Check in database to see if username exists
        if (!checkUsernameExists(username, conn)) {
            System.out.println("Username does not exist. Please try again.");
            printBackToMainMenu();
            return true;
        }
        // Ask user for password
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        // Check to see if password matches with username
        if (!verifyLogin(username, password, conn)) {
            System.out.println("Password does not match. Please try again.");
            return true;
        }

        return false;
    }

    private static ArrayList<String> getAmenities(Connection conn, String amenitiesString) throws SQLException {
        // Check that amenitiesCodes is not null or blank
        if (amenitiesString == null || amenitiesString.isBlank()) {
            return null;
        }

        String[] amenitiesCodes = amenitiesString.split(",");
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Amenities WHERE amenityID = ?;");
        ArrayList<String> amenities = new ArrayList<>();

        for (String code : amenitiesCodes) {
            int amenityID = Integer.parseInt(code);
            stmt.setInt(1, amenityID);
            ResultSet rs = stmt.executeQuery();

            // If amenity exists, add to list
            if (rs.next()) {
                amenities.add(rs.getString("amenityName"));
            } else {
                System.out.println("Amenity with ID: " + amenityID + " does not exist.");
            }
        }
        stmt.close();

        return amenities;
    }

    private static void checkCreditCard(Scanner scanner, Connection conn, String username) throws SQLException {
        // Get user from database
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ?;");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        String creditCard = rs.getString("creditCard");

        if (creditCard == null || creditCard.isBlank()) {
            System.out.println("You do not have a credit card on file. A credit card is required to book a listing.");
            System.out.println("Please enter your credit card number (no dashes): ");
            creditCard = scanner.nextLine();

            // Update the credit card in the database
            stmt = conn.prepareStatement("UPDATE Users SET creditCard = ? WHERE username = ?;");
            stmt.setString(1, creditCard);
            stmt.setString(2, username);
            stmt.execute();
            System.out.println("Credit card updated successfully!");
        }

        rs.close();
        stmt.close();
    }

    private static void storeListings(Connection conn, ResultSet rs, ArrayList<Listing> listings) throws
            SQLException {
        while (rs.next()) {
            Listing listing = new Listing(rs.getString("hostID"),
                    rs.getString("listingType"), rs.getString("address"),
                    rs.getString("country"), rs.getString("city"),
                    rs.getString("postalCode"), rs.getFloat("price"),
                    rs.getFloat("longitude"), rs.getFloat("latitude"),
                    rs.getString("amenities"));
            listing.setListingID(rs.getInt("listingID"));
            listing.setAmenitiesList(getAmenities(conn, listing.amenities));
            listings.add(listing);
        }
    }


    // Helper function for printing back to main menu message
    private static void printBackToMainMenu() {
        System.out.println("Bringing back to main menu...\n\n\n");
        System.out.println("------------------------------------------------------------");
    }
}
