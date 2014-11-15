package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.ir.enum_interfaces.EmergentGuiInterface;
import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class EmergentComponent extends GroundComponent implements EmergentGuiInterface  {


	private List<Location> calculatedEmergWaypoints = new ArrayList<>();
	private List<Location> emergentSearchArea = new ArrayList<>();
	
	private boolean emergReadyToStart = false;
	private Location emerTargetLastKnownPosition = null;
	
	
	
	
	/* EmergentSearchArea Gebiet eintragen per GUI
	 */
	public void addEmergentSearchAreaWP(double longitude, double latitude) {
		emergentSearchArea.add(new Location(longitude, latitude, EMERGENT_ALT));
	}
	
	
	/* Getter für aktuelle EmergentSearchArea GUI
	 */
	public List<Location> getEmergentSearchArea() {
		return emergentSearchArea;
	}


	@Override
	public void startMission() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isTaskCalculated() {
		return emergReadyToStart;
	}


	@Override
	public void calcWaypoints(double longitude, double latitude) {
		emerTargetLastKnownPosition = new Location(longitude, latitude, EMERGENT_ALT);
		
		//---
		//Berechnung der Wegpunkte für Dynamic Target...
		//---
		
		emergReadyToStart = true;
		
	}

}
