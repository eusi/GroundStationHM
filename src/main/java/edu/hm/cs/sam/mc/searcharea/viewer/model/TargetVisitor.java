package edu.hm.cs.sam.mc.searcharea.viewer.model;

/**
 * @author Philipp Trepte
 * Visitor which visit the targets in the search area.
 */
public interface TargetVisitor {

	/**
	 * Visit the qrc target.
	 * @param qrc qrc target
	 */
	public void visit(final QRCTarget qrc);

	/**
	 * Visit the standard target.
	 * @param standard standard target
	 */
	public void visit(final StandardTarget standard);

	/**
	 * Visit the area of interest.
	 * @param aoi area of interest
	 */
	public void visit(final AreaOfInterest aoi);

	/**
	 * Visit the picture.
	 * @param picture picture
	 */
	public void visit(final Picture picture);
}
