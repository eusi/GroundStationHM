package edu.hm.sam.infrared;

import edu.hm.sam.Message;

import java.io.Serializable;

/**
 * @author Thomas Angermeier
 * @version 1.0
 */
public class InfraredPictureMessage extends Message implements Serializable {
    private byte[] picture;

    private String timestamp;

    public void setPictureTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPictureTimestamp() {
        return timestamp;
    }

    public void setInfraredPicture(byte[] picture) {
        this.picture = picture;
    }

    public byte[] getInfraredPicture() {
        return picture;
    }
}
