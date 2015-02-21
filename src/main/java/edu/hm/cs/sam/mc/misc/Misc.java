package edu.hm.cs.sam.mc.misc;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * Helper class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class Misc {

    /**
     * Copies a file from source to destination.
     *
     * @param log
     *            logger of parent.
     * @param parent
     *            frame initializing copy.
     * @param source
     *            source file.
     * @param destination
     *            destination file.
     * @param giveMessage
     *            true=pop-up if successful, false=not.
     */
    public static void copyFile(final Logger log, final JInternalFrame parent, final File source,
            final File destination, final boolean giveMessage) {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(source).getChannel();
            outChannel = new FileOutputStream(destination).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (final IOException e) {
            log.error("selected image couldn't be copied to report sheet", e);
            JOptionPane.showMessageDialog(parent,
                    "Selected image could not be copied to report sheet!", "Copy Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
                if (giveMessage) {
                    JOptionPane.showMessageDialog(parent, "\"" + source.getAbsolutePath()
                            + "\" was copied to \"" + destination.getAbsolutePath() + "\"",
                            "Copy successful", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (final IOException e) {
                log.error("channel could not be closed", e);
            }
        }
    }

    /**
     * creates a icon of a image.
     *
     * @param log
     *            logger of parent.
     * @param parent
     *            frame initializing image icon.
     * @param path
     *            path to the image.
     * @param description
     *            description for the icon.
     * @return image icon.
     */
    public static ImageIcon createImageIcon(final Logger log, final JInternalFrame parent,
            final String path, final String description) {
        java.net.URL imgURL = null;
        try {
            imgURL = new File(path).toURI().toURL();
        } catch (final MalformedURLException e) {
            log.error("Couldn't load file: " + path, e);
        }
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            log.error("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Checks a folder for images.
     *
     * @param fileDir
     *            folder to check for images.
     * @param log
     *            log for logging.
     * @return List of images in folder.
     */
    public static List<String> getImagesInDir(final String fileDir, final Logger log) {
        final File dir = new File(fileDir);
        if (dir.isDirectory()) {
            final List<String> imageFileNames = new ArrayList<>();

            for (final File f : dir.listFiles(Constants.IMAGE_FILTER)) {
                try {
                    ImageIO.read(f);
                    imageFileNames.add(f.getName());
                } catch (final IOException e) {
                    log.error("ERROR", e);
                }
            }

            return imageFileNames;
        }

        return new ArrayList<>();
    }

    /**
     * Scales a image.
     *
     * @param srcImg
     *            image to scale.
     * @param width
     *            new width.
     * @param height
     *            new height.
     * @return scaled image.
     */
    public static Image getScaledImage(final Image srcImg, final int width, final int height) {
        final BufferedImage resizedImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();
        return resizedImg;
    }

    /**
     * Rounds a value to defined digits behind the dot.
     *
     * @param value
     *            value to round.
     * @param decimalPlaces
     *            number of digits behind the dot.
     * @return rounded value.
     */
    public static double roundValue(final double value, final int decimalPlaces) {
        return Math.round(value * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
    }

    private Misc() {

    }
}
