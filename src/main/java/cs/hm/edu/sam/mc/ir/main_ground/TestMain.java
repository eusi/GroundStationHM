package cs.hm.edu.sam.mc.ir.main_ground;


import java.util.ArrayList;
import java.util.List;

import cs.hm.edu.sam.mc.misc.Location;

/**
 * @author Maximilian Haag
 *
 */
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GroundComponent groundComponent = new StaticIRComponent();
		
	}
	
	
	public static List<Location> createTestWaypoints() {
		List<Location> testWaypoints = new ArrayList<>();
		
		//0.000 000 000 0X = ca. ZENTIMETER
		//0.000 000 0X0 00 = ca. METER
		double [] lat = {48.14031960001, 48.14031960002, 48.14031960003, 48.14031960004, 48.14031960005};
		double [] lng = {11.55549460001, 11.55549460001, 11.55549460001, 11.55549460001, 11.55549460001};
		
		for(int i = 0; i<lat.length; i++) {
			testWaypoints.add(new Location(lng[i], lat[i], 80));
		}
		return testWaypoints;
		
	}

}
