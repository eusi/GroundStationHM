package edu.hm.cs.sam.mc.ir.enuminterfaces;

/**
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas Angermeier (Team 05 - Infrared/Emergent Task)
 */
public interface GroundGuiInterface {

    /**
     * Calculates waypoints for mission task.
     * Uploading waypoints to routing.
     */
    public abstract void calcWaypoints();

    /**
     * Checks if task ready to start.
     * @return true if task is calculated, false else.
     */
    public abstract boolean isTaskCalculated();

    /**
     * Starting mission, checking reached waypoints, sending orders to aircomponent.
     */
    public abstract void startMission();

    
    /**
     * Fallback method start.
     */
	public void startFallBack();

    /**
     * Fallback method stop.
     */
	public void stopFallBack();
    
}
