package cs.hm.edu.sam.mc.ir.main_ground;

import org.zeromq.ZMQ;



//PUBLISHER - SUBSCRIBER (!! TEST !!)
public class GroundAirPublisher extends Thread {
    private String whoami;
	private ZMQ.Context context = ZMQ.context(1);
	private String task = "IR_STAT";
    
    
    public GroundAirPublisher(String whoami) {
        this.whoami = whoami;
    }

    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:12345");
        int counter = 0;

        while (!Thread.currentThread ().isInterrupted ()) {
            System.out.println(("JEROMQ" + "Publisher " + whoami + ": " + counter));
            publisher.sendMore("A");
            publisher.send(Integer.toString(counter));
            publisher.sendMore("B");
            publisher.send(Long.toString(System.currentTimeMillis()));
            counter++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //publisher.close();
        //MyActivity.context.term();
    }
}


