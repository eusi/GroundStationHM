/**
 * 
 */
package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.ir.Ir;
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

    private Ir irGui;
    
    public StaticIRComponent(Ir irGui) {
		this.irGui = irGui;
	}
    
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

            // Ersten abzufliegenden Waypoint aus Liste holen, solange Fotos im Radius machen bis verlassen, dann entfernen
            
        	Location locToCompute = calculatedStatWaypoints.get(i);
            boolean isInWPRadius = isDroneAtWaypoint(locToCompute, TasksEnum.IRSTATIC);

            while(!isInWPRadius) {
            	
            	try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	isInWPRadius = isDroneAtWaypoint(locToCompute, TasksEnum.IRSTATIC);
            }
            
          //Fotos 8,7 Hz -> 8 Fotos /Sek
            while (isInWPRadius) {
            	
                takeInfraredPhoto();
                super.takeNormalPhoto();
            	
            	try {
					Thread.sleep(1000/8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                
                System.out.println("Drohne ist innerhalb von Waypoint: "+ locToCompute.toString()+" ----> FOTO");
                isInWPRadius = isDroneAtWaypoint(locToCompute, TasksEnum.IRSTATIC);
 
            }

        }

        taskActive = false;
    }

    private void takeInfraredPhoto() {
        // TODO Auto-generated method stub

    }

}
