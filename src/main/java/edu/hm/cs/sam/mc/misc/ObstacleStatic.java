package edu.hm.cs.sam.mc.misc;

import edu.hm.sam.location.LocationWp;

/**
 * ObstacleStatic-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class ObstacleStatic {
    private final LocationWp wp; // contains longitude, latitude
    private final double cylinderHeight;
    private final double cylinderRadius;

    /**
     * ObstacleStatic constructor.
     *
     * @param waypoint
     *            - LocationWP-object
     * @param cylinderHeight
     *            .
     * @param cylinderRadius
     *            .
     */
    public ObstacleStatic(final LocationWp waypoint, final double cylinderHeight,
            final double cylinderRadius) {
        wp = waypoint;
        this.cylinderHeight = cylinderHeight;
        this.cylinderRadius = cylinderRadius;
    }

    /**
     * getter for cylinderHeight
     * 
     * @return cylinderHeight.
     */
    public double getCylinderHeight() {
        return cylinderHeight;
    }

    /**
     * getter for cylinderRadius
     * 
     * @return cylinderRadius.
     */
    public double getCylinderRadius() {
        return cylinderRadius;
    }

    /**
     * getter for wp
     * 
     * @return wp.
     */
    public LocationWp getWaypoint() {
        return wp;
    }
}
