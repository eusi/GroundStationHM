package edu.hm.cs.sam.mc.searcharea.viewer.view;

/**
 * @author Philipp Trepte
 * Listener which informs the program to select a sub image of a picture.
 */
public interface SubImageListener {

	/**
	 * Informs the program to select a sub image of a picture.
	 * @param topLeftX top left x-position
	 * @param topLeftY top left y-position
	 * @param width width of the sub image
	 * @param height height of the sub image
	 */
	public void updateListener(final int topLeftX, final int topLeftY, final int width, final int height);
}