public class Review {
    String reviewerID;
    String revieweeID;
    String comment;
    int listingID;
    int hostRating;
    int listingRating;
    int renterRating;


    public Review(){
    }

    public String validateData(){
        // If any of the fields are empty, return false
        if (reviewerID.equals("") || revieweeID.equals("")){
            return "Reviwer or Reviewee ID cannot be blank.";
        }
        // If any of the fields are too long, return false
        if (reviewerID.length() > 16 || revieweeID.length() > 16 || comment.length() > 400){
            return "One or more fields are too long.";
        }
        // If hostRating exists, it must be between 1 and 5
        if (hostRating != 0 && (hostRating < 1 || hostRating > 5)){
            return "Host rating must be between 1 and 5.";
        }
        if (listingRating != 0 && (listingRating < 1 || listingRating > 5)){
            return "Listing rating must be between 1 and 5.";
        }
        if (renterRating != 0 && (renterRating < 1 || renterRating > 5)){
            return "Renter rating must be between 1 and 5.";
        }

        return "pass";
    }


    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public void setRevieweeID(String revieweeID) {
        this.revieweeID = revieweeID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setListingID(int listingID) {
        this.listingID = listingID;
    }

    public void setHostRating(int hostRating) {
        this.hostRating = hostRating;
    }

    public void setListingRating(int listingRating) {
        this.listingRating = listingRating;
    }

    public void setRenterRating(int renterRating) {
        this.renterRating = renterRating;
    }
}
