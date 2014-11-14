/**
 * 
 */
package cs.hm.edu.sam.mc.ir.main_ground;


import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.ir.enum_interfaces.StaticGuiInterface;
import cs.hm.edu.sam.mc.misc.Data;
import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class StaticIRComponent extends GroundComponent implements StaticGuiInterface{

	private List<Location> staticTarget = new ArrayList<>();
	private boolean statReady = false;
	private Location statTarget = null;

	
	
	//Groundcomponent GUI Interface, Button entrypoint	
	@Override
	public void calcWaypoints(double longitude, double latitude) {
		statTarget = new Location(longitude, latitude, STATICALT);
		
		//---
		//Berechnung der Wegpunkte für Static Target...
		//---
		staticTarget = TestMain.createTestWaypoints();
		//---
		
		statReady = true;
		
	}
	
	

	@Override
	public void startMission() {
		//Sendet Waypoints an MissionControl für Drohne...
		
	}



	@Override
	public boolean isTaskCalculated() {
		return statReady;
	}



	
}
