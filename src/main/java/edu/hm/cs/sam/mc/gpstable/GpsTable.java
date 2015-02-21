package edu.hm.cs.sam.mc.gpstable;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import edu.hm.cs.sam.mc.misc.Constants;

/**
 * Creates a GPS-history to a trivial specific file.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class GpsTable {

    /**
     * Clears the GPS-Table field and deletes the GPS-Table-Log-File.
     */
    public static void clearGpsTable() {
        try {
            final File file = new File(Constants.GPS_TABLE_LOG);

            if (!file.exists()) {
                file.createNewFile();
            }

            final FileWriter fw = new FileWriter(file);
            fw.write("");
            fw.close();
        } catch (final FileNotFoundException e) {
            LOG.error("File not found.", e);
        } catch (final UnsupportedEncodingException e) {
            LOG.error("Encoding not supported.", e);
        } catch (final IOException e) {
            LOG.error("IO error.", e);
        }
    }

    /**
     * Writes a string to the GPS-Table-Log-File.
     *
     * @param inputLine - Input-String (currentLocation)
     */
    public static void writeGpsTable(final String inputLine) {
        try {
            final File file = new File(Constants.GPS_TABLE_LOG);

            if (!file.exists()) {
                file.createNewFile();
            }

            // true = append file
            final FileWriter fileWritter = new FileWriter(file, true);
            final BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(inputLine + "\n");
            bufferWritter.close();
        } catch (final FileNotFoundException e) {
            LOG.error("File not found.", e);
        } catch (final UnsupportedEncodingException e) {
            LOG.error("Encoding not supported.", e);
        } catch (final IOException e) {
            LOG.error("IO error.", e);
        }
    }

    private static final Logger LOG = Logger.getLogger(GpsTable.class.getName());

    private GpsTable() {

    }
}