package edu.hm.sam.sric;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Frederic on 10.11.2014.
 */
public class GetFileListInput extends Message implements Serializable {
    public static final String TOPIC = "SRIC_READ_LIST_IN";
    private String ftpAddress;
    private String username;
    private String password;
    private String folder;

    public GetFileListInput() {
        super();
        setTopic(TOPIC);
    }

    public String getFtpAddress() {
        return ftpAddress;
    }

    public void setFtpAddress(String ftpAddress) {
        this.ftpAddress = ftpAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
