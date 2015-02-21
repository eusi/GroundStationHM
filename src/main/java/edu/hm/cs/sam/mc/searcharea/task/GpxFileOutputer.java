package edu.hm.cs.sam.mc.searcharea.task;

import org.apache.log4j.Logger;

import edu.hm.sam.location.LocationWp;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * This class represents an outputter for coordinates to gpx-format. Very useful
 * for testing with online mapping tools which can read this format.
 *
 * @author Maximilian Bayer
 */
public class GpxFileOutputer {

    PrintWriter out;
    DecimalFormat df_counter;
    DecimalFormat df_coord;
    int counter = 1;
    char character = 'A';
    static final DecimalFormat ELEVATION_FORMAT = new DecimalFormat("#.#");
    static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final Logger LOG = Logger.getLogger(GpxFileOutputer.class.getName());

    static {
        TIMESTAMP_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public GpxFileOutputer(final String path) {
        try {
            out = new PrintWriter(new FileWriter(path));
        } catch (final IOException e) {
            LOG.error(path + "could not be created", e);
        }
        df_counter = new DecimalFormat("0");
        df_coord = new DecimalFormat("#.#####");
    }

    /**
     * Writes all coordinates to a file using sub-method writeWaypoint.
     *
     * @param corners List of coordinates
     */
    public void outputWaypointsToGPX(final List<LocationWp> corners) {
        writeHeader();
        for (final LocationWp c : corners) {
            writeWaypoint(c);
        }
        out.close();
    }

    /**
     * Write the header.
     */
    public void writeHeader() {
        if (out != null) {
            out.format("<?xml version=\"1.0\" encoding=\"%s\" standalone=\"yes\"?>\n", Charset
                    .defaultCharset().name());
            out.println("<?xml-stylesheet type=\"text/xsl\" href=\"details.xsl\"?>");
            out.println("<gpx");
            out.println(" version=\"1.1\"");
            out.println(" creator=\"Maximilian Bayer\"");
            out.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            out.println(" xmlns=\"http://www.topografix.com/GPX/1/1\"");
            out.print(" xmlns:topografix=\"http://www.topografix.com/GPX/Private/"
                    + "TopoGrafix/0/1\"");
            out.print(" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 ");
            out.print("http://www.topografix.com/GPX/1/1/gpx.xsd ");
            out.print("http://www.topografix.com/GPX/Private/TopoGrafix/0/1 ");
            out.println("http://www.topografix.com/GPX/Private/TopoGrafix/0/1/"
                    + "topografix.xsd\">");
            out.flush();
        }
    }

    /**
     * Write a single waypoint to a file.
     *
     * @param waypoint
     */
    public void writeWaypoint(final LocationWp waypoint) {
        if (out != null) {
            out.println("<wpt lat=\"" + waypoint.getLat() + "\" lon=\"" + waypoint.getLng() + "\">");
            out.println("<ele>" + "2843.2" + "</ele>");
            out.println("<name>" + "WP0" + df_counter.format(counter) + "-" + character + "</name>");
            out.println("</wpt>");
            out.flush();
            counter++;
            character += 1;
        }
    }

    /**
     * Close the PrintWriter.
     */
    public void closePw() {
        out.close();
    }
}
