package edu.hm.cs.sam.mc.images;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Misc;
import edu.hm.cs.sam.mc.misc.Settings;
import edu.hm.cs.sam.mc.misc.Window;

/**
 * @author Opitz, Jan (jopitz@hm.edu)
 * @version 23.11.2014 - 15:57:42
 */
@SuppressWarnings("serial")
public class ImageViewer extends Window {

    private final JLabel lblPicture = new JLabel();
    private final JToolBar tlbPicture = new JToolBar();
    private final JScrollPane scroller = new JScrollPane();
    private final JPanel pnlSouth = new JPanel();
    private final JPanel pnlNorth = new JPanel();
    private final JButton btnCopy = new JButton("copy to report");
    private final JTextField txtPath = new JTextField("...");
    private final JButton btnReload = new JButton("reload");

    private static final Logger LOG = Logger.getLogger(ImageViewer.class.getName());

    /**
     * JInternalFrame for displaying the received images from a UVS.
     *
     * @param icon
     *            icon of the frame.
     * @param title
     *            title of the frame.
     */
    public ImageViewer(final String title, final Icon icon) {
        super(title, icon);
        formatWidgets();
        addWidgets();
        addListener();
        refreshImages();
    }

    private void addListener() {
        btnCopy.addActionListener(new CopyAction(this));

        btnReload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                lblPicture.setIcon(null);
                txtPath.setText("...");
                setTitle(Constants.IMAGE_TITLE);
                refreshImages();
            }
        });
    }

    private void addWidgets() {
        setLayout(new BorderLayout(5, 5));
        getMainPanel().add(BorderLayout.CENTER, lblPicture);
        getMainPanel().add(BorderLayout.NORTH, pnlNorth);
        getMainPanel().add(BorderLayout.SOUTH, pnlSouth);
        pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.LINE_AXIS));
        pnlNorth.add(scroller);
        pnlNorth.add(btnReload);
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlSouth.setLayout(new BoxLayout(pnlSouth, BoxLayout.LINE_AXIS));
        pnlSouth.add(getTxtPath());
        pnlSouth.add(btnCopy);
    }

    private void formatWidgets() {
        btnReload.setSize((int) btnReload.getPreferredSize().getWidth(), 500);
        tlbPicture.setFloatable(false);
        scroller.setViewportView(tlbPicture);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
        lblPicture.setVerticalAlignment(SwingConstants.CENTER);
    }

    JLabel getLblPicture() {
        return lblPicture;
    }

    JTextField getTxtPath() {
        return txtPath;
    }

    private void loadImages() {
        List<String> imageFiles = new ArrayList<>();
        imageFiles = Misc.getImagesInDir(Settings.getImgFldr(), LOG);
        for (int i = 0; i < imageFiles.size(); i++) {
            ImageIcon icon;
            final String fileName = imageFiles.get(i);
            // downsize photo
            icon = Misc.createImageIcon(LOG, this, Settings.getImgFldr() + fileName, fileName);
            final ImageToolbarAction imageAction;
            if (icon != null) {
                final double height;
                final double width;
                if (icon.getIconWidth() >= icon.getIconHeight()) {
                    height = ((double) icon.getIconHeight() / (double) icon.getIconWidth())
                            * Constants.FRAME_DEFAULT_SIZE;
                    width = Constants.FRAME_DEFAULT_SIZE;
                } else {
                    height = Constants.FRAME_DEFAULT_SIZE;
                    width = ((double) icon.getIconWidth() / (double) icon.getIconHeight())
                            * Constants.FRAME_DEFAULT_SIZE;
                }
                final ImageIcon downSizedIcon = new ImageIcon(Misc.getScaledImage(icon.getImage(),
                        (int) (width / 2), (int) (height / 2)));
                final ImageIcon imageIcon = new ImageIcon(Misc.getScaledImage(icon.getImage(),
                        (int) (width / 10), (int) (height / 10)));
                imageAction = new ImageToolbarAction(this, downSizedIcon, imageIcon, fileName,
                        Settings.getImgFldr() + fileName);
            } else {
                // the image failed to load for some reason
                // so load a placeholder instead
                imageAction = new ImageToolbarAction(this, Constants.PLACEHOLDER_ICON,
                        Constants.PLACEHOLDER_ICON, fileName, Settings.getImgFldr() + fileName);
            }
            final JButton imageButton = new JButton(imageAction);
            tlbPicture.add(imageButton, tlbPicture.getComponentCount() - 1);
        }
    }

    @Override
    public void loadProperties() {
    }

    /**
     * Refreshes the Images displayed in the toolbar.
     */
    public void refreshImages() {
        resetToolbar();
        loadImages();
    }

    private void resetToolbar() {
        tlbPicture.removeAll();
        tlbPicture.revalidate();
        tlbPicture.repaint();
    }

    @Override
    public void saveProperties() {
    }
}