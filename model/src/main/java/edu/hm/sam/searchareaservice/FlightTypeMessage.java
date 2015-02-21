package edu.hm.sam.searchareaservice;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Philipp on 11.12.2014.
 * Represents the message for the flight type.
 */
public class FlightTypeMessage extends Message implements Serializable {

	/** */
	private static final long serialVersionUID = -1699092821022184274L;

	/** flight type */
	private final String flightType;

	/**
	 * Custom Constructor
	 * @param flightType flight type: HIGH, LOW
	 */
	public FlightTypeMessage(final String flightType) {
		this.flightType = flightType;
	}

	/**
	 * Returns the flight type.
	 * @return flight type
	 */
	public String getFlightType() {
		return flightType;
	}
}
