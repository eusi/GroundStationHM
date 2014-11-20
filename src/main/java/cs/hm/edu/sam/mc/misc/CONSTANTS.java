package cs.hm.edu.sam.mc.misc;

/**
 * Some constants are stored in this variable. An (later) options-ui could
 * change this settings.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class CONSTANTS {

	//some folder
    public final static String IMAGE_DIR = "/images/";
    public final static String REPORT_DIR = "/images/report/";
    public final static String ICON_DIR = "/icons/";
    
    //some file path
    public final static String IMAGE_DIR_FILE = "./src/main/resources" + IMAGE_DIR;
    public final static String REPORT_DIR_FILE = "./src/main/resources" + REPORT_DIR;
    
    //GPS table log file path
    public final static String GPS_TABLE_LOG = "./src/main/resources/gpsTable.log";
    
    //rest interface
    public final static String REST_MP = "http://localhost:8000/MissionPlannerService";
    
    //polling delay to MP for currentPosition
    public final static int GET_CURRENT_POSITION_DELAY = 1000;

}

// memo for config file
// File f = new File("config.properties");
// System.out.println(f.getAbsolutePath());
