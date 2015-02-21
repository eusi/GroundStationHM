package edu.hm.cs.sam.mc.searcharea.viewer.view;

/**
 * @author Philipp Trepte
 * Listener which inform the controller to save the classification of the target.
 */
public interface SaveClassificationListener {

	/**
	 * Informs the controller to save the classification of the target.
	 * @param id target id
	 * @param targetType target type
	 */
	public void updateListener(final int id, final String targetType);
}