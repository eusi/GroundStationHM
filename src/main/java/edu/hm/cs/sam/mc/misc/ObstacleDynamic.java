package edu.hm.cs.sam.mc.misc;

import edu.hm.sam.location.LocationWp;

/**
 * ObstacleDynamic-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class ObstacleDynamic {
    private final LocationWp wp; // contains longitude, latitude, altitude
    private final double sphereRadius;

    /**
     * ObstacleDynamic constructor.
     *
     * @param waypoint
     *            - LocationWP-object
     * @param sphereRadius
     *            .
     */
    public ObstacleDynamic(final LocationWp waypoint, final double sphereRadius) {
        wp = waypoint;
        this.sphereRadius = sphereRadius;
    }

    /**
     * getter for sphereRadius.
     * 
     * @return sphereRadius.
     */
    public double getSphereRadius() {
        return sphereRadius;
    }

    /**
     * getter for wp.
     * 
     * @return wp.
     */
    public LocationWp getWaypoint() {
        return wp;
    }
}
