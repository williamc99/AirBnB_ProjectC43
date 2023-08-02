public class DistancePair {
    Listing listing;
    double distance;

    public DistancePair(){}

    public DistancePair(Listing listing, double distance){
        this.listing = listing;
        this.distance = distance;
    }

    public Listing getListing(){
        return this.listing;
    }
    public double getDistance(){
        return distance;
    }
    public int getListingID(){ return listing.getListingID(); }

}
