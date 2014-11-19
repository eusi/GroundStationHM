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


	
	
	private static ArrayList<Double> photoWaypointlat = new ArrayList<>();
	private static ArrayList<Double> photoWaypointlng = new ArrayList<>();
	
	
//    private static double[] photoWaypointlat = { 48.50000000100, 48.50000000200 };
//    private static double[] photoWaypointlng = { 11.50000000000, 11.50000000000 };
//
//    // Waypoints, die FlugSimulation abfliegt...
//    private static double[] flylat = { 48.50000000900, 48.50000000800, 48.50000000700, 48.50000000600, 48.50000000500, 48.50000000100, 48.50000000200 };
//    private static double[] flylng = { 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000, 11.50000000000 };

	private static ArrayList<Double> flylat = new ArrayList<>();
	private static ArrayList<Double> flylng = new ArrayList<>();
	
    // Current Test Location
    private volatile static Location currentSimulatedPos = new Location(0, 0, 0);

    /**
     * @param args
     */
    public static void main(String[] args) {
    	
    	//fillTestData();
    	//startTestFlight();

    	testAirConnection();
    	
    	//guiTest();
    	
    }

    
 
    
    private static void testAirConnection() {
//        new GroundAirSubscriber("A", 0, new String[] {"A"}).start();
//        new GroundAirSubscriber("B - Lagger",2000, new String[] {"B"}).start();
//        new GroundAirSubscriber("A&B", 0, new String[] {"A", "B"}).start();

      GroundAirPublisher g =   new GroundAirPublisher("0");
      g.start();
      
      try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      GroundAirSubscriber s = new GroundAirSubscriber("A", 0, new String[] {"A"});
      GroundAirSubscriber s2 = new GroundAirSubscriber("B", 0, new String[] {"B"});
      s.start();
      s2.start();
      
		
	}


    private static void fillTestData() {
    	
    	//10 cm
    	double flightStep = 0.00000000010;

    	//Wegpunkte für Flieger (ca. 5000cm = 50m )
    	for(int i = 0; i < 500; i++) {
    		flylat.add(48.50000000000 + flightStep);
    		flylng.add(11.50000000001);
    		flightStep += 0.00000000010 ;
    	}
    	
    	//Wegpunkte für Foto
    	for(int i = 0; i < 500; i+=50) {
    		photoWaypointlat.add(flylat.get(i));
    		photoWaypointlng.add(11.50000000001);
    		
    	}
    	
//    	for(int i = 0; i < flylat.size(); i++) {
//    		System.out.printf("%.11f\n", flylat.get(i));
//    		System.out.printf("%.11f\n", flylng.get(i));
//    	}
//		System.out.println("--------------");
//    	for(int i = 0; i< photoWaypointlat.size(); i++) {
//    		System.out.printf("%.11f\n", photoWaypointlat.get(i));
//    		System.out.printf("%.11f\n", photoWaypointlng.get(i));
//    	}


    }



	private static void startTestFlight() {
		Ir rolandsGUI = new Ir();
    	StaticIRComponent test = new StaticIRComponent(rolandsGUI);
    	 
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
     *   ~43,2km/h minimum == 12m/s
     *   
     *   Testdaten: 10m/s -> pro 100ms 1 Meter weiter
     *   
     */
    @Override
    public void run() {
        System.out.println("Flieger gestartet");

        for (int i = 0; i < flylat.size(); i++) {

            synchronized (currentSimulatedPos) {
                currentSimulatedPos = new Location(flylng.get(i), flylat.get(i), 80);
            }

            try {
            	//10 ms Sleep bis neuen Waypoint
                Thread.sleep(10);
                
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static synchronized Location getCurrentTESTPosition() {
       // System.out.println("RETURN: " + currentSimulatedPos.toString());
        return currentSimulatedPos;
    }

    public static List<Location> createTestWaypoints() {
        List<Location> testWaypoints = new ArrayList<>();

        for (int i = 0; i < photoWaypointlat.size(); i++) {
            testWaypoints.add(new Location(photoWaypointlng.get(i), photoWaypointlat.get(i), 80));
        }
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
