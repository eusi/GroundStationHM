package cs.hm.edu.sam.mc.misc;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Create RESTful service.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

@Path("/json")
public class RESTService {
	
    @POST
    @Path("/getCurrentLocation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCurrentLocation(InputStream incomingData) {
        StringBuilder crunchifyBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                crunchifyBuilder.append(line);
            }
        } catch (Exception e) {
            System.out.println("Error Parsing: - ");
        }
        System.out.println("Data Received: " + crunchifyBuilder.toString());
 
        // return HTTP response 200 in case of success
        return Response.status(200).entity(crunchifyBuilder.toString()).build();
    }
    
    @GET
    @Path("/setWaypoints")
    @Produces(MediaType.APPLICATION_JSON)
    public String setWaypoints() {

        return Data.getCurrentPosition().toString();

    }
    
}
