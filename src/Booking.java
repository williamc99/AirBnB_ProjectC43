import java.time.LocalDate;

public class Booking {
    int bookingID;
    int listingID;
    String userID;
    String status;
    String statusReason;
    float price;
    LocalDate startDate;
    LocalDate endDate;


    public Booking() {
    }

    public Booking(int listingID, String userID, String status, LocalDate startDate, LocalDate endDate) {
        this.listingID = listingID;
        this.userID = userID;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String validateData(){
        // If any of the fields are empty, return false
        if (userID.equals("") || status.equals("") || startDate == null || endDate ==null){
            return "All fields must be filled out, do not leave any fields blank.";
        }
        // If any of the fields are too long, return false
        if (userID.length() > 16 || status.length() > 16){
            return "One or more fields are too long.";
        }
        // If startDate is after endDate or endDate is before startDate, return false
        if (startDate.isAfter(endDate)){
            return "Start date must be before end date.";
        }
        else if (endDate.isBefore(startDate)){
            return "End date must be after start date.";
        }

        return "pass";
    }


    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setListingID(int listingID) {this.listingID = listingID;}

    public void setUserID(String userID) {this.userID = userID;}

    public void setStatus(String status) {this.status = status;}

    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

}
