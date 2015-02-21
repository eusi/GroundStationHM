package edu.hm.sam.control;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.hm.sam.Message;

/**
 * HeartbeatMessage with information about state of service and running task.
 *
 * @author Christoph Kapinos
 * @author Sebastian Schuster
 */
public class HeartBeatMessage extends Message implements Serializable {

    public static final String HEARTBEAT = "heartbeat";
    public static final String SUMMARY = "summary";

    private ControlMessage.Task task = ControlMessage.Task.NO_TASK;
    private long lastMessageSent = -1;
    private long lastMessageReceived = -1;
    private ControlMessage.Service service;
    private String state = "UNDEFINED";
    private ControlMessage.Device device;
    private Map<ControlMessage.Service, HeartBeatMessage> summary = new HashMap<>();

    public HeartBeatMessage() {
    }

    ;

    public HeartBeatMessage(ControlMessage.Service service, String state, long lastMessageSent, long lastMessageReceived) {
        this.state = state;
        this.service = service;
        this.lastMessageReceived = lastMessageReceived;
        this.lastMessageSent = lastMessageSent;
    }

    public long getLastMessageSent() {
        return lastMessageSent;
    }

    public HeartBeatMessage setLastMessageSent(long lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
        return this;
    }

    public void setDevice(ControlMessage.Device device) {
        this.device = device;
    }

    public ControlMessage.Device getDevice() {
        return device;
    }

    public void setSummary(ControlMessage.Service service, HeartBeatMessage heartBeatMessage) {
        summary.put(service, heartBeatMessage);
    }

    public Map<ControlMessage.Service, HeartBeatMessage> getSummary() {
        return summary;
    }

    public long getLastMessageReceived() {
        return lastMessageReceived;
    }

    public HeartBeatMessage setLastMessageReceived(long lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
        return this;
    }

    public ControlMessage.Service getService() {
        return service;
    }

    public HeartBeatMessage setService(ControlMessage.Service service) {
        this.service = service;
        return this;
    }

    public String getState() {
        return state;
    }

    public HeartBeatMessage setState(String state) {
        this.state = state;
        return this;
    }

    public ControlMessage.Task getTask() {
        return task;
    }

    public void setTask(ControlMessage.Task task) {
        this.task = task;
    }

}
