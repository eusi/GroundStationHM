package cs.hm.edu.sam.mc.misc;

/**
 * Wapoints-class.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

public class Waypoints {
	private Location[] list; //contains x and y
	private String listName; //waypoint list name
	
	public Waypoints(Location[] waypointList, String waypointListName) {
		list = (Location[])waypointList.clone();
		this.listName = waypointListName;
	}

	//Get waypoints
	public Location[] getWaypoints() {
		return list;
	}
	
	//Get list name
	public String getWaypointListName() {
		return listName;
	}
	
}
