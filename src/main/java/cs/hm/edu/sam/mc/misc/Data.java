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
 *		Location test = new Location( 1.2, 1.3, 50 );
 *		Data.setCurrentPosition( test );
 *		System.out.println( Data.getCurrentPosition().getLng() );
 *
 *
 * Waypoint-TEST (e.g. waypoint-list)
 *
 *		Location test1 = new Location( 1.2, 1.3, 50 ); //HOME!
 *		Location test2 = new Location( 2.2, 2.3, 50 );
 *      Location test3 = new Location( 2.4, 2.6, 100 );
 *		Location[] waypoints = { test1, test2, test3 };
 *		Data.setWaypointList( waypoints, "test" );
 *		System.out.println( Data.getWaypoints().getWaypointListName() );
 *		System.out.println( Data.getWaypoints().getWaypoints()[0].getLng() );
 *      RESTClient.sendWaypoints();
*/

public class Data {
	private static Location currentPosition; //this is acutally the current position
	                                         //of the aircraft (by MP)
	private static Waypoints thisWaypoints; //this should be the waypoints which
	                                        //are created by routing-module and
		                                    //will be send to MP
	
	private static boolean currentLocationIsActive; //if we want to poll a current loc

	//Get current position - for e.g. IR-task
	public static Location getCurrentPosition() {
		return currentPosition;
	}
	
	//Set current position - for rest only, don't call this manually!
	public static void setCurrentPosition( Location newPosition ) {
		currentPosition = newPosition;
	}
	
	//Get waypoint list - for e.g. rest
	public static Waypoints getWaypoints() {
		return thisWaypoints;
	}
	
	//Set waypoint list - for e.g. routing
	public static void setWaypointList( Location[] waypointList, String waypointListName ) {
		thisWaypoints = new Waypoints( waypointList, waypointListName );
	}

	public static boolean isCurrentLocationActive() {
		return currentLocationIsActive;
	}

	public static void setCurrentLocationIsActive(boolean currentLocationIsActive) {
		Data.currentLocationIsActive = currentLocationIsActive;
	}

}
