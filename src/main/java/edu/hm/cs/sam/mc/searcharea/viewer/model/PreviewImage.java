package edu.hm.cs.sam.mc.searcharea.viewer.model;

/**
 * @author Philipp Trepte
 * Represents the model for a preview image in the slide show.
 */
public class PreviewImage {

	/** picture path */
	private final String picturePath;
	/** current number */
	private final int number;
	/** maximal number */
	private final int maxNumber;

	/**
	 * custom constructor.
	 * @param number current number
	 * @param maxNumber maximal number
	 */
	public PreviewImage(final int number, final int maxNumber, final String picturePath) {
		this.number = number;
		this.maxNumber = maxNumber;
		this.picturePath = picturePath;
	}

	/**
	 * Returns the picture path.
	 * @return picture path
	 */
	public String getPicturePath() {
		return picturePath;
	}

	/**
	 * Returns the current number.
	 * @return current number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns the maximal number.
	 * @return maximal number
	 */
	public int getMaxNumber() {
		return maxNumber;
	}
}