/**
 * 
 */
package cs.hm.edu.sam.mc.ir.main_ground;

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
	
	
	//5 Meter Radius, um zu prüfen, ob Waypoint erreicht
	public final double STATICRADIUS = 0.0005;
	public final double DYNAMICRADIUS = 0.0005;
	public final double EMERGENTRADIUS = 0.0005;
	
	public final double STATICALT = 80;
	public final double DYNAMICALT = 80;
	public final double EMERGENTALT = 80;
	
	
	private TasksEnum currentTask = TasksEnum.NOTASSIGNED;


	/**
	 * Prüft, ob Drohne Location (Wegpunkt) erreicht hat
	 * @param currentTaskWPList
	 * @return
	 */
	private boolean isDroneAtWP(final List<Location> currTaskWaypoints, TasksEnum currentTask) {
		boolean waypointInRad = false;
		
		//Current von MissionControl Team...
		Location currentLoc = Data.getCurrentPosition();
		
		//Alle Locations prüfen, escape wenn innerhalb des Radius.
		for(int i = 0; i<currTaskWaypoints.size() || waypointInRad; i++) {
			waypointInRad = inRadius(currTaskWaypoints.get(i), currentLoc, currentTask);
		}	
		return waypointInRad;
	}
	
	/**
	 * Prüft, ob aktuelle Drohnenposition innerhalb eines Radius zum nächsten Wegpunkt liegt.
	 * @param location
	 * @param currentLoc
	 * @return
	 */
	private boolean inRadius(Location location, Location currentLoc, TasksEnum current) {
		double taskLat = location.getLat();
		double taskLong = location.getLng();
		double droneLat = currentLoc.getLat();
		double droneLong = currentLoc.getLng();
		
		double latGap = Math.abs(taskLat - droneLat);
		double longGap = Math.abs(taskLong - droneLong);
		double distance = Math.sqrt(latGap*latGap + longGap*longGap);
		
		double radius = 0; 
				
		//Current Task Radius
		if(current == TasksEnum.IRSTATIC) {
			radius = STATICRADIUS;
		} else if(current == TasksEnum.IRDYNAMIC) {
			radius = DYNAMICRADIUS;
		} else {
			radius = EMERGENTRADIUS;
		}
		
		//Innerhalb des Kreises
		if(distance < radius)
			return true;
		else
			return false;
	}

	
	public boolean isAirCompOnline() {
		//TODO
		return false;
		
	}
	
	private void setCurrentTask(TasksEnum current) {
		currentTask = current;
	}

	abstract public boolean isTaskCalculated();
	abstract public void calcWaypoints(double longitude, double latitude);


}
