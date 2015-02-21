package edu.hm.cs.sam.mc.searcharea.viewer.view;

/**
 * @author Philipp Trepte
 * Listener which informs the controller to save the target.
 */
public interface SaveFileListener {

	/**
	 * Informs the controller to save the target.
	 * @param id target id
	 */
	public void updateListener(final int id);
}