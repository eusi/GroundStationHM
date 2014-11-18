package cs.hm.edu.sam.mc.misc;

/**
 * All constants are stored in this variable. An (later) options-ui can change
 * this settings.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class CONSTANTS {

    public final static String IMAGE_DIR = "/images/";
    public final static String ICON_DIR = "/icons/";
    public final static String IMAGE_DIR_FILE = "./src/main/resources" + IMAGE_DIR;
    
    public final static String GPS_TABLE_LOG = "./src/main/resources/gpsTable.log";

    public final static String REST_MP = "http://localhost:8000/MissionPlannerService";

    public final static int GET_CURRENT_POSITION_DELAY = 2000;

}

// memo for config file
// File f = new File("config.properties");
// System.out.println(f.getAbsolutePath());