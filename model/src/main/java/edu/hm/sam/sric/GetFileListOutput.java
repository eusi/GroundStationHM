package edu.hm.sam.sric;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Frederic on 10.11.2014.
 */
public class GetFileListOutput extends Message implements Serializable {
    public static final String TOPIC = "SRIC_READ_LIST_OUT";
    private boolean success = false;
    private String[] fileList;
    private String statusMessage;

    public GetFileListOutput() {
        super();
        setTopic(TOPIC);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String[] getFileList() {
        return fileList;
    }

    public void setFileList(String[] fileList) {
        this.fileList = fileList;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
