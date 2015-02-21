package edu.hm.cs.sam.mc.ir.mainground;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.ir.enuminterfaces.ColorEnum;
import edu.hm.cs.sam.mc.ir.enuminterfaces.DynamicGuiInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.GUIInterface;
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
 * Class for handling dynamic infrared target.
 * Calculating waypoints, flying mission.
 * 
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas Angermeier (Team 05 - Infrared/Emergent Task)
 */
public class DynamicIRComponent extends GroundComponent implements DynamicGuiInterface {

    private List<WPWithInfo> calculatedDynWaypoints = new ArrayList<>();

    private static double circleCenterLat;
    private static double circleCenterLon;
    private static double currLat;
    private static double currLon;

    private static List<Double> dynamicLats = new ArrayList<>();
    private static List<Double> dynamicLons = new ArrayList<>();
    
    //############## done by me #############
    private boolean killThread = false;

    private static final Logger LOG = Logger.getLogger(DynamicIRComponent.class.getName());

    
    private boolean dynReadyToStart = false;

    private Target irDynTar;
    private final GUIInterface irGui;
    
    private int onlineTick = 0;

    

    /**
     * Dynamic IR component.
     * 
     * @param irGui Infrared GUI.
     * @param publisher Publisher to handle orders with air.
     */
    public DynamicIRComponent(final GUIInterface irGui, final GroundAirPublisher publisher) {
        this.irGui = irGui;
        groundPublisher = publisher;
    }


    @Override
    public void calcWaypoints() {
    	
    	
        //Dynamic IR Target direkt per Routen Gui
        irDynTar = Data.getTargets(SamType.TARGET_IR_DYNAMIC);
        
        
        if(irDynTar == null) {
    		LOG.info("Dynamic IR target not insert yet. Please insert dynamic IR target");
        } else {
        	
        	
    		calculatedDynWaypoints = new ArrayList<WPWithInfo>();
    		dynamicLats = new ArrayList<Double>();
    		dynamicLons = new ArrayList<Double>();
    		
    		
	        double targetLat = irDynTar.getWaypoint().getLat();
	        double targetLng = irDynTar.getWaypoint().getLng();
	      
	        
	        irGui.setDynamicTargetCoordinates(targetLat, targetLng);
	        
	        // Berechnung der Wegpunkte für Dynamic Target...
	        printDynamicWP(targetLat, targetLng);
	
	        for (int i = 0; i < dynamicLats.size(); i++) {
	            if (i == 1) {
	                calculatedDynWaypoints.add(new WPWithInfo(dynamicLons.get(i), dynamicLats.get(i),
	                		GroundComponent.DYNAMIC_ALT, MavCmd.WAYPOINT, 1, 0, 0, 0, 0,
	                		WpType.STARTIRVIDEO));
	            } else if (i == (dynamicLats.size() - 2)) {
	            	calculatedDynWaypoints.add(new WPWithInfo(dynamicLons.get(i), dynamicLats.get(i),
	            			GroundComponent.DYNAMIC_ALT, MavCmd.WAYPOINT, 1, 0, 0, 0, 0,
	            			WpType.STOPIRVIDEO));
	            } else {
	            	calculatedDynWaypoints
	            	.add(new WPWithInfo(dynamicLons.get(i), dynamicLats.get(i),
	            			GroundComponent.DYNAMIC_ALT, MavCmd.WAYPOINT, 1, 0, 0, 0, 0,
	            			WpType.FLYING));
	            }

	        }
	        // ---
	        dynReadyToStart = true;
	        irGui.printConsole("Dynamic IR waypoints calculated, uploaded to Routing, dynamic IR task ready...");
	        
	        // Sendet Waypoints an MissionControl fuer Drohne...
	        uploadTaskToMissionPlanner(calculatedDynWaypoints, SamType.TASK_IR_DYNAMIC);
        }
    }

    // Groundcomponent GUI Interface, Button "Start Mission" etc. Entrypoint
    @Override
    public void startMission() {
       
        if (dynReadyToStart) {
            irGui.setDynamicTaskActive(true);
            flyRoute();
        } else {
            irGui.printConsole("Dynamic Waypoints not calculated yet");
        }
    }

    // Groundcomponent GUI Interface, Button "Start Mission" etc. Entrypoint
    @Override
    public void run() {
        startMission();
    }

    @Override
    public boolean isTaskCalculated() {
        return dynReadyToStart;
    }

    /**
     * Checking waypoints the airplane is about to steer. Performing actions on the different types of waypoints.
     */
    private void flyRoute() {
    	
    	try {
    		LocationWp nextWaypoint = Data.getNextPosition();
    		WPWithInfo firstDynamicWaypoint = calculatedDynWaypoints.get(0);

    		onlineTick = 0;
    		//############## done by me #############
    		while (!killThread && distance(nextWaypoint, firstDynamicWaypoint) > 2) {
    			Thread.sleep(500);
    			nextWaypoint = Data.getNextPosition();
    			sendOnlineRequest();
    		}
    		//############## done by me #############
    		while (!killThread && !isDroneAtWaypoint(firstDynamicWaypoint, Data.getCurrentPosition()) || !(distance(nextWaypoint, calculatedDynWaypoints.get(1)) < 2)) {
    			Thread.sleep(500);
    			nextWaypoint = Data.getNextPosition();
    			sendOnlineRequest();
    		}

    		//am ersten wp vorbei
    		startIRVideo();

    		Thread.sleep(500);
    		WPWithInfo preLastDynamicWaypoint = calculatedDynWaypoints.get(calculatedDynWaypoints.size() - 2);
    		WPWithInfo lastDynamicWaypoint = calculatedDynWaypoints.get(calculatedDynWaypoints.size() - 1);


        	LocationWp currentPos = Data.getCurrentPosition();
        	nextWaypoint = Data.getNextPosition();
        	//############## done by me #############
        	while(!killThread && !isDroneAtWaypoint(preLastDynamicWaypoint, currentPos) || 
        			!isDroneAtWaypoint(lastDynamicWaypoint, currentPos) ||
        			!(distance(nextWaypoint, lastDynamicWaypoint) < 2)) {
        		Thread.sleep(500);
        		currentPos = Data.getCurrentPosition();
        		nextWaypoint = Data.getNextPosition();
        		sendOnlineRequest();
        	}

        	stopIRVideo();

            irGui.printConsole("Dynamic Task done");
            irGui.printConsole(" ");
            //############## done by me #############
            irGui.taskCancelled(TasksEnum.IRDYNAMIC,true);
        	
    	} catch (InterruptedException e) {
            LOG.error("Sleep error", e);
          //############## done by me #############
            killThread = true;
		} catch (NullPointerException e) {
    		LOG.error("Rest client not available", e);
    		//############## done by me #############
    		killThread = true;
    	}
    	
        //############## done by me #############
        if(killThread){
            killThread = false;
            irGui.taskCancelled(TasksEnum.IRDYNAMIC,false);
        }
    }


    /**
     * Sending order to publisher, ir video start, normal photos start.
     */
    private void startIRVideo() {
        irGui.printConsole("GS: IR video START.");
        groundPublisher.setOrder(START_IR_VIDEO);
        synchronized (groundPublisher) {
            groundPublisher.notify();
        }
        
        groundPublisher.setOrder(TAKE_PHOTO);
        synchronized (groundPublisher) {
            groundPublisher.notify();
        }
    }

    /**
     * Sending order to publisher, ir video stop, normal photos stop.
     */
    private void stopIRVideo() {
        irGui.printConsole("GS: IR video STOP.");
        groundPublisher.setOrder(STOP_IR_VIDEO);
        synchronized (groundPublisher) {
            groundPublisher.notify();
        }
        
    	groundPublisher.setOrder(STOP_PHOTO);
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
			if(!GroundAirSubscriber.isOnlineQueryReceived())
				irGui.setDroneConnectionColor(ColorEnum.COLORRED);
			GroundAirSubscriber.setOnlineQueryReceived(false);
			groundPublisher.setOrder(GET_ONLINE_STATUS);
			synchronized (groundPublisher) {
				groundPublisher.notify();
			}
		}
    }
    
    /**
     * Calculating waypoints for dynamic target.
     * 
     * @param lat Latitude
     * @param lon Longitude
     */
    private void printDynamicWP(final double lat, final double lon) {

    	calcCoordinates(lat, lon, -120, 0);
    	calcCoordinates(lat, lon, -80, 0);
        calcCoordinates(lat, lon, -40, 0);
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
        calcCoordinates(lat, lon, 0, -80);
        calcCoordinates(lat, lon, 0, -120);
        
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
            	if (distance(latObstacle, lonObstacle, dynamicLats.get(0), dynamicLons.get(0)) < obstacleRadius) {
            		LocationWp newPoint = Misc.calcCoordsWithOffset(latObstacle, lonObstacle, -obstacleRadius - 5, 0);
            		dynamicLats.add(0, newPoint.getLat());
            		dynamicLons.add(0, newPoint.getLng());
            	}
            	// Falls der letzte Punkt im Obstacle liegt
            	if (distance(latObstacle, lonObstacle, dynamicLats.get(dynamicLats.size() - 1), dynamicLons.get(dynamicLons.size() - 1)) < obstacleRadius) {
            		LocationWp newPoint = Misc.calcCoordsWithOffset(latObstacle, lonObstacle, obstacleRadius/Math.sqrt(2), obstacleRadius/Math.sqrt(2));
            		dynamicLats.add(newPoint.getLat());
            		dynamicLons.add(newPoint.getLng());
            	}
            	
            	boolean insideObstacle = false;
            	boolean obstacleFound = false;
            	double latBefore = 0.0;
            	double lonBefore = 0.0;
            	double latAfter = 0.0;
            	double lonAfter = 0.0;
            	
            	int posBefore = 0;
            	int posAfter = 0;
            	
            	for(int i = toStartAt; i < dynamicLats.size(); i++) {
            		double currentLat = dynamicLats.get(i);
            		double currentLon = dynamicLons.get(i);

            		if(distance(latObstacle, lonObstacle, currentLat, currentLon) < obstacleRadius) {
            			if (!insideObstacle) {
            				obstacleFound = true;
            				
            				latBefore = dynamicLats.get(i-1);
            				lonBefore = dynamicLons.get(i-1);
            				posBefore = i-1;
        	    			insideObstacle = true;
            			}
            		} else {
            			if (insideObstacle) {
            				latAfter = dynamicLats.get(i);
            				lonAfter = dynamicLons.get(i);
            				posAfter = i;
            				break;
            			}
            		}
            	}
            	if (obstacleFound) {
            		calcObstacleWaypoints(latObstacle, lonObstacle, latBefore, lonBefore, latAfter, lonAfter, obstRadiusTol);
            		List<Double> allLats = new ArrayList<>(dynamicLats.subList(0, posBefore+1));
            		allLats.addAll(latsObstacle);
            		allLats.addAll(dynamicLats.subList(posAfter, dynamicLats.size()));
            		dynamicLats = allLats;
            		
            		List<Double> allLons = new ArrayList<>(dynamicLons.subList(0, posBefore+1));
            		allLons.addAll(lonsObstacle);
            		allLons.addAll(dynamicLons.subList(posAfter, dynamicLons.size()));
            		dynamicLons = allLons;
            		
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
    public void calcCoordinates(final double lat, final double lon, final double offsetX,
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

        dynamicLats.add(currLat);
        dynamicLons.add(currLon);

        
//        System.out.println(currLat+", "+currLon); Anzeige auf Map
    }

    /**
     * Calculating coordiantes of the new circle
     * 
     * @param lat Latitude of the center
     * @param lon Longitude of the center
     * @param offsetX offset in x direction of the new circle (in meters)
     * @param offsetY offset in y direction of the new circle (in meters)
     */
    public void calcCircleCoordinates(final double lat, final double lon, final double offsetX,
            final double offsetY) {

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

    //############## done by me #############
    public void stop(){
        killThread = true;
    }


	@Override
	public void startFallBack() {
		startIRVideo();	
	}

	@Override
    public void stopFallBack() {
    	stopIRVideo();
    }
}
