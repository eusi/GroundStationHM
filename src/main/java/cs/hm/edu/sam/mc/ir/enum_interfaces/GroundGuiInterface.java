/**
 * 
 */
package cs.hm.edu.sam.mc.ir.enum_interfaces;



/**
 * @author Maximilian Haag
 *
 */
public interface GroundGuiInterface {
	
	
	abstract public void calcWaypoints(double longitude, double latitude);
	abstract public boolean isTaskCalculated();
	abstract public void startMission();
	
	
	public boolean isAirCompOnline();

}
