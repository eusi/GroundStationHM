package cs.hm.edu.sam.mc.misc;

/**
 * Data class contains some global data.
 * 
 * @author Christoph Friegel
 * @version 0.1
 *
 *
 * Location-TEST (e.g. currentPosition)
 *
 *		Location test = new Location( 1.2, 1.3 );
 *		Data.setCurrentPosition( test );
 *		System.out.println( Data.getCurrentPosition().getX() );
 *
 *
 * Waypoint-TEST (e.g. waypoint-list)
 *
 *		Location test1 = new Location( 1.2, 1.3 );
 *		Location test2 = new Location( 2.2, 2.3 );
 *		Location[] waypoints = { test1, test2 };
 *		Data.setWaypointList( waypoints, "test" );
 *		System.out.println( Data.getWaypoints().getWaypointListName() );
 *		System.out.println( Data.getWaypoints().getWaypoints()[0].getX() );
*/

public class Data {
	private static Location currentPosition; //contains x and y
	private static Waypoints thisWaypoints; //contains some locations and a list name

	//Get current position
	public static Location getCurrentPosition() {
		return currentPosition;
	}
	
	//Set current position
	public static void setCurrentPosition( Location newPosition ) {
		currentPosition = newPosition;
	}
	
	//Get waypoint list
	public static Waypoints getWaypoints() {
		return thisWaypoints;
	}
	
	//Set waypoint list
	public static void setWaypointList( Location[] waypointList, String waypointListName ) {
		thisWaypoints = new Waypoints( waypointList, waypointListName );
	}

}
