package edu.hm.sam.sric;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Frederic on 10.11.2014.
 */
public class ImageUploadOutput extends Message implements Serializable {
    public static final String TOPIC = "SRIC_WRITE_OUT";
    private boolean success;
    private String statusMessage;

    public ImageUploadOutput() {
        super();
        setTopic(TOPIC);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
