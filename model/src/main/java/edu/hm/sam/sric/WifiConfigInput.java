package edu.hm.sam.sric;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Frederic on 10.11.2014.
 */

public class WifiConfigInput extends Message implements Serializable {
    public static final String TOPIC = "SRIC_WIFI_IN";
    private String ssid;
    private String password;

    public WifiConfigInput() {
        super();
        setTopic(TOPIC);
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
