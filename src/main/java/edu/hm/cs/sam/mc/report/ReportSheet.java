package edu.hm.cs.sam.mc.report;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
 * @version 28.11.2014 - 13:04:22
 */
@SuppressWarnings("serial")
public class ReportSheet extends Window {

    private final JLabel lblPicture = new JLabel();
    private final JToolBar tlbPicture = new JToolBar();
    private final JScrollPane scroller = new JScrollPane();
    private final JPanel pnlNorth = new JPanel();
    private final JPanel pnlSouth = new JPanel();
    private final JPanel pnlEast1 = new JPanel();
    private final JPanel pnlEast2 = new JPanel();
    private final JLabel lblNumber = new JLabel("Number:");
    private final JLabel lblType = new JLabel("Type:");
    private final JLabel lblLatitude = new JLabel("Latitude:");
    private final JLabel lblLongitude = new JLabel("Longitude:");
    private final JLabel lblOrientatation = new JLabel("Orientation:");
    private final JLabel lblShape = new JLabel("Shape:");
    private final JLabel lblColor = new JLabel("Color:");
    private final JLabel lblAlphanumeric = new JLabel("Alphanumeric:");
    private final JLabel lblAlphanumericColor = new JLabel("Alphanumeric color:");
    private final JLabel lblFileName = new JLabel("File name:");
    private final JLabel lblAmplifyingText = new JLabel("Amplifying text:");
    private final JLabel lblStatus = new JLabel("Report Sheet:");
    private final JTextField txtNumber = new JTextField();
    private final JTextField txtType = new JTextField();
    private final JTextField txtLatitude = new JTextField();
    private final JTextField txtLongitude = new JTextField();
    private final JTextField txtOrientatation = new JTextField();
    private final JTextField txtShape = new JTextField();
    private final JTextField txtColor = new JTextField();
    private final JTextField txtAlphanumeric = new JTextField();
    private final JTextField txtAlphanumericColor = new JTextField();
    private final JTextField txtFileName = new JTextField();
    private final JTextField txtAmplifyingText = new JTextField();
    private final JButton btnDelete = new JButton("delete");
    private final JButton btnSave = new JButton("save");
    private final JButton btnCreate = new JButton("create report");
    private final JButton btnReload = new JButton("reload");
    private final JButton btnOpen = new JButton("open");
    private String currentLoadedPicture = "";
    private boolean loadedPictureIsAutomatic = false;

    private static final Logger LOG = Logger.getLogger(ReportSheet.class.getName());

    /**
     * JInternalFrame for displaying images marked as targets and edit their
     * target informations.
     *
     * @param icon
     *            icon of the frame.
     * @param title
     *            title of the frame.
     */
    public ReportSheet(final String title, final Icon icon) {
        super(title, icon);
        formatWidgets();
        addWidgets();
        addListener();
        refreshImages();
    }

    private void addListener() {
        btnSave.addActionListener(new SaveAction(this));
        btnDelete.addActionListener(new DeleteAction(this));
        btnCreate.addActionListener(new CreateAction(this));
        btnOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(Settings.getRprtSvFldr()));
                } catch (final IOException e1) {
                    LOG.error("Could not open folder \"" + Settings.getRprtSvFldr() + "\"", e1);
                }
            }
        });
        btnReload.addActionListener(new ReloadAction(this));
    }

    private void addPanelEast() {
        pnlEast1.setLayout(new BoxLayout(pnlEast1, BoxLayout.PAGE_AXIS));
        pnlEast1.add(pnlEast2);
        pnlEast1.add(Box.createVerticalGlue());
        pnlEast2.setLayout(new GridLayout(0, 2, 5, 5));
        pnlEast2.add(lblNumber);
        pnlEast2.add(txtNumber);
        pnlEast2.add(lblType);
        pnlEast2.add(txtType);
        pnlEast2.add(lblLatitude);
        pnlEast2.add(txtLatitude);
        pnlEast2.add(lblLongitude);
        pnlEast2.add(txtLongitude);
        pnlEast2.add(lblOrientatation);
        pnlEast2.add(txtOrientatation);
        pnlEast2.add(lblShape);
        pnlEast2.add(txtShape);
        pnlEast2.add(lblColor);
        pnlEast2.add(txtColor);
        pnlEast2.add(lblAlphanumeric);
        pnlEast2.add(txtAlphanumeric);
        pnlEast2.add(lblAlphanumericColor);
        pnlEast2.add(txtAlphanumericColor);
        pnlEast2.add(lblFileName);
        pnlEast2.add(txtFileName);
        pnlEast2.add(lblAmplifyingText);
        pnlEast2.add(txtAmplifyingText);
        pnlEast2.add(btnDelete);
        pnlEast2.add(btnSave);
        pnlEast2.setMaximumSize(pnlEast2.getPreferredSize());
    }

    private void addPanelNorth() {
        pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.LINE_AXIS));
        pnlNorth.add(scroller);
        pnlNorth.add(btnReload);
    }

    private void addPanelSouth() {
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnlSouth.setLayout(new GridLayout(0, 3, 5, 5));
        pnlSouth.add(lblStatus);
        pnlSouth.add(btnCreate);
        pnlSouth.add(btnOpen);
    }

    private void addWidgets() {
        getMainPanel().setLayout(new BorderLayout(5, 5));
        getMainPanel().add(BorderLayout.CENTER, lblPicture);
        getMainPanel().add(BorderLayout.NORTH, pnlNorth);
        getMainPanel().add(BorderLayout.EAST, pnlEast1);
        getMainPanel().add(BorderLayout.SOUTH, pnlSouth);
        addPanelNorth();
        addPanelEast();
        addPanelSouth();
    }

    private void formatWidgets() {
        tlbPicture.setFloatable(false);
        scroller.setViewportView(tlbPicture);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
        lblPicture.setVerticalAlignment(SwingConstants.CENTER);
        btnOpen.setEnabled(false);
    }

    JButton getBtnOpen() {
        return btnOpen;
    }

    String getCurrentLoadedPicture() {
        return currentLoadedPicture;
    }

    JLabel getLblPicture() {
        return lblPicture;
    }

    boolean getLoadedPictureIsAutomatic() {
        return loadedPictureIsAutomatic;
    }

    JTextField getTxtAlphanumeric() {
        return txtAlphanumeric;
    }

    JTextField getTxtAlphanumericColor() {
        return txtAlphanumericColor;
    }

    JTextField getTxtAmplifyingText() {
        return txtAmplifyingText;
    }

    JTextField getTxtColor() {
        return txtColor;
    }

    JTextField getTxtFileName() {
        return txtFileName;
    }

    JTextField getTxtLatitude() {
        return txtLatitude;
    }

    JTextField getTxtLongitude() {
        return txtLongitude;
    }

    JTextField getTxtNumber() {
        return txtNumber;
    }

    JTextField getTxtOrientatation() {
        return txtOrientatation;
    }

    JTextField getTxtShape() {
        return txtShape;
    }

    JTextField getTxtType() {
        return txtType;
    }

    private void loadImages() {
        final List<String> imageFiles = Misc.getImagesInDir(Settings.getRprtImgFldr(), LOG);
        for (int i = 0; i < imageFiles.size(); i++) {
            ImageIcon icon;
            final String fileName = imageFiles.get(i);
            icon = Misc.createImageIcon(LOG, this, Settings.getRprtImgFldr() + fileName, fileName);
            final ReportToolbarAction reportAction;
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
                final ImageIcon reportIcon = new ImageIcon(Misc.getScaledImage(icon.getImage(),
                        (int) (width / 10), (int) (height / 10)));
                reportAction = new ReportToolbarAction(this, downSizedIcon, reportIcon, fileName,
                        Settings.getRprtImgFldr() + fileName);
            } else {
                // the image failed to load for some reason
                // so load a placeholder instead
                reportAction = new ReportToolbarAction(this, Constants.PLACEHOLDER_ICON,
                        Constants.PLACEHOLDER_ICON, fileName, Settings.getRprtImgFldr() + fileName);
            }
            final JButton reportButton = new JButton(reportAction);
            tlbPicture.add(reportButton, tlbPicture.getComponentCount() - 1);
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

    void resetLoadedPicture() {
        setCurrentLoadedPicture("");
        setTitle(Constants.REPORT_TITLE);
        lblPicture.setToolTipText("");
        lblPicture.setIcon(null);
        txtAlphanumeric.setText("");
        txtAlphanumericColor.setText("");
        txtAmplifyingText.setText("");
        txtColor.setText("");
        txtFileName.setText("");
        txtLatitude.setText("");
        txtLongitude.setText("");
        txtNumber.setText("");
        txtOrientatation.setText("");
        txtShape.setText("");
        txtType.setText("");
    }

    private void resetToolbar() {
        tlbPicture.removeAll();
        tlbPicture.revalidate();
        tlbPicture.repaint();
    }

    @Override
    public void saveProperties() {
    }

    void setCurrentLoadedPicture(final String currentLoadedPicture) {
        this.currentLoadedPicture = currentLoadedPicture;
    }

    void setLoadedPictureIsAutomatic(final boolean isAutomatic) {
        loadedPictureIsAutomatic = isAutomatic;
    }
}
