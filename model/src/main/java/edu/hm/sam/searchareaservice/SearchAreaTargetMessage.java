package edu.hm.sam.searchareaservice;

import java.io.Serializable;

import edu.hm.sam.Message;
import edu.hm.sam.location.LocationWp;

/**
 * Created by Philipp on 28.11.2014.
 * Represents the base message of any target in the search area.
 */
public abstract class SearchAreaTargetMessage extends Message implements Serializable {

	/** */
	private static final long serialVersionUID = 7464377586011421462L;

	/** picture of the target */
	private final ByteImage image;
	/** target location */
	private final LocationWp location;
	/** target type: AOI, STANDARD or QRC */
	private final String targetType;
	/** picture type: AOIHF, AOILF, ACTIONABLEINTELLI, ADLC, SEARCHAREA */
	private final String pictureCategoryType;
	/** target id */
	protected static int targetID = 0;

	/**
	 * custom constructor.
	 * @param image the picture with the target
	 * @param location target location
	 * @param targetType target type: AOI, STANDARD or QRC
	 * @param pictureCategoryType picture type: AOIHF, AOILF, ACTIONABLEINTELLI, ADLC, SEARCHAREA
	 */
	public SearchAreaTargetMessage(final ByteImage image, final LocationWp location, final String targetType, final String pictureCategoryType) {
		this.image = image;
		this.location = location;
		this.targetType = targetType;
		this.pictureCategoryType = pictureCategoryType;
		targetID++;
	}

	/**
	 * Returns the picture with the target.
	 * @return picture with the target
	 */
	public ByteImage getImage() {
		return image;
	}

	/**
	 * Returns the target location.
	 * @return target location
	 */
	public LocationWp getLocation() {
		return location;
	}

	/**
	 * Returns the target type.
	 * @return target type
	 */
	public String getTargetType() {
		return targetType;
	}

	/**
	 * Returns the picture type.
	 * @return picture type
	 */
	public String getPictureCategoryType() {
		return pictureCategoryType;
	}

	/**
	 * Returns the target id.
	 * @return target id
	 */
	public abstract int getID();
}
