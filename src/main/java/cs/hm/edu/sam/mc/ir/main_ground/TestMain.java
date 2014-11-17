package cs.hm.edu.sam.mc.ir.main_ground;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import cs.hm.edu.sam.mc.ir.Ir;
import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class TestMain extends Thread {

    // 0.000 000 000 0X = ca. ZENTIMETER
    // 0.000 000 0X0 00 = ca. METER

    // Test Waypoints
    // private static double [] photoWaypointlat = {48.50000000100, 48.50000000200, 48.50000000300, 48.50000000400, 48.50000000500};
    // private static double [] photoWaypointlng = {11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000};

    private static double[] photoWaypointlat = { 48.50000000100, 48.50000000200 };
    private static double[] photoWaypointlng = { 11.50000000000, 11.50000000000 };

    // Waypoints, die FlugSimulation abfliegt...
    private static double[] flylat = { 48.50000000900, 48.50000000800, 48.50000000700, 48.50000000600, 48.50000000500, 48.50000000100, 48.50000000200 };
    private static double[] flylng = { 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000 };

    // Current Test Location
    private volatile static Location currentSimulatedPos = new Location(0, 0, 0);

    /**
     * @param args
     */
    public static void main(String[] args) {

        StaticIRComponent test = new StaticIRComponent();

       
        // Target Location
        double latTest = 48.50000000000;
        double lngTest = 11.50000000000;

        // Waypoints berechnen
        test.calcWaypoints(lngTest, latTest);

        // Flieger Simulation starten - Current Position simulieren
        Thread fly = new TestMain();
        fly.start();

        // Task starten
        test.startMission();

    }

    /*
     * (non-Javadoc) Flug Simulation
     */
    @Override
    public void run() {
        System.out.println("Flieger gestartet");

        for (int i = 0; i < flylat.length; i++) {

            synchronized (currentSimulatedPos) {
                currentSimulatedPos = new Location(flylng[i], flylat[i], 80);
            }

            try {
            	//Sleep bis neuen Waypoint
                Thread.sleep(100);
                
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static synchronized Location getCurrentTESTPosition() {
        System.out.println("RETURN: " + currentSimulatedPos.toString());
        return currentSimulatedPos;
    }

    public static List<Location> createTestWaypoints() {
        List<Location> testWaypoints = new ArrayList<>();

        for (int i = 0; i < photoWaypointlat.length; i++) {
            testWaypoints.add(new Location(photoWaypointlng[i], photoWaypointlat[i], 80));
        }
        System.out.println(testWaypoints.toString());
        return testWaypoints;
    }

    public static void guiTest() {
        JInternalFrame rolandsGUI = new Ir();
        JFrame j = new JFrame();
        j.setBounds(400, 400, 800, 800);
        j.add(rolandsGUI);
        rolandsGUI.setVisible(true);
        j.setVisible(true);
    }

}
