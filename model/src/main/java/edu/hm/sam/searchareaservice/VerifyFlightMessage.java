package edu.hm.sam.searchareaservice;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import edu.hm.sam.Message;
import edu.hm.sam.location.LocationWp;

/**
 * Created by Philipp on 13.12.2014.
 * Represents a message for flight change.
 */
public class VerifyFlightMessage extends Message implements Serializable {

	/** */
	private static final long serialVersionUID = 8499692881848213009L;
	/** flight type */
	private final String flightType;
	/** is flight started */
	private final boolean isStarted;
	/** way points */
	private final List<LocationWp> wayPoints;

	/**
	 * custom constructor.
	 * @param flightType flight type
	 * @param isStarted is flight started
	 * @param wayPoints way points
	 */
	public VerifyFlightMessage(final String flightType, final boolean isStarted,
			final List<LocationWp> wayPoints) {
		this.flightType = flightType;
		this.isStarted = isStarted;
		this.wayPoints = wayPoints;
	}

	/**
	 * Returns the flight type.
	 * @return flight type
	 */
	public String getFlightType() {
		return flightType;
	}

	/**
	 * Checks if the flight is started.
	 * @return true is started, false otherwise
	 */
	public boolean isStarted() {
		return isStarted;
	}

	/**
	 * Returns the way points for the low flight.
	 * @return way points
	 */
	public List<LocationWp> getWayPoints() {
		return Collections.unmodifiableList(wayPoints);
	}
}
