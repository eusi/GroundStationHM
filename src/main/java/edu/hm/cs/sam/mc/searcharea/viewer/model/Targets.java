package edu.hm.cs.sam.mc.searcharea.viewer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Philipp Trepte
 * Model to represent the targets in the search area.
 */
public class Targets {

	/** list with targets */
	private final List<Target> searchAreaTargets = new ArrayList<Target>();

	/**
	 * Add a target
	 * @param object target
	 */
	public synchronized void addTarget(final SearchAreaTarget object) {
		searchAreaTargets.add(new Target(object));
	}

	/**
	 * Returns a target.
	 * @param id target id
	 * @return target
	 */
	public synchronized SearchAreaTarget getTarget(final int id) {
		return searchAreaTargets.get(id).getTarget();
	}

	/**
	 * Sets the target.
	 * @param id target id
	 * @param target target
	 */
	public synchronized void setTarget(final int id, final SearchAreaTarget target) {
		searchAreaTargets.set(id, new Target(target));
	}

	/**
	 * Removes the target.
	 * @param id target id
	 */
	public synchronized void removeTarget(final int id) {
		searchAreaTargets.remove(id);
	}

	/**
	 * Sets the target to completed.
	 * @param id target id
	 */
	public synchronized void setToCompleted(final int id) {
		searchAreaTargets.get(id).setIsCompleted(true);
	}

	/**
	 * Checks if the target is completed.
	 * @param id target id
	 * @return true is completed, false otherwise
	 */
	public synchronized boolean isTargetCompleted(final int id) {
		return searchAreaTargets.get(id).isCompleted();
	}

	/**
	 * Returns all targets.
	 * @return all targets
	 */
	public synchronized List<SearchAreaTarget> getAllTargets() {
		final List<SearchAreaTarget> list = new ArrayList<SearchAreaTarget>();
		for (final Target target : searchAreaTargets) {
			list.add(target.getTarget());
		}
		return Collections.unmodifiableList(list);
	}

	/**
	 * Returns the number of the targets.
	 * @return number of the targets
	 */
	public synchronized int getSize() {
		return searchAreaTargets.size();
	}
}