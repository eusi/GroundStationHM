package edu.hm.cs.sam.mc.searcharea.viewer.view;

/**
 * @author Philipp Trepte
 * Listener to update gui with the new picture list.
 */
public interface PictureListListener {

	/**
	 * Updates the gui with the new picture list.
	 * @param listType new list type
	 */
	public void updateListener(final String listType);
}