/**
 * 
 */
package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.ir.enum_interfaces.GroundGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.TasksEnum;
import cs.hm.edu.sam.mc.misc.Data;
import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */

public abstract class GroundComponent implements GroundGuiInterface {
	
	private final int STATICTARGET = 1;
	private final int DYNAMICTARGET = 2;
	private final int EMERGENTTARGET = 3;
	
	//5 Meter Radius, um zu prüfen, ob Waypoint erreicht
	private final double STATICRADIUS = 0.0005;
	private final double DYNAMICRADIUS = 0.0005;
	private final double EMERGENTRADIUS = 0.0005;
	
	private final double STATICALT = 80;
	private final double DYNAMICALT = 80;
	private final double EMERGENTALT = 80;
	
	
	private double currentRadius = 0; 
	private int currentTask = 0; 
	
	private Location statTarget = null;
	private Location dynTarget = null;
	private Location emerTarget = null;
	
	private boolean statReady = false;
	private boolean dynReady = false;
	private boolean emergentReady = false;
	
	
	private final List<Location> staticTarget = new ArrayList<>();
	private final List<Location> dynamicTarget = new ArrayList<>();
	private final List<Location> emergentTarget = new ArrayList<>();
	
	

	//Groundcomponent GUI Interface, Button entrypoint
	public void calcWaypoints(TasksEnum task , double longitude, double latitude) {
		if(task == TasksEnum.IRSTATIC) {
			statTarget = new Location(longitude, latitude, STATICALT);
			calcStaticWaypoints(statTarget);
		} else if (task == TasksEnum.IRDYNAMIC) {
			dynTarget = new Location(longitude, latitude, DYNAMICALT);
			calcDynamicWaypoints(dynTarget);
		} else if(task == TasksEnum.EMERGENT){
			emerTarget = new Location(longitude, latitude, EMERGENTALT);
			calcEmergentWaypoints(emerTarget);
		}
	}
	
	public boolean isTaskCalculated(TasksEnum task) {
		if(task == TasksEnum.IRSTATIC) {
			return statReady;
		} else if(task == TasksEnum.IRDYNAMIC) {
			return dynReady;
		} else if(task == TasksEnum.EMERGENT)
			return emergentReady;
		else {
			return false;
		}
	}
	
	
	public boolean isAirCompOnline() {
		//TODO
		return false;
		
	}
	
	
	
	/**
	 * Berechnung der Waypoints anhand Location, Input per GUI
	 * 
	 */
	private void calcStaticWaypoints(Location statTarget) {
		// TODO Auto-generated method stub
		
	}
	private void calcEmergentWaypoints(Location emerTarget) {
		// TODO Auto-generated method stub
		
	}
	private void calcDynamicWaypoints(Location dynTarget) {
		// TODO Auto-generated method stub
	}
	
	


	/**
	 * Prüft, ob Drohne Location (Wegpunkt) erreicht hat
	 * @param currentTaskWPList
	 * @return
	 */
	private boolean isDroneAtWP(final List<Location> currTaskWaypoints) {
		boolean waypointInRad = false;
		
		//Current von MissionControl Team...
		Location currentLoc = Data.getCurrentPosition();
		
		for(int i = 0; i<currTaskWaypoints.size() || waypointInRad; i++) {
			waypointInRad = inRadius(currTaskWaypoints.get(i), currentLoc);
		}	
		return waypointInRad;
	}
	
	/**
	 * Prüft, ob aktuelle Drohnenposition innerhalb eines Radius zum nächsten Wegpunkt liegt.
	 * @param location
	 * @param currentLoc
	 * @return
	 */
	private boolean inRadius(Location location, Location currentLoc) {
		double taskLat = location.getLat();
		double taskLong = location.getLng();
		double droneLat = currentLoc.getLat();
		double droneLong = currentLoc.getLng();
		
		double latGap = Math.abs(taskLat - droneLat);
		double longGap = Math.abs(taskLong - droneLong);
		double distance = Math.sqrt(latGap*latGap + longGap*longGap);
		
		//Innerhalb des Kreises
		if(distance < currentRadius)
			return true;
		else
			return false;
	}


	
	private void setCurrentTask(final int currentTaskNr) {
		currentTask = currentTaskNr;
		if(currentTaskNr == STATICTARGET)
			currentRadius = STATICRADIUS;
		else if(currentTaskNr == DYNAMICTARGET)
			currentRadius = DYNAMICRADIUS;
		else if(currentTaskNr == EMERGENTTARGET)
			currentRadius = EMERGENTRADIUS;
		else
			try {
				throw new Exception();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	abstract public void startMission();
	abstract protected void controlDrone();
	
}
