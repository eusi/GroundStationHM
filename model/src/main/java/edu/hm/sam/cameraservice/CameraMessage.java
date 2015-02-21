package edu.hm.sam.cameraservice;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Alexander on 28.11.2014.
 */
public class CameraMessage extends Message implements Serializable {

    private byte[] imageData;
    private int zoomLevel;

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }
}

