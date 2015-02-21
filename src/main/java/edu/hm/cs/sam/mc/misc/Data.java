package edu.hm.cs.sam.mc.misc;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;

/**
 * Data class contains some global data like the currentPosition of the aircraft
 * and the several waypoint-lists by several task-modules.
 *
 * @author Christoph Friegel
 * @version 0.1 EXAMPLE: SEE edu.hm.cs.sam.mc.options.Options.java
 */

public class Data {

    /**
     * clear zone in hash map
     *
     * @param type
     *            type to clear.
     */
    public static void clearZone(final SamType type) {
        zones.remove(type);
        LOG.info("Zones cleared: " + type.name().toString());
    }

    /**
     * getter for current obstacles.
     *
     * @return currentObstacles.
     */
    public static Obstacles getCurrentObstacles() {
        return currentObstacles;
    }

    /**
     * get current position - for e.g. routing tasks ATTENTION: catch if you 're
     * getting null-LocationWp-object (0-values for lng, lat, alt, type)!
     *
     * @return currentPosition - of the aircraft
     */
    public static LocationWp getCurrentPosition() {
        if (isCurrentLocationActive()
                && ((currentPosition.getLat() != 0.0) && (currentPosition.getLng() != 0.0))) {
            return currentPosition;
        }
        // else
        return null;
    }

    /**
     * get next position aka waypoint ATTENTION: catch if you 're getting
     * null-LocationWp-object (0-values for lng, lat, alt, type)!
     *
     * @return currentPosition - of the aircraft
     */
    public static LocationWp getNextPosition() {
        if (isCurrentLocationActive()
                && ((nextPosition.getLat() != 0.0) && (nextPosition.getLng() != 0.0))) {
            return nextPosition;
        }
        // else
        return null;
    }

    /**
     * get storaged target of type from hash map
     *
     * @param type
     *            sam type.
     * @return targets for type.
     */
    public static Target getTargets(final SamType type) {
        return targets.get(type);
    }

    /**
     * get storaged waypoints of type from hash map
     *
     * @param type
     *            sam type.
     * @return waypoints for type.
     */
    public static Waypoints getWaypoints(final SamType type) {
        return waypoints.get(type);
    }

    /**
     * get storaged zone of type from hash map.
     *
     * @param type
     *            sam type.
     * @return zones for sam type.
     */
    public static Zone getZones(final SamType type) {
        return zones.get(type);
    }

    /**
     * get polling (current aircraft position)
     *
     * @return currentLocationIsActive.
     */
    public static boolean isCurrentLocationActive() {
        return currentLocationIsActive;
    }

    /**
     * set polling (current aircraft position) - via GUI only, don't call this
     * manually!
     *
     * @param currentLocationIsActive
     *            polling: true = active, false = not active.
     */
    public static void setCurrentLocationIsActive(final boolean currentLocationIsActive) {
        Data.currentLocationIsActive = currentLocationIsActive;
    }

    /**
     * setter for current obstacles.
     *
     * @param newCurrentObstalces
     *            current obstacles.
     */
    public static void setCurrentObstacles(final Obstacles newCurrentObstalces) {
        currentObstacles = newCurrentObstalces;
    }

    /**
     * setter for new position. set current position - for rest only, don't call
     * this manually!
     *
     * @param newPosition
     *            waypoint for new position.
     */
    public static void setCurrentPosition(final LocationWp newPosition) {
        currentPosition = newPosition;
    }

    /**
     * setter for next position. set current position - for rest only, don't
     * call this manually!
     *
     * @param nextPos
     *            waypoint for next position.
     */
    public static void setNextPosition(final LocationWp nextPos) {
        nextPosition = nextPos;
    }

    /**
     * storage target in hash map.
     *
     * @param target
     *            new target.
     */
    public static void setTargets(final Target target) {
        targets.put(target.getSAMType(), target);
        LOG.info("Targets saved: " + target.getSAMType().name().toString());
    }

    /**
     * storage waypoints in hash map.
     *
     * @param wps
     *            new waypoint.
     */
    public static void setWaypoints(final Waypoints wps) {
        waypoints.put(wps.getSAMType(), wps);
        LOG.info("Waypoints saved: " + wps.getSAMType().name().toString());
    }

    /**
     * storage zone in hash map
     *
     * @param zone
     *            new zone.
     */
    public static void setZones(final Zone zone) {
        zones.put(zone.getSAMType(), zone);
        LOG.info("Zones saved: " + zone.getSAMType().name().toString());
    }

    private static final Logger LOG = Logger.getLogger(Data.class.getName());

    /**
     * this is actually the current position of the aircraft (by MP)
     */
    private static volatile LocationWp currentPosition;

    /**
     * this is actually the next position's waypoint of the aircraft (by MP)
     */
    private static volatile LocationWp nextPosition;

    /**
     * waypoints which are created by routing-modules for routing (to MP) use
     * this to storage your module-waypoints
     */
    private static volatile Map<SamType, Waypoints> waypoints = new HashMap<SamType, Waypoints>();

    /**
     * current created zone-objects use this to storage your module-zones
     */
    private static volatile Map<SamType, Zone> zones = new HashMap<SamType, Zone>();

    /**
     * current created target-objects use this to storage your module-zones
     */
    private static volatile Map<SamType, Target> targets = new HashMap<SamType, Target>();

    /**
     * this are the current existing obstacles
     */
    private static volatile Obstacles currentObstacles;

    /**
     * if we want to poll a current (and next) location, we have to turn it on
     * check GUI-option: FILE -> GET LOCATION
     */
    private static boolean currentLocationIsActive;

    private Data() {

    }
}
