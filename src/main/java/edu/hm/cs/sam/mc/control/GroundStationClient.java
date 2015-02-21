package edu.hm.cs.sam.mc.control;


import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import edu.hm.sam.Message;
import edu.hm.sam.MessageHolder;
import edu.hm.sam.control.ControlMessage;
import edu.hm.sam.control.ControlMessage.Service;
import edu.hm.sam.control.HeartBeatMessage;
import edu.hm.sam.control.SubscriptionHandler;

/**
 * Class for receiving/sending messages to devices on drone.
 *
 * @author Christoph Kapinos
 * @author Sebastian Schuster
 */
public class GroundStationClient {

    private ZMQ.Context zmqContext = ZMQ.context(1);
    private Map<String, SubscriptionHandler> subscriptionHandlers;
    private OutputHandler outputHandler;
    private InputHandler inputHandler;
    private static GroundStationClient client;
    private Map<Service, List<ZMQ.Socket>> sockets = new HashMap<>();
    private Boolean hasInitialized = false;

    private GroundStationClient() {
        subscriptionHandlers = new ConcurrentHashMap<>();
        outputHandler = new OutputHandler();
        inputHandler = new InputHandler();
        outputHandler.start();
        inputHandler.start();
    }

    /**
     * Returns singleton GroundStationClient-Object
     *
     * @return
     */
    public static GroundStationClient getInstance() {
        if (client == null)
            client = new GroundStationClient();
        return client;
    }


    /**
     * Sends ControlMessage to given service.
     *
     * @param service Servicename
     * @param topic   topicname
     * @param message parameters
     */
    public void sendAction(Service service, String topic, Message message) {
        try {
            outputHandler.queue.put(new MessageHolder(MessageHolder.Channel.ACTION, service.getServiceName() + "." + topic, message));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles incoming messages.
     *
     * @param service             servicename
     * @param topic               topicname
     * @param subscriptionHandler individual subscriptionhandler for handling information in message
     */
    public void addSubscriptionHandler(Service service, String topic, SubscriptionHandler<?> subscriptionHandler) {
        synchronized (GroundStationClient.this) {
            while (!hasInitialized) {
                try {
                    GroundStationClient.this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //Subscribe to topic at devices
        for (ZMQ.Socket socket : sockets.get(service)) {
            System.out.println(service.getServiceName() + "." + topic + " Handler");
            socket.subscribe((service.getServiceName() + "." + topic).getBytes());
        }
        subscriptionHandlers.put(service.getServiceName() + "." + topic, subscriptionHandler);
    }


    /**
     * Sends messages in qeueue.
     *
     * @author Christoph Kapinos
     * @author Sebastian Schuster
     */
    class OutputHandler extends Thread {
        ZMQ.Socket outputSockets;
        Service name;
        public LinkedBlockingQueue<MessageHolder> queue;

        public void run() {
            onSetup();

            while (!this.isInterrupted()) {
                try {
                    MessageHolder msg = queue.take();
                    outputSockets.sendMore(msg.getTopic());
                    outputSockets.send(msg.getMessage().serialize());

                } catch (InterruptedException e) {
                    System.out.println("GS-Client konnte keine Message aus Queue lesen");
                }
            }
        }

        private void onSetup() {
            queue = new LinkedBlockingQueue<>();
            outputSockets = zmqContext.socket(ZMQ.PUB);
            outputSockets.connect("tcp://" + ControlMessage.Device.SMARTPHONE.getIP() + ":" + (Service.CONTROLLER.getStartPort() + 1));
            outputSockets.connect("tcp://" + ControlMessage.Device.ODROID.getIP() + ":" + (Service.CONTROLLER.getStartPort() + 1));
        }
    }

    /**
     * Receives messages.
     *
     * @author Christoph Kapinos
     * @author Sebastian Schuster
     */
    class InputHandler extends Thread {
        List<ZMQ.Socket> inputSocket;
        ZMQ.Poller poller;
        Message message = null;

        /**
         * initialize class.
         */
        private void onSetup() {
            poller = new ZMQ.Poller(100);

            ZMQ.Socket socket = zmqContext.socket(ZMQ.SUB);
            for (Service service : Service.class.getEnumConstants()) {
                //init map
                sockets.put(service, new ArrayList<ZMQ.Socket>());

                socket.connect("tcp://" + ControlMessage.Device.SMARTPHONE.getIP() + ":" + service.getStartPort());
                poller.register(socket, ZMQ.Poller.POLLIN);
                sockets.get(service).add(socket);

                socket.connect("tcp://" + ControlMessage.Device.ODROID.getIP() + ":" + service.getStartPort());
                poller.register(socket, ZMQ.Poller.POLLIN);
                sockets.get(service).add(socket);
            }

            synchronized (GroundStationClient.this) {
                hasInitialized = true;
                GroundStationClient.this.notify();
            }
        }

        public void run() {
            onSetup();
            //addHeartBeatListener();

            while (!this.isInterrupted()) {
                poller.poll(10000);

                for (int i = 0; i < poller.getNext(); i++) {
                    if (poller.pollin(i)) {
                        String topic = poller.getSocket(i).recvStr();

                        try {
                            message = Message.deserialize(poller.getSocket(i).recv());
                        } catch (Exception e) {
                            System.out.println("Deserilization failed for topic " + topic);

                            //Ignore this message
                            continue;
                        }

                        message.setTopic(topic);
                        if (subscriptionHandlers.containsKey(message.getTopic())) {
                            subscriptionHandlers.get(message.getTopic()).handleSubscription(message);
                            System.out.println("Händler für " + message.getTopic() + " aufgerufen");
                        } else {
                            System.out.println("Kein GS-Händler gefunden für" + topic);
                        }
                    }
                }
            }
        }
    }

    /**
     * SubscriptionHandler for received Heartbeatmessages.
     */
    private void addHeartBeatListener() {
        addSubscriptionHandler(Service.CONTROLLER, HeartBeatMessage.SUMMARY, new SubscriptionHandler<HeartBeatMessage>() {
            @Override
            public void handleSubscription(HeartBeatMessage subscription) {
                Map<Service, HeartBeatMessage> summary = subscription.getSummary();
                for (Service service : summary.keySet()) {
                    System.out.println("Service: " + service + " STATUS: " + summary.get(service).getState() + " " + summary.get(service).getLastMessageSent());
                }
            }
        });
    }
}

