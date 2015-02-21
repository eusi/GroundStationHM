package edu.hm.sam.location;


/**
 * Waypoints-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class Waypoints {
    private final LocationWp[] list; // contains longitude, latitude, etc.
    private final SamType listType; // waypoint list type - e.g. SEARCH_AREA

    /**
     * Waypoint constructor.
     *
     * @param waypointList
     *            - LocationWP-array
     * @param waypointListType
     *            - waypoint list type - e.g. SEARCH_AREA
     */
    public Waypoints(final LocationWp[] waypointList, final SamType waypointListType) {
        list = waypointList.clone();
        listType = waypointListType;
    }

    /** Get SAM_TYPE */
    public SamType getSAMType() {
        return listType;
    }

    // Get list type (id)
    private int getWaypointListType() {
        return listType.getValue();
    }

    /** Get waypoints */
    public LocationWp[] getWaypoints() {
        return list;
    }

    /**
     * JSON-file has to be like following struct: public class Locationwp {
     * public byte id; // command id public byte options; public float p1; //
     * param 1 public float p2; // param 2 public float p3; // param 3 public
     * float p4; // param 4 public double lat; // Lattitude * 10**7 public
     * double lng; // Longitude * 10**7 public float alt; // Altitude in
     * centimeters (meters * 100) } By default ID = 16 or MAV_CMD.WAYPOINT (for
     * waypoint). Lat, lng, alt used by location object. p1-p4 and options are
     * unused so far. If they will be needed, the location object have to get
     * extended by this parameters.
     */

    /** waypoints to json-string */
    @Override
    public String toString() {
        final StringBuilder jsonString = new StringBuilder();

        jsonString.append("[");

        for (int i = 0; i < list.length; i++) {
            jsonString.append("{\"alt\":").append(list[i].getAlt() + ",").append("\"id\":")
            .append(list[i].getId() + ",").append("\"lat\":")
            .append(list[i].getLat() + ",").append("\"lng\":")
            .append(list[i].getLng() + ",").append("\"options\":")
            .append(list[i].getOptions() + ",").append("\"p1\":")
            .append(list[i].getP1() + ",").append("\"p2\":").append(list[i].getP2() + ",")
            .append("\"p3\":").append(list[i].getP3() + ",").append("\"p4\":")
            .append(list[i].getP4() + "}");

            if ((i + 1) < list.length) {
                jsonString.append(",");
            }
        }

        jsonString.append("]");
        jsonString.append(", \"append\":1, \"objective\":" + getWaypointListType());

        return jsonString.toString();
    }
}
