package edu.hm.sam.location;


import java.io.Serializable;

/**
 * Location-WayPoint-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

//TODO: split LocationWp in several objects for specific use cases (like ctors atm)
//LocationWp should be the root object with only lat,lng and optionally alt
//another object could be LocationNextWaypoint which extends LocationWp, aso.

//TODO: add an id to LocationWp (as well as in MP), so we can check if
//any (specific) waypoint is reached or not

public class LocationWp implements Serializable{

	private static final long serialVersionUID = 4683888240566344757L;
	
	private double lng; // longitude
    private double lat; // latitude
    private double alt; // altitude, in real it's a float
    private double yaw; // heading

    private String ts;  // timestamp, only needed for import UAS Position from MP

    private MavCmd id; // e.g. WAYPOINT - 16, see MavCmd)
    private int options;
    private int p1;
    private int p2;
    private int p3;
    private int p4;

    private SamType type;
    private double distanceToNextWaypoint; // distance to next waypoint

    /**
     * Location-WayPoint-Ctor. The simplest one.
     *
     * @param longitude
     * @param latitude
     */
    public LocationWp(final double lat, final double lng) {
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * Location-WayPoint-Ctor. A typically waypoint.
     *
     * @param longitude
     * @param latitude
     * @param altitude
     */
    public LocationWp(final double lat, final double lng, final double alt) {
        this.lng = lng;
        this.lat = lat;
        this.alt = alt;
    }

    /**
     * Location-WayPoint-Ctor for getNextWaypoint.
     *
     * @param longitude - whats next wp?
     * @param latitude - whats next wp?
     * @param altitude - whats next wp?
     * @param type - whats next task?
     */
    public LocationWp(final double lat, final double lng, final double alt, SamType type, double distanceToNextWaypoint) {
        this.lng = lng;
        this.lat = lat;
        this.alt = alt;
        this.type = type;
        this.distanceToNextWaypoint = distanceToNextWaypoint;
    }

    /**
     * Location-WayPoint-Ctor. Take this if you want to set a Waypoint in MP.
     *
     * @param id        - e.g. WAYPOINT - 16, see MavCmd)
     * @param options
     * @param p1        - parameter
     * @param p2        - parameter
     * @param p3        - parameter
     * @param p4        - parameter
     * @param longitude
     * @param latitude
     * @param altitude
     */
    public LocationWp(final double lat, final double lng, final double alt, final MavCmd id,
                      final int options, final int p1, final int p2, final int p3, final int p4) {
        this.lng = lng;
        this.lat = lat;
        this.alt = alt;
        this.id = id;
        this.options = options;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    /**
     * Location-WayPoint-Ctor. Only needed for import UAS Position from MP.
     *
     * @param longitude
     * @param latitude
     * @param altitude
     * @param timestamp
     */
    public LocationWp(final double lat, final double lng, final double alt, final String ts,
                      final double yaw) {
        this.lng = lng;
        this.lat = lat;
        this.alt = alt;
        this.ts = ts;
        this.yaw = yaw;
    }

    /**
     * Modify String to a formatted DateTime-String Pattern: yyyyMMddHHmmssffff
     * -> YYYY-MM-DD HH:MM:SS:FF
     *
     * @param dateTime -String (yyyyMMddHHmmssffff)
     * @return String
     */
    public String formatDateTime(final String dateTime) {
        return dateTime.substring(0, 4) + "-" + dateTime.substring(4, 6) + "-"
                + dateTime.substring(6, 8) + " " + dateTime.substring(8, 10) + ":"
                + dateTime.substring(10, 12) + ":" + dateTime.substring(12, 14);
    }

    // Get altitude
    public double getAlt() {
        return alt;
    }

    public int getId() {
        return id.getValue();
    }

    // Get latitude
    public double getLat() {
        return lat;
    }

    // Get longitude
    public double getLng() {
        return lng;
    }

    public int getOptions() {
        return options;
    }

    public int getP1() {
        return p1;
    }

    public int getP2() {
        return p2;
    }

    public int getP3() {
        return p3;
    }

    public int getP4() {
        return p4;
    }

    // Get timestamp
    public String getTs() {
        return ts;
    }

    // Get heading (yaw)
    public double getYaw() {
        return yaw;
    }

    public SamType getSamType() {
        return type;
    }    

    public double getDistanceToNextWaypoint() {
        return distanceToNextWaypoint;
    }

    public void setId(final MavCmd id) {
        this.id = id;
    }

    public void setLat(final double l) {
        lat = l;
    }

    public void setLng(final double l) {
        lng = l;
    }

    public void setOptions(final int options) {
        this.options = options;
    }

    public void setP1(final int p1) {
        this.p1 = p1;
    }

    public void setP2(final int p2) {
        this.p2 = p2;
    }

    public void setP3(final int p3) {
        this.p3 = p3;
    }

    public void setP4(final int p4) {
        this.p4 = p4;
    }

    public void setTs(final String ts) {
        this.ts = ts;
    }

    public void setYaw(final Double yaw) {
        this.yaw = yaw;
    }
    
    public void setSamType(final SamType type) {
        this.type = type;
    }

    /**
     * Location-WayPoint-Ctor. Only needed for import UAS Position from MP.
     *
     * @return String
     */
    public String toGpsTable() {
        return new StringBuffer(formatDateTime(ts)).append("  -  lng: ").append(lng).append("  -  lat: ")
                .append(lat).append("  -  alt: ").append(alt).toString();
    }

    @Override
    public String toString() {
        return new StringBuffer(" lng : ").append(lng).append(" lat : ").append(lat)
                .append(" alt : ").append(alt).toString();
    }
}