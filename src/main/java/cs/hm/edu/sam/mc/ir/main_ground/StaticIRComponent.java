/**
 * 
 */
package cs.hm.edu.sam.mc.ir.main_ground;

<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> 0e379df1b7967e8766a7b5ca66bc221f246ca9e1
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

<<<<<<< HEAD
=======
	@Override
	public void addEmergentSearchAreaWP(double longitude, double latitude) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Location> getEmergentSearchArea() {
		// TODO Auto-generated method stub
		return null;
	}
>>>>>>> 0e379df1b7967e8766a7b5ca66bc221f246ca9e1

	
}
