package edu.hm.sam.searchareaservice;

import edu.hm.sam.location.LocationWp;

/**
 * Created by Philipp Trepte on 24.12.2014.
 * Represents a message for a picture without a area of interest.
 */
public class PictureMessage extends SearchAreaTargetMessage {

	/** */
	private static final long serialVersionUID = 7298214696169069020L;

    /** default location */
	private static final LocationWp defaultLocation = new LocationWp(0.0, 0.0);
    /** picture id */
	private final int id;
    /** flight type */
	private final FlightType flightType;

    /**
     * custom constructor.
     * @param image picture with the target
     * @param flightType flight type
     */
	public PictureMessage(final ByteImage image, final FlightType flightType) {
		super(image, defaultLocation, TargetType.PICTURE.toString(), PictureCategoryType.NORESULTS.toString());
		this.flightType = flightType;
		this.id = targetID;
	}

	/**
	 * Returns the flight type.
	 * @return flight type
	 */
	public FlightType getFlightType() {
		return flightType;
	}

	@Override
	public int getID() {
		return id;
	}
}