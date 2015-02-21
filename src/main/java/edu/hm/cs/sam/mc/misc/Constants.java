package edu.hm.cs.sam.mc.misc;

import java.awt.Color;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Some constants are stored in this variable. An (later) options-ui could
 * change this settings.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class Constants {

    /**
     * user profile
     */
    public static final String USER_PROFILE = System.getenv("USERPROFILE");

    /**
     * Default size for a JInternalFrame.
     */
    public static final int FRAME_DEFAULT_SIZE = 500;

    /**
     * Default size for Options-JInternalFrame.
     */
    public static final int OPTIONS_DEFAULT_SIZE = 500;
    /**
     * Placeholder if a image for a button cant be loaded.
     */
    public static final MissingIcon PLACEHOLDER_ICON = new MissingIcon();

    /**
     * folder for images
     */
    public static final String IMAGE_DIR = "/images/";

    /**
     * folder for report images
     */
    public static final String REPORT_DIR = "/images/report/";

    /**
     * folder for icons
     */
    public static final String ICON_DIR = "/icons/";

    /**
     * SAM logo original
     */
    public static final String LOGO_ORI = ICON_DIR + "sam_logo.jpg";

    /**
     * SAM cloud
     */
    public static final String IMG_LOC_CLOUD = ICON_DIR + "sam_bg_cloud.png";

    /**
     * SAM plane
     */
    public static final String IMG_LOC_PLANE = ICON_DIR + "sam_bg_plane.png";

    /**
     * SAM title
     */
    public static final String IMG_LOC_TITLE = ICON_DIR + "sam_bg_title.png";

    /**
     * SAM MC title
     */
    public static final String IMG_LOC_TITLE_MC = ICON_DIR + "sam_bg_title_mc.png";

    /**
     * SAM background
     */
    public static final String IMG_LOC_BG = ICON_DIR + "sam_bg.png";

    /**
     * folder for zones
     */
    public static final String ZONE_DIR = "/zones/";

    /**
     * direction to images
     */
    public static final String IMAGE_DIR_FILE = "./src/main/resources" + IMAGE_DIR;

    /**
     * direction to report images
     */
    public static final String REPORT_DIR_FILE = "./src/main/resources" + REPORT_DIR;

    /**
     * direction to zones
     */
    public static final String ZONE_DIR_FILE = "./src/main/resources" + ZONE_DIR;

    /**
     * GPS table log file path
     */
    public static final String GPS_TABLE_LOG = Constants.USER_PROFILE + "/SAM/Logs/gpsTable.log";

    /**
     * Color red with 255 alpha
     */
    public static final Color RED = new Color(255, 0, 0, 255);

    /**
     * Color blue with 255 alpha
     */
    public static final Color BLUE = new Color(0, 0, 255, 255);

    /**
     * Color green with 255 alpha
     */
    public static final Color GREEN = new Color(0, 128, 0, 255);

    /**
     * Color black with 255 alpha
     */
    public static final Color BLACK = new Color(0, 0, 0, 255);

    /**
     * Color white with 255 alpha
     */
    public static final Color WHITE = new Color(255, 255, 255, 255);

    /**
     * Color red with 100 alpha
     */
    public static final Color RED_A100 = new Color(255, 0, 0, 100);

    /**
     * Color blue with 100 alpha
     */
    public static final Color BLUE_A100 = new Color(0, 0, 255, 100);

    /**
     * Color green with 100 alpha
     */
    public static final Color GREEN_A100 = new Color(0, 128, 0, 100);

    /**
     * Color black with 100 alpha
     */
    public static final Color BLACK_A100 = new Color(0, 0, 0, 100);

    /**
     * Color white with 100 alpha
     */
    public static final Color WHITE_A100 = new Color(255, 255, 255, 100);

    /**
     * Color SAM blue
     */
    public static final Color BLUE_SAM = new Color(143, 217, 254, 255);
    /**
     * Icon for report sheet frame
     */
    public static final Icon REPORT_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "report_icon_mini.png"));

    /**
     * Icon for report sheet frame
     */
    public static final Icon REPORT_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "report_icon.png"));

    /**
     * Icon for image viewer frame
     */
    public static final Icon IMAGE_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "image_icon_mini.png"));

    /**
     * Icon for image viewer frame
     */
    public static final Icon IMAGE_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "image_icon.png"));
    /**
     * Icon for airdrop frame
     */
    public static final Icon AIRDROP_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "airdrop_icon_mini.png"));

    /**
     * Icon for airdrop frame
     */
    public static final Icon AIRDROP_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "airdrop_icon.png"));

    /**
     * Icon for camera frame
     */
    public static final Icon CAMERA_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "camera_icon_mini.png"));

    /**
     * Icon for camera frame
     */
    public static final Icon CAMERA_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "camera_icon.png"));

    /**
     * Icon for control frame
     */
    public static final Icon CONTROL_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "control_icon_mini.png"));

    /**
     * Icon for control frame
     */
    public static final Icon CONTROL_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "control_icon.png"));

    /**
     * Icon for emergent frame
     */
    public static final Icon EMERGENT_MINI_ICON = new ImageIcon(
            Constants.class.getResource(ICON_DIR + "emergent_icon_mini.png"));

    /**
     * Icon for emergent frame
     */
    public static final Icon EMERGENT_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "emergent_icon.png"));

    /**
     * Icon for GPS frame
     */
    public static final Icon GPS_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "gps_icon_mini.png"));

    /**
     * Icon for GPS frame
     */
    public static final Icon GPS_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "gps_icon.png"));

    /**
     * Icon for IR frame
     */
    public static final Icon IR_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "ir_icon_mini.png"));

    /**
     * Icon for IR frame
     */
    public static final Icon IR_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "ir_icon.png"));

    /**
     * Icon for main frame
     */
    public static final ImageIcon SAM_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "sam_icon.png"));

    /**
     * Icon for offaxis frame
     */
    public static final Icon OFFAXIS_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "offaxis_icon_mini.png"));

    /**
     * Icon for offaxis frame
     */
    public static final Icon OFFAXIS_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "offaxis_icon.png"));

    /**
     * Icon for routing frame
     */
    public static final Icon ROUTING_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "routing_icon_mini.png"));

    /**
     * Icon for routing frame
     */
    public static final Icon ROUTING_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "routing_icon.png"));

    /**
     * Icon for searcharea frame
     */
    public static final Icon SEARCHAREA_MINI_ICON = new ImageIcon(
            Constants.class.getResource(ICON_DIR + "searcharea_icon_mini.png"));

    /**
     * Icon for searcharea frame
     */
    public static final Icon SEARCHAREA_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "searcharea_icon.png"));

    /**
     * Icon for secretmessage frame
     */
    public static final Icon SECRETMESSAGE_MINI_ICON = new ImageIcon(
            Constants.class.getResource(ICON_DIR + "secretmessage_icon_mini.png"));

    /**
     * Icon for secretmessage frame
     */
    public static final Icon SECRETMESSAGE_ICON = new ImageIcon(
            Constants.class.getResource(ICON_DIR + "secretmessage_icon.png"));

    /**
     * Icon for SRIC frame
     */
    public static final Icon SRIC_MINI_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "sric_icon_mini.png"));

    /**
     * title for image viewer frame
     */
    public static final String IMAGE_TITLE = "Image Viewer";

    /**
     * Icon for SRIC frame
     */
    public static final Icon SRIC_ICON = new ImageIcon(Constants.class.getResource(ICON_DIR
            + "sric_icon.png"));
    /**
     * LOGIN_FILE_PATH for SRIC frame
     */
    public static final String SRIC_LOGIN_FILE_PATH = Constants.USER_PROFILE + "/SAM/";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_GET_CURRENT_POSITION = "/getUAVPosition";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_GET_NEXT_POSITION = "/getNextWaypoint";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_GET_WIND_DATA = "/getWind";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_GET_OBSTACLES = "/getObstacles";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_STOP_SRIC_TASK = "/stopLoiter";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_TEST_CONNECTION_TO_MP = "/testConnection";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_SET_WAYPOINTS = "/setWaypoints";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_SET_ZONES = "/setZones";
    /**
     * REST Requests by interface definition
     */
    public static final String REST_SET_TARGETS = "/setTargets";
    private static final String[] EXTENSIONS = new String[] { "gif", "png", "bmp", "jpg" };
    /**
     * Filter for getting pictures out of a folder based on their extension
     */
    public static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    /**
     * title for report sheet frame
     */
    public static final String REPORT_TITLE = "Report Sheet";

    /**
     * title for airdrop frame
     */
    public static final String AIRDROP_TITLE = "Airdrop";

    /**
     * title for camera frame
     */
    public static final String CAMERA_TITLE = "Camera";

    /**
     * title for secret message frame
     */
    public static final String SECRETMESSAGE_TITLE = "Secret Message";

    /**
     * title for control frame
     */
    public static final String CONTROL_TITLE = "Control";

    /**
     * title for emergent frame
     */
    public static final String EMERGENT_TITLE = "Emergent Target";

    /**
     * title for gps frame
     */
    public static final String GPS_TITLE = "GPS Table";

    /**
     * title for ir frame
     */
    public static final String IR_TITLE = "Infrared";

    /**
     * title for log frame
     */
    public static final String LOG_TITLE = "Log";

    /**
     * title for offaxis frame
     */
    public static final String OFFAXIS_TITLE = "Off-Axis";

    /**
     * title for options frame
     */
    public static final String OPTIONS_TITLE = "Options";

    /**
     * title for routing frame
     */
    public static final String ROUTING_TITLE = "Routing";

    /**
     * title for search area viewer frame
     */
    public static final String SEARCHAREA_VIEWER_TITLE = "Search Area Viewer";

    /**
     * title for search area task frame
     */
    public static final String SEARCHAREA_TASK_TITLE = "Seach Area Task";

    /**
     * title for sric frame
     */
    public static final String SRIC_TITLE = "SRIC";

    private Constants() {
    }
}
