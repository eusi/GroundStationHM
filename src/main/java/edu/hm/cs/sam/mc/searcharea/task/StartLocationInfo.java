package edu.hm.cs.sam.mc.searcharea.task;
/**
 * This class represents the info for a starting point. It contains
 * the index of the corner in the corners arrayList, the pattern direction,
 * the direction of the first track and the length.
 *
 * @author Maximilian Bayer
 */
public class StartLocationInfo {
	private int index;
	private double patternDir;
	private double firstTrackDir;
	private double lengthFirstTrack;
	
	public StartLocationInfo(int index, double patternDir, double firstTrackDir, double lengthFirstTrack) {
		this.index = index;
		this.patternDir = patternDir;
		this.firstTrackDir = firstTrackDir;
		this.lengthFirstTrack = lengthFirstTrack;
	}
	
	public double getPatternDir() {
		return patternDir;
	}
	public double getFirstTrackDir() {
		return firstTrackDir;
	}
	public double getLengthFirstTrack() {
		return lengthFirstTrack;
	}
	public int getIndex() {
		return index;
	}
	public String toString() {
		return "Index: "+index+ " patternDir: "+patternDir+" firstTrackDir: "+firstTrackDir+" length: "+lengthFirstTrack;
	}
}
