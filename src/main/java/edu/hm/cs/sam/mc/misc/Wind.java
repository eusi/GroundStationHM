package edu.hm.cs.sam.mc.misc;

/**
 * Wind-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class Wind {
    private final double direction; // Wind Direction (deg)
    private final double velocity; // Wind Velocity (speed)

    /**
     * Target constructor.
     *
     * @param direction
     *            - Wind Direction (deg) in double
     * @param velocity
     *            - Wind Velocity (speed) in double
     */
    public Wind(final double direction, final double velocity) {
        this.direction = direction;
        this.velocity = velocity;
    }

    /**
     * getter for direction
     * 
     * @return direction.
     */
    public double getDirection() {
        return direction;
    }

    /**
     * getter for velocity
     * 
     * @return velocity.
     */
    public double getVelocity() {
        return velocity;
    }
}
