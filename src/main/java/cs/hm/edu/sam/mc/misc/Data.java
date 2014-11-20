package cs.hm.edu.sam.mc.misc;

/**
 * Data class contains some global data.
 * 
 * @author Christoph Friegel
 * @version 0.1
 *
 *
 *          Location-EXAMPLE (e.g. currentPosition)
 *
 *          Location test = new Location( 1.2, 1.3, 50 );
 *          Data.setCurrentPosition( test ); System.out.println(
 *          Data.getCurrentPosition().getLng() );
 *
 *
 *          Waypoints-EXAMPLE (e.g. waypoint-list)
 *
 *          Location test1 = new Location( 1.2, 1.3, 50 ); //HOME! Location
 *          test2 = new Location( 2.2, 2.3, 50 ); Location test3 = new Location(
 *          2.4, 2.6, 100 ); Location[] waypoints = { test1, test2, test3 };
 *          Data.setWaypointList( waypoints, "test" ); System.out.println(
 *          Data.getWaypoints().getWaypointListName() ); System.out.println(
 *          Data.getWaypoints().getWaypoints()[0].getLng() );
 *          RESTClient.sendWaypoints();
 */

public class Data {
    
	// this is actually the current position of the aircraft (by MP)
	private static volatile Location currentPosition; 

    // waypoints which are created by routing-modules for routing (to MP)
	// use this to storage your module-waypoints
    private static volatile Waypoints sricWaypoints;
    private static volatile Waypoints routingWaypoints;
    private static volatile Waypoints irWaypoints;
    private static volatile Waypoints emergentWaypoints;
    private static volatile Waypoints searchareaWaypoints;
    private static volatile Waypoints airdropWaypoints;


    // if we want to poll a current location, we have to turn it on
    // check GUI-option: FILE -> GET LOCATION
    private static boolean currentLocationIsActive; 

    
    // get current position - for e.g. routing tasks
    public static Location getCurrentPosition() {
        return currentPosition;
    }

    // set current position - for rest only, don't call this manually!
    public static void setCurrentPosition(Location newPosition) {
        currentPosition = newPosition;
    }
    

    // get polling (current aircraft position)
    public static boolean isCurrentLocationActive() {
        return currentLocationIsActive;
    }

    // set polling (current aircraft position) - via GUI only, don't call this manually!
    public static void setCurrentLocationIsActive(boolean currentLocationIsActive) {
        Data.currentLocationIsActive = currentLocationIsActive;
    }

    
    
    // getter and setter: waypoint lists - for e.g. rest or routing
    // use this to storage your module-waypoints
	public static Waypoints getSricWaypoints() {
		return sricWaypoints;
	}

	public static void setSricWaypoints(Location[] sricWaypoints, String waypointListName) {
		Data.sricWaypoints = new Waypoints(sricWaypoints, waypointListName);
	}

	public static Waypoints getRoutingWaypoints() {
		return routingWaypoints;
	}

	public static void setRoutingWaypoints(Location[] routingWaypoints, String waypointListName) {
		Data.routingWaypoints = new Waypoints(routingWaypoints, waypointListName);
	}

	public static Waypoints getIrWaypoints() {
		return irWaypoints;
	}

	public static void setIrWaypoints(Location[] irWaypoints, String waypointListName) {
		Data.irWaypoints = new Waypoints(irWaypoints, waypointListName);
	}

	public static Waypoints getEmergentWaypoints() {
		return emergentWaypoints;
	}

	public static void setEmergentWaypoints(Location[] emergentWaypoints, String waypointListName) {
		Data.emergentWaypoints = new Waypoints(emergentWaypoints, waypointListName);
	}

	public static Waypoints getSearchareaWaypoints() {
		return searchareaWaypoints;
	}

	public static void setSearchareaWaypoints(Location[] searchareaWaypoints, String waypointListName) {
		Data.searchareaWaypoints = new Waypoints(searchareaWaypoints, waypointListName);
	}

	public static Waypoints getAirdropWaypoints() {
		return airdropWaypoints;
	}

	public static void setAirdropWaypoints(Location[] airdropWaypoints, String waypointListName) {
		Data.airdropWaypoints = new Waypoints(airdropWaypoints, waypointListName);
	}
}
