package edu.hm.cs.sam.mc.misc;

/**
 * Obstacles-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class Obstacles {
    private ObstacleStatic[] oStaticlist; // contains a list of static obstacles
    private ObstacleDynamic[] oDynamiclist; // contains a list of dynamic

    // obstacles

    /**
     * Obstacles constructor.
     *
     * @param oStaticlist
     *            .
     * @param oDynamiclist
     *            .
     */
    public Obstacles(final ObstacleStatic[] oStaticlist, final ObstacleDynamic[] oDynamiclist) {
        this.oStaticlist = oStaticlist;
        this.oDynamiclist = oDynamiclist;
    }

    /**
     * getter for oDynamiclist.
     * 
     * @return oDynamiclist.
     */
    public ObstacleDynamic[] getoDynamiclist() {
        return oDynamiclist;
    }

    /**
     * getter for oStaticlist
     * 
     * @return oStaticlist.
     */
    public ObstacleStatic[] getoStaticlist() {
        return oStaticlist;
    }

    /**
     * setter for oDynamiclist
     * 
     * @param oDynamiclist
     *            new oDynamiclist.
     */
    public void setoDynamiclist(final ObstacleDynamic[] oDynamiclist) {
        this.oDynamiclist = oDynamiclist;
    }

    /**
     * setter for oStaticlist.
     * 
     * @param oStaticlist
     *            new oStaticlist.
     */
    public void setoStaticlist(final ObstacleStatic[] oStaticlist) {
        this.oStaticlist = oStaticlist;
    }

    /**
     * obstacles to json-string - just for testing
     */
    @Override
    public String toString() {
        final StringBuilder jsonString = new StringBuilder();

        jsonString.append("{\"MovingObstacles\":[");

        for (int i = 0; i < oDynamiclist.length; i++) {
            jsonString.append("{\"Altitude\":" + oDynamiclist[i].getWaypoint().getAlt() + ",")
            .append("\"Latitude\":" + oDynamiclist[i].getWaypoint().getLat() + ",")
            .append("\"Longitude\":" + oDynamiclist[i].getWaypoint().getLng() + ",")
            .append("\"SphereRadius\":" + oDynamiclist[i].getSphereRadius() + "}");

            if ((i + 1) < oDynamiclist.length) {
                jsonString.append(",");
            } else {
                jsonString.append("],");
            }
        }

        jsonString.append("\"StationaryObstacles\":[");

        for (int j = 0; j < oStaticlist.length; j++) {
            jsonString.append("\"CylinderHeight\":" + oStaticlist[j].getCylinderHeight() + ",")
            .append("\"CylinderRadius\":" + oStaticlist[j].getCylinderRadius() + ",")
            .append("\"Latitude\":" + oStaticlist[j].getWaypoint().getLat() + ",")
            .append("\"Longitude\":" + oStaticlist[j].getWaypoint().getLng() + "}");

            if ((j + 1) < oStaticlist.length) {
                jsonString.append(",");
            } else {
                jsonString.append("]}");
            }
        }

        return jsonString.toString();
    }
}
