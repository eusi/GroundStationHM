package edu.hm.cs.sam.mc.routing;

/**
 * Interface for a Reorderable Table
 * @author Stefan Hoelzl
 *
 */

public interface Reorderable {
	/**
	 * Method to reorder a Column from on position to another
	 * @param fromIndex initial position
	 * @param toIndex position after reorder
	 */
    public void reorder(int fromIndex, int toIndex);
}
