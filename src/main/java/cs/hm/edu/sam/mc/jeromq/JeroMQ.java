package cs.hm.edu.sam.mc.jeromq;

import org.jeromq.ZMQ;
import org.jeromq.ZMQ.Context;
import org.jeromq.ZMQ.Socket;

/**
 * JeroMQ communication interface to the android devices.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class JeroMQ {
  
    static final String TOPIC = "test";
	
    //response-test
	public boolean recTest()
	{
		boolean success = false;
		
		Context context = ZMQ.context(1);
	    Socket socket = context.socket(ZMQ.REP);
		socket.bind("tcp://127.0.0.1:5555");
		
        byte[] raw = socket.recv(0);
        String rawMessage = new String(raw);
        success = rawMessage != null;
        System.out.println(rawMessage);
        
        if (!success) {
            System.out.println("getting message failed!");
        }
        
        return success;
	}
	
	//request-test
	public boolean resTest()
	{
		boolean success = false;
		
		Context context = ZMQ.context(1);
	    Socket socket = context.socket(ZMQ.REQ);
		socket.connect("tcp://localhost:5555");
		
		String requestString = "message";
        byte[] request = requestString.getBytes();
        success = socket.send(request, 0);
        
        if (!success) {
            System.out.println("sending message failed!");
        }
        
        return success;
	}
	
	//publish-test
	public boolean pubTest()
	{
		boolean success = false;
		
		Context context = ZMQ.context(1);
        Socket publisher = context.socket(ZMQ.PUSH);
        publisher.bind("tcp://127.0.0.1:5563");
        
        publisher.setHWM(1000000);
        publisher.setSndHWM(1000000);
        
        publisher.sendMore(TOPIC);
        success = publisher.send("message number ");
        
        if (!success) {
            System.out.println("getting message failed!");
        }
        
        return success;
	}
	
	//subscribe-test
	public boolean subTest()
	{
		boolean success = false;
		
	    Context context = ZMQ.context(1);
	    ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://127.0.0.1:5563");
        
        subscriber.setRcvHWM(1000000);
        subscriber.subscribe(TOPIC.getBytes());
        
        String msg = subscriber.recvStr();
        success = msg != null;
        System.out.println(msg);
        
        if (!success) {
            System.out.println("sending message failed!");
        }
        
        return success;
	}
	
	public int takePhoto()
	{
		return 0;
	}
	
	public int getPicture()
	{
		return 0;
	}
	
	public int sendWlanData()
	{
		return 0;
	}
	
	public String getFtpData()
	{
		return null;
	}
	
	public int sendFtpData()
	{
		return 0;
	}
	
}