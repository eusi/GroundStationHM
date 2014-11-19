package cs.hm.edu.sam.mc.ir.main_ground;

import org.zeromq.ZMQ;

	
//PUBLISHER - SUBSCRIBER (!! TEST !!)
public class GroundAirSubscriber extends Thread {
	
	private ZMQ.Context context = ZMQ.context(1);
	private String task = "IR_STAT";
	
    String whoami;
    int lag = 0;
    String[] topics;

    public GroundAirSubscriber(String whoami) {
        this.whoami = whoami;
    }

    public GroundAirSubscriber(String whoami, int lag, String[] topics) {
        this.whoami = whoami;
        this.lag = lag;
        this.topics = topics;
    }


    public void run() {
    	ZMQ.Socket publisher = context.socket(ZMQ.SUB);
            for(String topic : topics)
                publisher.subscribe(topic.getBytes());
            publisher.connect("tcp://localhost:12345");

        while (!Thread.currentThread ().isInterrupted ()) {
                String topic = publisher.recvStr();
                String content = publisher.recvStr();
                System.out.println(("JEROMQ" + "Subscriber " + whoami + ":" + topic + " Content: " + content));
                try {
                    Thread.sleep(lag);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //publisher.close();
            //MyActivity.context.term();
    }
}
	
