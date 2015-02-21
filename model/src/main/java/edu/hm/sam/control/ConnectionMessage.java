package edu.hm.sam.control;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Contains ControlMessage and ConnectionState.
 * @author Christoph Kapinos, Sebastian Schuster
 */
public class ConnectionMessage extends Message implements Serializable {
    public enum State {
        CONNECT, DISCONNECT, HEARTBEAT
    }

    private State state;
    private ControlMessage message;

    public ConnectionMessage(State state, ControlMessage message) {
        this.state = state;
        this.message = message;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ControlMessage getMessage() {
        return message;
    }

    public void setMessage(ControlMessage message) {
        this.message = message;
    }
}
