package cs.hm.edu.sam.mc.gpsTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cs.hm.edu.sam.mc.misc.CONSTANTS;

/**
 * Creates a GPS-history to a specific file.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class GpsTable {
	
	public static void writeGpsTable(String inputLine)
	{
		try {
			File file = new File( CONSTANTS.GPS_TABLE_LOG );
 
    		if(!file.exists()){
    			file.createNewFile();
    		}
 
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(inputLine+"\n");
	        bufferWritter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearGpsTable()
	{
		try {
			File file = new File( CONSTANTS.GPS_TABLE_LOG );
 
    		if(!file.exists()){
    			file.createNewFile();
    		}
 
    		FileWriter fw = new FileWriter(file);
    		fw.write("");
	        fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}