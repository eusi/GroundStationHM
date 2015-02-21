package edu.hm.cs.sam.mc.searcharea.viewer.model;

import edu.hm.sam.location.LocationWp;

/**
 * @author Philipp Trepte
 * Model to represent a target in the search area.
 */
public abstract class SearchAreaTarget implements TargetElement {

	/** target id */
	private final int id;
	/** picture path */
	private final String picturePath;
	/** location */
	private LocationWp location;
	/** target type */
	private final String targetType;
	/** picture type */
	private final String pictureCategoryType;

	/**
	 * custom constructor.
	 * @param id target id
	 * @param picturePath picture path
	 * @param location location
	 * @param targetType target type
	 * @param pictureCategoryType picture type
	 */
	public SearchAreaTarget(final int id, final String picturePath, final LocationWp location,
			final String targetType, final String pictureCategoryType) {
		this.id = id;
		this.picturePath = picturePath;
		this.location = location;
		this.targetType = targetType;
		this.pictureCategoryType = pictureCategoryType;
	}

	/**
	 * Returns the id.
	 * @return id
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the picture path.
	 * @return picture path
	 */
	public String getPicturePath() {
		return picturePath;
	}

	/**
	 * Returns the location.
	 * @return location
	 */
	public LocationWp getLocation() {
		return location;
	}

	/**
	 * Sets the location
	 * @param location location
	 */
	public void setLocation(final LocationWp location) {
		this.location = location;
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
}
