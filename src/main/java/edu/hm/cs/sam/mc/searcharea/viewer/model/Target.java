package edu.hm.cs.sam.mc.searcharea.viewer.model;

/**
 * @author Philipp Trepte
 * Model to represent a target.
 */
public class Target {

	/** target in the search area */
	private final SearchAreaTarget targetMsg;
	/** flag which indicates that the target is been fully processed */
	private boolean isCompleted;

	/**
	 * custom constructor.
	 * @param target target in the search area
	 */
	public Target(final SearchAreaTarget target) {
		this.targetMsg = target;
		this.isCompleted = false;
	}

	/**
	 * Returns the target.
	 * @return target
	 */
	public SearchAreaTarget getTarget() {
		return targetMsg;
	}

	/**
	 * Checks if the target is been fully processed.
	 * @return true is fully processed, false otherwise
	 */
	public boolean isCompleted() {
		return isCompleted;
	}

	/**
	 * Set the flag.
	 * @param isCompleted flag
	 */
	public void setIsCompleted(final boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
}
