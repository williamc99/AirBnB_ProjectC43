public class DistancePair {
    int listingID;
    double distance;

    public DistancePair(){}

    public DistancePair(int listingID, double distance){
        this.listingID = listingID;
        this.distance = distance;
    }

    public int getListingID(){
        return listingID;
    }

    public double getDistance(){
        return distance;
    }

}
