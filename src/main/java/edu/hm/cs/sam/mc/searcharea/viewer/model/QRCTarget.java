package edu.hm.cs.sam.mc.searcharea.viewer.model;

import edu.hm.sam.location.LocationWp;

/**
 * @author Philipp Trepte
 * Model to represent a qrc target.
 */
public class QRCTarget extends SearchAreaTarget {

	/** decoded message */
	private String message;

	/**
	 * custom constructor.
	 * @param id qrc target id
	 * @param picturePath picture path
	 * @param location location
	 * @param targetType target type
	 * @param pictureCategoryType picture type
	 * @param message decoded message
	 */
	public QRCTarget(final int id, final String picturePath, final LocationWp location,
			final String targetType, final String pictureCategoryType, final String message) {
		super(id, picturePath, location, targetType, pictureCategoryType);
		this.message = message;
	}

	/**
	 * Returns the decoded message.
	 * @return decoded message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the new decoded message.
	 * @param message decoded message
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public void accept(final TargetVisitor visitor) {
		visitor.visit(this);
	}
}
