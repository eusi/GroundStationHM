package edu.hm.sam.searchareaservice;

import edu.hm.sam.location.LocationWp;

/**
 * Created by Philipp on 28.11.2014.
 * Represents the message for the qrc target.
 */
public class QRCTargetMessage extends SearchAreaTargetMessage {

	/** */
	private static final long serialVersionUID = -7380424240239462941L;

	/** decoded message */
	private final String message;
	/** target id */
	private final int id;

	/**
	 * Custom Constructor.
	 * @param image the picture with the target
	 * @param location target location
	 * @param targetType target type: AOI, STANDARD or QRC
	 * @param pictureCategoryType picture type: AOIHF, AOILF, ACTIONABLEINTELLI, ADLC, SEARCHAREA
	 * @param message the decoded message from a qrc target
	 */
	public QRCTargetMessage(final ByteImage image, final LocationWp location,
			final String targetType, final String pictureCategoryType, final String message) {
		super(image, location, targetType, pictureCategoryType);
		this.message = message;
		id = targetID;
	}

	/**
	 * Returns the decoded message.
	 * @return decoded message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public int getID() {
		return id;
	}
}
