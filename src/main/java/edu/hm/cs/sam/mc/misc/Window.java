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

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * @author Opitz, Jan (jopitz@hm.edu)
 * @version 19.12.2014 - 14:00:24
 */
@SuppressWarnings("serial")
public abstract class Window extends JInternalFrame implements Serializable {

    private final JPanel pnlMain = new JPanel();

    private final String title;

    private static final Logger LOG = Logger.getLogger(Window.class.getName());

    /**
     * Construktor for window.
     *
     * @param title
     *            title for this window.
     * @param icon
     *            icon for this window.
     */
    public Window(final String title, final Icon icon) {
        this.title = title;
        setTitle(title);
        setFrameIcon(icon);
        setIconifiable(true);
        setClosable(true);
        setResizable(true);
        setSize(Constants.FRAME_DEFAULT_SIZE, Constants.FRAME_DEFAULT_SIZE);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setContentPane(pnlMain);
    }

    /**
     * getter for Main Panel.
     *
     * @return pnlMain.
     */
    public JPanel getMainPanel() {
        return pnlMain;
    }

    /**
     * loading properties.
     */
    public void load() {
        final Properties props = new Properties();
        final File file = new File(Settings.getSvFldr() + title + ".properties");
        if (file.exists()) {
            try {
                final InputStream is = new FileInputStream(file);
                props.load(is);
            } catch (final FileNotFoundException e) {
                LOG.error(title + ".properties could not be found.", e);
            } catch (final IOException e) {
                LOG.error(title + ".properties could not be loaded.", e);
            }
            final int height = new Integer(props.getProperty("height", ""
                    + Constants.FRAME_DEFAULT_SIZE));
            final int width = new Integer(props.getProperty("width", ""
                    + Constants.FRAME_DEFAULT_SIZE));
            final int x = new Integer(props.getProperty("x", "0"));
            final int y = new Integer(props.getProperty("y", "0"));
            this.setSize(width, height);
            this.setLocation(x, y);
        }
        loadProperties();
        LOG.info("Window " + title + " loaded");
    }

    /**
     * defined properties to load.
     */
    public abstract void loadProperties();

    /**
     * saving properties.
     */
    public void save() {
        final int height = (int) this.getSize().getHeight();
        final int width = (int) this.getSize().getWidth();
        final int x = (int) this.getLocation().getX();
        final int y = (int) this.getLocation().getY();
        final Properties props = new Properties();
        props.setProperty("height", "" + height);
        props.setProperty("width", "" + width);
        props.setProperty("x", "" + x);
        props.setProperty("y", "" + y);
        final File file = new File(Settings.getSvFldr() + title + ".properties");
        try {
            final OutputStream os = new FileOutputStream(file);
            props.store(os, title);
        } catch (final FileNotFoundException e) {
            LOG.error(title + ".properties could not be found.", e);
        } catch (final IOException e) {
            LOG.error(title + ".properties could not be saved.", e);
        }
        saveProperties();
        LOG.info("Window " + title + " saved");
    }

    /**
     * defined properties to save.
     */
    public abstract void saveProperties();
}
