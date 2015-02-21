package edu.hm.cs.sam.mc.misc;

import java.awt.Color;

import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;

/**
 * Waypoints-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class Zone {
    private LocationWp[] list; // contains longitude, latitude, etc.
    private final SamType zoneType; // zone type - e.g. NO_FLIGHT_ZONE
    private Color rgba; // zone color

    /**
     * Zone constructor.
     *
     * @param zoneList
     *            - LocationWP-array
     * @param zoneType
     *            - zone type - e.g. NO_FLIGHT_ZONE
     * @param rgba
     *            - line colors
     */
    public Zone(final LocationWp[] zoneList, final SamType zoneType, final Color rgba) {
        list = zoneList.clone();
        this.zoneType = zoneType;
        this.rgba = rgba;
    }

    /**
     * getter for color
     * 
     * @return rgba.
     */
    public Color getColor() {
        return rgba;
    }

    /**
     * getter for zoneType.
     * 
     * @return zoneType.
     */
    public SamType getSAMType() {
        return zoneType;
    }

    /**
     * getter for waypoints
     * 
     * @return list.
     */
    public LocationWp[] getWaypoints() {
        return list;
    }

    // Get list type (id)
    private int getZoneType() {
        return zoneType.getValue();
    }

    /**
     * setter for color
     * 
     * @param color
     *            new color.
     */
    public void setColor(final Color color) {
        rgba = color;
    }

    /**
     * setter for waypoints.
     * 
     * @param lwps
     *            new waypoint list.
     */
    public void setWaypoints(final LocationWp[] lwps) {
        list = lwps;
    }

    /**
     * JSON-file has to be like following struct:
     * [{"Color":{"alpha":100,"blue":100,"green":100,"red":100}, "ZoneType": 1,
     * "ZonePoints":[{"NotEmpty":true,"lat":1,"lng":1},
     * {"NotEmpty":true,"lat":2,"lng":2}, {"NotEmpty":true,"lat":3,"lng":3},
     * {"NotEmpty":true,"lat":4,"lng":4}]}, {"Color":{...}]
     */

    /** zone to json-string */
    @Override
    public String toString() {
        final StringBuilder jsonString = new StringBuilder();

        jsonString.append("{\"Color\":{").append("\"alpha\":" + rgba.getAlpha() + ",")
                .append("\"blue\":" + rgba.getBlue() + ",")
                .append("\"green\":" + rgba.getGreen() + ",")
                .append("\"red\":" + rgba.getRed() + "},").append("\"ZoneType\":")
                .append(getZoneType() + ",").append("\"ZonePoints\":[");

        for (int i = 0; i < list.length; i++) {
            jsonString.append("{\"NotEmpty\":").append("true,").append("\"lat\":")
                    .append(list[i].getLat() + ",").append("\"lng\":")
                    .append(list[i].getLng() + "}");

            if ((i + 1) < list.length) {
                jsonString.append(",");
            } else {
                jsonString.append("]}");
            }
        }

        return jsonString.toString();
    }
}
