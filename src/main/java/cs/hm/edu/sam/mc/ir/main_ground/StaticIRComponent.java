/**
 * 
 */
package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.ir.enum_interfaces.StaticGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.TasksEnum;
import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class StaticIRComponent extends GroundComponent implements StaticGuiInterface {

    private List<Location> calculatedStatWaypoints = new ArrayList<>();
    private boolean statReadyToStart = false;
    private Location statTargetLoc = null;
    private boolean taskActive = false;

    // Groundcomponent GUI Interface, Button "Wegpunkte berechnen" etc.
    // Entrypoint
    @Override
    public void calcWaypoints(double longitude, double latitude) {
        statTargetLoc = new Location(longitude, latitude, STATIC_ALT);

        // ----------------
        // Berechnung der Wegpunkte für Static Target hier...
        // ----------------

        // --- TEST
        calculatedStatWaypoints = TestMain.createTestWaypoints();
        // --- TEST

        statReadyToStart = true;
        System.out.println("Static IR waypoints calculated, static IR task ready...");

    }

    // Groundcomponent GUI Interface, Button "Start Mission" etc. Entrypoint
    @Override
    public void startMission() {
        // Sendet Waypoints an MissionControl für Drohne...
        if (statReadyToStart) {
            uploadTaskToMissionPlanner(calculatedStatWaypoints, GroundComponent.STATIC_IR_T);
            taskActive = true;
            flyRoute();
        } else {
            System.out.println("NOT CALCULATED YET");
        }
    }

    @Override
    public boolean isTaskCalculated() {
        return statReadyToStart;
    }

    /**
     * Fliegt alle übermittelten Waypoints ab, bis Liste abgearbeitet
     */
    private void flyRoute() {

        // 1/8 Sekunde = 125ms warten

        long sleepTime = (long) GroundComponent.STATIC_REFRESH_TIME * 1000;

        for (int i = 0; i < calculatedStatWaypoints.size(); i++) {

            // Ersten abzufliegenden Waypoint aus Liste holen, solange prüfen,
            // bis erreicht, dann entfernen
            Location locToCompute = calculatedStatWaypoints.get(i);

            boolean isAtWaypoint = isDroneAtWaypoint(locToCompute, TasksEnum.IRSTATIC);

            while (!isAtWaypoint) {

                isAtWaypoint = isDroneAtWaypoint(locToCompute, TasksEnum.IRSTATIC);
                System.out.println("Drohne ist NICHT an static Waypoint ODER Foto bereits geschossen: "+ locToCompute.toString());
            }

            // ----------------
            // HIER FOTOS MACHEN !!!!!!!!
            System.out.println("Drohne ist an static Waypoint, FOTO: >>>>>>>>>> "+ locToCompute.toString());
            takeInfraredPhoto();
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

    private void takeInfraredPhoto() {
        // TODO Auto-generated method stub

    }

}
