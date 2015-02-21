package edu.hm.sam.sric;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Frederic on 05.12.2014.
 */
public class LoginFileDownloadOutput extends Message implements Serializable {
    public static final String TOPIC = "SRIC_READ_FILE_OUT";
    private byte[] loginFile;
    private String statusMessage;
    private boolean success = false;

    public LoginFileDownloadOutput() {
        super();
        setTopic(TOPIC);
    }

    public byte[] getLoginFile() {
        return loginFile;
    }

    public void setLoginFile(byte[] loginFile) {
        this.loginFile = loginFile;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
