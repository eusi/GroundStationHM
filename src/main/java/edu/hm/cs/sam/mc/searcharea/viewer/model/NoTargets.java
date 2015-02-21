package edu.hm.cs.sam.mc.searcharea.viewer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Philipp Trepte
 * Model to save the pictures without area of interest.
 */
public class NoTargets {

	/** list of all pictures */
	private final List<SearchAreaTarget> noTargets = new ArrayList<>();

	/**
	 * Add a picture.
	 * @param target picture
	 */
	public synchronized void addPicture(final SearchAreaTarget target) {
		noTargets.add(target);
	}

	/**
	 * Returns a picture.
	 * @param id picture id
	 * @return picture
	 */
	public synchronized SearchAreaTarget getTarget(final int id) {
		return noTargets.get(id);
	}

	/**
	 * Removes a picture.
	 * @param id picture id
	 */
	public synchronized void removePicture(final int id) {
		noTargets.remove(id);
	}

	/**
	 * Returns a list of all pictures.
	 * @return list of all pictures
	 */
	public synchronized List<SearchAreaTarget> getAllPicturesWithoutTargets() {
		return Collections.unmodifiableList(noTargets);
	}

	/**
	 * Returns the size.
	 * @return size
	 */
	public synchronized int getSize() {
		return noTargets.size();
	}
}