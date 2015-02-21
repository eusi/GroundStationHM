package edu.hm.cs.sam.mc.searcharea.viewer.view;

import java.awt.image.BufferedImage;

/**
 * @author Philipp Trepte
 * Listener which informs the controller to save the area of interest of a picture.
 */
public interface SavePictureToAoIListener {

	/**
	 * Informs the controller to save the area of interest of a picture.
	 * @param id target id
	 * @param image picture
	 */
	public void updateListener(final int id, final BufferedImage image);
}