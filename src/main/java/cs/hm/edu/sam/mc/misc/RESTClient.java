package cs.hm.edu.sam.mc.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.codehaus.jettison.json.JSONObject;

/**
 * Send waypoints and get current location RESTful / JSON.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

public class RESTClient {

	public void getCurrentLocation()
	{
		Location loc = new Location(1.2, 2.2);
		
        try {
 
            JSONObject jsonObject = new JSONObject( loc.toString() );
            System.out.println(jsonObject);
 
            try {
                URL url = new URL("http://localhost:8080/...");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();
 
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
 
                while (in.readLine() != null) {
                }
                System.out.println("\nREST Service Invoked Successfully..");
                in.close();
            } catch (Exception e) {
                System.out.println("\nError while calling REST Service");
                System.out.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	
	public void sendWaypoints()
	{
		/*try {

			String string = "[{\"alt\":0,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0},{\"alt\":1,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0},{\"alt\":2,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0},{\"alt\":3,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0},{\"alt\":4,\"id\":16,\"lat\":2,\"lng\":3,\"options\":1,\"p1\":0,\"p2\":0,\"p3\":0,\"p4\":0}}]";

			ClientConfig clientConfig = new DefaultClientConfig();

			clientConfig.getFeatures().put(
					JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

			Client client = Client.create(clientConfig);

			WebResource webResource = client
					.resource("http://localhost:9090/...");

			ClientResponse response = webResource.accept("application/json")
					.type("application/json").post(ClientResponse.class, string);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Server response .... \n");
			System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}*/
	}

}
