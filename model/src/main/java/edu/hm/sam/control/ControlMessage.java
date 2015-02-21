package edu.hm.sam.control;

import edu.hm.sam.Message;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Information(IP-Address, onAir) about used devices. Information(Port, Size of receiveQueue, sendQeue) about used services.
 * @author Christoph Kapinos
 * @author Sebastian Schuster
 */
public class ControlMessage extends Message implements Serializable {

    public static enum Device implements Serializable {

        // Ips für Aufbau
        // SMARTPHONE("192.168.1.6", true), ODROID("192.168.1.10", true),
        // GROUNDSTATION(false);
        SMARTPHONE("192.168.1.6", true), ODROID("192.168.1.10", true), GROUNDSTATION(false);

        private String ip = "127.0.0.1";
        private final boolean onAir;

        Device(final boolean onAir) {
            this.onAir = onAir;
        }

        Device(final String ip, final boolean onAir) {
            this.ip = ip;
            this.onAir = onAir;
        }

        public String getIP() {
            return ip;
        }

        public boolean isOnAir() {
            return onAir;
        }
    }

    public enum Service {
        CONTROLLER(10000, 100, 100), CAMERA(10100, 10, 100), IRCAMERA(10200, 100, 20), SEARCHAREA(
                10300, 100, 20), OFFAXIS(10400, 20, 20), LOGGER(10500, 100, 100), SENSORDATA(10600,
                100, 100), SRIC(10700, 100, 100), EMERGENT(10800, 100, 100);

        private final int startPort;

        private final int maxRecvQueue;
        private final int maxSendQueue;

        Service(final int startPort, final int maxSendQueue, final int maxRecvQueue) {
            this.startPort = startPort;
            this.maxSendQueue = maxSendQueue;
            this.maxRecvQueue = maxRecvQueue;
        }

        public int getMaxRecvQueue() {
            return maxRecvQueue;
        }

        public int getMaxSendQueue() {
            return maxSendQueue;
        }

        public String getServiceName() {
            return name().toLowerCase();
        }

        public int getStartPort() {
            return startPort;
        }
    }

    public static enum State {
        STARTSERVICE
    }

    /**
     * Information about Tasks and their needed Services.
     */
    public static enum Task implements Serializable {
        NO_TASK,

        SEARCHAREA_STANDARD_TASK(Service.CAMERA, Service.SEARCHAREA, Service.SENSORDATA),

        SEARCHAREA_QR_TASK(Service.CAMERA), OFFAXIS_TASK(Service.OFFAXIS, Service.CAMERA), INFRARED_STATIC_TASK(
                Service.IRCAMERA, Service.CAMERA), INFRARED_DYNAMIC_TASK(Service.IRCAMERA), AIRDROP_TASK(), SRIC_TASK(Service.SRIC),
        EMERGENT_TASK(Service.CAMERA);

        private final List<Service> services;

        Task(final Service... services) {
            this.services = Arrays.asList(services);
        }

        public List<Service> getServices() {
            return services;
        }

    }

    private Task task = Task.NO_TASK;
    private State state = State.STARTSERVICE;

    private final Map<Service, Device> assignment = new HashMap<>();

    public ControlMessage(final Task task) {
        setTask(task);
    }

    public void clearAssignments() {
        assignment.clear();
    }

    public Device getAssignment(final Service service) {
        return assignment.get(service);
    }

    public State getState() {
        return state;
    }

    public Task getTask() {
        return task;
    }

    public void setAssignment(final Service service, final Device device) {
        assignment.put(service, device);
    }

    public void setState(final State state) {
        this.state = state;
    }

    public void setTask(final Task task) {
        this.task = task;
    }
}
