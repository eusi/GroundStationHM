package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.ir.Ir;
import cs.hm.edu.sam.mc.ir.enum_interfaces.DynamicGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.TasksEnum;
import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class DynamicIRComponent extends GroundComponent implements DynamicGuiInterface {

    private List<Location> calculatedDynWaypoints = new ArrayList<>();
    private boolean dynReadyToStart = false;
    private Location dynTargetLoc = null;
    private boolean taskActive = false;

    
    public DynamicIRComponent(Ir gui) {
		super(gui);
	}
    
    @Override
    public void calcWaypoints(double longitude, double latitude) {
        dynTargetLoc = new Location(longitude, latitude, DYNAMIC_ALT);

        // ---
        // Berechnung der Wegpunkte für Dynamic Target hier...
        // ---

        dynReadyToStart = true;
    }

    // Groundcomponent GUI Interface, Button "Start Mission" etc. Entrypoint
    @Override
    public void startMission() {
        // Sendet Waypoints an MissionControl für Drohne...
        if (dynReadyToStart) {
            uploadTaskToMissionPlanner(calculatedDynWaypoints, GroundComponent.DYNAMIC_IR_T);
            taskActive = true;
            flyRoute();
        } else {
            System.out.println("NOT CALCULATED YET");
        }
    }

    @Override
    public boolean isTaskCalculated() {
        return dynReadyToStart;
    }

    /**
     * Fliegt alle übermittelten Waypoints ab, bis Liste abgearbeitet
     */
    private void flyRoute() {
        long sleepTime = (long) GroundComponent.STATIC_REFRESH_TIME * 1000;

        for (int i = 0; i < calculatedDynWaypoints.size(); i++) {

            // Ersten abzufliegenden Waypoint aus Liste holen, solange prüfen,
            // bis erreicht, dann entfernen
            Location locToCompute = calculatedDynWaypoints.remove(i);
            boolean isAtWaypoint = isDroneAtWaypoint(locToCompute, TasksEnum.IRDYNAMIC);

            while (!isAtWaypoint) {

                isAtWaypoint = isDroneAtWaypoint(locToCompute, TasksEnum.IRDYNAMIC);
            }

            // ----------------
            // HIER IR VIDEO MACHEN !!!!!!!!
            takeIRVideo();
            // ---------------

            // Delay für Waypoint Check
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        taskActive = false;
    }

    private void takeIRVideo() {
        // TODO Auto-generated method stub

    }

}
