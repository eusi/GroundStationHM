package cs.hm.edu.sam.mc.misc;

/**
 * Location-class.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

public class Location {

    private double lng; // longitude
    private double lat; // latitude
    private double alt; // altitude, in real it's a float

    public Location(double newLng, double newLat, double newAlt) {
        this.lng = newLng;
        this.lat = newLat;
        this.alt = newAlt;
    }

    // Get longitude
    public double getLng() {
        return lng;
    }

    // Get latitude
    public double getLat() {
        return lat;
    }

    // Get altitude
    public double getAlt() {
        return alt;
    }

    @Override
    public String toString() {
        return new StringBuffer(" lng : ").append(this.lng).append(" lat : ").append(this.lat)
                .append(" alt : ").append(this.alt).toString();
    }

}
