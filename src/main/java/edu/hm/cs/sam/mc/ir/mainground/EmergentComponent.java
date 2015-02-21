package edu.hm.cs.sam.mc.ir.mainground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.ir.enuminterfaces.ColorEnum;
import edu.hm.cs.sam.mc.ir.enuminterfaces.EmergentGuiInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.GUIInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.TasksEnum;
import edu.hm.cs.sam.mc.ir.enuminterfaces.WpType;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.cs.sam.mc.misc.Zone;
import edu.hm.cs.sam.mc.searcharea.task.WaypointCalculator;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;
import edu.hm.sam.location.SamType;

/**
 * 
 * Class for handling emergent target.
 * Calculating waypoints, flying mission.
 * 
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas
 *         Angermeier (Team 05 - Infrared/Emergent Task)
 */
public class EmergentComponent extends GroundComponent implements EmergentGuiInterface {

    private List<WPWithInfo> calculatedEmergWaypoints = new ArrayList<>();

    private Target lastKnownPosition;
    private Zone cornersForLMP;

    private boolean emergReadyToStart = false;

    private final GUIInterface emGui;
    private static final Logger LOG = Logger.getLogger(EmergentComponent.class.getName());

    // ############## done by me #############
    private boolean killThread = false;

    /**
     * Emergent component
     * 
     * @param emGui Emergent GUI
     */
    public EmergentComponent(final GUIInterface emGui) {
    	this.emGui = emGui;
    }

    // GUI entrypoint, Area bereits eingetragen
    @Override
    public void calcWaypoints() {

        // Last Known Position direkt per Routen Gui
        lastKnownPosition = Data.getTargets(SamType.TARGET_EMERGENT);

        // Corners direkt per Routen Gui
        cornersForLMP = Data.getZones(SamType.ZONE_EMERGENT);


        if (lastKnownPosition == null) {
            LOG.info("Last Known Position not insert yet. Please insert Last Known Position");
        } else if (cornersForLMP == null) {
            LOG.info("Emergent Zone not insert yet. Please insert Waypoints for Emergent Zone calculation");
        } else {

            calculatedEmergWaypoints = new ArrayList<WPWithInfo>();

            emGui.setStaticTargetCoordinates(lastKnownPosition.getWaypoint().getLat(),
                    lastKnownPosition.getWaypoint().getLng());

            final LocationWp[] cornersFromRouting = cornersForLMP.getWaypoints();

            
            // Lawn Mowing
            final WaypointCalculator waypCalc = new WaypointCalculator();
            
            final List<LocationWp> calcEmerWaypoints = waypCalc.lawnMowingPattern(Arrays.asList(cornersFromRouting), 0.1, GroundComponent.EMERGENT_ALT);

            // Coord -> WPWithInfo
            for (int i = 0; i < calcEmerWaypoints.size(); i++) {

                if (i == 0) {
                    calculatedEmergWaypoints.add(new WPWithInfo(calcEmerWaypoints.get(i).getLng(),
                            calcEmerWaypoints.get(i).getLat(), GroundComponent.EMERGENT_ALT,
                            MavCmd.WAYPOINT, 1, 0, 0, 0, 0, WpType.STARTPHOTO));
                } else if (i == (calcEmerWaypoints.size() - 1)) {
                    calculatedEmergWaypoints.add(new WPWithInfo(calcEmerWaypoints.get(i).getLng(),
                            calcEmerWaypoints.get(i).getLat(), GroundComponent.EMERGENT_ALT,
                            MavCmd.WAYPOINT, 1, 0, 0, 0, 0, WpType.STOPPHOTO));
                } else {
                    calculatedEmergWaypoints.add(new WPWithInfo(calcEmerWaypoints.get(i).getLng(),
                            calcEmerWaypoints.get(i).getLat(), GroundComponent.EMERGENT_ALT,
                            MavCmd.WAYPOINT, 1, 0, 0, 0, 0, WpType.FLYING));
                }
            }

            emergReadyToStart = true;
            emGui.printConsole("Emergent waypoints, uploaded to Routing, emergent task ready...");

            // Sendet Waypoints an MissionControl für Drohne...
            uploadTaskToMissionPlanner(calculatedEmergWaypoints, SamType.TASK_EMERGENT);
        }
    }

    
    // Groundcomponent GUI Interface, Button "Start Mission" etc. Entrypoint
    @Override
    public void startMission() {
        if (emergReadyToStart) {
            emGui.setEmergentTaskActive(true);
            flyRoute();
        } else {
            emGui.printConsole("Emergent target waypoints not calculated yet...");
        }
    }    
    
    /**
     * Checking waypoints the airplane is about to steer. Performing actions on the different types of waypoints.
     */
    private void flyRoute() {

        try {
            LocationWp nextWaypoint = Data.getNextPosition();
            final WPWithInfo firstEmergentWaypoint = calculatedEmergWaypoints.get(0);
            int onlineTick = 0;
            // ############## done by me #############
            while (!killThread && (distance(nextWaypoint, firstEmergentWaypoint) > 2)) {
                Thread.sleep(500);
                nextWaypoint = Data.getNextPosition();
                onlineTick = (onlineTick + 1) % 4;
                if (onlineTick == 0) {
                    sendOnlineRequest();
                }
            }
            // ############## done by me #############
            while ((!killThread && !isDroneAtWaypoint(firstEmergentWaypoint,
                    Data.getCurrentPosition()))
                    || !(distance(nextWaypoint, calculatedEmergWaypoints.get(1)) < 2)) {
                Thread.sleep(500);
                nextWaypoint = Data.getNextPosition();
                onlineTick = (onlineTick + 1) % 4;
                if (onlineTick == 0) {
                    sendOnlineRequest();
                }
            }

            startEmergentPhotos();

            Thread.sleep(500);
            final WPWithInfo preLastEmergentWaypoint = calculatedEmergWaypoints
                    .get(calculatedEmergWaypoints.size() - 2);
            final WPWithInfo lastEmergentWaypoint = calculatedEmergWaypoints
                    .get(calculatedEmergWaypoints.size() - 1);

            LocationWp currentPos = Data.getCurrentPosition();
            nextWaypoint = Data.getNextPosition();
            // ############## done by me #############
            while ((!killThread && !isDroneAtWaypoint(preLastEmergentWaypoint, currentPos))
                    || !isDroneAtWaypoint(lastEmergentWaypoint, currentPos)
                    || !(distance(nextWaypoint, lastEmergentWaypoint) < 2)) {
                Thread.sleep(500);
                currentPos = Data.getCurrentPosition();
                nextWaypoint = Data.getNextPosition();

                onlineTick = (onlineTick + 1) % 4;
                if (onlineTick == 0) {
                    sendOnlineRequest();
                }
            }

            stopEmergentPhotos();

            emGui.printConsole("Emergent Task done");
            emGui.printConsole(" ");
            // ############## done by me #############
            emGui.taskCancelled(TasksEnum.EMERGENT, true);

        } catch (final InterruptedException e) {
            LOG.error("Sleep error", e);
            // ############## done by me #############
            killThread = true;

        } catch (final NullPointerException e) {
            LOG.error("Rest client not available", e);
            // ############## done by me #############
            killThread = true;
        }

        // ############## done by me #############
        if (killThread) {
            killThread = false;
            emGui.taskCancelled(TasksEnum.EMERGENT, false);
        }
    }

    /*
     * Getter fuer aktuelle EmergentSearchArea GUI
     */
    @Override
    public List<LocationWp> getEmergentSearchArea() {
        final List<LocationWp> searchArea = new ArrayList<>();
        final LocationWp[] areaToAdd = cornersForLMP.getWaypoints();
        for (int i = 0; i < cornersForLMP.getWaypoints().length; i++) {
            searchArea.add(areaToAdd[i]);
        }
        return searchArea;
    }

    @Override
    public boolean isTaskCalculated() {
        return emergReadyToStart;
    }

    @Override
    public void run() {
        startMission();
    }
    
    /**
     * Checking plane online status, sending order to air component.
     */
    private void sendOnlineRequest() {
        if (!GroundAirSubscriber.isOnlineQueryReceived()) {
            emGui.setDroneConnectionColor(ColorEnum.COLORRED);
        }
        GroundAirSubscriber.setOnlineQueryReceived(false);
        groundPublisher.setOrder(GET_ONLINE_STATUS);
        synchronized (groundPublisher) {
            groundPublisher.notify();
        }
    }
    
    
    /**
     * Sending order to publisher, taking normal photos start.
     */
    private void startEmergentPhotos() {
        emGui.printConsole("GS: Emergent photo START.");
        groundPublisher.setOrder(TAKE_PHOTO);
        synchronized (groundPublisher) {
            groundPublisher.notify();
        }
    }

    /**
     * Sending order to publisher, taking normal photos stop.
     */
    private void stopEmergentPhotos() {
        emGui.printConsole("GS: Emergent photo STOP.");
        groundPublisher.setOrder(STOP_PHOTO);
        synchronized (groundPublisher) {
            groundPublisher.notify();
        }
    }

    // ############## done by me #############
    @Override
    public void stop() {
        killThread = true;
    }

	@Override
	public void startFallBack() {
		startEmergentPhotos();		
	}

	@Override
	public void stopFallBack() {
		stopEmergentPhotos();		
	}

}