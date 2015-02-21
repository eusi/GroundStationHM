package edu.hm.cs.sam.mc.misc;

import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;

/**
 * Target-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class Target {
    private final LocationWp wp; // contains longitude, latitude
    private final SamType targetType; // target type - e.g. ZONE_EMERGENT

    /**
     * Target constructor.
     *
     * @param waypoint
     *            - LocationWP-object
     * @param targetType
     *            - target type - e.g. "Emergent"
     */
    public Target(final LocationWp waypoint, final SamType targetType) {
        wp = waypoint;
        this.targetType = targetType;
    }

    /**
     * getter for targetType
     * 
     * @return targetType.
     */
    public SamType getSAMType() {
        return targetType;
    }

    // Get target type (id)
    private int getTargetType() {
        return targetType.getValue();
    }

    /**
     * getter for wp
     * 
     * @return wp.
     */
    public LocationWp getWaypoint() {
        return wp;
    }

    /**
     * JSON-file has to be like following struct: {"targets":[
     * {"Coordinates":{"NotEmpty"
     * :true,"lat":47.4626504325423,"lng":-122.307100296021},"TargetType": 0},
     * {"Coordinates":{"NotEmpty"
     * :true,"lat":47.4626504325423,"lng":-122.307100296021},"TargetType": 0} ]}
     */

    /** target to json-string */
    @Override
    public String toString() {
        final StringBuilder jsonString = new StringBuilder();

        jsonString.append("{\"Coordinates\":").append("{\"NotEmpty\":").append("true,")
                .append("\"lat\":").append(wp.getLat() + ",").append("\"lng\":")
                .append(wp.getLng() + "},").append("\"TargetType\":").append(getTargetType() + "}");

        return jsonString.toString();
    }
}
