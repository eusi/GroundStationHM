/**
 *
 */
package edu.hm.cs.sam.mc.ir.mainground;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.ir.enuminterfaces.ColorEnum;
import edu.hm.cs.sam.mc.ir.enuminterfaces.GUIInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.StaticGuiInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.TasksEnum;
import edu.hm.cs.sam.mc.ir.enuminterfaces.WpType;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.ObstacleStatic;
import edu.hm.cs.sam.mc.misc.Obstacles;
import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;
import edu.hm.sam.location.SamType;

/**
 * Class for handling static infrared target.
 * Calculating waypoints, flying mission.
 * 
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas Angermeier (Team 05 - Infrared/Emergent Task)
 */
public class StaticIRComponent extends GroundComponent implements StaticGuiInterface, Runnable {

    private final GUIInterface irGui;
   
    private List<WPWithInfo> calculatedStatWaypoints = new ArrayList<>();
    
    private Target irStatTar;
    
    private boolean statReadyToStart = false;


    private static double circleCenterLat;
    private static double circleCenterLon;
    private static double currLat;
    private static double currLon;

    private static List<Double> staticLats = new ArrayList<>();
    private static List<Double> staticLons = new ArrayList<>();
    
    //############## done by me #############
    private boolean killThread = false;
    
    private int onlineTick = 0;

    private static final Logger LOG = Logger.getLogger(StaticIRComponent.class.getName());


    /**
     * Static IR component.
     * 
     * @param irGui Infrared GUI.
     * @param publisher Publisher to handle orders with air.
     */
    public StaticIRComponent(final GUIInterface irGui, final GroundAirPublisher groundPublisher) {
        this.irGui = irGui;
        this.groundPublisher = groundPublisher;
    }

    // GUI Entrypoint
    @Override
    public void calcWaypoints() {

    	//Static IR Target direkt per Routen Gui
    	irStatTar = Data.getTargets(SamType.TARGET_IR_STATIC);
    	

    	if(irStatTar == null) {
    		LOG.info("Static IR target not insert yet. Please insert static IR target");
    	} else {
    		
    		calculatedStatWaypoints = new ArrayList<WPWithInfo>();
    		staticLats = new ArrayList<Double>();
    		staticLons = new ArrayList<Double>();

    		double targetLat = irStatTar.getWaypoint().getLat();
    		double targetLng = irStatTar.getWaypoint().getLng();

//    		irGui.setStaticTargetCoordinates(targetLat, targetLng);
    		
    		// ----------------

	        // Berechnung der Wegpunkte fuer Static hier...
	        printStaticWP(targetLat, targetLng);
	
	        

	        
	        // Einkapseln der Berechnung in Waypoints
	        for (int i = 0; i < staticLons.size(); i++) {
	        	switch(i) {
	        	case 2 :
	        	case 10 :
	        	case 19 :
	        	case 27 :
	        		calculatedStatWaypoints.add(new WPWithInfo(staticLons.get(i),staticLats.get(i),
		                    GroundComponent.STATIC_ALT, MavCmd.WAYPOINT, 1, 0, 0, 0, 0, WpType.STARTIRPHOTO));
	        		break;
	        	case 4 :
	        	case 12 :
	        	case 21 :
	        	case 29 :
	        		calculatedStatWaypoints.add(new WPWithInfo(staticLons.get(i),staticLats.get(i),
		                    GroundComponent.STATIC_ALT, MavCmd.WAYPOINT, 1, 0, 0, 0, 0, WpType.STOPIRPHOTO));
	        		break;
	        	default :
	        		calculatedStatWaypoints.add(new WPWithInfo(staticLons.get(i),staticLats.get(i),
		                    GroundComponent.STATIC_ALT, MavCmd.WAYPOINT, 1, 0, 0, 0, 0, WpType.FLYING));
	        		break;	        		
	        	}
	        }
	        // ----------------
	
	        statReadyToStart = true;
	        irGui.printConsole("Static IR waypoints calculated, uploaded to Routing, static IR task ready...");
	
	        
	        // Route (Waypoints) an MC schicken,
	        uploadTaskToMissionPlanner(calculatedStatWaypoints, SamType.TASK_IR_STATIC);
	        
    	}
    }

    @Override
    public void startMission() {
        // Sendet Waypoints an MissionControl fuer Drohne...

        if (statReadyToStart) {
            irGui.setStaticTaskActive(true);
            flyRoute();
        } else {
            irGui.printConsole("Static Waypoints not calculated yet");
        }
    }

    // Groundcomponent GUI Interface, Button "Start Mission" etc. Entrypoint
    @Override
    public void run() {
        startMission();
    }

    @Override
    public boolean isTaskCalculated() {
        return statReadyToStart;
    }
    

    /**
     * Checking waypoints the airplane is about to steer. Performing actions on the different types of waypoints.
     */
    private void flyRoute() {
    	boolean restClientFailed = true; 
    	//############## done by me #############
    	while (restClientFailed && !killThread) {
    		restClientFailed = false;
	    	try {
	    		LocationWp nextWaypoint = Data.getNextPosition();
	        	WPWithInfo firstStaticWaypoint = calculatedStatWaypoints.get(0);
	        	onlineTick = 0;

	        	// ist der nächste anzufliegende Wegpunkt der erste, zweite
	        	//############## done by me #############
	        	while (!killThread && (distance(nextWaypoint, firstStaticWaypoint) > 2.0)) {
	        		Thread.sleep(1000);
	        		nextWaypoint = Data.getNextPosition();
	        		sendOnlineRequest();
	        		
	        		if (onlineTick == 0) {
		        		irGui.printConsole("nextWP2 - lat: " + nextWaypoint.getLat() + " lon: " + nextWaypoint.getLng());
			        	irGui.printConsole("toCheck - lat: " + firstStaticWaypoint.getLat() + " lon: " + firstStaticWaypoint.getLng() + " dist: " + distance(nextWaypoint, firstStaticWaypoint));
			        	irGui.printConsole("toCheck2 - lat: " + calculatedStatWaypoints.get(1).getLat() + " lon: " + calculatedStatWaypoints.get(1).getLng() + " dist: " + distance(nextWaypoint, calculatedStatWaypoints.get(1)));
			        	irGui.printConsole(" ");
	        		}
	        	}
	        	irGui.printConsole("Next waypoint is part of the IR-Static waypoints");
	        	
	        	// ist der nächste anzufliegende Wegpunkt der zweite bzw. ist der Flieger gerade über dem ersten Wegpunkt
	        	while (!killThread && !isDroneAtWaypoint(firstStaticWaypoint, Data.getCurrentPosition()) && (distance(nextWaypoint, calculatedStatWaypoints.get(1)) > 2.0)) {
	        		Thread.sleep(500);
	        		try {
	        			nextWaypoint = Data.getNextPosition();
	        		} catch (NullPointerException e) {
	        			LOG.info("Ir: Nullpointer", e);
	        		}
	        		sendOnlineRequest();
	        	}
	        	
	        	irGui.printConsole("Relevant part now!");

	        	//############## done by me #############
	        	for (int i = 1; i < calculatedStatWaypoints.size() - 2 && !killThread; i++) {
	        	  //############## done by me #############
	        		while(!killThread && !isDroneAtWaypoint(calculatedStatWaypoints.get(i), Data.getCurrentPosition()) && (distance(nextWaypoint, calculatedStatWaypoints.get(i+1)) > 2.0)) {
	            		Thread.sleep(500);
	            		try {
		        			nextWaypoint = Data.getNextPosition();
		        		} catch (NullPointerException e) {
		        			LOG.info("Ir: Nullpointer", e);
		        		}
	            		sendOnlineRequest();
	            	}
	        		sendOnlineRequest();
	        		
	        		WPWithInfo relevantWP = calculatedStatWaypoints.get(i+1);
	        		switch(relevantWP.getWayPointType()) {
	        		case STARTIRPHOTO :
	        			takePhotoAndIrPhoto();
	        			break;
	        		case STOPIRPHOTO :
	        			stopPhotoAndIrPhoto();
	        			break;
	        		case FLYING :
	        			continue;
					default:
						break;
	        		}
	        	}
	        	
	        	irGui.printConsole("Static Task done");
	            irGui.printConsole(" ");
	            //############## done by me #############
	            irGui.taskCancelled(TasksEnum.IRSTATIC,!killThread);
	        	
	    	} catch (InterruptedException e) {
	            LOG.error("Sleep error", e);
	            killThread = true;
			} catch (NullPointerException e) {
	    		LOG.error("Rest client not available", e);
	    		restClientFailed = true;
	    		try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
		            LOG.error("Sleep error", e);
		            killThread = true;
				}
	    	}
    	}
    	//############## done by me #############
    	if(killThread){
    	    irGui.taskCancelled(TasksEnum.IRSTATIC,!killThread);
    	    killThread = false;
    	}
    }


    /**
     * Sending order to publisher, ir photo start, normal photos start.
     */
    private void takePhotoAndIrPhoto() {
        irGui.printConsole("GS: IR/Normal photo START.");
        groundPublisher.setOrder(TAKE_PHOTO_AND_IRPHOTO);
        synchronized (groundPublisher) {
            groundPublisher.notify();
        }
    }
    
    /**
     * Sending order to publisher, ir photo stop, normal photos stop.
     */
    private void stopPhotoAndIrPhoto() {
    	irGui.printConsole("GS: IR/Normal photo STOP.");
        groundPublisher.setOrder(STOP_PHOTO_AND_IRPHOTO);
        synchronized (groundPublisher) {
            groundPublisher.notify();
        }
    }
    
    /**
     * Checking plane online status, sending order to air component.
     */
    private void sendOnlineRequest() {
		onlineTick = (onlineTick + 1) % 4;
		if (onlineTick == 0) {
	    	if(!GroundAirSubscriber.isOnlineQueryReceived()) {
	    		irGui.setDroneConnectionColor(ColorEnum.COLORRED);
	    	}
	    	GroundAirSubscriber.setOnlineQueryReceived(false);
	    	groundPublisher.setOrder(GET_ONLINE_STATUS);
	        synchronized (groundPublisher) {
	            groundPublisher.notify();
	        }
		}
    }

    /**
     * Calculating waypoints for static target.
     * 
     * @param lat Latitude
     * @param lon Longitude
     */
    private void printStaticWP(final double lat, final double lon) {




    	
    	
    	
    	//--------------------------------
    	calcCoordinates(lat, lon, -130, 0);
    	calcCoordinates(lat, lon, -50, 0);
        calcCoordinates(lat, lon, 0, 0);
        calcCoordinates(lat, lon, 40, 0);

        calcCircleCoordinates(lat, lon, 40, 40);

        calcCoordinates(circleCenterLat, circleCenterLon, 28.28, -28.28);
        calcCoordinates(circleCenterLat, circleCenterLon, 40, 0);
        calcCoordinates(circleCenterLat, circleCenterLon, 28.28, 28.28);
        calcCoordinates(circleCenterLat, circleCenterLon, 0, 40);
        calcCoordinates(circleCenterLat, circleCenterLon, -28.28, 28.28);

        calcCoordinates(lat, lon, 0, 40);
        calcCoordinates(lat, lon, 0, 0);
        calcCoordinates(lat, lon, 0, -40);
        calcCoordinates(lat, lon, 0, -75);

        calcCircleCoordinates(lat, lon, 40, -80);

        calcCoordinates(circleCenterLat, circleCenterLon, -28.28, -28.28);
        calcCoordinates(circleCenterLat, circleCenterLon, 0, -40);
        calcCoordinates(circleCenterLat, circleCenterLon, 28.28, -28.28);
        calcCoordinates(circleCenterLat, circleCenterLon, 35, 0);

        calcCoordinates(lat, lon, 55, -55);
        calcCoordinates(lat, lon, 30, -30);
        calcCoordinates(lat, lon, 0, 0);

        calcCircleCoordinates(lat, lon, -57, 0);

        calcCoordinates(circleCenterLat, circleCenterLon, 28.28, 28.28);
        calcCoordinates(circleCenterLat, circleCenterLon, 0, 40);
        calcCoordinates(circleCenterLat, circleCenterLon, -28.28, 28.28);
        calcCoordinates(circleCenterLat, circleCenterLon, -40, 0);
        calcCoordinates(circleCenterLat, circleCenterLon, -28.28, -28.28);
        calcCoordinates(circleCenterLat, circleCenterLon, 0, -40);
        calcCoordinates(circleCenterLat, circleCenterLon, 28.28, -28.28);

        calcCoordinates(lat, lon, 0, 0);
        calcCoordinates(lat, lon, 30, 30);
        calcCoordinates(lat, lon, 60, 60);
        calcCoordinates(lat, lon, 90, 90);

        
        checkStaticObstacles();

    }

    private void checkStaticObstacles() {
    	Obstacles obs = Data.getCurrentObstacles();
    	if (obs != null) {
    		ObstacleStatic [] obstacles = obs.getoStaticlist();
//        	ObstacleStatic[] obstacles = {new ObstacleStatic(new LocationWp(47.25647055033816, 11.337347164447692), 100, 40)}; fuer test

        	int toStartAt = 0;

        	
        	for (int obstIter = 0; obstIter < obstacles.length; obstIter++) {

        		toStartAt = 0;
        		
            	double obstacleRadius = obstacles[obstIter].getCylinderRadius();
            	double obstRadiusTol = obstacleRadius + OBSTACLETOLERANCE;
            	
            	if(obstRadiusTol < 40) {
            		obstacleRadius = 40;
            	}
            	
            	
            	
            	double latObstacle = obstacles[obstIter].getWaypoint().getLat();
            	double lonObstacle = obstacles[obstIter].getWaypoint().getLng();
            	
            	
            	// Falls der erste Punkt im Obstacle liegt
            	if (distance(latObstacle, lonObstacle, staticLats.get(0), staticLons.get(0)) < obstacleRadius) {
            		LocationWp newPoint = Misc.calcCoordsWithOffset(latObstacle, lonObstacle, -obstacleRadius - 5, 0);
            		staticLats.add(0, newPoint.getLat());
            		staticLons.add(0, newPoint.getLng());
            	}
            	// Falls der letzte Punkt im Obstacle liegt
            	if (distance(latObstacle, lonObstacle, staticLats.get(staticLats.size() - 1), staticLons.get(staticLats.size() - 1)) < obstacleRadius) {
            		LocationWp newPoint = Misc.calcCoordsWithOffset(latObstacle, lonObstacle, obstacleRadius/Math.sqrt(2), obstacleRadius/Math.sqrt(2));
            		staticLats.add(newPoint.getLat());
            		staticLons.add(newPoint.getLng());
            	}
            	
            	boolean insideObstacle = false;
            	boolean obstacleFound = false;
            	double latBefore = 0.0;
            	double lonBefore = 0.0;
            	double latAfter = 0.0;
            	double lonAfter = 0.0;
            	
            	int posBefore = 0;
            	int posAfter = 0;
            	
            	for(int i = toStartAt; i < staticLats.size(); i++) {
            		double currentLat = staticLats.get(i);
            		double currentLon = staticLons.get(i);

            		if(distance(latObstacle, lonObstacle, currentLat, currentLon) < obstacleRadius) {
            			if (!insideObstacle) {
            				obstacleFound = true;
            				
            				latBefore = staticLats.get(i-1);
            				lonBefore = staticLons.get(i-1);
            				posBefore = i-1;
        	    			insideObstacle = true;
            			}
            		} else {
            			if (insideObstacle) {
            				latAfter = staticLats.get(i);
            				lonAfter = staticLons.get(i);
            				posAfter = i;
            				break;
            			}
            		}
            	}
            	if (obstacleFound) {
            		calcObstacleWaypoints(latObstacle, lonObstacle, latBefore, lonBefore, latAfter, lonAfter, obstRadiusTol);
            		List<Double> allLats = new ArrayList<>(staticLats.subList(0, posBefore+1));
            		allLats.addAll(latsObstacle);
            		allLats.addAll(staticLats.subList(posAfter, staticLats.size()));
            		staticLats = allLats;
            		
            		List<Double> allLons = new ArrayList<>(staticLons.subList(0, posBefore+1));
            		allLons.addAll(lonsObstacle);
            		allLons.addAll(staticLons.subList(posAfter, staticLons.size()));
            		staticLons = allLons;
            		
            		obstIter--;
            		toStartAt = posBefore + latsObstacle.size() + 1;
            	}

        	}
    	}
    	
    	    	
    	
    	

    	
	}

	/**
     * Calculating coordinates for waypoint calculation.
     * 
     * @param lat Latitude of the center
     * @param lon Longitude of the center
     * @param offsetX offset in x direction (in meters)
     * @param offsetY offset in y direction (in meters)
     */
    private static void calcCoordinates(final double lat, final double lon, final double offsetX,
            final double offsetY) {

        final double r = 6378137;
        final double dn = offsetY;
        final double de = offsetX;

        double dLat;
        double dLon;

        dLat = dn / r;
        currLat = lat + ((dLat * 180) / Math.PI);

        dLon = de / (r * Math.cos((Math.PI * currLat) / 180));
        currLon = lon + ((dLon * 180) / Math.PI);

        staticLats.add(currLat);
        staticLons.add(currLon);
        
//        System.out.println(currLat+", "+currLon);	Anzeige auf Map

    }

    /**
     * Calculating coordiantes of the new circle
     * 
     * @param lat Latitude of the center
     * @param lon Longitude of the center
     * @param offsetX offset in x direction of the new circle (in meters)
     * @param offsetY offset in y direction of the new circle (in meters)
     */
    private static void calcCircleCoordinates(final double lat, final double lon,
            final double offsetX, final double offsetY) {

        final double r = 6378137;
        final double dn = offsetY;
        final double de = offsetX;

        double dLat;
        double dLon;

        dLat = dn / r;
        circleCenterLat = lat + ((dLat * 180) / Math.PI);

        dLon = de / (r * Math.cos((Math.PI * currLat) / 180));
        circleCenterLon = lon + ((dLon * 180) / Math.PI);
    }

    

	@Override
	public void startFallBack() {
		takePhotoAndIrPhoto();		
	}

	@Override
    public void stopFallBack() {
    	stopPhotoAndIrPhoto();
    }
    
    //############## done by me #############
    public void stop(){
        killThread = true;
    }


}
