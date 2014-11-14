package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.ir.enum_interfaces.DynamicGuiInterface;
import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class DynamicIRComponent extends GroundComponent implements DynamicGuiInterface {


	private List<Location> dynamicTarget = new ArrayList<>();
	private boolean dynReady = false;
	private Location dynTarget = null;
	
	

	@Override
	public void startMission() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTaskCalculated() {
		return dynReady; 
	}

	@Override
	public void calcWaypoints(double longitude, double latitude) {
		dynTarget = new Location(longitude, latitude, DYNAMICALT);
		
		//---
		//Berechnung der Wegpunkte für Dynamic Target...
		//---
		
		dynReady = true;
		
	}



}
