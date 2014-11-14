package cs.hm.edu.sam.mc.images;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

import cs.hm.edu.sam.mc.report.ReportSheet;

/**
 * ImageViewer class. This module displays images as original and
 * thumbnailed.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class ImageViewer extends JInternalFrame {

    private final JLabel photographLabel = new JLabel();
    private final JToolBar buttonBar = new JToolBar();

    private final String imagedir = "/images/";
    /*
     * TODO Change this to an while(true)-thread-function which checks a
     * specific folder for new images
     */

    private final MissingIcon placeholderIcon = new MissingIcon();

    /**
     * List of all the descriptions of the image files with caption.
     */
    private final String[] imageCaptions = { "Desc 1", "Desc 2", "Desc 3", "Desc 4" };
    /*
     * TODO Change this to an while(true)-thread-function which checks a
     * specific folder for new images
     */

    /**
     * List of all the image files to load.
     */
    private final String[] imageFileNames = { "1.jpg", "2.jpg", "3.jpg", "4.jpg" };

    /*
     * TODO Change this to an while(true)-thread-function which checks a
     * specific folder for new images
     */

    /*
     * KeyListener kl=new KeyAdapter() {
     * @Override public void keyPressed(KeyEvent evt) { //If someone click Esc
     * key, this program will exit if(evt.getKeyCode()==KeyEvent.VK_ESCAPE) {
     * System.exit(0); } } };
     */

    /**
     * Default constructor
     */
    public ImageViewer() {

        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Image Viewer");
        setIconifiable(true);
        setClosable(true);
        setBounds(0, 0, 500, 400);
        setResizable(true);
        // setSize(900, 700);
        // this.setResizable(false);
        setFrameIcon(new ImageIcon(ReportSheet.class.getResource("/icons/image_icon_mini.png")));

        // A label for displaying the pictures
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Two glue components to add thumbnail buttons to the toolbar inbetween
        // thease glue compoents.
        buttonBar.add(Box.createGlue());
        buttonBar.add(Box.createGlue());

        add(buttonBar, BorderLayout.NORTH);
        add(photographLabel, BorderLayout.CENTER);

        // switching to fullscreen mode
        // GraphicsEnvironment.getLocalGraphicsEnvironment().
        // getDefaultScreenDevice().setFullScreenWindow(this);

        // this centers the frame on the screen
        // setLocationRelativeTo(null);

        // start the image loading SwingWorker in a background thread
        loadimages.execute();
    }

    /**
     * SwingWorker class that loads the images a background thread and calls
     * publish when a new one is ready to be displayed.
     */
    private final SwingWorker<Void, ThumbnailAction> loadimages = new SwingWorker<Void, ThumbnailAction>() {

        /**
         * Creates full size and thumbnail versions of the target image files.
         */
        @SuppressWarnings("unused")
        @Override
        protected Void doInBackground() throws Exception {
            for (int i = 0; i < imageCaptions.length; i++) {
                ImageIcon icon;
                icon = createImageIcon(imagedir + imageFileNames[i], imageCaptions[i]);

                // downsize photo
                final ImageIcon downSizedIcon = new ImageIcon(getScaledImage(icon.getImage(), 400,
                        250));

                ThumbnailAction thumbAction;
                if (icon != null) {

                    final ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(),
                            64, 64));

                    thumbAction = new ThumbnailAction(downSizedIcon, thumbnailIcon,
                            imageCaptions[i]);

                } else {
                    // the image failed to load for some reason
                    // so load a placeholder instead
                    thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon,
                            imageCaptions[i]);
                }
                publish(thumbAction);
            }
            return null;
        }

        /**
         * Process all loaded images.
         */
        @Override
        protected void process(final List<ThumbnailAction> chunks) {
            for (final ThumbnailAction thumbAction : chunks) {
                final JButton thumbButton = new JButton(thumbAction);
                // add the new button BEFORE the last glue
                // this centers the buttons in the toolbar
                buttonBar.add(thumbButton, buttonBar.getComponentCount() - 1);
            }
        }
    };

    /**
     * Creates an ImageIcon if the path is valid.
     *
     * @param String
     *            - resource path
     * @param String
     *            - description of the file
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
     * @param srcImg
     *            - source image to scale
     * @param w
     *            - desired width
     * @param h
     *            - desired height
     * @return - the new resized image
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

    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction {

        /**
         * The icon if the full image we want to display.
         */
        private final Icon displayPhoto;

        /**
         * @param Icon
         *            - The bigger photo to show in the button.
         * @param Icon
         *            - The thumbnail to show in the button.
         * @param String
         *            - The descriptioon of the icon.
         */
        public ThumbnailAction(final Icon photo, final Icon thumb, final String desc) {

            // photo
            displayPhoto = photo;

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
        }
    }
    
    /**
     * Icon class that creates an default icon when missing.
     */
    private class MissingIcon implements Icon {

        private final int width = 32;
        private final int height = 32;

        private final BasicStroke stroke = new BasicStroke(4);

        @Override
        public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
            final Graphics2D g2d = (Graphics2D) g.create();

            g2d.setColor(Color.WHITE);
            g2d.fillRect(x + 1, y + 1, width - 2, height - 2);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x + 1, y + 1, width - 2, height - 2);

            g2d.setColor(Color.RED);

            g2d.setStroke(stroke);
            g2d.drawLine(x + 10, y + 10, x + width - 10, y + height - 10);
            g2d.drawLine(x + 10, y + height - 10, x + width - 10, y + 10);

            g2d.dispose();
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

    }
}


