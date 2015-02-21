package edu.hm.sam.searchareaservice;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import edu.hm.sam.Message;
import edu.hm.sam.location.LocationWp;

/**
 * Created by Philipp on 07.01.2015.
 */
public class LowFlightRouteCalc extends Message implements Serializable {

    /** */
    private static final long serialVersionUID = 4801785668180262870L;

    /** way points for the low flight */
    private final List<LocationWp> waypoints;

    /**
     * custom constructor.
     * @param waypoints way points for the low flight
     */
    public LowFlightRouteCalc(final List<LocationWp> waypoints) {
        this.waypoints = waypoints;
    }

    /**
     * Returns the way points for the low flight.
     * @return way points
     */
    public List<LocationWp> getWaypoints() {
        return Collections.unmodifiableList(waypoints);
    }
}
