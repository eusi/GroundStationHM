package edu.hm.cs.sam.mc.searcharea.viewer.model;

import edu.hm.sam.location.LocationWp;
import edu.hm.sam.searchareaservice.FlightType;

/**
 * @author Philipp Trepte
 * Represents a picture without a area of interest.
 */
public class Picture extends SearchAreaTarget {

	/** flight type */
	private final FlightType flightType;

	/**
	 * custom constructor.
	 * @param id picture id
	 * @param picturePath picture path
	 * @param location location
	 * @param targetType target type
	 * @param pictureCategoryType picture type
	 */
	public Picture(final int id, final String picturePath, final LocationWp location,
			final String targetType, final String pictureCategoryType, final FlightType flightType) {
		super(id, picturePath, location, targetType, pictureCategoryType);
		this.flightType = flightType;
	}

	/**
	 * Returns the flight type.
	 * @return flight type
	 */
	public FlightType getFlightType() {
		return flightType;
	}

	@Override
	public void accept(final TargetVisitor visitor) {
		visitor.visit(this);
	}
}
