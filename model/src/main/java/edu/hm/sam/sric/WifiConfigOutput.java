package edu.hm.sam.sric;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Frederic on 05.12.2014.
 */
public class WifiConfigOutput extends Message implements Serializable {
    public static final String TOPIC = "SRIC_WIFI_OUT";
    private String statusMessage = "No Status Message";

    public WifiConfigOutput() {
        super();
        setTopic(TOPIC);
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
