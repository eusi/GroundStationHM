package cs.hm.edu.sam.mc.misc;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Send waypoints and get current location RESTful / JSON.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

public class RESTClient {

	public static void getCurrentPosition()
	{
    	try {
    		Client client = Client.create();
     
    		WebResource webResource = client
    		   .resource("http://localhost:8000/MissionPlannerService/getCurrentPosition");
     
    		//TODO: Handle Connection refused 
    		ClientResponse response = webResource.accept("application/json")
                       .get(ClientResponse.class);
     
    		if (response.getStatus() != 200) {
    		   throw new RuntimeException("Failed : HTTP error code : "
    			+ response.getStatus());
    		}
     
    		String output = response.getEntity(String.class);
     
    		System.out.println("Output from Server ...");
    		System.out.println(output + "\n");
    		
    		//cannot parse JSON when it starts with [
    		if( output.charAt(0) == '[' )
    			output = output.substring(1);
    		
    		//if there are waypoints and if there is a connection to MP...
    		if( output.charAt(0) != ']' && output != null )
    		{
	    		JSONObject obj = new JSONObject(output);
	    		double lng = obj.getDouble("lng");
	    		double lat = obj.getDouble("lat");
	    		double alt = obj.getDouble("alt");
	    		
	    		Location newLocation = new Location(lng, lat, alt);
	    		Data.setCurrentPosition( newLocation );
    		}
     
    	  } catch (Exception e) 
    	{
    		e.printStackTrace();
     
    	  }
	}
	
	
	public static void sendWaypoints()
	{
		try {
			String waypointsJSON = "[" + Data.getWaypoints().toString() + "]";
			//String waypointsJSON = "[{\"alt\":0,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0},{\"alt\":1,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0},{\"alt\":2,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0},{\"alt\":3,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0},{\"alt\":4,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0}}]";
			//System.out.println(waypointsJSON);
			
			ClientConfig clientConfig = new DefaultClientConfig();

			clientConfig.getFeatures().put(
					JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

			Client client = Client.create(clientConfig);

			WebResource webResource = client
					.resource("http://localhost:8000/MissionPlannerService/setWaypoints");

			ClientResponse response = webResource.accept("application/json")
					.type("application/json").post(ClientResponse.class, waypointsJSON);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Server response ...");
			System.out.println(output + "\n");

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

}
