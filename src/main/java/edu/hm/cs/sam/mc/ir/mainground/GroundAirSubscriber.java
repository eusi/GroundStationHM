package edu.hm.cs.sam.mc.ir.mainground;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.control.GroundStationClient;
import edu.hm.cs.sam.mc.emergent.Emergent;
import edu.hm.cs.sam.mc.ir.Ir;
import edu.hm.cs.sam.mc.ir.enuminterfaces.ColorEnum;
import edu.hm.sam.control.ControlMessage.Service;
import edu.hm.sam.control.SubscriptionHandler;
import edu.hm.sam.infrared.InfraredMessage;

/**
 * Class for handling subscriptions, received by air.
 * 
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas
 *         Angermeier (Team 05 - Infrared/Emergent Task)
 */
public class GroundAirSubscriber extends Thread {


    private static final String[] TOPICS = { "send_response_ir" };
    private static final String ONSTAT = "online";

    private static final String PHOTOSUCCESS = "photoTakenSuccess";

    private static final String IRPHSUCCESS = "irPhotoTakenSuccess";
    private static final String IRVIDSTART = "startedIRVideo";

    private static final String IRVIDSTOP = "stoppedIRVideo";
    private static boolean onlineQueryReceived = false;

    private static final Service IRCAMSERVICE = Service.IRCAMERA;
    private static final Logger LOG = Logger.getLogger(GroundAirSubscriber.class.getName());
    
    private Ir irGui;

    private Emergent emGui;

    private GroundStationClient gsc;


    /**
     * If emergent GUI is started first.
     *  
     * @param emGui Emergent GUI
     */
    public GroundAirSubscriber(final Emergent emGui) {
        this.emGui = emGui;
//        emGui.printConsole(toString() + " emeKonstr");

    }
    
    /**
     * If infrared GUI is started first.
     *  
     * @param emGui Emergent GUI
     */
    public GroundAirSubscriber(final Ir irGui) {
        this.irGui = irGui;
//        irGui.printConsole(toString() + " irkonstr");
    }

    /**
     * Adding subscription handlers to receive orders from air.
     */
    private void addSubscriptionHandlers() {
        gsc = GroundStationClient.getInstance();
        gsc.addSubscriptionHandler(IRCAMSERVICE, TOPICS[0],
                new SubscriptionHandler<InfraredMessage>() {

                    @Override
            public void handleSubscription(final InfraredMessage subscription) {

                final String recMsg = subscription.getMessage();
                LOG.info("Subscribe received");
                if (recMsg.equals(ONSTAT)) {
                    setOnlineQueryReceived(true);
                    if (Ir.isActivated()) {
                        irGui.setDroneConnectionColor(ColorEnum.COLORGREEN);
                    } else {
                        emGui.setDroneConnectionColor(ColorEnum.COLORGREEN);
                    }
                } else if (recMsg.equals(PHOTOSUCCESS)) {
                    irGui.printConsole("AIR: Normal Photo taken...");
                } else if (recMsg.equals(IRPHSUCCESS)) {
                    irGui.printConsole("AIR: IR-Photo taken...");
                } else if (recMsg.equals(IRVIDSTART)) {
                    irGui.printConsole("AIR: IR-Video started...");
                } else if (recMsg.equals(IRVIDSTOP)) {
                    irGui.printConsole("AIR: IR-Video stopped...");
                } else {
                    if (Ir.isActivated()) {
                        irGui.printConsole("AIR: Not recognized command received");
                    } else {
                        emGui.printConsole("AIR: Not recognized command received");
                    }
                        }
                    }
                });
    }



    @Override
    public void run() {
        addSubscriptionHandlers();
        while (!isInterrupted()) {
            final GroundAirPublisher gap = PubSubControl.getPublisher();
            try {
                gap.join();
            } catch (final InterruptedException e) {
                LOG.error("Subscriber interrupted", e);
            }
        }
    }

    /**
     * Changing GUI if infrared was opened first.
     * 
     * @param emGui Emergent GUI.
     */
    public void setEmGui(final Emergent emGui) {
//        emGui.printConsole(toString() + " emSet");
        this.emGui = emGui;
    }
    
    /**
     * Changing GUI if emergent was opened first.
     * 
     * @param irGui Infrared GUI.
     */
    public void setIrGui(final Ir irGui) {
//        irGui.printConsole(toString() + " irSet");
        this.irGui = irGui;
    }
    
    
    public static boolean isOnlineQueryReceived() {
        return onlineQueryReceived;
    }

    public static void setOnlineQueryReceived(final boolean onlineQueryReceived) {
        GroundAirSubscriber.onlineQueryReceived = onlineQueryReceived;
    }
    
    public Emergent getEmGui() {
        return emGui;
    }

    public Ir getIrGui() {
        return irGui;
    }

}
