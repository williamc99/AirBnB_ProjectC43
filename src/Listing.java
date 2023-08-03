import java.time.LocalDate;
import java.util.*;

public class Listing {
    int listingID;
    String hostID;
    String listingType;
    String address;
    String country;
    String city;
    String postalCode;
    float price;
    float longitude;
    float latitude;
    String amenities;
    ArrayList<String> amenitiesList;
    float distance;

    public Listing() {
    }

    public Listing(String hostID, String listingType, String address, String country, String city, String postalCode, float price, float longitude, float latitude, String amenities) {
        this.hostID = hostID;
        this.listingType = listingType;
        this.address = address;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.amenities = amenities;
    }


    public String sqlInsertString(){
        return "('" + hostID + "', '" + listingType + "', '" + address + "', '" + country + "', '" + city + "', '"
                + postalCode + "', '" + price + "', '" + longitude + "', '" + latitude + "', '" + amenities + "');";
    }

    public String validateData(){
        // If any of the fields are empty, return false
        if (hostID.equals("") || listingType.equals("") || address.equals("") || country.equals("") || city.equals("")
                || postalCode.equals("") || amenities.equals("")){
            return "All fields must be filled out, do not leave any fields blank.";
        }
        // If any of the fields are too long, return false
        if (hostID.length() > 16 || listingType.length() > 20 || address.length() > 25 || country.length() > 16 || city.length() > 24
                || postalCode.length() > 10 || amenities.length() > 255){
            return "One or more fields are too long.";
        }
        // If the price is negative, return false
        if (price < 0){
            return "Price cannot be negative.";
        }

        return "pass";
    }

    public float estimatePrice() {
        // TODO: Implement and refine the price suggesting algorithm
        // Estimate the price per night by taking into account different factors

        float price = switch (this.listingType) {
            case "House" -> 300;
            case "Apartment" -> 200;
            case "Guesthouse" -> 250;
            case "Hotel" -> 175;
            default -> 0;
        };

        // Get the number of amenities
        int numAmenities = this.amenities.split(",").length;

        // Add 2% for each amenity
        price += price * (numAmenities * 0.02);

        return price;
    }


    public void setListingID(int listingID) {
        this.listingID = listingID;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getListingID() { return listingID; }

    public void setAmenitiesList(List<String> amenitiesList) {
        this.amenitiesList = new ArrayList<>(amenitiesList);
    }

    public ArrayList<String> getAmenitiesList() {
        return this.amenitiesList;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return this.distance;
    }
}
