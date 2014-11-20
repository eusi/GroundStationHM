package cs.hm.edu.sam.mc.main;

import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import cs.hm.edu.sam.mc.airdrop.Airdrop;
import cs.hm.edu.sam.mc.emergent.Emergent;
import cs.hm.edu.sam.mc.gpsTable.GPSViewer;
import cs.hm.edu.sam.mc.gpsTable.GpsTable;
import cs.hm.edu.sam.mc.images.ImageViewer;
import cs.hm.edu.sam.mc.ir.Ir;
import cs.hm.edu.sam.mc.misc.CONSTANTS;
import cs.hm.edu.sam.mc.misc.Data;
import cs.hm.edu.sam.mc.options.Options;
import cs.hm.edu.sam.mc.report.ReportSheet;
import cs.hm.edu.sam.mc.rest.RESTClient;
import cs.hm.edu.sam.mc.routing.Routing;
import cs.hm.edu.sam.mc.searchArea.SearchArea;
import cs.hm.edu.sam.mc.sric.SRIC;

/**
 * Main class. This main class contains the outer gui window.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class Main extends JFrame {

    private JPanel contentPane;
    private JButton btnReportSheet;
    private JButton btnImageviewer;
    private JButton btnSric;
    private JButton btnIr;
    private JButton btnEmergent;
    private JButton btnGPStable;
    private JButton btnSearchArea;
    private JButton btnAirdrop;
    private ReportSheet reportSheet;
    private SRIC sric;
    private Routing routing;
    private Ir ir;
    private Emergent emergent;
    private GPSViewer gpsviewer;
    private SearchArea searchArea;
    private Airdrop airdrop;
    private Options options;
    private ImageViewer imageViewer;
    private JDesktopPane desktopPane;
    private JButton btnRouting;
    private JLabel lblCoor;
    private JCheckBoxMenuItem mntmLocation;
    private JMenuItem mntmOptions;
    private JMenuItem mntmExit;

    /**
     * Launch the application.
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final Main frame = new Main();
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Main() {
        initComponents();
        createEvents();
        new Thready("currentLocation").start();
    }

    private void initComponents() {
        setTitle("MissionControl");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 800);

        final JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        final JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        mntmLocation = new JCheckBoxMenuItem("Get Location");
        mntmLocation.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
                + "location_icon_mini.png")));
        mnFile.add(mntmLocation);

        mntmOptions = new JMenuItem("Options");
        mntmOptions.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
                + "options_icon_mini.png")));
        mnFile.add(mntmOptions);

        mntmExit = new JMenuItem("Exit");
        mntmExit.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
                + "exit_icon_mini.png")));
        mnFile.add(mntmExit);

        mntmLocation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Data.setCurrentLocationIsActive(mntmLocation.isSelected());
            }
        });

        menuBar.add(Box.createHorizontalGlue());

        final JLabel lblLocation = new JLabel("Location: ");
        lblLocation.setHorizontalAlignment(SwingConstants.RIGHT);
        menuBar.add(lblLocation);

        lblCoor = new JLabel("lng: 0, lat: 0, alt: 0");
        menuBar.add(lblCoor);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        desktopPane = new JDesktopPane();
        desktopPane.setBackground(SystemColor.activeCaption);
        final GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane
                .createParallelGroup(Alignment.LEADING)
                .addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                .addComponent(desktopPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 774,
                        Short.MAX_VALUE));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(
                        gl_contentPane
                        .createSequentialGroup()
                        .addComponent(toolBar, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(desktopPane, GroupLayout.DEFAULT_SIZE, 479,
                                        Short.MAX_VALUE)));
        final GroupLayout gl_desktopPane = new GroupLayout(desktopPane);
        gl_desktopPane.setHorizontalGroup(gl_desktopPane.createParallelGroup(Alignment.LEADING)
                .addGap(0, 615, Short.MAX_VALUE));
        gl_desktopPane.setVerticalGroup(gl_desktopPane.createParallelGroup(Alignment.LEADING)
                .addGap(0, 318, Short.MAX_VALUE));
        desktopPane.setLayout(gl_desktopPane);

        btnReportSheet = new JButton("ReportSheet");
        btnReportSheet.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
                + "report_icon.png")));
        toolBar.add(btnReportSheet);

        btnImageviewer = new JButton("ImageViewer");
        btnImageviewer.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
                + "image_icon.png")));
        toolBar.add(btnImageviewer);

        btnSric = new JButton("SRIC");
        btnSric.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR + "sric_icon.png")));
        toolBar.add(btnSric);

        btnRouting = new JButton("Routing");
        btnRouting.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
                + "routing_icon.png")));
        toolBar.add(btnRouting);

        btnIr = new JButton("IR");
        btnIr.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR + "ir_icon.png")));
        toolBar.add(btnIr);

        btnEmergent = new JButton("Emergent");
        btnEmergent.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
                + "emergent_icon.png")));
        toolBar.add(btnEmergent);
        
        btnGPStable = new JButton("GPS Table");
        btnGPStable.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
                + "gps_icon.png")));
        toolBar.add(btnGPStable);
        
        btnSearchArea = new JButton("SearchArea");
        btnSearchArea.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
            + "searcharea_icon.png")));
        toolBar.add(btnSearchArea);
        
        btnAirdrop = new JButton("AirDrop");
        btnAirdrop.setIcon(new ImageIcon(Main.class.getResource(CONSTANTS.ICON_DIR
            + "airdrop_icon.png")));
        toolBar.add(btnAirdrop);

        contentPane.setLayout(gl_contentPane);
    }

    private void createEvents() {
        btnReportSheet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (reportSheet == null) {
                    reportSheet = new ReportSheet(); // only one instace of this
                    desktopPane.add(reportSheet);
                    reportSheet.show();
                } else if (!reportSheet.isVisible()) {
                    reportSheet.setVisible(true);
                } else {
                    reportSheet.moveToFront();
                }
            }
        });

        btnImageviewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (imageViewer == null) {
                    imageViewer = new ImageViewer(); // only one instace of this
                    desktopPane.add(imageViewer);
                    imageViewer.show();
                } else if (!imageViewer.isVisible()) {
                    imageViewer.setVisible(true);
                } else {
                    imageViewer.moveToFront();
                }
            }
        });

        btnSric.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (sric == null) {
                    sric = new SRIC(); // only one instance of this
                    desktopPane.add(sric);
                    sric.show();
                } else if (!sric.isVisible()) {
                    sric.setVisible(true);
                } else {
                    sric.moveToFront();
                }
            }
        });

        btnRouting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (routing == null) {
                    routing = new Routing(); // only one instance of this
                    desktopPane.add(routing);
                    routing.show();
                } else if (!routing.isVisible()) {
                    routing.setVisible(true);
                } else {
                    routing.moveToFront();
                }
            }
        });

        btnIr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (ir == null) {
                    ir = new Ir(); // only one instance of this
                    desktopPane.add(ir);
                    ir.show();
                } else if (!ir.isVisible()) {
                    ir.setVisible(true);
                } else {
                    ir.moveToFront();
                }
            }
        });

        btnEmergent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (emergent == null) {
                    emergent = new Emergent(); // only one instance of this
                    desktopPane.add(emergent);
                    emergent.show();
                } else if (!emergent.isVisible()) {
                    emergent.setVisible(true);
                } else {
                    emergent.moveToFront();
                }
            }
        });
        
        btnGPStable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (gpsviewer == null) {
                	gpsviewer = new GPSViewer(); // only one instance of this
                    desktopPane.add(gpsviewer);
                    gpsviewer.show();
                } else if (!gpsviewer.isVisible()) {
                	gpsviewer.setVisible(true);
                } else {
                	gpsviewer.moveToFront();
                }
            }
        });
        
        btnAirdrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (airdrop == null) {
                  airdrop = new Airdrop(); // only one instance of this
                    desktopPane.add(airdrop);
                    airdrop.show();
                } else if (!airdrop.isVisible()) {
                  airdrop.setVisible(true);
                } else {
                  airdrop.moveToFront();
                }
            }
        });
          
          btnSearchArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (searchArea == null) {
                  searchArea = new SearchArea(); // only one instance of this
                    desktopPane.add(searchArea);
                    searchArea.show();
                } else if (!gpsviewer.isVisible()) {
                  searchArea.setVisible(true);
                } else {
                  searchArea.moveToFront();
                }
            }
        });

        mntmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        mntmOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (options == null) {
                    options = new Options();
                }
                options.setVisible(true);
            }
        });
    }

    private class Thready extends Thread {
        public Thready(String str) {
            super(str);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (Data.isCurrentLocationActive()) {
                        RESTClient.getCurrentPosition();

                        if (Data.getCurrentPosition() != null) {
                            lblCoor.setText("lng: " + Data.getCurrentPosition().getLng()
                                    + " , lat: " + Data.getCurrentPosition().getLat() + " , alt: "
                                    + Data.getCurrentPosition().getAlt());
                            
                            GpsTable.writeGpsTable( Data.getCurrentPosition().toGpsTable() );
                        }
                    }

                    // sleep x sec
                    sleep(CONSTANTS.GET_CURRENT_POSITION_DELAY);
                } catch (final InterruptedException e) {
                }
            }
        }
    }
}
