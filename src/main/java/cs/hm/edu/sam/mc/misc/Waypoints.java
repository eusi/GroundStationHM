package cs.hm.edu.sam.mc.misc;

/**
 * Waypoints-class.
 * 
 * @author Christoph Friegel
 * @version 0.1
 * 
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
	
	
	/**
	 * 
	 * JSON-file has to be like following struct: 
	 * 
	 * 
	 *  public class Locationwp
     *  {
     *    public byte id;				// command id
     *    public byte options;
     *    public float p1;				// param 1
     *    public float p2;				// param 2
     *    public float p3;				// param 3
     *    public float p4;				// param 4
     *    public double lat;		    // Lattitude * 10**7
     *    public double lng;			// Longitude * 10**7
     *    public float alt;				// Altitude in centimeters (meters * 100)
     * }
     * 
     * By default ID = 16 or MAV_CMD.WAYPOINT (for waypoint).
     * Lat, lng, alt used by location object.
     * p1-p4 and options are unused so far. If they will be needed,
     * the location object have to get extended by this parameters.
     * 
     */
	
	@Override
	public String toString() {
		StringBuffer jsonString = new StringBuffer();
		
		for(int i=0; i<list.length; i++)
		{
			jsonString.append("{alt:").append(list[i].getAlt()+",")
					.append("id:").append(MAV_CMD.WAYPOINT + ",")
					.append("lat:").append(list[i].getLat()+",")
					.append("lng:").append(list[i].getLng()+",")
					.append("options:").append("1,")
					.append("p1:").append("0,")
					.append("p2:").append("0,")
					.append("p3:").append("0,")
					.append("p4:").append("0}");
			
			if(i+1 < list.length)
				jsonString.append(",");
		}
		
		return jsonString.toString();
	}	
}
