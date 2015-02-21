package edu.hm.cs.sam.mc.rest;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.ObstacleDynamic;
import edu.hm.cs.sam.mc.misc.ObstacleStatic;
import edu.hm.cs.sam.mc.misc.Obstacles;
import edu.hm.cs.sam.mc.misc.Settings;
import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.cs.sam.mc.misc.Wind;
import edu.hm.cs.sam.mc.misc.Zone;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;

/**
 * Send and get data to and from Mission Planner (RESTful via JSON).
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class RestClient {

    /**
     * Connects RESTful to MissionPlanner and pushes/pulls data.
     *
     * @param restCall
     *            - String (rest request) - required
     * @param printLog
     *            - boolean (if logs should be printed) - default: true
     * @param jsonToSend
     *            - String (JSON itself, required for sending JSON to MP) -
     *            default: null
     * @param type
     *            - SamType (needed for print out specific SamType in log) -
     *            default: SamType.UNKNOWN or 0
     * @return output - String (JSON response from MissionPlanner)
     */
    public static String connectToMissionPlanner(final String restCall, final boolean printLog,
            final String jsonToSend, final SamType type) {
        String output = null;

        Client client = null;
        ClientResponse response = null;

        // if jsonToSend is not null, so we want to send a json to MP
        // otherwise, if jsonToSend is null we want to get data from MP
        final boolean sendJson = (jsonToSend != null);

        try {
            // create client structure for WebResource
            // getting data from MP
            if (!sendJson) {
                client = Client.create();
            }
            // sending data to MP
            else {
                final ClientConfig clientConfig = new DefaultClientConfig();
                clientConfig.getFeatures()
                        .put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

                client = Client.create(clientConfig);
                client.setConnectTimeout(30 * 1000);
            }

            // print start log if needed
            if (printLog) {
                LOG.info("Request MP-REST-Call: " + restCall);
            }

            // print SamType
            if (printLog && (type != SamType.UNKNOWN)) {
                LOG.info("MP-REST-Call for: " + type.name().toString());
            }

            // create WebResource with client and rest request
            final WebResource webResource = client.resource(Settings.getRestMpUrl() + restCall);

            // getting data from MP
            if (!sendJson) {
                response = webResource.accept("application/json").get(ClientResponse.class);
                // sending data to MP
            } else {
                response = webResource.accept("application/json").type("application/json")
                        .post(ClientResponse.class, jsonToSend);
            }

            // log http response from MP service
            if (response.getStatus() != 200) {
                LOG.warn("Failed : HTTP error code : " + response.getStatus());
            }

            // get MP response
            output = response.getEntity(String.class);

            // now the http connection is closed

            // print end log if needed
            if (printLog) {
                LOG.info("Response MP-REST-Service: " + output);
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }

        return output;
    }

    /**
     * Gets current position (aircraft) via REST interface and gives it to
     * Data.setCurrentPosition (Data.currentPosition).
     */
    public static void getCurrentPosition() {
        try {
            String output = connectToMissionPlanner(Constants.REST_GET_CURRENT_POSITION, false,
                    null, SamType.UNKNOWN);

            if (TRANSMITION_NO_OUTPUT.equals(output)) {
                LOG.warn("MP-Service Response: Not able to get current aircraft position!");
            } else {
                // cannot parse JSON when it starts with [
                if (output.charAt(0) == '[') {
                    output = output.substring(1);
                }

                // if there are waypoints and if there is a connection to MP...
                if ((output != null) && (output.charAt(0) != ']')) {
                    final JSONObject obj = new JSONObject(output);
                    final double alt = obj.getDouble("Alt");
                    final double lat = obj.getDouble("Lat");
                    final double lng = obj.getDouble("Lng");
                    final String ts = obj.getString("Ts");
                    final double yaw = obj.getDouble("Yaw");

                    final LocationWp newLocation = new LocationWp(lat, lng, alt, ts, yaw);
                    Data.setCurrentPosition(newLocation);
                }
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
    }

    /**
     * Gets wind data (by MP) via REST interface and gives it to
     * 
     * @return currentWindData
     */
    public static Wind getCurrentWindData() {
        try {
            String output = connectToMissionPlanner(Constants.REST_GET_WIND_DATA, true, null,
                    SamType.UNKNOWN);

            if (TRANSMITION_NO_OUTPUT.equals(output)) {
                LOG.warn("MP-Service Response: Not able to get wind data!");
            } else {
                // cannot parse JSON when it starts with [
                if (output.charAt(0) == '[') {
                    output = output.substring(1);
                }

                // if there are wind data and if there is a connection to MP...
                if ((output.charAt(0) != ']') && (output != null)) {
                    final JSONObject obj = new JSONObject(output);
                    final double windDirection = obj.getDouble("Direction");
                    final double windVelocity = obj.getDouble("Velocity");

                    final Wind windData = new Wind(windDirection, windVelocity);
                    return windData;
                }
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
        // return null if there was an issue or no data available
        return null;
    }

    /**
     *
     */
    public static void getNextPosition() {
        try {
            String output = connectToMissionPlanner(Constants.REST_GET_NEXT_POSITION, false, null,
                    SamType.UNKNOWN);

            if (TRANSMITION_NO_OUTPUT.equals(output)) {
                LOG.warn("MP-Service Response: Not able to get next waypoint!");
            } else {
                // cannot parse JSON when it starts with [
                if (output.charAt(0) == '[') {
                    output = output.substring(1);
                }

                // if there are waypoints and if there is a connection to MP...
                if ((output.charAt(0) != ']') && (output != null)) {
                    final JSONObject obj = new JSONObject(output);
                    final double alt = obj.getDouble("alt");
                    final double lat = obj.getDouble("lat");
                    final double lng = obj.getDouble("lng");
                    SamType nextTask = SamType.fromValue( obj.getInt("samType") );
                    Double distanceToNextWaypoint = obj.getDouble("distance");

                    final LocationWp newLocation = new LocationWp(lat, lng, alt, nextTask, distanceToNextWaypoint);
                    Data.setNextPosition(newLocation);
                }
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
        // return null if there was an issue or no data available
    }

    /**
     * Gets current obstacles (dynamic and static)
     */
    public static void getObstacles() {
        try {
            String output = connectToMissionPlanner(Constants.REST_GET_OBSTACLES, true, null,
                    SamType.UNKNOWN);

            // cannot parse JSON when it starts with [
            if (output.charAt(0) == '[') {
                output = output.substring(1);
            }

            // if there are waypoints and if there is a connection to MP...
            if ((output.charAt(0) != ']') && (output != null)) {
                final JSONObject obj = new JSONObject(output);

                // Parsing obstacles...
                ObstacleDynamic[] odA = null;
                final JSONArray array = (JSONArray) obj.get("MovingObstacles");
                odA = new ObstacleDynamic[array.length()];

                for (int i = 0; i < array.length(); i++) {
                    final JSONObject obj2 = (JSONObject) array.get(i);

                    odA[i] = new ObstacleDynamic(new LocationWp(obj2.getDouble("Latitude"),
                            obj2.getDouble("Longitude"), obj2.getDouble("Altitude")),
                            obj2.getDouble("SphereRadius"));
                }

                ObstacleStatic[] osA = null;
                final JSONArray array2 = (JSONArray) obj.get("StationaryObstacles");
                osA = new ObstacleStatic[array2.length()];

                for (int i = 0; i < array2.length(); i++) {
                    final JSONObject obj2 = (JSONObject) array2.get(i);

                    osA[i] = new ObstacleStatic(new LocationWp(obj2.getDouble("Latitude"),
                            obj2.getDouble("Longitude")), obj2.getDouble("CylinderHeight"),
                            obj2.getDouble("CylinderRadius"));
                }

                final Obstacles o = new Obstacles(osA, odA);

                Data.setCurrentObstacles(o);
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
    }

    /**
     * @param target
     *            to send.
     */
    public static void sendTarget(final Target target) {
        try {
            final String targetJSON = "{\"targets\":[" + target.toString() + "]}";
            LOG.info("Preparing to send JSON: " + targetJSON);

            final ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            final String output = connectToMissionPlanner(Constants.REST_SET_TARGETS, true,
                    targetJSON, target.getSAMType());

            if (TRANSMITION_SUCCESSFUL.equals(output)) {
                LOG.info("MP-Service Response: Set Targets successfully.");
            } else {
                LOG.warn("MP-Service Response: Set Targets NOT successfully.");
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
    }

    /**
     * @param waypoints
     *            to send.
     */
    public static void sendWaypoints(final Waypoints waypoints) {
        try {
            final String waypointsJSON = "{\"waypoints\":" + waypoints.toString() + "}";
            LOG.info("Preparing to send waypointJSON: " + waypointsJSON);

            final ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            final String output = connectToMissionPlanner(Constants.REST_SET_WAYPOINTS, true,
                    waypointsJSON, waypoints.getSAMType());

            if (TRANSMITION_SUCCESSFUL.equals(output)) {
                LOG.info("MP-Service Response: Set Waypoints successfully.");
            } else {
                LOG.warn("MP-Service Response: Set Waypoints NOT successfully.");
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
    }

    /**
     * @param zone
     *            to send.
     */
    public static void sendZone(final Zone zone) {
        try {
            final String zoneJSON = "{\"newZones\":[" + zone.toString() + "]}";
            LOG.info("Preparing to send waypointJSON: " + zoneJSON);

            final ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            final String output = connectToMissionPlanner(Constants.REST_SET_ZONES, true, zoneJSON,
                    zone.getSAMType());

            if (TRANSMITION_SUCCESSFUL.equals(output)) {
                LOG.info("MP-Service Response: Set Zones successfully.");
            } else {
                LOG.warn("MP-Service Response: Set Zones NOT successfully.");
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
    }

    /**
     * Function stops the loiter-waypoint in MissionPlanner, because SRIC task
     * shall be finished. Function needs a next waypoint in MP to finish
     * successfully action.
     */
    public static void stopSRICTask() {
        try {
            final String output = connectToMissionPlanner(Constants.REST_STOP_SRIC_TASK, true,
                    null, SamType.UNKNOWN);

            if (TRANSMITION_SUCCESSFUL.equals(output)) {
                LOG.info("MP-Service Response: MP stopped loiter as command.");
            } else {
                LOG.warn("MP-Service Response: MP didn't want to stop loiter progress!! Maybe there is no next waypoint?");
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
    }

    /**
     * Function checks if there is a connection to the Mission Planner (service)
     */
    public static void testRestConnection() {
        try {
            final String output = connectToMissionPlanner(Constants.REST_TEST_CONNECTION_TO_MP,
                    true, null, SamType.UNKNOWN);

            if (TRANSMITION_SUCCESSFUL.equals(output)) {
                LOG.info("MP-Service Response: REST-connection established.");
            } else {
                LOG.info("MP-Service Response: REST-connection NOT established.");
            }

        } catch (final Exception e) {
            LOG.error("REST ERROR: " + e.getMessage()
                    + " - Probably no REST service is running under THIS host, check host-data.", e);
        }
    }

    private static final Logger LOG = Logger.getLogger(RestClient.class.getName());

    private final static String TRANSMITION_SUCCESSFUL = "true";

    private final static String TRANSMITION_NO_OUTPUT = "";

    private RestClient() {
    }
}