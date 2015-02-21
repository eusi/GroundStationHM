package edu.hm.cs.sam.mc.ir.enuminterfaces;

import java.util.List;

import edu.hm.sam.location.LocationWp;

/**
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas Angermeier (Team 05 - Infrared/Emergent Task)
 */
public interface EmergentGuiInterface extends GroundGuiInterface, Runnable {

	public List<LocationWp> getEmergentSearchArea();
	public void stop();


}
