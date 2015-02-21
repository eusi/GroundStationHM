package edu.hm.sam.sric;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Frederic on 05.12.2014.
 */
public class LoginFileDownloadInput extends Message implements Serializable {
    public static final String TOPIC = "SRIC_READ_FILE_IN";
    private String filename;

    public LoginFileDownloadInput() {
        super();
        setTopic(TOPIC);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
