package edu.hm.cs.sam.mc.ir.mainground;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.emergent.Emergent;
import edu.hm.cs.sam.mc.ir.Ir;

/**
 * Class for alternating the publisher and subscriber.
 * 
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas
 *         Angermeier (Team 05 - Infrared/Emergent Task)
 */
public class PubSubControl {


    private static GroundAirPublisher publisher;
    private static GroundAirSubscriber subscriber;

    private static boolean pubActivated = false;

    private static boolean subActivated = false;

    private static final Logger LOG = Logger.getLogger(PubSubControl.class.getName());

    private PubSubControl() {

    }

    public static GroundAirPublisher getPublisher() {
        return publisher;
    }

    public static GroundAirSubscriber getSubscriber() {
        return subscriber;
    }

    public static void initPublisher() {
        publisher = new GroundAirPublisher();
        pubActivated = true;
    }

    public static void initSubscriber(final Emergent emGui) {
        subscriber = new GroundAirSubscriber(emGui);
        subActivated = true;
    }

    public static void initSubscriber(final Ir irGui) {
        subscriber = new GroundAirSubscriber(irGui);
        subActivated = true;
    }

    public static boolean isPubActivated() {
        return pubActivated;
    }

    public static boolean isSubActivated() {
        return subActivated;
    }

    public static void setPublisher(final GroundAirPublisher nPublisher) {
        publisher = nPublisher;
    }

    public static void setSubscriber(final GroundAirSubscriber nSubscriber) {
        subscriber = nSubscriber;
    }
    
    public static Logger getLog() {
        return LOG;
    }

}
