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
    private String ts; // timestamp

    public Location(double newLng, double newLat, double newAlt) {
        this.lng = newLng;
        this.lat = newLat;
        this.alt = newAlt;
    }
    
    public Location(double newLng, double newLat, double newAlt, String newTs) {
        this.lng = newLng;
        this.lat = newLat;
        this.alt = newAlt;
        this.ts  = newTs;
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
    
    // Get altitude
    public String getTs() {
        return ts;
    }

    @Override
    public String toString() {
        return new StringBuffer(" lng : ").append(this.lng).append(" lat : ").append(this.lat)
                .append(" alt : ").append(this.alt).toString();
    }
    
    public String toGpsTable() {
        return new StringBuffer(this.ts).append("  -  lng: ").append(this.lng).append("  -  lat: ").append(this.lat)
                .append("  -  alt: ").append(this.alt).toString();
    }
    
    //yyyyMMddHHmmssffff -> YYYY-MM-DD  HH:MM:SS:FF
    public String formatDateTime(String dateTime) {
    	return dateTime.substring(0,3) + "-" + dateTime.substring(4,5) + "-" + dateTime.substring(6,7)
    			+ "   " + dateTime.substring(8,9) + ":" + dateTime.substring(10,11) 
    			 + ":" + dateTime.substring(12,13) + ":" + dateTime.substring(14,15);
    }

}
