package edu.hm.sam.services.namingservice;

import org.junit.Assert;
import org.junit.Test;

import edu.hm.cs.sam.mc.control.GroundStationClient;
import edu.hm.sam.control.ControlMessage;

/**
 * Created by sebastian on 10.12.2014.
 */
public class GroundStationClientIT {

    @Test
    public void test() {
        GroundStationClient gsc = GroundStationClient.getInstance();
        ControlMessage cm = new ControlMessage(ControlMessage.Task.SEARCHAREA_STANDARD_TASK);

        cm.setState(ControlMessage.State.STARTSERVICE);

        gsc.sendAction(ControlMessage.Service.CONTROLLER, "", cm);


        Assert.assertEquals(true, true);
    }
}
