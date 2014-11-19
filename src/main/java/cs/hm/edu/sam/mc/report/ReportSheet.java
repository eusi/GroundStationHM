package cs.hm.edu.sam.mc.report;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;

import cs.hm.edu.sam.mc.misc.CONSTANTS;
import cs.hm.edu.sam.mc.misc.MissingIcon;

/**
 * ReportSheet class. This module creates a report sheet from specific files.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class ReportSheet extends JInternalFrame {
    private final JTextField txtNumber;
    private final JTextField txtType;
    private final JTextField txtLatitude;
    private final JTextField txtLongitude;
    private final JTextField txtOrientation;
    private final JTextField txtShape;
    private final JTextField txtColor;
    private final JTextField txtAlphanumeric;
    private final JTextField txtAlphanumericColor;
    private final JTextField txtFileName;
    private final JTextField txtAmplifyingText;
    private final String imagedir = CONSTANTS.REPORT_DIR;
    private final File dir = new File(CONSTANTS.REPORT_DIR_FILE);
    private final JToolBar buttonBar = new JToolBar();
    private final JLabel photographLabel = new JLabel("");

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

    /**
     * Create the frame.
     */
    public ReportSheet() {
        setFrameIcon(new ImageIcon(ReportSheet.class.getResource(CONSTANTS.ICON_DIR
                + "report_icon_mini.png")));
        setTitle("ReportSheet");
        setIconifiable(true);
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setBounds(0, 0, 500, 500);

        final JPanel panel = new JPanel();

        final JLabel lblNumber = new JLabel("Number:");

        final JLabel lblType = new JLabel("Type:");

        final JLabel lblLatitude = new JLabel("Latitude:");

        txtNumber = new JTextField();
        txtNumber.setColumns(10);

        txtType = new JTextField();
        txtType.setColumns(10);

        txtLatitude = new JTextField();
        txtLatitude.setColumns(10);

        final JLabel lblLongitude = new JLabel("Longitude:");

        txtLongitude = new JTextField();
        txtLongitude.setColumns(10);

        final JLabel lblOrientation = new JLabel("Orientation:");

        txtOrientation = new JTextField();
        txtOrientation.setColumns(10);

        txtShape = new JTextField();
        txtShape.setColumns(10);

        txtColor = new JTextField();
        txtColor.setColumns(10);

        txtAlphanumeric = new JTextField();
        txtAlphanumeric.setColumns(10);

        txtAlphanumericColor = new JTextField();
        txtAlphanumericColor.setColumns(10);

        txtFileName = new JTextField();
        txtFileName.setColumns(10);

        txtAmplifyingText = new JTextField();
        txtAmplifyingText.setColumns(10);

        final JLabel lblNewLabel = new JLabel("Shape:");

        final JLabel lblColor = new JLabel("Color:");

        final JLabel lblAlphanumeric = new JLabel("\u00A0Alphanumeric:");

        final JLabel lblAlphanumericColor = new JLabel("\u00A0Alphanumeric color:");

        final JLabel lblFileName = new JLabel("File Name:");

        final JLabel lblAmplifyingText = new JLabel("Amplifying Text:");
        final GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout
        .setHorizontalGroup(groupLayout
                .createParallelGroup(Alignment.LEADING)
                .addGroup(
                        groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                groupLayout
                                .createParallelGroup(Alignment.LEADING)
                                .addComponent(buttonBar,
                                        GroupLayout.DEFAULT_SIZE, 464,
                                        Short.MAX_VALUE)
                                        .addComponent(panel, Alignment.TRAILING,
                                                GroupLayout.DEFAULT_SIZE, 464,
                                                Short.MAX_VALUE)
                                                .addGroup(
                                                        groupLayout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                                groupLayout
                                                                .createParallelGroup(
                                                                        Alignment.LEADING)
                                                                        .addComponent(
                                                                                photographLabel,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                232,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(
                                                                                        lblFileName))
                                                                                        .addPreferredGap(
                                                                                                ComponentPlacement.UNRELATED)
                                                                                                .addGroup(
                                                                                                        groupLayout
                                                                                                        .createParallelGroup(
                                                                                                                Alignment.LEADING)
                                                                                                                .addGroup(
                                                                                                                        Alignment.TRAILING,
                                                                                                                        groupLayout
                                                                                                                        .createSequentialGroup()
                                                                                                                        .addGroup(
                                                                                                                                groupLayout
                                                                                                                                .createParallelGroup(
                                                                                                                                        Alignment.LEADING)
                                                                                                                                        .addComponent(
                                                                                                                                                lblAlphanumericColor)
                                                                                                                                                .addComponent(
                                                                                                                                                        lblAlphanumeric)
                                                                                                                                                        .addComponent(
                                                                                                                                                                lblColor)
                                                                                                                                                                .addComponent(
                                                                                                                                                                        lblNewLabel)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                lblOrientation)
                                                                                                                                                                                .addComponent(
                                                                                                                                                                                        lblLongitude)
                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                lblLatitude)
                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                        lblType)
                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                lblNumber))
                                                                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                                                        ComponentPlacement.RELATED)
                                                                                                                                                                                                                        .addGroup(
                                                                                                                                                                                                                                groupLayout
                                                                                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                                                                        Alignment.LEADING)
                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                txtNumber,
                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                121,
                                                                                                                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                        txtType,
                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                        121,
                                                                                                                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                                txtLatitude,
                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                121,
                                                                                                                                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                        txtLongitude,
                                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                        121,
                                                                                                                                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                                                txtOrientation,
                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                121,
                                                                                                                                                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                        txtShape,
                                                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                        121,
                                                                                                                                                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                                                                txtColor,
                                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                121,
                                                                                                                                                                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                                        txtAlphanumeric,
                                                                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                        121,
                                                                                                                                                                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                                                                                txtAlphanumericColor,
                                                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                121,
                                                                                                                                                                                                                                                                                                                Short.MAX_VALUE)))
                                                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                                                        txtFileName,
                                                                                                                                                                                                                                                                                                                        Alignment.TRAILING,
                                                                                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                        222,
                                                                                                                                                                                                                                                                                                                        Short.MAX_VALUE)))
                                                                                                                                                                                                                                                                                                                        .addGroup(
                                                                                                                                                                                                                                                                                                                                groupLayout
                                                                                                                                                                                                                                                                                                                                .createSequentialGroup()
                                                                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                                                                        lblAmplifyingText)
                                                                                                                                                                                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                                                                                                                                                                                ComponentPlacement.UNRELATED)
                                                                                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                                                                                        txtAmplifyingText,
                                                                                                                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                        376,
                                                                                                                                                                                                                                                                                                                                                        Short.MAX_VALUE)))
                                                                                                                                                                                                                                                                                                                                                        .addContainerGap()));
        groupLayout
        .setVerticalGroup(groupLayout
                .createParallelGroup(Alignment.LEADING)
                .addGroup(
                        groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(buttonBar, GroupLayout.PREFERRED_SIZE, 28,
                                GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(
                                        groupLayout
                                        .createParallelGroup(Alignment.LEADING,
                                                false)
                                                .addGroup(
                                                        groupLayout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                                groupLayout
                                                                .createParallelGroup(
                                                                        Alignment.BASELINE)
                                                                        .addComponent(
                                                                                lblNumber)
                                                                                .addComponent(
                                                                                        txtNumber,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.PREFERRED_SIZE))
                                                                                        .addPreferredGap(
                                                                                                ComponentPlacement.RELATED)
                                                                                                .addGroup(
                                                                                                        groupLayout
                                                                                                        .createParallelGroup(
                                                                                                                Alignment.BASELINE)
                                                                                                                .addComponent(
                                                                                                                        txtType,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                        .addComponent(
                                                                                                                                lblType))
                                                                                                                                .addPreferredGap(
                                                                                                                                        ComponentPlacement.RELATED)
                                                                                                                                        .addGroup(
                                                                                                                                                groupLayout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                        Alignment.BASELINE)
                                                                                                                                                        .addComponent(
                                                                                                                                                                txtLatitude,
                                                                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                .addComponent(
                                                                                                                                                                        lblLatitude))
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                ComponentPlacement.RELATED)
                                                                                                                                                                                .addGroup(
                                                                                                                                                                                        groupLayout
                                                                                                                                                                                        .createParallelGroup(
                                                                                                                                                                                                Alignment.BASELINE)
                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                        lblLongitude)
                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                txtLongitude,
                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE))
                                                                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                                                        ComponentPlacement.RELATED)
                                                                                                                                                                                                                        .addGroup(
                                                                                                                                                                                                                                groupLayout
                                                                                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                                                                        Alignment.BASELINE)
                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                txtOrientation,
                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                        lblOrientation))
                                                                                                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                                                                                                ComponentPlacement.RELATED)
                                                                                                                                                                                                                                                                .addGroup(
                                                                                                                                                                                                                                                                        groupLayout
                                                                                                                                                                                                                                                                        .createParallelGroup(
                                                                                                                                                                                                                                                                                Alignment.BASELINE)
                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                        txtShape,
                                                                                                                                                                                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                                                                lblNewLabel))
                                                                                                                                                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                                                                                                                                        ComponentPlacement.RELATED)
                                                                                                                                                                                                                                                                                                        .addGroup(
                                                                                                                                                                                                                                                                                                                groupLayout
                                                                                                                                                                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                                                                                                                                                        Alignment.BASELINE)
                                                                                                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                                                                                                txtColor,
                                                                                                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                                                                        lblColor))
                                                                                                                                                                                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                                                                                                                                                                                ComponentPlacement.RELATED)
                                                                                                                                                                                                                                                                                                                                                .addGroup(
                                                                                                                                                                                                                                                                                                                                                        groupLayout
                                                                                                                                                                                                                                                                                                                                                        .createParallelGroup(
                                                                                                                                                                                                                                                                                                                                                                Alignment.BASELINE)
                                                                                                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                                                                                                        txtAlphanumeric,
                                                                                                                                                                                                                                                                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                                                                                                                                                lblAlphanumeric))
                                                                                                                                                                                                                                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                                                                                                                                                                                                                        ComponentPlacement.RELATED)
                                                                                                                                                                                                                                                                                                                                                                                        .addGroup(
                                                                                                                                                                                                                                                                                                                                                                                                groupLayout
                                                                                                                                                                                                                                                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                                                                                                                                                                                                                                        Alignment.BASELINE)
                                                                                                                                                                                                                                                                                                                                                                                                        .addComponent(
                                                                                                                                                                                                                                                                                                                                                                                                                txtAlphanumericColor,
                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                                                                                                                                                                                                                .addComponent(
                                                                                                                                                                                                                                                                                                                                                                                                                        lblAlphanumericColor)))
                                                                                                                                                                                                                                                                                                                                                                                                                        .addComponent(photographLabel,
                                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                Short.MAX_VALUE))
                                                                                                                                                                                                                                                                                                                                                                                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                                                                                                                                                                                                                                                                                                                                                .addGroup(
                                                                                                                                                                                                                                                                                                                                                                                                                                        groupLayout
                                                                                                                                                                                                                                                                                                                                                                                                                                        .createParallelGroup(Alignment.BASELINE)
                                                                                                                                                                                                                                                                                                                                                                                                                                        .addComponent(txtFileName,
                                                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                                                                                                                                                                                                                                                .addComponent(lblFileName))
                                                                                                                                                                                                                                                                                                                                                                                                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                                                                                                                                                                                                                                                                                                                                                                .addGroup(
                                                                                                                                                                                                                                                                                                                                                                                                                                                        groupLayout
                                                                                                                                                                                                                                                                                                                                                                                                                                                        .createParallelGroup(Alignment.BASELINE)
                                                                                                                                                                                                                                                                                                                                                                                                                                                        .addComponent(txtAmplifyingText,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                                                                                                                                                                                                                                                                .addComponent(lblAmplifyingText))
                                                                                                                                                                                                                                                                                                                                                                                                                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                                                                                                                                                                                                                                                                                                                                                                                .addComponent(panel, GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        .addContainerGap(46, Short.MAX_VALUE)));

        final JButton btnCreate = new JButton("create");

        final JLabel lblReportsheet = new JLabel("ReportSheet");

        final JLabel lblStatus = new JLabel("Status");

        final JProgressBar progressBar = new JProgressBar(0, 100);

        final JButton btnOpen = new JButton("open");
        final GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_panel.createSequentialGroup()
                .addGroup(
                        gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(
                                gl_panel.createSequentialGroup()
                                .addComponent(lblReportsheet).addGap(78)
                                .addComponent(btnCreate))
                                .addComponent(lblStatus)
                                .addGroup(
                                        Alignment.TRAILING,
                                        gl_panel.createSequentialGroup()
                                        .addComponent(progressBar,
                                                GroupLayout.DEFAULT_SIZE, 391,
                                                Short.MAX_VALUE)
                                                .addPreferredGap(
                                                        ComponentPlacement.UNRELATED)
                                                        .addComponent(btnOpen))).addContainerGap()));
        gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_panel.createSequentialGroup()
                .addContainerGap()
                .addGroup(
                        gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblReportsheet).addComponent(btnCreate))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(lblStatus)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(
                                gl_panel.createParallelGroup(Alignment.LEADING)
                                .addComponent(btnOpen)
                                .addComponent(progressBar, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap()));
        panel.setLayout(gl_panel);
        getContentPane().setLayout(groupLayout);

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

    private class ThumbnailAction extends AbstractAction {

        /**
         * The icon if the full image we want to display.
         */
        private final Icon displayPhoto;

        /**
         * @param filePath
         * @param Icon
         *            - The bigger photo to show in the button.
         * @param Icon
         *            - The thumbnail to show in the button.
         * @param String
         *            - The descriptioon of the icon.
         */
        public ThumbnailAction(final Icon photo, final Icon thumb, final String desc,
                String filePath) {

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
            // txtPath.setText(filePath);
        }
    }
}