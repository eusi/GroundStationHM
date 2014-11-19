/**
 * 
 */
package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.List;

import cs.hm.edu.sam.mc.ir.Ir;
import cs.hm.edu.sam.mc.ir.enum_interfaces.GroundGuiInterface;
import cs.hm.edu.sam.mc.ir.enum_interfaces.TasksEnum;
import cs.hm.edu.sam.mc.misc.Data;
import cs.hm.edu.sam.mc.misc.Location;


/**
 * @author Maximilian Haag
 *
 */

public abstract class GroundComponent implements GroundGuiInterface {

    // Waypoint descriptions für MissionPlanner
    protected static final String STATIC_IR_T = "Static_IR_Task";
    protected static final String DYNAMIC_IR_T = "Dynamic_IR_Task";
    protected static final String EMERGENT_T = "Emergent_Task";

    // "Waypoint erreicht" refresh check time (Sekunden), 8 x pro Sekunde abfragbar.
    protected static final double STATIC_REFRESH_TIME = 0.125;
    protected static final double DYNAMIC_REFRESH_TIME = 0.125;
    protected static final double EMERGENT_REFRESH_TIME = 0.125;

    // 1 Meter Toleranz Radius, um Wegpunkte als "erreicht" zu kennzeichnen
    protected final double STATIC_TOLERANCE = 0.00000000200;
    protected final double DYNAMIC_TOLERANCE = 0.00000000100;
    protected final double EMERGENT_TOLERANCE = 0.00000000100;

    // 5 Meter Range Radius, um zu prüfen, ob Waypoint RANGE erreicht (Meter)
    protected final double STATIC_RANGE = 0.00000000500;
    protected final double DYNAMIC_RANGE = 0.00000000500;
    protected final double EMERGENT_RANGE = 0.00000000500;

    // Höhen (Meter)
    protected final double STATIC_ALT = 80;
    protected final double DYNAMIC_ALT = 80;
    protected final double EMERGENT_ALT = 80;
    
       
    private TasksEnum currentTask = TasksEnum.NOTASSIGNED;
    private Location wayPointBefore = new Location(0, 0, 0);
    
    
    abstract public boolean isTaskCalculated();
    abstract public void calcWaypoints(double longitude, double latitude);

    
    @Override
    public boolean isAirCompOnline() {
        // TODO
        return false;
    }

    /**
     * Formt Liste in Waypoints Format für MissionPlanner, setzt Waypoint Liste
     * für einen Task.
     * 
     * @param toUpload
     */
    protected void uploadTaskToMissionPlanner(List<Location> toUpload, String taskName) {

        Location[] wayPointsArray = new Location[toUpload.size()];
        for (int i = 0; i < wayPointsArray.length; i++)
            wayPointsArray[i] = toUpload.get(i);

        // Sende an MissionPlanner Gruppe...
        Data.setWaypointList(wayPointsArray, taskName);

    }

    /**
     * Drohne hat bestimmten Waypoint erreicht, inkl Toleranz
     * 
     * @param locToCheck
     * @param currentTask
     * @return
     */
    protected boolean isDroneAtWaypoint(Location locToCheck, TasksEnum currentTask) {

        // Current von MissionControl Team...
        // Location currentLoc = Data.getCurrentPosition();

        // ----
        // Für Simulation CurrentPosition aus TestMain
        Location currentLoc = TestMain.getCurrentTESTPosition();
      //  System.out.println("Drohne ist an Location: " + currentLoc.toString());
        // ----

        boolean newWaypoint = locationHasChanged(currentLoc);
        double range = getToleranceRadius();
        return inRadius(locToCheck, currentLoc, currentTask, range);
        
    }

    /**
     * Prüft, ob Drohne Location RANGE erreicht hat.
     * 
     * @param currentTaskWPList
     * @return
     */
    protected boolean isDroneInWPRange(final List<Location> currTaskWaypoints, TasksEnum currentTask) {
        boolean waypointInRad = false;

        // Current von MissionControl Team...
        Location currentLoc = Data.getCurrentPosition();

        // Alle Locations prüfen, escape wenn innerhalb des Radius.
        for (int i = 0; i < currTaskWaypoints.size() || waypointInRad; i++) {
            double range = getRange();
            waypointInRad = inRadius(currTaskWaypoints.get(i), currentLoc, currentTask, range);
        }
        return waypointInRad;
    }

    /**
     * @param current
     */
    protected void setCurrentTask(TasksEnum current) {
        currentTask = current;
    }

    /**
     * Normales Foto machen
     */
    protected void takeNormalPhoto() {
        // TODO Auto-generated method stub

    }

    /**
     * Prüft ob sich Waypoint Daten vom Flieger seit letzter Abfrage verändert
     * haben
     * 
     * @param currentLoc
     * @return
     */
    private boolean locationHasChanged(Location currentLoc) {
        double cLat = currentLoc.getLat();
        double cLng = currentLoc.getLng();
        double oLat = wayPointBefore.getLat();
        double oLng = wayPointBefore.getLng();
        if (cLat == oLat && cLng == oLng)
            return false;
        else
            return true;
    }

    /**
     * Toleranz für Waypoint Überflug
     * 
     * @return
     */
    private double getToleranceRadius() {
        // Current Task Radius
        if (currentTask == TasksEnum.IRSTATIC) {
            return STATIC_TOLERANCE;
        } else if (currentTask == TasksEnum.IRDYNAMIC) {
            return DYNAMIC_TOLERANCE;
        } else {
            return EMERGENT_TOLERANCE;
        }
    }

    /**
     * Range, ob innerhalb Wegpunkt Reichweite
     * 
     * @return
     */
    private double getRange() {
        // Current Task Radius
        if (currentTask == TasksEnum.IRSTATIC) {
            return STATIC_RANGE;
        } else if (currentTask == TasksEnum.IRDYNAMIC) {
            return DYNAMIC_RANGE;
        } else {
            return EMERGENT_RANGE;
        }
    }

    /**
     * Prüft, ob aktuelle Drohnenposition innerhalb eines Radius zum nächsten
     * Wegpunkt liegt.
     * 
     * @param location
     * @param currentLoc
     * @return
     */
    private boolean inRadius(Location location, Location currentLoc, TasksEnum current, double radius) {
        double taskLat = location.getLat();
        double taskLong = location.getLng();
        double droneLat = currentLoc.getLat();
        double droneLong = currentLoc.getLng();

        double latGap = Math.abs(taskLat - droneLat);
        double longGap = Math.abs(taskLong - droneLong);
        double distance = Math.sqrt(latGap * latGap + longGap * longGap);

        // Innerhalb des Kreises
        if (distance <= radius)
            return true;
        else
            return false;
    }

}
