package edu.hm.cs.sam.mc.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Opitz, Jan (jopitz@hm.edu)
 * @version 04.01.2015 - 17:04:21
 */
@SuppressWarnings("serial")
public class Settings implements Serializable {

    /**
     * getter for current postion delay
     *
     * @return the currentPositionDelay
     */
    public static int getCurrentPositionDelay() {
        return currentPositionDelay;
    }

    /**
     * getter for image folder.
     *
     * @return imgFldr.
     */
    public static String getImgFldr() {
        return imgFldr;
    }

    /**
     * getter for polling delay
     *
     * @return the picturePollingDelay
     */
    public static int getPicturePollingDelay() {
        return picturePollingDelay;
    }

    /**
     * getter for mission planner ip.
     *
     * @return restMp.
     */
    public static String getRestMp() {
        return restMp;
    }

    /**
     * getter for mission planner url.
     *
     * @return restMpUrl.
     */
    public static String getRestMpUrl() {
        return restMpUrl;
    }

    /**
     * getter for report image folder.
     *
     * @return rprtImgFldr.
     */
    public static String getRprtImgFldr() {
        return rprtImgFldr;
    }

    /**
     * getter for report save folder.
     *
     * @return rprtSvFldr.
     */
    public static String getRprtSvFldr() {
        return rprtSvFldr;
    }

    /**
     * getter for routing folder.
     *
     * @return routingFlrd.
     */
    public static String getRtgFldr() {
        return rtgFldr;
    }

    /**
     * getter for search area folder
     *
     * @return srchAreaFldr.
     */
    public static String getSrchAreaFldr() {
        return srchAreaFldr;
    }

    /**
     * getter for save folder.
     *
     * @return svFldr.
     */
    public static String getSvFldr() {
        return svFldr;
    }

    /**
     * @return tmpImgFldr.
     */
    public static String getTmpImgFldr() {
        return tmpImgFldr;
    }

    /**
     * loading settings.
     *
     * @param log
     *            log to log.
     */
    public static void load(final Logger log) {
        final Properties props = new Properties();
        final File file = new File(Settings.getSvFldr() + "Settings.properties");
        if (file.exists()) {
            try {
                final InputStream is = new FileInputStream(file);
                props.load(is);
            } catch (final FileNotFoundException e) {
                log.error("Settings.properties could not be found.", e);
            } catch (final IOException e) {
                log.error("Settings.properties could not be loaded.", e);
            }
            setRestMP(props.getProperty("restMp", "192.168.1.4:8000"));
            setImgFldr(props.getProperty("imgFldr", Constants.USER_PROFILE + "\\SAM\\Images\\"));
            setRprtImgFldr(props.getProperty("rprtImgFldr", Constants.USER_PROFILE
                    + "\\SAM\\Report Images\\"));
            setRprtSvFldr(props.getProperty("rprtSvFldr", Constants.USER_PROFILE
                    + "\\SAM\\Report\\"));
            setRtgFldr(props.getProperty("rtgFldr", Constants.USER_PROFILE + "\\SAM\\Routing\\"));
            setSrchAreaFldr(props.getProperty("srchAreaFldr", Constants.USER_PROFILE
                    + "\\SAM\\SearchArea\\"));
            setSvFldr(props.getProperty("svFldr", Constants.USER_PROFILE + "\\SAM\\Save\\"));
            setCurrentPositionDelay(new Integer(
                    props.getProperty("currentPositionDelay", "" + 1000)));
            setPicturePollingDelay(new Integer(props.getProperty("picturePollingDelay", "" + 2000)));
            log.info("Settings loaded");
        }
    }

    /**
     * saving settings.
     *
     * @param log
     *            log to log.
     */
    public static void save(final Logger log) {
        final Properties props = new Properties();
        props.setProperty("restMp", restMp);
        props.setProperty("imgFldr", imgFldr);
        props.setProperty("rprtImgFldr", rprtImgFldr);
        props.setProperty("rprtSvFldr", rprtSvFldr);
        props.setProperty("rtgFldr", rtgFldr);
        props.setProperty("svFldr", svFldr);
        props.setProperty("srchAreaFldr", srchAreaFldr);
        props.setProperty("currentPositionDelay", "" + currentPositionDelay);
        props.setProperty("picturePollingDelay", "" + picturePollingDelay);
        final File file = new File(Settings.getSvFldr() + "Settings.properties");
        try {
            final OutputStream os = new FileOutputStream(file);
            props.store(os, "Settings");
        } catch (final FileNotFoundException e) {
            log.error("Settings.properties could not be found.", e);
        } catch (final IOException e) {
            log.error("Settings.properties could not be saved.", e);
        }
        log.info("Settings saved");
    }

    /**
     * setter for current position delay
     *
     * @param currentPositionDelay
     *            the currentPositionDelay to set.
     */
    public static void setCurrentPositionDelay(final int currentPositionDelay) {
        Settings.currentPositionDelay = currentPositionDelay;
    }

    /**
     * setter for image folder.
     *
     * @param imgFldr
     *            path to new image folder.
     */
    public static void setImgFldr(final String imgFldr) {
        Settings.imgFldr = imgFldr;
    }

    /**
     * setter for polling delay
     *
     * @param picturePollingDelay
     *            the picturePollingDelay to set
     */
    public static void setPicturePollingDelay(final int picturePollingDelay) {
        Settings.picturePollingDelay = picturePollingDelay;
    }

    /**
     * setter for address to mission planner.
     *
     * @param restMp
     *            new ip for mission planner.
     */
    public static void setRestMP(final String restMp) {
        Settings.restMp = restMp;
        Settings.restMpUrl = "http://" + restMp + "/MissionPlannerService";
    }

    /**
     * setter for report image folder.
     *
     * @param rprtImgFldr
     *            path to new report image folder.
     */
    public static void setRprtImgFldr(final String rprtImgFldr) {
        Settings.rprtImgFldr = rprtImgFldr;
    }

    /**
     * setter for report save folder.
     *
     * @param rprtSvFldr
     *            path to new report save folder.
     */
    public static void setRprtSvFldr(final String rprtSvFldr) {
        Settings.rprtSvFldr = rprtSvFldr;
    }

    /**
     * setter for routing folder.
     *
     * @param rtgFldr
     *            path to new routing folder.
     */
    public static void setRtgFldr(final String rtgFldr) {
        Settings.rtgFldr = rtgFldr;
    }

    /**
     * sette for sarch area folder
     *
     * @param srchAreaFldr
     *            path to new search area folder.
     */
    public static void setSrchAreaFldr(final String srchAreaFldr) {
        Settings.srchAreaFldr = srchAreaFldr;
    }

    /**
     * setter for save folder.
     *
     * @param svFldr
     *            path to new save folder.
     */
    public static void setSvFldr(final String svFldr) {
        Settings.svFldr = svFldr;
    }

    // if you want to change the following "userprofile"-paths, just also edit
    // "GPS table log file path" in Constants as well as log-path in
    // log4j.properties
    private static String tmpImgFldr = Constants.USER_PROFILE + "\\SAM\\TempImages\\";

    private static String restMp = "192.168.1.4:8000";

    // if you want to change the following "userprofile"-paths, just also edit
    // "GPS table log file path" in Constants as well as log-path in
    // log4j.properties
    private static String imgFldr = Constants.USER_PROFILE + "\\SAM\\Images\\";

    private static String rprtImgFldr = Constants.USER_PROFILE + "\\SAM\\Report Images\\";

    private static String rprtSvFldr = Constants.USER_PROFILE + "\\SAM\\Report\\";

    private static String restMpUrl = "http://" + restMp + "/MissionPlannerService";

    private static String rtgFldr = Constants.USER_PROFILE + "\\SAM\\Routing\\";

    private static String svFldr = Constants.USER_PROFILE + "\\SAM\\Save\\";

    private static String srchAreaFldr = Constants.USER_PROFILE + "\\SAM\\SearchArea\\";

    private static int currentPositionDelay = 1000;

    private static int picturePollingDelay = 2000;

    private Settings() {

    }
}
