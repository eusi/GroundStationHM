package edu.hm.sam.sensors;

import java.io.Serializable;
import java.util.Formatter;

import edu.hm.sam.Message;

public class SensorDataMessage extends Message implements Serializable {

    private double lat, lon;
    private float alt, rel_alt, heading, airspeed, groundspeed, roll, pitch, yaw;

    private static final String formatString = new String("Lat: %.6f Lon: %.6f Alt: %.1f Rel_Alt: %.1f Heading: %f AirS: %.1f GroundS: %.1f Roll: %.1f Pitch: %.1f Yaw: %.1f");
    private static StringBuilder b = new StringBuilder(250);
    private static Formatter f = new Formatter(b);


    public SensorDataMessage(double lat, double lon, float alt, float rel_alt, float heading, float airs, float grounds, float roll, float pitch, float yaw) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.rel_alt = rel_alt;
        this.heading = heading;
        this.airspeed = airs;
        this.groundspeed = grounds;
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public float getAlt() {
        return alt;
    }

    public void setAlt(float alt) {
        this.alt = alt;
    }

    public float getRel_alt() {
        return rel_alt;
    }

    public void setRel_alt(float rel_alt) {
        this.rel_alt = rel_alt;
    }

    public float getAirspeed() {
        return airspeed;
    }

    public void setAirspeed(float airspeed) {
        this.airspeed = airspeed;
    }

    public float getGroundspeed() {
        return groundspeed;
    }

    public void setGroundspeed(float groundspeed) {
        this.groundspeed = groundspeed;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public String toString() {
        b.setLength(0);
        return f.format(formatString, lat, lon, alt, rel_alt, heading, airspeed, groundspeed, roll, pitch, yaw).toString();

    }

}
