package cs.hm.edu.sam.mc.images;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import net.miginfocom.swing.MigLayout;
import cs.hm.edu.sam.mc.misc.CONSTANTS;
import cs.hm.edu.sam.mc.misc.MissingIcon;
import cs.hm.edu.sam.mc.report.ReportSheet;

/**
 * ImageViewer class. This module displays images as original and thumbnailed.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class ImageViewer extends JInternalFrame implements ActionListener {

    private final JLabel photographLabel = new JLabel();
    private final JToolBar buttonBar = new JToolBar();
    private final JTextField txtPath;
    private final JButton btnRefresh;

    private final String imagedir = CONSTANTS.IMAGE_DIR;
    private final File dir = new File(CONSTANTS.IMAGE_DIR_FILE);

    private final MissingIcon placeholderIcon = new MissingIcon();

    static final String[] EXTENSIONS = new String[] { "gif", "png", "bmp", "jpg" };

    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

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
    private final JButton btnCopyReport;

    /**
     * Default constructor
     */
    public ImageViewer() {
        setTitle("Image Viewer");
        setIconifiable(true);
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setBounds(0, 0, 550, 450);
        setResizable(true);
        setFrameIcon(new ImageIcon(ReportSheet.class.getResource(CONSTANTS.ICON_DIR
                + "image_icon_mini.png")));

        // A label for displaying the pictures
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        getContentPane().setLayout(new MigLayout("", "[540px]", "[][][][2px][440px][20px]"));

        // Two glue components to add thumbnail buttons to the toolbar inbetween
        // thease glue compoents.
        buttonBar.add(Box.createGlue());
        buttonBar.add(Box.createGlue());

        getContentPane().add(buttonBar, "cell 0 0,growx,aligny top");
        getContentPane().add(photographLabel, "cell 0 4,grow");

        btnCopyReport = new JButton("Copy to report");
        btnCopyReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtPath.getText().equals("...")) {
                    final String filename = txtPath.getText().replace(imagedir, "");
                    final File inF = new File(CONSTANTS.IMAGE_DIR_FILE + filename);
                    final File outF = new File(CONSTANTS.REPORT_DIR_FILE + filename);
                    try {
                        copyFile(inF, outF);
                    } catch (final IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }

            private void copyFile(File in, File out) throws IOException {
                FileChannel inChannel = null;
                FileChannel outChannel = null;
                try {
                    inChannel = new FileInputStream(in).getChannel();
                    outChannel = new FileOutputStream(out).getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                } catch (final IOException e) {
                    throw e;
                } finally {
                    try {
                        if (inChannel != null) {
                            inChannel.close();
                        }
                        if (outChannel != null) {
                            outChannel.close();
                        }
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        getContentPane().add(btnCopyReport, "flowx,cell 0 5");

        txtPath = new JTextField();
        txtPath.setEditable(false);
        txtPath.setText("...");
        getContentPane().add(txtPath, "cell 0 5,growx,aligny top");
        txtPath.setColumns(10);

        btnRefresh = new JButton("Refresh Image List");
        btnRefresh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createImageIconList();
                } catch (final Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        getContentPane().add(btnRefresh, "cell 0 5");
        try {
            createImageIconList();
        } catch (final Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Function is looking for images in folder.
     */
    private ArrayList<String> getImagesInDir() {
        if (dir.isDirectory()) {
            final ArrayList<String> imageFileNames = new ArrayList<String>();

            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                try {
                    ImageIO.read(f);
                    imageFileNames.add(f.getName());
                } catch (final IOException e) {
                }
            }

            return imageFileNames;
        }

        return null;
    }

    @SuppressWarnings("unused")
    protected Void createImageIconList() throws Exception {
        // reset bar first
        buttonBar.removeAll();
        buttonBar.revalidate();
        buttonBar.repaint();

        List<String> imageFileNames = new ArrayList<>();
        imageFileNames = getImagesInDir();

        for (int i = 0; i < imageFileNames.size(); i++) {
            ImageIcon icon;
            final String fileName = imageFileNames.get(i);
            icon = createImageIcon(imagedir + fileName, fileName);
            // downsize photo
            final ImageIcon downSizedIcon = new ImageIcon(getScaledImage(icon.getImage(), 400, 250));

            ThumbnailAction thumbAction;
            if (icon != null) {

                final ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 64,
                        64));

                thumbAction = new ThumbnailAction(downSizedIcon, thumbnailIcon, fileName, imagedir
                        + fileName);

            } else {
                // the image failed to load for some reason
                // so load a placeholder instead
                thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, fileName,
                        imagedir + fileName);
            }
            final JButton thumbButton = new JButton(thumbAction);
            buttonBar.add(thumbButton, buttonBar.getComponentCount() - 1);
        }
        return null;
    }

    /**
     * Creates an ImageIcon if the path is valid.
     *
     * @param path - resource path
     * @param description - description of the file
     */
    protected ImageIcon createImageIcon(final String path, final String description) {
        final java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     *
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return the new resized image
     */
    private Image getScaledImage(final Image srcImg, final int w, final int h) {
        final BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    private class ThumbnailAction extends AbstractAction {

        /**
         * The icon if the full image we want to display.
         */
        private final Icon displayPhoto;
        private final String filePath;

        /**
         * @param filePath
         * @param Icon - The bigger photo to show in the button.
         * @param Icon - The thumbnail to show in the button.
         * @param String - The descriptioon of the icon.
         */
        public ThumbnailAction(final Icon photo, final Icon thumb, final String desc,
                String filePath) {

            // photo
            displayPhoto = photo;
            this.filePath = filePath;

            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);

            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }

        /**
         * Shows the full image in the main area and sets the application title.
         */
        @Override
        public void actionPerformed(final ActionEvent e) {

            photographLabel.setIcon(displayPhoto);
            setTitle("Image Viewer: " + getValue(SHORT_DESCRIPTION).toString());
            txtPath.setText(filePath);
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    }
}