/**
 * 
 */
package cs.hm.edu.sam.mc.ir.enum_interfaces;


/**
 * @author Maximilian Haag
 *
 */
public interface GroundGuiInterface {
	
	
	public void calcWaypoints(TasksEnum task , double longitude, double latitude);
	
	public boolean isTaskCalculated(TasksEnum task);
	
	public boolean isAirCompOnline();
}
