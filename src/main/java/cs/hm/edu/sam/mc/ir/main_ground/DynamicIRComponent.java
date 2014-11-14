package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.misc.Location;

public class DynamicIRComponent extends GroundComponent {


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
		dynReady = true;
		
	}



}
