package edu.hm.sam.mc.searcharea.task.test;

import org.junit.Assert;
import org.junit.Test;
import edu.hm.cs.sam.mc.searcharea.task.WaypointCalculator;

public class SearchAreaRoutingTest {
	/*WaypointCalculator calc = new WaypointCalculator();
	@Test
    public void testConvertToBearing() {
		final double deg = -30.54;
		double bearing = calc.convertToBearing(deg);
        Assert.assertEquals(329.46, bearing,0);
    }
	@Test
    public void testRadToDeg() {
		final double rad = 130.54;
		double radians = calc.radToDeg(rad);
        Assert.assertEquals(7479.391057637766, radians, 0);
    }
	@Test
    public void testDegToRad() {
		final double deg = 77.22;
		double radians = calc.degToRad(deg);
        Assert.assertEquals(1.3477432483900214, radians, 0);
    }/*
	@Test
    public void testCalcBearing() {
		LocationWp c1 = new LocationWp(68.4521, 14.9461);
		LocationWp c2 = new LocationWp(65.9185, 12.6248);
		double bearing = calc.calcBearing(c1, c2);
        Assert.assertEquals(2, bearing, 0);
    }
	@Test
	public void testFindLongestLine() {
        final LocationWp cornerA = new LocationWp(48.56677, 11.96126, 'A');
        final LocationWp cornerB = new LocationWp(48.56992, 11.96199, 'B');
        final LocationWp cornerC = new LocationWp(48.56930, 11.96512, 'C');
        final LocationWp cornerD = new LocationWp(48.56847, 11.96418, 'D');
        final LocationWp cornerE = new LocationWp(48.57208, 11.96160, 'E');
        final LocationWp cornerF = new LocationWp(48.56830, 11.96684, 'F');
        List<Line> lines = new ArrayList<Line>();
        final Line line1 = calc.calculateLineFrom2Points(cornerA, cornerB);
        final Line line2 = calc.calculateLineFrom2Points(cornerC, cornerD);
        final Line line3 = calc.calculateLineFrom2Points(cornerE, cornerF);
        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        int index = calc.findLongestLine(lines);
        Assert.assertEquals(1,index, 0);
	}*/
}
