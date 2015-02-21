package edu.hm.cs.sam.mc.searcharea.viewer.model;

import edu.hm.sam.location.LocationWp;

/**
 * @author Philipp Trepte
 * Model to represent an area of interest.
 */
public class AreaOfInterest extends SearchAreaTarget {

	/**
	 * custom constructor
	 * @param id area of interest id
	 * @param picturePath picture path
	 * @param location location
	 * @param targetType target type
	 * @param pictureCategoryType picture type
	 */
	public AreaOfInterest(final int id, final String picturePath, final LocationWp location,
			final String targetType, final String pictureCategoryType) {
		super(id, picturePath, location, targetType, pictureCategoryType);
	}

	@Override
	public void accept(final TargetVisitor visitor) {
		visitor.visit(this);
	}
}