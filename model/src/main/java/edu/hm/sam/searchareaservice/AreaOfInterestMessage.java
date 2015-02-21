package edu.hm.sam.searchareaservice;

import edu.hm.sam.location.LocationWp;

/**
 * Created by Philipp on 01.12.2014.
 * Represents the message for the area of interest.
 */
public class AreaOfInterestMessage extends SearchAreaTargetMessage {

	/**
	 *
	 */
	private static final long serialVersionUID = -2264606908346402259L;

	/** picture id */
	private final int id;

	/**
	 * Custom Constructor.
	 * @param image the picture with the target
	 * @param location target location
	 * @param targetType target type: AOI, STANDARD or QRC
	 * @param pictureCategoryType picture type: AOIHF, AOILF, ACTIONABLEINTELLI, ADLC, SEARCHAREA
	 */
	public AreaOfInterestMessage(final ByteImage image, final LocationWp location,
			final String targetType, final String pictureCategoryType) {
		super(image, location, targetType, pictureCategoryType);
		id = targetID;
	}

	@Override
	public int getID() {
		return id;
	}
}
