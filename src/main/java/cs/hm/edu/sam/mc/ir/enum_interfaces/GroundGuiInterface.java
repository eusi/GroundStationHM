/**
 * 
 */
package cs.hm.edu.sam.mc.ir.enum_interfaces;

import java.util.List;

import cs.hm.edu.sam.mc.misc.Location;


/**
 * @author Maximilian Haag
 *
 */
public interface GroundGuiInterface {
	
	
	public void calcWaypoints(TasksEnum task , double longitude, double latitude);
	
	public boolean isTaskCalculated(TasksEnum task);
	
	public void addEmergentSearchAreaWP(double longitude, double latitude);
	public List<Location> getEmergentSearchArea();
	
	public boolean isAirCompOnline();
}
