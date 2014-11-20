package cs.hm.edu.sam.mc.rest;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import cs.hm.edu.sam.mc.misc.CONSTANTS;
import cs.hm.edu.sam.mc.misc.Data;
import cs.hm.edu.sam.mc.misc.Location;
import cs.hm.edu.sam.mc.misc.Waypoints;

/**
 * Send waypoints and get current location RESTful / JSON.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

public class RESTClient {

    public static void getCurrentPosition() {
        try {
            Client client = Client.create();

            WebResource webResource = client.resource(CONSTANTS.REST_MP + "/getUAVPosition");

            // TODO: Handle Connection refused
            ClientResponse response = webResource.accept("application/json").get(
                    ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            String output = response.getEntity(String.class);

            System.out.println("Output from Server ...");
            System.out.println(output + "\n");

            // cannot parse JSON when it starts with [
            if (output.charAt(0) == '[')
                output = output.substring(1);

            // if there are waypoints and if there is a connection to MP...
            if (output.charAt(0) != ']' && output != null) {
                JSONObject obj = new JSONObject(output);
                double lng = obj.getDouble("Lng");
                double lat = obj.getDouble("Lat");
                double alt = obj.getDouble("Alt");
                String ts = obj.getString("Ts");

                Location newLocation = new Location(lng, lat, alt, ts);
                Data.setCurrentPosition(newLocation);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    //@param like Data.getIrWaypoints()
    public static void sendWaypoints(Waypoints waypoints) {
        try {
            String waypointsJSON = "{\"waypoints\":[" + waypoints.toString()
                    + "], \"append\":1, \"name\":" + waypoints.getWaypointListName()
                    + "}";

            ClientConfig clientConfig = new DefaultClientConfig();

            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            Client client = Client.create(clientConfig);

            WebResource webResource = client.resource(CONSTANTS.REST_MP + "/setWaypoints");

            ClientResponse response = webResource.accept("application/json")
                    .type("application/json").post(ClientResponse.class, waypointsJSON);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            String output = response.getEntity(String.class);

            System.out.println("Server response ...");
            System.out.println(output + "\n");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}
