package edu.hm.cs.sam.mc.searcharea.viewer.model;

/**
 *@author Philipp Trepte
 * Interface for the target elements to accept the visitor.
 */
public interface TargetElement {

	/**
	 * Accepts the visitor to visit the element.
	 * @param visitor visitor
	 */
	public void accept(final TargetVisitor visitor);
}