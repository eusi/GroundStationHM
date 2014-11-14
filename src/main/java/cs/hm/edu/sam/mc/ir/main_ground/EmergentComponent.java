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

	
	
	private List<Location> emergentTarget = new ArrayList<>();
	private List<Location> emergentSearchArea = new ArrayList<>();
	
	private boolean emergentReady = false;
	private Location emerTarget = null;
	
	
	
	
	/* EmergentSearchArea Gebiet eintragen per GUI
	 */
	public void addEmergentSearchAreaWP(double longitude, double latitude) {
		emergentSearchArea.add(new Location(longitude, latitude, EMERGENTALT));
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
		return emergentReady;
	}


	@Override
	public void calcWaypoints(double longitude, double latitude) {
		emerTarget = new Location(longitude, latitude, EMERGENTALT);
		
		//---
		//Berechnung der Wegpunkte für Dynamic Target...
		//---
		
		emergentReady = true;
		
	}

}
