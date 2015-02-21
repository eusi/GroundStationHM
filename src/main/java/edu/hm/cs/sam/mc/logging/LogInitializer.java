package edu.hm.cs.sam.mc.logging;

/**
 * Log-Initializer-class (configuration).
 *
 * @author Christoph Friegel
 * @version 0.1
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Opitz, Jan (jopitz@hm.edu)
 * @version 08.01.2015 - 08:53:05
 */
public class LogInitializer {

    /**
     * initialize logging.
     *
     * @param gui
     *            interface for logging.
     */
    public static void initializeLogging(final LogMonitor gui) {

        // loading log4j properties object first
        final Properties props = new Properties();
        final InputStream is = LogInitializer.class.getClassLoader().getResourceAsStream(
                "log4j.properties");
        try {
            props.load(is);
        } catch (final IOException e) {
            LOG.error("Log4J-Config not found in JAR: ", e);
        } finally {
            try {
                is.close();
            } catch (final Exception e) {
                LOG.error("ignore this error", e);
            }
        }

        // configure properties for log4j
        PropertyConfigurator.configure(props);

        final PatternLayout pl = new PatternLayout();
        pl.setConversionPattern("%d{HH:mm:ss} | %-5p | %F:%L  |  %m%n");

        final LogAppender logAppender = new LogAppender(gui.getjList(), gui.getDefaultListModel());

        logAppender.setLayout(pl);

        final Logger rl = Logger.getRootLogger();
        rl.addAppender(logAppender);
    }

    private static final Logger LOG = Logger.getLogger(LogInitializer.class.getName());

    private LogInitializer() {

    }
}
