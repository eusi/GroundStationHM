package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.ir.enum_interfaces.EmergentGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.TasksEnum;
import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class EmergentComponent extends GroundComponent implements EmergentGuiInterface {

    private List<Location> calculatedEmergWaypoints = new ArrayList<>();
    private List<Location> emergentSearchArea = new ArrayList<>();

    private boolean emergReadyToStart = false;

    private Location emerTargetLastKnownPosition = null;
    private boolean taskActive = false;

    @Override
    public void calcWaypoints(double longitude, double latitude) {
        emerTargetLastKnownPosition = new Location(longitude, latitude, EMERGENT_ALT);

        // ---
        // Berechnung der Wegpunkte für Emergent Target...
        // ---

        emergReadyToStart = true;

    }

    /*
     * EmergentSearchArea Gebiet eintragen per GUI
     */
    public void addEmergentSearchAreaWP(double longitude, double latitude) {
        emergentSearchArea.add(new Location(longitude, latitude, EMERGENT_ALT));
    }

    /*
     * Getter für aktuelle EmergentSearchArea GUI
     */
    public List<Location> getEmergentSearchArea() {
        return emergentSearchArea;
    }

    @Override
    public boolean isTaskCalculated() {
        return emergReadyToStart;
    }

    // Groundcomponent GUI Interface, Button "Start Mission" etc. Entrypoint
    @Override
    public void startMission() {
        // Sendet Waypoints an MissionControl für Drohne...
        if (emergReadyToStart) {
            uploadTaskToMissionPlanner(calculatedEmergWaypoints, GroundComponent.EMERGENT_T);
            taskActive = true;
            flyRoute();
        } else {
            System.out.println("NOT CALCULATED YET");
        }
    }

    /**
     * Fliegt alle übermittelten Waypoints ab, bis Liste abgearbeitet
     */
    private void flyRoute() {
        long sleepTime = (long) GroundComponent.EMERGENT_REFRESH_TIME * 1000;

        for (int i = 0; i < calculatedEmergWaypoints.size(); i++) {

            // Ersten abzufliegenden Waypoint aus Liste holen, solange prüfen,
            // bis erreicht, dann entfernen
            Location locToCompute = calculatedEmergWaypoints.remove(i);
            boolean isAtWaypoint = isDroneAtWaypoint(locToCompute, TasksEnum.IRSTATIC);

            while (!isAtWaypoint) {

                isAtWaypoint = isDroneAtWaypoint(locToCompute, TasksEnum.IRSTATIC);
            }

            // ----------------
            // HIER FOTOS MACHEN !!!!!!!!
            super.takeNormalPhoto();
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

}
