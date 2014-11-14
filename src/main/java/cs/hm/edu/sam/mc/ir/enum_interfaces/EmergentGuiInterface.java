package cs.hm.edu.sam.mc.ir.enum_interfaces;

import java.util.List;

import cs.hm.edu.sam.mc.misc.Location;



public interface EmergentGuiInterface extends GroundGuiInterface{
	

	public void addEmergentSearchAreaWP(double longitude, double latitude);
	public List<Location> getEmergentSearchArea();
	
}
