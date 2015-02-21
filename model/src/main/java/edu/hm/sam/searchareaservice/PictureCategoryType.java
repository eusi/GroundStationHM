package edu.hm.sam.searchareaservice;

/**
 * Created by Philipp on 04.12.2014.
 * Enumeration for the picture category.
 */
public enum PictureCategoryType {

	/** type for the area of interest during the high flight */
	AOIHF("area of interest (high flight)"),
	/** type for the area of interest during the low flight */
	AOILF("area of interest (low flight)"),
	/** type for the adlc task */
	ADLC("automatic detection, localization and classification task"),
	/** type for the search area task */
	SEARCHAREA("search area task"),
	/** type for the actionable intelligence task */
	ACTIONABLEINTELL("actionable intelligence task"),
	COMPLETEDTARGETS("completed targets"),
    NORESULTS("no results");

    /** name of the enum */
    private final String name;

    /**
     * private constructor.
     * @param name name of the enum
     */
    private PictureCategoryType(final String name) {
        this.name = name;
    }

    /**
     * Returns the name of the enum.
     * @return name
     */
    public String toString() {
        return name;
    }
}