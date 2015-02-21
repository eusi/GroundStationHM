/**
 *
 */
package edu.hm.cs.sam.mc.ir.mainground;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.ir.enuminterfaces.GroundGuiInterface;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;

/**
 * Abstract class for handling the tasks ir static, ir dynamic and emergent.
 * Contains uploading to routing, calculating distances, and waypoint checking. 
 * 
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas Angermeier (Team 05 - Infrared/Emergent Task)
 */
public abstract class GroundComponent implements GroundGuiInterface {

    // "Waypoint erreicht" refresh check time (Sekunden), 8 x pro Sekunde
    protected static final long STATIC_REFRESH_TIME = 125;
    protected static final long DYNAMIC_REFRESH_TIME = 125;
    protected static final long EMERGENT_REFRESH_TIME = 125;

    // Hoehen (Meter)
    protected static final double STATIC_ALT = 40;
    protected static final double DYNAMIC_ALT = 40;
    protected static final double EMERGENT_ALT = 40;
    
    protected static final String PUBNAME = "GroundPublisher";
    protected static final String TOPIC = "send_orders_ir";

    protected static final String GET_ONLINE_STATUS = "getOnlineStatus";
    protected static final String TAKE_PHOTO = "takePhoto";
    
    protected static final String STOP_PHOTO = "stopPhoto";
    protected static final String TAKE_PHOTO_AND_IRPHOTO = "takePhotoAndIRPhoto";
    protected static final String STOP_PHOTO_AND_IRPHOTO = "stopPhotoAndIRPhoto";
    
    protected static final String START_IR_VIDEO = "startIRVideo";
    protected static final String STOP_IR_VIDEO = "stopIRVideo";
    
    protected static final double WAYPOINTDISTANCE = 50.0;
    protected static final double OBSTACLETOLERANCE = 10.0;
    
    protected static List<Double> latsObstacle = new ArrayList<>();
    protected static List<Double> lonsObstacle = new ArrayList<>();
    
    private static final Logger LOG = Logger.getLogger(GroundComponent.class.getName());
    
    protected GroundAirPublisher groundPublisher;

    @Override
    public abstract boolean isTaskCalculated();

    @Override
    public abstract void calcWaypoints();

    

    /**
     * Transfers list into waypoints format for uploading to routing.
     * Sends calculated waypoints to routing.
     *
     * @param calculatedStatWaypoints The calculated waypoints to be sent to routing.
     */
    protected void uploadTaskToMissionPlanner(final List<WPWithInfo> calculatedStatWaypoints,
            final SamType taskType) {

        // WPWithInfo list to LocationWp array
        final LocationWp[] wayPointsArray = new LocationWp[calculatedStatWaypoints.size()];
        for (int i = 0; i < wayPointsArray.length; i++) {
            wayPointsArray[i] = calculatedStatWaypoints.get(i);
        }

        // ----
        // Sende an MissionPlanner Gruppe...
        final Waypoints wps = new Waypoints(wayPointsArray, taskType);
        Data.setWaypoints(wps);
        // ----

    }

    /**
     * Drone has reached one waypoint and is now inside the tolerance radius.
     *
     * @param locToCheck The current location to check.
     * @return True if inside, false if not.
     */
    protected boolean isDroneAtWaypoint(final LocationWp locToCheck, final LocationWp currentLoc) {
        //TODO
    	try {
    		return 5 <= distance(locToCheck, currentLoc);
    	} catch (NullPointerException e) {
    		LOG.info("IR: Nullpointer", e);
    		return false;
    	}
        
    }



    /**
     * Sending order to air, take normal photo.
     */
    protected void takeNormalPhoto() {
        groundPublisher.setOrder(TAKE_PHOTO);
        synchronized (groundPublisher) {
            groundPublisher.notify();

        }
    }

    /**
     * Calculating distance between two locations, regarding earth data.
     *
     * @param location Location to be compared.
     * @param currentLoc Current location to be compared.
     * @return Distance between two waypoints.
     */
    protected double distance(final LocationWp location, final LocationWp currentLoc) {
        final double lat1 = location.getLat();
        final double lon1 = location.getLng();

        final double lat2 = currentLoc.getLat();
        final double lon2 = currentLoc.getLng();
        
        final long earthRadius = 6378137;
        return earthRadius * Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(lon2-lon1)));
    }

    protected double distance(final LocationWp location, final double lat2, final double lon2) {
        final double lat1 = location.getLat();
        final double lon1 = location.getLng();
        
        final long earthRadius = 6378137;
        return earthRadius * Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(lon2-lon1)));
    }
    
    /**
     * Calculating distance between two locations, regarding earth data.
     * 
     * @param lat1 First latitude.
     * @param lon1 First longitude.
     * @param lat2 Second latitude.
     * @param lon2 Second longitude.
     * @return Distance between corrdinates.
     */
    protected double distance(final double lat1, final double lon1,  final double lat2, final double lon2 ) {
        
        final long earthRadius = 6378137;
        return earthRadius * Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(lon2-lon1)));
    }


    protected void calcObstacleWaypoints(double latObstacle, double lonObstacle, double latBefore, double lonBefore, double latAfter, double lonAfter, double radius) {
		latsObstacle = new ArrayList<>();
		lonsObstacle = new ArrayList<>();
		
		double x1Origin = getXOffset(lonObstacle, lonBefore, latBefore);
		double y1Origin = getYOffset(latObstacle, latBefore);
		double x2Origin = getXOffset(lonObstacle, lonAfter, latAfter);
		double y2Origin = getYOffset(latObstacle, latAfter);
		
		double x1 = (x1Origin * radius) / Math.sqrt(x1Origin * x1Origin + y1Origin * y1Origin);
		double y1 = (y1Origin * radius) / Math.sqrt(x1Origin * x1Origin + y1Origin * y1Origin);
		double x2 = (x2Origin * radius) / Math.sqrt(x2Origin * x2Origin + y2Origin * y2Origin);
		double y2 = (y2Origin * radius) / Math.sqrt(x2Origin * x2Origin + y2Origin * y2Origin);
		
		double angle = Math.toDegrees(Math.acos((x1*x2 + y1*y2)/(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2))));
		if (angle != 180) {
			angle = 180 + angle;
		}
		

		double crossProd = x1*y2 - x2*y1;
		boolean counterClockwise = crossProd >= 0;
		
		double arcLength = angle/360 * 2 * Math.PI * radius;
		
		int pointsBetween = (int)(arcLength / WAYPOINTDISTANCE) + 1;
		double pointsAngle = angle/pointsBetween;
		
		//calcCoordinatesObst(latObstacle, lonObstacle, x1, y1); Test
		
		double newX = x1;
		double newY = y1;
		double angleSin = Math.sin(Math.toRadians(counterClockwise ? -pointsAngle : pointsAngle));
		double angleCos = Math.cos(Math.toRadians(counterClockwise ? -pointsAngle : pointsAngle));
		
		for (int i = 1; i <= pointsBetween; i++) {
			double oldX = newX;
			double oldY = newY;
			newX = Math.round((angleCos * oldX - angleSin * oldY) * 10000) / 10000.0;
			newY = Math.round((angleSin * oldX + angleCos * oldY) * 10000) / 10000.0;
			
			calcCoordinatesObst(latObstacle, lonObstacle, newX, newY);			
		}
	}
	


	
	private static void calcCoordinatesObst(final double lat, final double lon, final double offsetX, final double offsetY) {

		final double r = 6378137;
		final double dn = offsetY;
		final double de = offsetX;

		double dLat;
		double dLon;

		dLat = dn / r;
		double currLat = lat + ((dLat * 180) / Math.PI);

		dLon = de / (r * Math.cos((Math.PI * currLat) / 180));
		double currLon = lon + ((dLon * 180) / Math.PI);

		latsObstacle.add(currLat);
		lonsObstacle.add(currLon);

//		System.out.println(currLat+", "+currLon); //Anzeige auf Map

	}



	private static double getYOffset(double latObstacle, double latPoint) {
		double lat1 = latObstacle;
		double lat2 = latPoint;
		final double r = 6378137;
		return (lat2 - lat1)*r*Math.PI/180;
	}
	
	private static double getXOffset(double lonObstacle, double lonPoint, double latPoint) {
		double lon1 = lonObstacle;
		double lon2 = lonPoint;
		double lat2 = latPoint;
		final double r = 6378137;
		return (lon2 - lon1)*Math.cos(lat2*Math.PI/180)*r*Math.PI/180;
	}
    
    
	public static Logger getLog() {
		return LOG;
	}

}