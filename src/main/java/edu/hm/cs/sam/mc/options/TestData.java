package edu.hm.cs.sam.mc.options;

import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.cs.sam.mc.misc.Zone;
import edu.hm.cs.sam.mc.rest.RestClient;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;

/**
 * @author Christoph Friegel
 * @author Opitz, Jan (jopitz@hm.edu)
 * @version 0.1
 */

public class TestData {

    /**
     *
     */
    public static void createAirdrop() {
        final LocationWp wp0 = new LocationWp(47.2613494, 11.3485014, 100, MavCmd.WAYPOINT, 1, 0,
                0, 0, 0);
        final LocationWp setServo = new LocationWp(47.2615970, 11.3475895, 100,
                MavCmd.DO_SET_SERVO, 1, 0, 0, 0, 0);
        final LocationWp wp1 = new LocationWp(47.2619319329718, 11.3471174240112, 100,
                MavCmd.WAYPOINT, 1, 0, 0, 0, 0);
        final LocationWp[] waypoints = { wp0, setServo, wp1 };
        final Waypoints wp = new Waypoints(waypoints, SamType.TASK_AIRDROP);
        Data.setWaypoints(wp);
        RestClient.sendWaypoints(Data.getWaypoints(SamType.TASK_AIRDROP));
    }

    /**
     *
     */
    public static void createAirdropTarget() {
        final LocationWp airdropT = new LocationWp(47.2619319329718, 11.3471174240112);
        final Target airdropTWP = new Target(airdropT, SamType.TARGET_AIRDROP);
        Data.setTargets(airdropTWP);
        RestClient.sendTarget(Data.getTargets(SamType.TARGET_AIRDROP));
    }

    /**
     *
     */
    public static void createEmergentZone() {
        final LocationWp emergent = new LocationWp(47.2522326180135, 11.3461303710938);
        final LocationWp emergent2 = new LocationWp(47.2526404327985, 11.3428258895874);
        final LocationWp emergent3 = new LocationWp(47.2541842746085, 11.3521814346313);
        final LocationWp emergent4 = new LocationWp(47.2517082801039, 11.3500785827637);
        final LocationWp emergent5 = new LocationWp(47.2522326180135, 11.3461303710938);
        final LocationWp[] emergentWP = { emergent, emergent2, emergent3, emergent4, emergent5 };
        final Zone emergentZ = new Zone(emergentWP, SamType.ZONE_EMERGENT, Constants.BLUE);
        Data.setZones(emergentZ);
        RestClient.sendZone(Data.getZones(SamType.ZONE_EMERGENT));
    }

    /**
     *
     */
    public static void createIrTarget() {
        final LocationWp irDy = new LocationWp(47.2593689233598, 11.3431477546692);
        final Target irDyWP = new Target(irDy, SamType.TARGET_IR_DYNAMIC);
        Data.setTargets(irDyWP);
        RestClient.sendTarget(Data.getTargets(SamType.TARGET_IR_DYNAMIC));

        final LocationWp irSt = new LocationWp(47.2575485286791, 11.336817741394);
        final Target irStWP = new Target(irSt, SamType.TARGET_IR_STATIC);
        Data.setTargets(irStWP);
        RestClient.sendTarget(Data.getTargets(SamType.TARGET_IR_STATIC));
    }

    /**
     *
     */
    public static void createNoFlightZone() {
        final LocationWp noFlight = new LocationWp(47.2653102560696, 11.3484048843384);
        final LocationWp noFlight2 = new LocationWp(47.2649025388595, 11.3366031646729);
        final LocationWp noFlight3 = new LocationWp(47.2623396730612, 11.3267755508423);
        final LocationWp noFlight4 = new LocationWp(47.253659956022, 11.3275480270386);
        final LocationWp noFlight5 = new LocationWp(47.2507469804535, 11.3538551330566);
        final LocationWp noFlight6 = new LocationWp(47.2620484304606, 11.3608074188232);
        final LocationWp noFlight7 = new LocationWp(47.2653102560696, 11.3484048843384);
        final LocationWp[] nofFlightWP = { noFlight, noFlight2, noFlight3, noFlight4, noFlight5,
                noFlight6, noFlight7 };
        final Zone nofFlightZ = new Zone(nofFlightWP, SamType.ZONE_NO_FLIGHT, Constants.WHITE);
        Data.setZones(nofFlightZ);
        RestClient.sendZone(Data.getZones(SamType.ZONE_NO_FLIGHT));
    }

    /**
     *
     */
    public static void createOffAxisTarget() {
        final LocationWp offaxis = new LocationWp(47.2656524448756, 11.3425147533417);
        final Target offaxisWP = new Target(offaxis, SamType.TARGET_OFFAXIS);
        Data.setTargets(offaxisWP);
        RestClient.sendTarget(Data.getTargets(SamType.TARGET_OFFAXIS));
    }

    /**
     *
     */
    public static void createSearchArea() {
        final LocationWp wp1 = new LocationWp(47.2604902553321, 11.3333415985107, 100,
                MavCmd.WAYPOINT, 1, 0, 0, 0, 0);
        final LocationWp wp2 = new LocationWp(47.2561504230734, 11.3349294662476, 100,
                MavCmd.WAYPOINT, 1, 0, 0, 0, 0);
        final LocationWp wp3 = new LocationWp(47.2568640, 11.3366246, 100, MavCmd.WAYPOINT, 1, 0,
                0, 0, 0);
        final LocationWp wp4 = new LocationWp(47.2607815, 11.3354874, 100, MavCmd.WAYPOINT, 1, 0,
                0, 0, 0);
        final LocationWp wp5 = new LocationWp(47.2611310, 11.3374829, 100, MavCmd.WAYPOINT, 1, 0,
                0, 0, 0);
        final LocationWp[] waypointss = { wp1, wp2, wp3, wp4, wp5 };
        final Waypoints wpp = new Waypoints(waypointss, SamType.TASK_SEARCH_AREA);
        Data.setWaypoints(wpp);
        RestClient.sendWaypoints(Data.getWaypoints(SamType.TASK_SEARCH_AREA));
    }

    /**
     *
     */
    public static void createSearchAreaZone() {
        final LocationWp sa = new LocationWp(47.261961057368, 11.3426971435547);
        final LocationWp sa2 = new LocationWp(47.2604902553321, 11.3333415985107);
        final LocationWp sa3 = new LocationWp(47.2561504230734, 11.3349294662476);
        final LocationWp sa4 = new LocationWp(47.2583495100493, 11.3401651382446);
        final LocationWp sa5 = new LocationWp(47.256616462377, 11.3452506065369);
        final LocationWp sa6 = new LocationWp(47.2598931854198, 11.3464093208313);
        final LocationWp sa7 = new LocationWp(47.261961057368, 11.3426971435547);
        final LocationWp[] saWP = { sa, sa2, sa3, sa4, sa5, sa6, sa7 };
        final Zone saZ = new Zone(saWP, SamType.ZONE_SEARCH_AREA, Constants.RED);
        Data.setZones(saZ);
        RestClient.sendZone(Data.getZones(SamType.ZONE_SEARCH_AREA));
    }

    /**
     *
     */
    public static void createSric() {
        final LocationWp wp = new LocationWp(47.2627474, 11.3306379318237, 100,
                MavCmd.LOITER_UNLIM, 1, 0, 0, 0, 0);
        final LocationWp[] waypoints = { wp };
        final Waypoints sricTWP = new Waypoints(waypoints, SamType.TASK_SRIC);
        Data.setWaypoints(sricTWP);
        RestClient.sendWaypoints(Data.getWaypoints(SamType.TASK_SRIC));
    }

    /**
     *
     */
    public static void createSricTarget() {
        final LocationWp wp = new LocationWp(47.2627474, 11.3306379318237, 100, MavCmd.WAYPOINT, 1,
                0, 0, 0, 0);

        final Target sricTWP = new Target(wp, SamType.TARGET_SRIC);
        Data.setTargets(sricTWP);
        RestClient.sendTarget(Data.getTargets(SamType.TARGET_SRIC));
    }

    private TestData() {

    }
}
