package edu.hm.cs.sam.mc.ir.mainground;

import edu.hm.sam.location.LocationWp;

/**
 * Miscellaneous class.
 * 
 * @author Maximilian Haag, Markus Linsenmaier (Team 05 - Infrared/Emergent Task)
 */
public class Misc {


	private Misc() {
	}
	
	/**
	 * Calculating coordinates with offset.
	 * 
	 * @param lat Latitude of the old point.
	 * @param lon Longitude of the old point.
	 * @param offsetX X offset (in meters).
	 * @param offsetY Y offset (in meters).
	 * @return Coordinates of the new point.
	 */
	public static LocationWp calcCoordsWithOffset(final double lat, final double lon, final double offsetX, final double offsetY) {

		final double r = 6378137;
		final double dn = offsetY;
		final double de = offsetX;

		double dLat;
		double dLon;

		dLat = dn / r;
		double currLat = lat + ((dLat * 180) / Math.PI);

		dLon = de / (r * Math.cos((Math.PI * currLat) / 180));
		double currLon = lon + ((dLon * 180) / Math.PI);

		return new LocationWp(currLat, currLon);


	}
}
