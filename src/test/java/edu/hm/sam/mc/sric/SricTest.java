package edu.hm.sam.mc.sric;

import org.junit.Assert;
import org.junit.Test;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;

/**
 * Created by Frederic on 19.12.2014.
 */
public class SricTest {
    @Test
    public void testSetWp() {
        final LocationWp wp = new LocationWp(1.0, 2.0, 3.0, MavCmd.LOITER_UNLIM, 0, 0, 0, 0, 0);
        final LocationWp[] wps = { wp };
        Data.setWaypoints(new Waypoints(wps, SamType.TARGET_SRIC));

        final LocationWp[] load = Data.getWaypoints(SamType.TARGET_SRIC).getWaypoints();

        Assert.assertArrayEquals(wps, load);
    }

    @Test
    public void testVollSinnlos() {
        Assert.assertTrue(true);
    }
}
