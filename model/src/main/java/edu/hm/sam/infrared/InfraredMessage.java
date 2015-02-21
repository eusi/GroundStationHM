package edu.hm.sam.infrared;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * @author Thomas Angermeier
 * @version 1.0
 */
public class InfraredMessage extends Message implements Serializable {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
