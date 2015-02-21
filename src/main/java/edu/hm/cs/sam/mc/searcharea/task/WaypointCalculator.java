package edu.hm.cs.sam.mc.searcharea.task;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.ObstacleStatic;
import edu.hm.cs.sam.mc.misc.Obstacles;
import edu.hm.cs.sam.mc.misc.Wind;
import edu.hm.cs.sam.mc.rest.RestClient;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The WaypointCalculator offers all kinds of coordinate-math-methods like
 * calculation of distances, bearing or interception of lines between
 * coordinates. Furthermore it offers the calculation of waypoints which are
 * spread over the borders of a zone to build up a lawn-mowing pattern.
 *
 * @author Maximilian Bayer
 */
public class WaypointCalculator {
    /**
     * Threshold for allowed bearing-change during low flight
     */
    private static final double THRESHOLD = 50;
    private static final Logger LOG = Logger.getLogger(WaypointCalculator.class.getName());

    /**
     * Constructor. Adds all subscriptionHandlers.
     */
    public WaypointCalculator() {
    }

    /**
     * Calculates the gradient and the yInterception of a line by two given
     * points.
     *
     * @param x1 x-coordinate of point1
     * @param y1 y-coordinate of point1
     * @param x2 x-coordinate of point2
     * @param y2 y-coordinate of point2
     * @return resulting line with gradient and yIntercept
     */
    public Line calculateLineFrom2Points(final LocationWp firstCoordinate,
                                         final LocationWp secondCoordinate) {
        final double gradient = (secondCoordinate.getLng() - firstCoordinate.getLng())
                / (secondCoordinate.getLat() - firstCoordinate.getLat());
        final double yIntercept = secondCoordinate.getLng()
                - (gradient * secondCoordinate.getLat());
        final double length = calculateDistanceBetween2CoordsInMeter(firstCoordinate,
                secondCoordinate);
        return new Line(gradient, yIntercept, firstCoordinate, secondCoordinate, length);
    }

    /**
     * Calculate the intersection point of two lines.
     *
     * @param firstLine  Line1
     * @param secondLine Line2
     * @return Coordinate of intersection point; null if no intersection
     */
    public LocationWp calculateIntersection(final Line firstLine, final Line secondLine, final double altitude) {
        final double xs = (secondLine.getyIntercept() - firstLine.getyIntercept())
                / (firstLine.getGradient() - secondLine.getGradient());
        final double ys = ((firstLine.getGradient() * secondLine.getyIntercept()) - (secondLine
                .getGradient() * firstLine.getyIntercept()))
                / (firstLine.getGradient() - secondLine.getGradient());
        final LocationWp intersect = new LocationWp(xs, ys, altitude,MavCmd.WAYPOINT, 1, 0, 0, 0, 0);
        // check whether it is ON the line
        final boolean onLine = checkWhetherPointIsOnLine(intersect, secondLine);
        if (onLine) {
            return intersect;
        } else {
            return null;
        }
    }

    /**
     * Check whether a coordinate is on this line.
     *
     * @param c          coordinate to check
     * @param secondLine line to check
     * @return true, if point is on the line; false else
     */
    public boolean checkWhetherPointIsOnLine(final LocationWp coordinate, final Line secondLine) {
        final LocationWp firstCoordinate = secondLine.getFirstCoordinate();
        final LocationWp secondCoordinate = secondLine.getSecondCoordinate();
        final double distInterFirstCoordinate = calculateDistanceBetween2CoordsInMeter(coordinate,
                firstCoordinate);
        final double distInterSecondCoordinate = calculateDistanceBetween2CoordsInMeter(coordinate,
                secondCoordinate);
        final double dist = calculateDistanceBetween2CoordsInMeter(firstCoordinate,
                secondCoordinate);
        if ((distInterFirstCoordinate > dist) || (distInterSecondCoordinate > dist)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Calculate a waypoint that is a given distance away from the Coordinate
     * firstCoordinate in direction of alpha in dregrees.
     *
     * @param firstCoordinate Coordinate to "travel" from
     * @param distance        Distance between this and the waypoint we're looking for.
     * @param alpha           Direction in which the waypoint we're looking for lies. In
     *                        Degrees: 0� = N ; 90� = E ; 180� = S ; 270� = W
     * @return
     */
    public LocationWp calculateNewWaypoint(final LocationWp firstCoordinate,
                                           final double distance, final double alpha) {
        final double earthRadius = 6378.1; // Radius of the Earth in km
        final double brng = Math.toRadians(alpha); // bearing in degrees
        // converted to radians.

        final double firstLatitude = Math.toRadians(firstCoordinate.getLat()); // Current
        // lat
        // point
        // converted to radians
        final double lon1 = Math.toRadians(firstCoordinate.getLng()); // Current
        // long
        // point
        // converted to radians

        double secondLatitude = Math.asin((Math.sin(firstLatitude) * Math.cos(distance
                / earthRadius))
                + (Math.cos(firstLatitude) * Math.sin(distance / earthRadius) * Math.cos(brng)));

        double lon2 = lon1
                + Math.atan2(
                Math.sin(brng) * Math.sin(distance / earthRadius) * Math.cos(firstLatitude),
                Math.cos(distance / earthRadius)
                        - (Math.sin(firstLatitude) * Math.sin(secondLatitude)));

        secondLatitude = Math.toDegrees(secondLatitude);
        lon2 = Math.toDegrees(lon2);
        return new LocationWp(secondLatitude, lon2);
    }

    /**
     * Calculates the distance between 2 coordinates in meter.
     *
     * @param firstCoordinate  1st coordinate
     * @param secondCoordinate 2nd coordinate
     * @return distance between coordinates
     */
    public double calculateDistanceBetween2CoordsInMeter(final LocationWp firstCoordinate,
                                                         final LocationWp secondCoordinate) {
        final double earthRadius = 6378.1; // Radius of the earth in km
        final double a1 = (0.5 - (Math
                .cos(((secondCoordinate.getLat() - firstCoordinate.getLat()) * Math.PI) / 180) / 2))
                + ((Math.cos((firstCoordinate.getLat() * Math.PI) / 180)
                * Math.cos((secondCoordinate.getLat() * Math.PI) / 180) * (1 - Math
                .cos(((secondCoordinate.getLng() - firstCoordinate.getLng()) * Math.PI) / 180))) / 2);
        return earthRadius * 1000 * 2 * Math.asin(Math.sqrt(a1)); // in meters
    }

    /**
     * Calculates the bearing from coordinate firstCoordinate to
     * secondCoordinate.
     *
     * @param firstCoordinate  First coordinate
     * @param secondCoordinate Second coordinate
     * @return bearing from firstCoordinate to secondCoordinate in degree
     */
    public double calcBearing(final LocationWp firstCoordinate, final LocationWp secondCoordinate) {
        // Convert input values to radians
        final double firstLatitude = degToRad(firstCoordinate.getLat());
        final double firstLongitude = degToRad(firstCoordinate.getLng());
        final double secondLatitude = degToRad(secondCoordinate.getLat());
        final double secondLongitude = degToRad(secondCoordinate.getLng());

        final double deltaLong = secondLongitude - firstLongitude;

        final double y = Math.sin(deltaLong) * Math.cos(secondLatitude);
        final double x = (Math.cos(firstLatitude) * Math.sin(secondLatitude))
                - (Math.sin(firstLatitude) * Math.cos(secondLatitude) * Math.cos(deltaLong));
        final double bearing = Math.atan2(y, x);
        return convertToBearing(radToDeg(bearing));
    }

    /**
     * Converts degrees into bearing
     *
     * @param deg degrees
     * @return bearing
     */
    public double convertToBearing(final double deg) {
        return (deg + 360) % 360;
    }

    /**
     * Converts radian angle to degrees
     *
     * @param radians angle in radian
     * @return angle in degree
     */
    public double radToDeg(final double radians) {
        return radians * (180 / Math.PI);
    }

    /**
     * Converts degree angle to radians
     *
     * @param degrees angle in degrees
     * @return angle in radian
     */
    public double degToRad(final double degrees) {
        return degrees * (Math.PI / 180);
    }

    /**
     * Rasenmaeher-Strategy: START AND DIRECTIONS ARE DEFINED THROUGH WIND
     * IF AVAILABLE; IF NOT LONGEST BORDER DEFINES START AND DIRECTIONS
     * Calculates waypoints so that an area defined by corner
     * coordinates can completely be overflown.
     * POINTS MUST DEFINE THE AREA LIKE THIS:
     * - this and next point make a border
     * - last and 1st point make  a border
     * Waypoints shall be arranged in a lawn-mowing-pattern.
     *
     * @param corners ArrayList of coordinates defining the area corners
     * @param width   width of 1 track of lawn-mowing cycle
     * @return ArrayList of Coordinates defining the route for lawn-mowing
     * flight
     */
    public List<LocationWp> lawnMowingPattern(final List<LocationWp> corners, final double width, final double altitude) {
    	List<StartLocationInfo> infos = findPossibleStarts(corners);
    	// try to get wind
		Wind wind = null;
    	try {
        	wind = RestClient.getCurrentWindData();
        	LOG.info("SearchArea Routing received winddata: "+wind.getVelocity()+" , "+wind.getDirection());
    	} catch(NullPointerException e2) {
        	LOG.error("SearchArea Routing could not receive winddata!");
    	}
    	// try to get obstacles
    	try {
    		RestClient.getObstacles();
    		Obstacles obstacles = Data.getCurrentObstacles();
        	ObstacleStatic[] staticObstacles = obstacles.getoStaticlist();
        	LOG.info("SearchArea Routing received "+staticObstacles.length+" static obstacles");
    	} catch(NullPointerException e3) {
        	LOG.error("SearchArea Routing could not receive obstacles!");
    	}
        final List<Line> borderLines = createAllBorders(corners);
		List<LocationWp> coords = null;
        // case 1: wind not available or too weak
    	if(wind == null || wind.getVelocity()*0.5144444444<1.5) {
    		LOG.info("Wind not received or too weak");
            while(coords == null || coords.size() == 0) {
	    		// find longest
	    		double tempLength = 0;
	    		int index = -1;
	    		for(int i = 0; i < infos.size(); i++) {
	    			if(infos.get(i).getLengthFirstTrack() > tempLength) {
	    				tempLength = infos.get(i).getLengthFirstTrack();
	    				index = i;
	    			}
	    		}
            	coords = lawnMowingAlgorithm(corners, width, borderLines, infos.get(index).getFirstTrackDir(),
    												infos.get(index).getPatternDir(), infos.get(index).getIndex(), altitude);
            	infos.remove(index);
            }
            return coords;
    	}
    	// case 2: regard wind
    	else {
    		while(coords == null || coords.size() == 0) {
    			double minDiff = Double.MAX_VALUE;
	    		int index = -1;
	    		// find start with best direction regarding wind
    			for(int i = 0; i < infos.size(); i++) {
    				if(Math.abs(180-Math.abs(wind.getDirection()-infos.get(i).getPatternDir()))<minDiff) {
    					minDiff = Math.abs(180-Math.abs(wind.getDirection()-infos.get(i).getPatternDir()));
    					index = i;
    				}
    			}
            	coords = lawnMowingAlgorithm(corners, width, borderLines, infos.get(index).getFirstTrackDir(),
    												infos.get(index).getPatternDir(), infos.get(index).getIndex(), altitude);
            	infos.remove(index);
            }
            return coords;
    	}
    }
    /**
     * Calculates all possible starts with their info.
     * Infos are index in corner array, pattern direction, direction of first track and the length.
     * Starts would end in a deadlock are sorted out by angel comparison. Corners with angle > 180 are useless.
     * Also the corners with angle > 180 may not be destination of first track.
     * @param corners Corners of the search area zone
     * @return List with all possible startinfos.
     */
	private List<StartLocationInfo> findPossibleStarts(List<LocationWp> corners) {
		ArrayList<StartLocationInfo> infos = new ArrayList<StartLocationInfo>();
		double sum = 0;
    	double anglesum = (corners.size()-2)*180+1;
    	double angles[] = new double[corners.size()];
    	//calculate all corner-angles and anglesum
    	for(int i = 0; i<corners.size(); i++) {
    		int follower = i +1;
    		int predecessor = i-1;
    		if(i==0) {
    			predecessor = corners.size()-1;
    		} else if(i==corners.size()-1) {
    			follower = 0;
    		}
    		double bearing1 = calcBearing(corners.get(i), corners.get(follower));
    		double bearing2 = calcBearing(corners.get(i), corners.get(predecessor));
    		double angle;
    		if(bearing1>bearing2) {
    			angle = bearing1-bearing2;
    			angle = 360-angle;
    		} else {
    			angle = bearing2-bearing1;
    		}
        	sum += angle;
        	angles[i] = angle;
    	}
    	// depending on the direction of rotation, in which the points where entered
    	// we may have calculated the exterior angles. Check for anglesum
    	if(sum > anglesum) {
    		for(int i = 0; i < angles.length; i++) {
    			angles[i] = 360-angles[i];
    		}
    	}
    	// create startInfos
    	for(int i = 0; i < angles.length; i++) {
    		int next = i +1;
    		int previous = i-1;
    		if(i==0) {
    			previous = corners.size()-1;
    		} else if(i==corners.size()-1) {
    			next = 0;
    		}
    		if(angles[i] < 180) {
    			if(angles[previous] < 180) {
    				double bearingPrev = calcBearing(corners.get(i), corners.get(previous));
    				double bearingNext = calcBearing(corners.get(i), corners.get(next));
    				double length = calculateDistanceBetween2CoordsInMeter(corners.get(i), corners.get(previous));
    				StartLocationInfo info = new StartLocationInfo(i, bearingNext, bearingPrev, length);
    				infos.add(info);
    			}
    			if(angles[next] < 180) {
    				double bearingPrev = calcBearing(corners.get(i), corners.get(previous));
    				double bearingNext = calcBearing(corners.get(i), corners.get(next));
    				double length = calculateDistanceBetween2CoordsInMeter(corners.get(i), corners.get(next));
    				StartLocationInfo info = new StartLocationInfo(i, bearingPrev, bearingNext, length);
    				infos.add(info);
    			}
    		}
    	}
		return infos;
	}
    /**
     * Calculates waypoints following the lawn-mowing pattern.
     *
     * @param corners           Corner coordinates defining the area
     * @param width             Wished width of lawn-mowing track
     * @param borderLines       All border lines defining the area
     * @param angle_first_track Angle for first flight track in degrees
     * @param angle_pattern     Angle for pattern growing in degrees
     * @param index             Index of start point in corners' list
     * @return All waypoints to cover whole area with lawn mowing pattern
     */
    private List<LocationWp> lawnMowingAlgorithm(final List<LocationWp> corners,
            final double width, final List<Line> borderLines, final double angleFirstTrack,
            final double anglePattern, final int index, final double altitude) {
    	double halfWidth = Double.MAX_VALUE;
        final List<LocationWp> waypoints = new ArrayList<LocationWp>();
        boolean takeNearIntersect = true;
        final double height = 5;
        boolean reachedOutside = false;
        LocationWp first = corners.get(index);
        // starting point
        first = calculateNewWaypoint(first, width / 2, anglePattern);
        LocationWp helpNext;
        while (!reachedOutside) {
            // help point in 5km distance
            helpNext = calculateNewWaypoint(first, height, angleFirstTrack);
            final Line line1 = calculateLineFrom2Points(first, helpNext);
            final List<LocationWp> intersections = new ArrayList<LocationWp>();
            // calculate all intersections
            for (int k = 0; k < borderLines.size(); k++) {
                final LocationWp intersect = calculateIntersection(line1, borderLines.get(k), altitude);
                if (intersect != null) {
                    intersections.add(intersect);
                }
            }
            if (!intersections.isEmpty()) {
            	// deadlock, try other starting point
            	if(intersections.size()>2) {
            		return null;
            	}
                // alternate between taking the closer intersection point(for
                // pattern growing)
                // and the one more far away (flying 1 track)
                if (takeNearIntersect) {
                    if (calculateDistanceBetween2CoordsInMeter(first, intersections.get(0)) < calculateDistanceBetween2CoordsInMeter(
                            first, intersections.get(1))) {
                        waypoints.add(intersections.get(0));
                        waypoints.add(intersections.get(1));
                    } else {
                        waypoints.add(intersections.get(1));
                        waypoints.add(intersections.get(0));
                    }
                } else {
                    if (calculateDistanceBetween2CoordsInMeter(first, intersections.get(0)) < calculateDistanceBetween2CoordsInMeter(
                            first, intersections.get(1))) {
                        waypoints.add(intersections.get(1));
                        waypoints.add(intersections.get(0));
                    } else {
                        waypoints.add(intersections.get(0));
                        waypoints.add(intersections.get(1));
                    }
                }
                first = calculateNewWaypoint(first, width, anglePattern); // new bottom point
            } else {
            	if(halfWidth == width/2) {
                    reachedOutside = true;
            	} else {
	            	// new bottom point with half width to secure coverage. ONLY ONCE
	            	halfWidth = width/2;
	            	first = calculateNewWaypoint(first, halfWidth, anglePattern);
            	}
            }
            takeNearIntersect = !takeNearIntersect;
        }
        return waypoints;
    }

    /**
     * Find index of the longest line in a ArrayList of lines
     *
     * @param borderLines ArrayList of lines to check
     * @return index of longest line
     */
    public int findLongestLine(final List<Line> borderLines) {
        int longestIndex = 0;
        double tempLength = 0;
        for (int i = 0; i < borderLines.size(); i++) {
            if (tempLength < borderLines.get(i).getLength()) {
                tempLength = borderLines.get(i).getLength();
                longestIndex = i;
            }
        }
        return longestIndex;
    }

    /**
     * Creates all border lines from the corner coordinates.
     *
     * @param zoneCorners All coordinates
     * @return ArrayList of all border lines
     */
    public List<Line> createAllBorders(final List<LocationWp> zoneCorners) {
        final List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < (zoneCorners.size() - 1); i++) {
            lines.add(calculateLineFrom2Points(zoneCorners.get(i), zoneCorners.get(i + 1)));
        }
        lines.add(calculateLineFrom2Points(zoneCorners.get(zoneCorners.size() - 1),
                zoneCorners.get(0)));
        return lines;
    }

    /**
     * Finds the best order for a list of points. Best means shortest distance
     * as well as minimal number of drastic curves.
     *
     * @param start  Coordinate to start from
     * @param coords List of coordinates to order
     * @return List of coordinates in optimal order
     */
    public List<LocationWp> nearestNeighbour(final List<LocationWp> coords) {
    	LocationWp current = null;
    	try {
    		current = Data.getCurrentPosition();
    		LOG.info("Current Position:  "+current.getLat()+"  "+current.getLng());
    	} catch(NullPointerException e) {
    		LOG.info("Current Position not received");
    	}
        final List<LocationWp> result = new ArrayList<LocationWp>();
        if(current == null) {
        	current = coords.remove(0);
            result.add(current);
        }
        double nextBearing = -1;
        int index = 0;
        while (!coords.isEmpty()) {
            LocationWp next = null;
            if (coords.size() > 1) {
                double nearest = Double.MAX_VALUE;
                for (int i = 0; i < coords.size(); i++) {
                    final double dist = calculateDistanceBetween2CoordsInMeter(current, coords.get(i));
                    if (nearest > dist) {
                        final double bearing = calcBearing(current, coords.get(i));
                        if ((nextBearing == -1) || (Math.abs(nextBearing - bearing) < THRESHOLD)) {
                            nearest = dist;
                            next = coords.get(i);
                            index = i;
                        }
                    }
                }
                // no coordinate fulfilled bearing condition, so just take
                // nearest
                if (next == null) {
                    for (int i = 0; i < coords.size(); i++) {
                        final double dist = calculateDistanceBetween2CoordsInMeter(current,
                                coords.get(i));
                        if (nearest > dist) {
                            nearest = dist;
                            next = coords.get(i);
                            index = i;
                        }
                    }
                }
            } else {
                // only 1 coordinate remaining, take it
                next = coords.get(0);
                index = 0;
            }
            coords.remove(index);
            result.add(next);
            nextBearing = calcBearing(current, next);
            current = next;
        }
        return result;
    }

    /**
     * Main for testing.
     *
     * @param args cmd arguments
     */
    public static void main(final String[] args) {
    	//redmine area
    	final LocationWp cornerA = new LocationWp(47.261961057368, 11.3426971435547, 'A');
    	final LocationWp cornerB = new LocationWp(47.2604902553321, 11.3333415985107, 'B');
    	final LocationWp cornerC = new LocationWp(47.2561504230734, 11.3349294662476, 'C');
    	final LocationWp cornerD = new LocationWp(47.2583495100493, 11.3401651382446, 'D');
        final LocationWp cornerE = new LocationWp(47.256616462377, 11.3452506065369, 'E');
        final LocationWp cornerF = new LocationWp(47.2598931854198, 11.3464093208313, 'F');
    	/*/ Viereck
    	final LocationWp cornerA = new LocationWp(48.56734, 11.95963, 'A');
    	final LocationWp cornerB = new LocationWp(48.57008, 11.95960, 'B');
    	final LocationWp cornerC = new LocationWp(48.57202, 11.96543, 'C');
    	final LocationWp cornerD = new LocationWp(48.56738, 11.96569, 'D');
        final LocationWp cornerE = new LocationWp(48.57013, 11.96251, 'E');
        final LocationWp cornerF = new LocationWp(48.57220, 11.96276, 'F');
        // Annähernd eine Area wie im Regelwerk (Sechseck)
        final LocationWp cornerA = new LocationWp(48.56677, 11.96126, 'A');
        final LocationWp cornerB = new LocationWp(48.56992, 11.96199, 'B');
        final LocationWp cornerC = new LocationWp(48.56930, 11.96512, 'C');
        final LocationWp cornerD = new LocationWp(48.56847, 11.96418, 'D');
        final LocationWp cornerE = new LocationWp(48.57208, 11.96160, 'E');
        final LocationWp cornerF = new LocationWp(48.56830, 11.96684, 'F');
        // Dreieck

         * Coordinate A = new Coordinate(48.56475,11.95113,'A'); Coordinate B =
         * new Coordinate(48.56109,11.97327,'B'); Coordinate C = new
         * Coordinate(48.57668,11.96679,'C');
         */
        final WaypointCalculator calc = new WaypointCalculator();
        final GpxFileOutputer outputer = new GpxFileOutputer(
                "C:/Users/Baysen/Desktop/currentWaypoints.gpx");
        final List<LocationWp> corners = new ArrayList<LocationWp>();
        corners.add(cornerA);
        corners.add(cornerB);
        corners.add(cornerC);
        corners.add(cornerD);
        corners.add(cornerE);
        corners.add(cornerF);
        List<StartLocationInfo> result1 = calc.findPossibleStarts(corners);
        List<LocationWp> result = calc.lawnMowingPattern(corners, 0.1, 150);
        corners.addAll(result);
        outputer.outputWaypointsToGPX(corners);
        for(int i = 0; i < result1.size(); i++)
        	System.out.println(result1.get(i));
    }

}
