package edu.hm.cs.sam.mc.searcharea.viewer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Trepte
 * Visitor for the search area targets.
 */
public class SearchAreaTargetVisitor implements TargetVisitor {

	/** current picture category */
	private final String category;

	/** list with all picture paths */
	private final List<String> imageList = new ArrayList<String>();
	/** list with all ids */
	private final List<Integer> idList = new ArrayList<Integer>();

	/**
	 * custom constructor.
	 * @param category current picture type
	 */
	public SearchAreaTargetVisitor(final String category) {
		this.category = category;
	}

	@Override
	public void visit(final QRCTarget qrc) {
		if (category.equals(qrc.getPictureCategoryType())) {
			idList.add(qrc.getID());
			imageList.add(qrc.getPicturePath());
		}
	}

	@Override
	public void visit(final StandardTarget standard) {
		if (category.equals(standard.getPictureCategoryType())) {
			idList.add(standard.getID());
			imageList.add(standard.getPicturePath());
		}
	}

	@Override
	public void visit(final AreaOfInterest aoi) {
		if (category.equals(aoi.getPictureCategoryType())) {
			idList.add(aoi.getID());
			imageList.add(aoi.getPicturePath());
		}
	}

	@Override
	public void visit(final Picture picture) {
		if (category.equals(picture.getPictureCategoryType())) {
			idList.add(picture.getID());
			imageList.add(picture.getPicturePath());
		}
	}

	/**
	 * Returns the picture paths.
	 * @return picture paths
	 */
	public List<String> getList() {
		return imageList;
	}

	/**
	 * Returns the ids.
	 * @return ids
	 */
	public List<Integer> getIDList() {
		return idList;
	}
}