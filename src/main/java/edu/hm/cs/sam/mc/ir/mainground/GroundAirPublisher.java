package edu.hm.cs.sam.mc.ir.mainground;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.control.GroundStationClient;
import edu.hm.sam.control.ControlMessage.Service;
import edu.hm.sam.infrared.InfraredMessage;

/**
 * Class for sending orders to air, for execution.
 * 
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas Angermeier (Team 05 - Infrared/Emergent Task)
 */
public class GroundAirPublisher extends Thread {

    private static final String TOPIC = "send_orders_ir";
   
    private String order = GET_ONLINE_STATUS;

    protected static final String PUBNAME = "GroundPublisher";

    protected static final String GET_ONLINE_STATUS = "getOnlineStatus";
    protected static final String TAKE_PHOTO_MSG = "takePhoto";
    
    protected static final String TAKE_PHOTO_AND_IRPHOTO = "takePhotoAndIRPhoto";
    protected static final String STOP_PHOTO_AND_IRPHOTO = "stopPhotoAndIRPhoto";
    
    protected static final String STOP_PHOTO_MSG = "stopPhoto";
    protected static final String START_IR_VIDEO_MSG = "startIRVideo";
    protected static final String STOP_IR_VIDEO_MSG = "stopIRVideo";

    private static final Logger LOG = Logger.getLogger(GroundAirPublisher.class.getName());

    private final GroundStationClient gsc;
    private final Service irCamServ;
    private final InfraredMessage currIrMsg;


    public GroundAirPublisher() {

        gsc = GroundStationClient.getInstance();
        irCamServ = Service.IRCAMERA;
        currIrMsg = new InfraredMessage();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            LOG.error("Sleep error", e);
        }

        LOG.info("IR Publisher gestartet.");

        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {

                try {
                    this.wait();
                } catch (final InterruptedException e) {
                    LOG.error("Wait error", e);
                }

                // ORDER senden
                currIrMsg.setMessage(order);
                gsc.sendAction(irCamServ, TOPIC, currIrMsg);


            }
        }
        LOG.info("Publisher was interrupted");

    }

    /**
     * Setting new order for air to receive.
     * 
     * @param order The new order to be set
     */
    public void setOrder(final String order) {
    	synchronized (this) {
            this.order = order;
		}
    }

}
