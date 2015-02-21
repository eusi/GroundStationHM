package edu.hm.sam.cameraservice;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Alexander on 03.12.2014.
 */
public class CameraSettingsMessage extends Message implements Serializable {

    public enum CameraSetting implements Serializable {
        CameraSettingResolution,
        CameraSettingISO,
        CameraSettingShutterSpeed,
        CameraSettingFrequency,
        CameraSettingZoom,
        CameraSettingAutofocusMode,
        CameraSettingAutofocus,
        CameraSettingCaptureMode,
        CameraSettingJPEGQuality
    }

    CameraSetting cameraSetting;

    int intValue1;
    int intValue2;
    boolean boolValue;

    String stringValue1;
    String stringValue2;

    public int getIntValue1() {
        return intValue1;
    }

    public int getIntValue2() {
        return intValue2;
    }

    public String getStringValue1() {
        return stringValue1;
    }

    public String getStringValue2() {
        return stringValue2;
    }

    public void setIntValue1(int intValue1) {
        this.intValue1 = intValue1;
    }

    public void setIntValue2(int intValue2) {
        this.intValue2 = intValue2;
    }

    public void setStringValue1(String stringValue1) {
        this.stringValue1 = stringValue1;
    }

    public void setStringValue2(String stringValue2) {
        this.stringValue2 = stringValue2;
    }

    public CameraSetting getCameraSetting() {
        return cameraSetting;
    }

    public void setCameraSetting(CameraSetting cameraSetting) {
        this.cameraSetting = cameraSetting;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public boolean getBoolValue() {
        return this.boolValue;
    }
}
