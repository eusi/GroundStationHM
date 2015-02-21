package edu.hm.cs.sam.mc.searcharea.viewer.view;

import edu.hm.sam.searchareaservice.FlightType;

/**
 * @author Philipp Trepte
 * Listener to update the flight change.
 */
public interface FlightChangeListener {

	/**
	 * Updates the flight change.
	 * @param flightType new flight type
	 * @param isStarted true the flight is started, false otherwise
	 */
	public void updateListener(final FlightType flightType, final boolean isStarted);
}