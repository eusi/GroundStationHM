package edu.hm.sam.searchareaservice;

import edu.hm.sam.location.LocationWp;

/**
 * Created by Philipp on 28.11.2014.
 * Represents the message for a standard target.
 */
public class StandardTargetMessage extends SearchAreaTargetMessage {

	/** */
	private static final long serialVersionUID = -6390857511555839739L;

	/** letter of the target */
	private final String letter;
	/** background color of the target */
	private final String bgColor;
	/** foreground color (letter color) of the target */
	private final String fgColor;
	/** orientation from the target */
	private final String orientation;
	/** shape of the target */
	private final String shape;
	/** target id */
	private final int id;

	/**
	 * Custom Constructor.
	 * @param image the picture with the target
	 * @param location target location
	 * @param targetType target type: AOI, STANDARD or QRC
	 * @param pictureCategoryType picture type: AOIHF, AOILF, ACTIONABLEINTELLI, ADLC, SEARCHAREA
	 * @param letter letter of the target
	 * @param bgColor background color of the target
	 * @param fgColor foreground color of the target
	 * @param orientation orientation of the target
	 * @param shape shape of the target
	 */
	public StandardTargetMessage(final ByteImage image, final LocationWp location,
			final String targetType, final String pictureCategoryType,
			final String letter, final String bgColor, final String fgColor, final String orientation, final String shape) {
		super(image, location, targetType, pictureCategoryType);
		this.letter = letter;
		this.bgColor = bgColor;
		this.fgColor = fgColor;
		this.orientation = orientation;
		this.shape = shape;
		id = targetID;
	}

	/**
	 * Returns the letter of the target.
	 * @return letter of the target
	 */
	public String getLetter() {
		return letter;
	}

	/**
	 * Returns the background color of the target.
	 * @return background color of the target
	 */
	public String getBGColor() {
		return bgColor;
	}

	/**
	 * Returns the foreground color of the target (letter color).
	 * @return foreground color of the target
	 */
	public String getFGColor() {
		return fgColor;
	}

	/**
	 * Returns the orientation of the target.
	 * @return orientation of the target
	 */
	public String getOrientation() {
		return orientation;
	}

	/**
	 * Returns the shape of the target.
	 * @return shape of the target
	 */
	public String getShape() {
		return shape;
	}

	@Override
	public int getID() {
		return id;
	}
}
