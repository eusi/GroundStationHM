/**
 * 
 */
package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class StaticIRComponent extends GroundComponent {

	private List<Location> staticTarget = new ArrayList<>();
	private boolean statReady = false;
	private Location statTarget = null;

	
	
	//Groundcomponent GUI Interface, Button entrypoint
	public void calcWaypoints(double longitude, double latitude) {
		statTarget = new Location(longitude, latitude, STATICALT);
		statReady = true;
		
	}

	@Override
	public void startMission() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isTaskCalculated() {
		return statReady;
	}


	
}
