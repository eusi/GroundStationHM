package edu.hm.sam.control;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Acknowledgment about received ControlMessage.
 * @author Christoph Kapinos
 * @author Sebastian Schuster
 */
public class ControlMessageReply extends Message implements Serializable {
    private ControlMessage.Task task;
    private int state;

    public ControlMessageReply(ControlMessage.Task task, int state) {
        this.task = task;
        this.state = state;
    }

    public ControlMessage.Task getTask() {
        return task;
    }

    public void setTask(ControlMessage.Task task) {
        this.task = task;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        state = state;
    }
}
