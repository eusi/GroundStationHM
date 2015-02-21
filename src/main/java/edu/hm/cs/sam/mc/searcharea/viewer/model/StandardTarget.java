package edu.hm.cs.sam.mc.searcharea.viewer.model;

import edu.hm.sam.location.LocationWp;

/**
 * @author Philipp Trepte
 * Model to represent a standard target.
 */
public class StandardTarget extends SearchAreaTarget {

	/** letter of the target */
	private String letter;
	/** background color of the target */
	private String bgColor;
	/** foreground color (letter color) of the target */
	private String fgColor;
	/** orientation from the target */
	private String orientation;
	/** shape of the target */
	private String shape;

	/**
	 * custom constructor.
	 * @param id target id
	 * @param picturePath picture path
	 * @param location location
	 * @param targetType target type
	 * @param pictureCategoryType picture type
	 * @param letter letter
	 * @param bgColor background color
	 * @param fgColor foreground color
	 * @param orientation orientation
	 * @param shape shape
	 */
	public StandardTarget(final int id, final String picturePath, final LocationWp location, final String targetType,
			final String pictureCategoryType, final String letter, final String bgColor,
			final String fgColor, final String orientation, final String shape) {
		super(id, picturePath, location, targetType, pictureCategoryType);
		this.letter = letter;
		this.bgColor = bgColor;
		this.fgColor = fgColor;
		this.orientation = orientation;
		this.shape = shape;
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

	/**
	 * Sets the letter
	 * @param letter letter
	 */
	public void setLetter(final String letter) {
		this.letter = letter;
	}

	/**
	 * Sets the background color.
	 * @param bgColor background color
	 */
	public void setBGColor(final String bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * Sets the foreground color.
	 * @param fgColor foreground color
	 */
	public void setFGColor(final String fgColor) {
		this.fgColor = fgColor;
	}

	/**
	 * Sets the orientation.
	 * @param orientation orientation
	 */
	public void setOrientation(final String orientation) {
		this.orientation = orientation;
	}

	/**
	 * Sets the shape.
	 * @param shape shape
	 */
	public void setShape(final String shape) {
		this.shape = shape;
	}

	@Override
	public void accept(final TargetVisitor visitor) {
		visitor.visit(this);
	}
}
