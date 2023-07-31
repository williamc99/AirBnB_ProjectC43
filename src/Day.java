import java.time.LocalDate;

public class Day {
    int dayID;
    int listingID;
    int bookingID;
    String status;
    LocalDate startDate;
    LocalDate endDate;

    public Day(){
    }

    public Day(int listingID, String status, String startDate, String endDate) {
        this.listingID = listingID;
        this.status = status;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
    }

    public String validateData(){
        // If any of the fields are empty, return false
        if (status.equals("") || startDate == null || endDate == null){
            return "All fields must be filled out, do not leave any fields blank.";
        }

        // If any of the fields are too long, return false
        if (status.length() > 10){
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

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
}


