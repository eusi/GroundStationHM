package edu.hm.cs.sam.mc.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.io.File;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.airdrop.Airdrop;
import edu.hm.cs.sam.mc.camera.Camera;
import edu.hm.cs.sam.mc.control.Control;
import edu.hm.cs.sam.mc.emergent.Emergent;
import edu.hm.cs.sam.mc.gpstable.GpsTable;
import edu.hm.cs.sam.mc.gpstable.GpsViewer;
import edu.hm.cs.sam.mc.images.ImageViewer;
import edu.hm.cs.sam.mc.ir.Ir;
import edu.hm.cs.sam.mc.logging.LogInitializer;
import edu.hm.cs.sam.mc.logging.LogMonitor;
import edu.hm.cs.sam.mc.misc.BuildProperties;
import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Credits;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Misc;
import edu.hm.cs.sam.mc.misc.Settings;
import edu.hm.cs.sam.mc.misc.Window;
import edu.hm.cs.sam.mc.offaxis.OffAxis;
import edu.hm.cs.sam.mc.options.Options;
import edu.hm.cs.sam.mc.report.ReportSheet;
import edu.hm.cs.sam.mc.rest.RestClient;
import edu.hm.cs.sam.mc.routing.Routing;
import edu.hm.cs.sam.mc.searcharea.task.SearchArea;
import edu.hm.cs.sam.mc.searcharea.viewer.SearchAreaViewer;
import edu.hm.cs.sam.mc.secretmessage.SecretMessage;
import edu.hm.cs.sam.mc.sric.SRIC;

/**
 * Main class. This main class contains the outer gui window.
 *
 * @author Christoph Friegel
 * @version 0.1
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

    private class locationThread extends Thread {
        public locationThread(final String str) {
            super(str);
        }

        private String getCurrentPositionText4MainGui() {
            return "lng: " + Misc.roundValue(Data.getCurrentPosition().getLng(), 5) + 
                    " , lat: " + Misc.roundValue(Data.getCurrentPosition().getLat(), 5) + 
                    " , alt: " + Misc.roundValue(Data.getCurrentPosition().getAlt(), 2);
        }

        private String getNextPositionText4MainGui() {
            return Data.getNextPosition().getSamType().name() + 
                    " reached in " + Math.round( Data.getNextPosition().getDistanceToNextWaypoint() ) + 
                    " meters";
        }

        @Override
        public void run() {
            while (true) {
                if (Data.isCurrentLocationActive()) {

                    RestClient.getNextPosition();
                    RestClient.getCurrentPosition();

                    if (Data.getCurrentPosition() != null) {
                        lblCoor.setText( getCurrentPositionText4MainGui() );

                        GpsTable.writeGpsTable(Data.getCurrentPosition().toGpsTable());
                    }
                    else
                        lblCoor.setText( "lng: - , lat: - , alt: -" );


                    if (Data.getNextPosition() != null) {
                        lblTask.setText( getNextPositionText4MainGui() ); 
                    }
                    else
                        lblTask.setText( "-" ); 
                }
                    
                try {
                    sleep( Settings.getCurrentPositionDelay() );
                } catch (final InterruptedException e) {
                    LOG.error("ERROR", e);
                }
            }
        }
    }

    /**
     * static main
     *
     * @param args
     *            default for static main.
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    final Main frame = new Main();
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(final java.awt.event.WindowEvent windowEvent) {
                            if (JOptionPane.showConfirmDialog(frame,
                                    "Are you sure to close this window?", "Really Closing?",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                LOG.info("closing GUI...");
                                frame.close();
                            } else {
                                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                            }
                        }
                    });
                    frame.setVisible(true);
                    LOG.info("Main loaded");
                } catch (final Exception e) {
                    LOG.error("Main can't be loaded", e);
                }
            }
        });
    }

    private JPanel contentPane;
    private JButton btnReportSheet;
    private JButton btnImageviewer;
    private JButton btnSric;
    private JButton btnIr;
    private JButton btnEmergent;
    private JButton btnGPStable;
    private JButton btnSearchAreaViewer;
    private JButton btnAirdrop;
    private JButton btnOffAxis;
    private JButton btnControl;
    private JButton btnCamera;
    private JButton btnSecretMessage;
    private Options options;
    private LogMonitor logMonitor;
    private Window reportSheet;
    private Window sric;
    private Window routing;
    private Window ir;
    private Window emergent;
    private Window gpsviewer;
    private Window searchAreaViewer;
    private Window searchAreaTask;
    private Window airdrop;
    private Window offAxis;
    private Window camera;
    private Window control;
    private Window secretMessage;
    private Window imageViewer;
    private JDesktopPane desktopPane;
    private JButton btnRouting;
    private JLabel lblCoor;
    private JLabel lblTask;
    private JCheckBoxMenuItem mntmLocation;
    private JMenuItem mntmLogging;
    private JMenuItem mntmOptions;
    private JMenuItem mntmExit;
    private Timer timer;
    private JMenuItem mntmCredits;
    private JButton btnSearchArea;
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private Component oneHorizontalGlue;

    private Component twoHorizontalGlue;

    /**
     * Initialize data and create frame.
     */

    private int offsetCloud1;
    private int offsetCloud2;
    private int offsetCloud3;
    private int offsetCloud4;
    private String person1;
    private String person2;
    private String person3;
    private String person4;

    private final Credits credits;

    /**
     *
     */
    public Main() {
        // TODO: LogMonitor should run on its own AsyncThread
        // Alternative: AsyncAppender?
        logMonitor = new LogMonitor();
        LogInitializer.initializeLogging(logMonitor);

        initComponents();

        createEvents();

        offsetCloud1 = 350;
        offsetCloud2 = getWidth() - 225;
        offsetCloud3 = getWidth() - 475;
        offsetCloud4 = 25;
        person1 = "";
        person2 = "";
        person3 = "";
        person4 = "";
        credits = new Credits();

        createSettingFolders();

        Settings.load(LOG);

        openWindows();
        loadWindows();

        new locationThread("location").start();
    }

    private void addFifthButtons() {
        btnControl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!control.isVisible()) {
                    control.setVisible(true);
                } else {
                    control.moveToFront();
                }
            }
        });

        btnSecretMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!secretMessage.isVisible()) {
                    secretMessage.setVisible(true);
                } else {
                    secretMessage.moveToFront();
                }
            }
        });
    }

    private void addFileButtons() {
        mntmExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                if (JOptionPane.showConfirmDialog(null, "Are you sure to close this window?",
                        "Really Closing?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    LOG.warn("Closing GUI...");
                    close();
                } else {
                    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            }

        });

        mntmCredits.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                } else {
                    timer.start();
                }
            }

        });

        mntmOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (options == null) {
                    options = new Options();
                }
                options.setVisible(true);
            }
        });

        mntmLogging.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (logMonitor == null) {
                    logMonitor = new LogMonitor();
                }
                logMonitor.setVisible(true);
            }
        });
    }

    private void addFirstButtons() {
        btnReportSheet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!reportSheet.isVisible()) {
                    reportSheet.setVisible(true);
                } else {
                    reportSheet.moveToFront();
                }
            }
        });

        btnImageviewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!imageViewer.isVisible()) {
                    imageViewer.setVisible(true);
                } else {
                    imageViewer.moveToFront();
                }
            }
        });

        btnSric.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!sric.isVisible()) {
                    sric.setVisible(true);
                } else {
                    sric.moveToFront();
                }
            }
        });
    }

    private void addFourthButtons() {
        btnSearchAreaViewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!searchAreaViewer.isVisible()) {
                    searchAreaViewer.setVisible(true);
                } else {
                    searchAreaViewer.moveToFront();
                }
            }
        });

        btnOffAxis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!offAxis.isVisible()) {
                    offAxis.setVisible(true);
                } else {
                    offAxis.moveToFront();
                }
            }
        });

        btnCamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!camera.isVisible()) {
                    camera.setVisible(true);
                } else {
                    camera.moveToFront();
                }
            }
        });
    }

    private void addSecondButtons() {
        btnRouting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!routing.isVisible()) {
                    routing.setVisible(true);
                } else {
                    routing.moveToFront();
                }
            }
        });

        btnIr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!ir.isVisible()) {
                    ir.setVisible(true);
                } else {
                    ir.moveToFront();
                }
            }
        });

        btnEmergent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!emergent.isVisible()) {
                    emergent.setVisible(true);
                } else {
                    emergent.moveToFront();
                }
            }
        });
    }

    private void addThirdButtons() {
        btnGPStable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!gpsviewer.isVisible()) {
                    gpsviewer.setVisible(true);
                } else {
                    gpsviewer.moveToFront();
                }
            }
        });
        btnAirdrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!airdrop.isVisible()) {
                    airdrop.setVisible(true);
                } else {
                    airdrop.moveToFront();
                }
            }
        });

        btnSearchArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (!searchAreaTask.isVisible()) {
                    searchAreaTask.setVisible(true);
                } else {
                    searchAreaTask.moveToFront();
                }
            }
        });
    }

    protected void close() {
        saveWindows();
        Settings.save(LOG);
        System.exit(0);
    }

    private void createEvents() {
        addFirstButtons();
        addSecondButtons();
        addThirdButtons();
        addFourthButtons();
        addFifthButtons();
        addFileButtons();
    }

    private void createSettingFolders() {
        File folder;
        folder = new File(Settings.getImgFldr());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        folder = new File(Settings.getRprtImgFldr());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        folder = new File(Settings.getRprtSvFldr());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        folder = new File(Settings.getRtgFldr());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        folder = new File(Settings.getSvFldr());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        folder = new File(Settings.getSrchAreaFldr());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void initComponents() {
        setIconImage(Constants.SAM_ICON.getImage());
        setTitle("MissionControl");
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) Math.round(screenSize.getWidth() * 0.75), (int) Math.round(screenSize.getHeight() * 0.75));
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        final JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        final JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        final JMenu mnHelp = new JMenu("?");
        menuBar.add(mnHelp);

        mntmLocation = new JCheckBoxMenuItem("Get Location");
        mntmLocation.setIcon(new ImageIcon(Main.class.getResource(Constants.ICON_DIR
                + "location_icon_mini.png")));
        mnFile.add(mntmLocation);

        mntmLogging = new JMenuItem("Log");
        mntmLogging.setIcon(new ImageIcon(Main.class.getResource(Constants.ICON_DIR
                + "logging_icon_mini.png")));
        mnFile.add(mntmLogging);

        mntmOptions = new JMenuItem("Options");
        mntmOptions.setIcon(new ImageIcon(Main.class.getResource(Constants.ICON_DIR
                + "options_icon_mini.png")));
        mnFile.add(mntmOptions);

        mntmExit = new JMenuItem("Exit");
        mntmExit.setIcon(new ImageIcon(Main.class.getResource(Constants.ICON_DIR
                + "exit_icon_mini.png")));
        mnFile.add(mntmExit);

        mntmCredits = new JMenuItem("Credits");
        mnHelp.add(mntmCredits);

        mntmLocation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                Data.setCurrentLocationIsActive(mntmLocation.isSelected());

                if (mntmLocation.isSelected()) {
                    LOG.info("Start polling aircraft data...");
                } else {
                    LOG.info("Stop polling aircraft data...");
                }
            }
        });

        final Component horizontalGlue = Box.createHorizontalGlue();
        menuBar.add(horizontalGlue);

        final JLabel lblLocation = new JLabel("Location: ");
        lblLocation.setHorizontalAlignment(SwingConstants.RIGHT);
        menuBar.add(lblLocation);

        lblCoor = new JLabel("lng: - , lat: - , alt: -");
        final Font fontCoor = lblCoor.getFont();
        final Font boldFontCoor = new Font(fontCoor.getFontName(), Font.BOLD, fontCoor.getSize());
        lblCoor.setFont(boldFontCoor);
        menuBar.add(lblCoor);

        oneHorizontalGlue = Box.createHorizontalGlue();
        menuBar.add(oneHorizontalGlue);

        final JLabel label = new JLabel("Next waypoint's task: ");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        menuBar.add(label);

        lblTask = new JLabel("-");
        final Font fontTask = lblTask.getFont();
        final Font boldFontTask = new Font(fontTask.getFontName(), Font.BOLD, fontTask.getSize());
        lblTask.setFont(boldFontTask);
        menuBar.add(lblTask);

        twoHorizontalGlue = Box.createHorizontalGlue();
        menuBar.add(twoHorizontalGlue);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
        setContentPane(contentPane);

        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                offsetCloud1++;
                offsetCloud2++;
                offsetCloud3 = offsetCloud3 + 2;
                offsetCloud4 = offsetCloud4 + 2;

                if (offsetCloud1 > getWidth()) {
                    offsetCloud1 = -350;
                    person1 = credits.getNextPerson();
                }
                if (offsetCloud2 > getWidth()) {
                    offsetCloud2 = -400;
                    person2 = credits.getNextPerson();
                }
                if (offsetCloud3 > getWidth()) {
                    offsetCloud3 = -250;
                    person3 = credits.getNextPerson();
                }
                if (offsetCloud4 > getWidth()) {
                    offsetCloud4 = -300;
                    person4 = credits.getNextPerson();
                }
                desktopPane.repaint();
            }
        });

        desktopPane = new JDesktopPane() {

            BuildProperties bp = new BuildProperties();
            ImageIcon backgroundIcon = new ImageIcon(Main.class.getResource(Constants.IMG_LOC_BG));
            Image backgroundImage = backgroundIcon.getImage();
            ImageIcon titleIcon = new ImageIcon(Main.class.getResource(Constants.IMG_LOC_TITLE));
            Image titleImage = titleIcon.getImage();
            ImageIcon titleMcIcon = new ImageIcon(
                    Main.class.getResource(Constants.IMG_LOC_TITLE_MC));
            Image titleMcImage = titleMcIcon.getImage();
            ImageIcon cloudIcon = new ImageIcon(Main.class.getResource(Constants.IMG_LOC_CLOUD));
            Image cloudImage = cloudIcon.getImage();
            ImageIcon planeIcon = new ImageIcon(Main.class.getResource(Constants.IMG_LOC_PLANE));
            Image planeImage = planeIcon.getImage();

            private void drawString(final Graphics g, final String text, final int x, int y) {
                for (final String line : text.split(";")) {
                    g.setColor(Color.GRAY);
                    g.drawString(line, x, y += g.getFontMetrics().getHeight());
                }
            }

            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);

                final Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                g2.drawImage(cloudImage, offsetCloud1, 25, 350, 224, this);
                drawString(g, person1, (offsetCloud1 + (350 / 2)) - 50, (25 + (224 / 2)) - 25);

                g2.drawImage(cloudImage, offsetCloud2, getHeight() - 550, 400, 256, this);
                drawString(g, person2, (offsetCloud2 + (400 / 2)) - 50,
                        ((getHeight() - 550) + (256 / 2)) - 25);

                g2.drawImage(cloudImage, offsetCloud3, getHeight() - 250, 250, 160, this);
                drawString(g, person3, (offsetCloud3 + (250 / 2)) - 50,
                        ((getHeight() - 250) + (160 / 2)) - 25);

                g2.drawImage(cloudImage, offsetCloud4, 250, 300, 192, this);
                drawString(g, person4, (offsetCloud4 + (300 / 2)) - 50, (250 + (192 / 2)) - 25);

                g2.drawImage(titleImage, getWidth() - 500 - 50, 0 + 50, 500, 32, this);
                g2.drawImage(titleMcImage, getWidth() - 450 - 50, 0 + 100, 450, 32, this);
                g2.drawImage(planeImage, 250, 150, 500, 276, this);

                g.setColor(Color.BLACK);
                g.drawString("Version: " + bp.getVersion(), 5, getHeight() - 65);
                g.drawString("Commit: " + bp.getBuild(), 5, getHeight() - 50);
                g.drawString("Branch: " + bp.getBranch(), 5, getHeight() - 35);
                g.drawString("Built by: " + bp.getBuiltBy(), 5, getHeight() - 20);
                g.drawString("Date: " + bp.getDate(), 5, getHeight() - 5);
            }
        };

        final GroupLayout glContentPane = new GroupLayout(contentPane);
        glContentPane.setHorizontalGroup(glContentPane
                .createParallelGroup(Alignment.LEADING)
                .addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                .addComponent(desktopPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 774,
                        Short.MAX_VALUE));
        glContentPane.setVerticalGroup(glContentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(
                        glContentPane
                        .createSequentialGroup()
                        .addComponent(toolBar, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, 479,
                                        Short.MAX_VALUE)));
        final GroupLayout glDesktopPane = new GroupLayout(desktopPane);
        glDesktopPane.setHorizontalGroup(glDesktopPane.createParallelGroup(Alignment.LEADING)
                .addGap(0, 615, Short.MAX_VALUE));
        glDesktopPane.setVerticalGroup(glDesktopPane.createParallelGroup(Alignment.LEADING).addGap(
                0, 318, Short.MAX_VALUE));
        desktopPane.setLayout(glDesktopPane);

        btnReportSheet = new JButton("ReportSheet");
        btnReportSheet.setIcon(Constants.REPORT_ICON);
        toolBar.add(btnReportSheet);

        btnImageviewer = new JButton("ImgViewer");
        btnImageviewer.setIcon(Constants.IMAGE_ICON);
        toolBar.add(btnImageviewer);

        btnSric = new JButton("SRIC");
        btnSric.setIcon(Constants.SRIC_ICON);
        toolBar.add(btnSric);

        btnRouting = new JButton("Routing");
        btnRouting.setIcon(Constants.ROUTING_ICON);
        toolBar.add(btnRouting);

        btnIr = new JButton("IR");
        btnIr.setIcon(Constants.IR_ICON);
        toolBar.add(btnIr);

        btnEmergent = new JButton("Emergent");
        btnEmergent.setIcon(Constants.EMERGENT_ICON);
        toolBar.add(btnEmergent);

        btnGPStable = new JButton("GPS Table");
        btnGPStable.setIcon(Constants.GPS_ICON);
        toolBar.add(btnGPStable);

        btnSearchArea = new JButton("SearchArea");
        btnSearchArea.setIcon(Constants.SEARCHAREA_ICON);
        toolBar.add(btnSearchArea);

        btnSearchAreaViewer = new JButton("SearchArea Viewer");
        btnSearchAreaViewer.setIcon(Constants.SEARCHAREA_ICON);
        toolBar.add(btnSearchAreaViewer);

        btnAirdrop = new JButton("AirDrop");
        btnAirdrop.setIcon(Constants.AIRDROP_ICON);
        toolBar.add(btnAirdrop);

        btnOffAxis = new JButton("Off-Axis");
        btnOffAxis.setIcon(Constants.OFFAXIS_ICON);
        toolBar.add(btnOffAxis);

        btnCamera = new JButton("Camera");
        btnCamera.setIcon(Constants.CAMERA_ICON);
        toolBar.add(btnCamera);

        btnControl = new JButton("Control");
        btnControl.setIcon(Constants.CONTROL_ICON);
        toolBar.add(btnControl);

        btnSecretMessage = new JButton("Secret Message");
        btnSecretMessage.setIcon(Constants.SECRETMESSAGE_ICON);
        toolBar.add(btnSecretMessage);

        contentPane.setLayout(glContentPane);
    }

    private void loadWindows() {
        if (airdrop != null) {
            airdrop.load();
        }
        if (camera != null) {
            camera.load();
        }
        if (control != null) {
            control.load();
        }
        if (emergent != null) {
            emergent.load();
        }
        if (gpsviewer != null) {
            gpsviewer.load();
        }
        if (imageViewer != null) {
            imageViewer.load();
        }
        if (ir != null) {
            ir.load();
        }
        if (offAxis != null) {
            offAxis.load();
        }
        if (reportSheet != null) {
            reportSheet.load();
        }
        if (routing != null) {
            routing.load();
        }
        if (searchAreaTask != null) {
            searchAreaTask.load();
        }
        if (searchAreaViewer != null) {
            searchAreaViewer.load();
        }
        if (secretMessage != null) {
            secretMessage.load();
        }
        if (sric != null) {
            sric.load();
        }
    }

    private void openWindows() {
        airdrop = new Airdrop(Constants.AIRDROP_TITLE, Constants.AIRDROP_MINI_ICON);
        desktopPane.add(airdrop);
        airdrop.show();
        airdrop.setVisible(false);
        camera = new Camera(Constants.CAMERA_TITLE, Constants.CAMERA_MINI_ICON);
        desktopPane.add(camera);
        camera.show();
        camera.setVisible(false);
        control = new Control(Constants.CONTROL_TITLE, Constants.CONTROL_MINI_ICON);
        desktopPane.add(control);
        control.show();
        control.setVisible(false);
        emergent = new Emergent(Constants.EMERGENT_TITLE, Constants.EMERGENT_MINI_ICON);
        desktopPane.add(emergent);
        emergent.show();
        emergent.setVisible(false);
        gpsviewer = new GpsViewer(Constants.GPS_TITLE, Constants.GPS_MINI_ICON);
        desktopPane.add(gpsviewer);
        gpsviewer.show();
        gpsviewer.setVisible(false);
        imageViewer = new ImageViewer(Constants.IMAGE_TITLE, Constants.IMAGE_MINI_ICON);
        desktopPane.add(imageViewer);
        imageViewer.show();
        imageViewer.setVisible(false);
        ir = new Ir(Constants.IR_TITLE, Constants.IR_MINI_ICON);
        desktopPane.add(ir);
        ir.show();
        ir.setVisible(false);
        offAxis = new OffAxis(Constants.OFFAXIS_TITLE, Constants.OFFAXIS_MINI_ICON);
        desktopPane.add(offAxis);
        offAxis.show();
        offAxis.setVisible(false);
        reportSheet = new ReportSheet(Constants.REPORT_TITLE, Constants.REPORT_MINI_ICON);
        desktopPane.add(reportSheet);
        reportSheet.show();
        reportSheet.setVisible(false);
        routing = new Routing(Constants.ROUTING_TITLE, Constants.ROUTING_MINI_ICON);
        desktopPane.add(routing);
        routing.show();
        routing.setVisible(false);
        searchAreaTask = new SearchArea(Constants.SEARCHAREA_TASK_TITLE,
                Constants.SEARCHAREA_MINI_ICON);
        desktopPane.add(searchAreaTask);
        searchAreaTask.show();
        searchAreaTask.setVisible(false);
        searchAreaViewer = new SearchAreaViewer(Constants.SEARCHAREA_VIEWER_TITLE,
                Constants.SEARCHAREA_MINI_ICON);
        desktopPane.add(searchAreaViewer);
        searchAreaViewer.show();
        searchAreaViewer.setVisible(false);
        secretMessage = new SecretMessage(Constants.SECRETMESSAGE_TITLE,
                Constants.SECRETMESSAGE_MINI_ICON);
        desktopPane.add(secretMessage);
        secretMessage.show();
        secretMessage.setVisible(false);
        sric = new SRIC(Constants.SRIC_TITLE, Constants.SRIC_MINI_ICON);
        desktopPane.add(sric);
        sric.show();
        sric.setVisible(false);
    }

    private void saveWindows() {
        if (airdrop != null) {
            airdrop.save();
        }
        if (camera != null) {
            camera.save();
        }
        if (control != null) {
            control.save();
        }
        if (emergent != null) {
            emergent.save();
        }
        if (gpsviewer != null) {
            gpsviewer.save();
        }
        if (imageViewer != null) {
            imageViewer.save();
        }
        if (ir != null) {
            ir.save();
        }
        if (offAxis != null) {
            offAxis.save();
        }
        if (reportSheet != null) {
            reportSheet.save();
        }
        if (routing != null) {
            routing.save();
        }
        if (searchAreaTask != null) {
            searchAreaTask.save();
        }
        if (searchAreaViewer != null) {
            searchAreaViewer.save();
        }
        if (secretMessage != null) {
            secretMessage.save();
        }
        if (sric != null) {
            sric.save();
        }
    }
}
