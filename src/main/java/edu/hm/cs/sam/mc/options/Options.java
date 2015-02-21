package edu.hm.cs.sam.mc.options;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Settings;
import edu.hm.cs.sam.mc.rest.RestClient;

/**
 * @author Opitz, Jan (jopitz@hm.edu)
 * @author Christoph Friegel
 * @version 07.01.2015 - 10:29:25
 */
@SuppressWarnings("serial")
public class Options extends JFrame {

    private static final Logger LOG = Logger.getLogger(Options.class.getName());

    private final JPanel pnlMain = new JPanel();
    private final JPanel pnlSettings = new JPanel();
    private final JPanel pnlTests = new JPanel();
    private final JPanel pnlClose = new JPanel();
    private final JPanel pnlSave = new JPanel();
    private final JButton btnSave = new JButton("apply & save");
    private final JButton btnTestWaypoints = new JButton("TestSetWaypoints");
    private final JButton btnTestZone = new JButton("TestSetZone");
    private final JButton btnTestTarget = new JButton("TestSetTarget");
    private final JButton btnTestObstacles = new JButton(
            "<html>TestGetObstacles<br>TestGetWind<br>TextGetNextWaypoint</html>");
    private final JButton btnTestStopSric = new JButton("TestStopSRIC");
    private final JButton btnTestLogging = new JButton("TestLogging");
    private final JButton btnTestCollection = new JButton("TestSuite");
    private final JButton btnTestConn = new JButton("TestConnection");
    private final JLabel lblMpip = new JLabel("MissonPlanner-IP");
    private final JTextField txtMpip = new JTextField(Settings.getRestMp());
    private final JLabel lblRestTest = new JLabel("Current REST Interface: "
            + Settings.getRestMpUrl());
    private final JLabel lblImgFldr = new JLabel("Image Folder");
    private final JTextField txtImgFldr = new JTextField(Settings.getImgFldr());
    private final JLabel lblRprtImgFldr = new JLabel("Report Image Folder");
    private final JTextField txtRprtImgFldr = new JTextField(Settings.getRprtImgFldr());
    private final JLabel lblRprtSvFldr = new JLabel("Report Save Folder");
    private final JTextField txtRprtSvFldr = new JTextField(Settings.getRprtSvFldr());
    private final JLabel lblRtgFldr = new JLabel("Routing Folder");
    private final JTextField txtRtgFldr = new JTextField(Settings.getRtgFldr());
    private final JLabel lblSvFldr = new JLabel("Save Folder");
    private final JTextField txtSvFldr = new JTextField(Settings.getSvFldr());
    private final JLabel lblSrchAreaFldr = new JLabel("Search area Folder");
    private final JTextField txtSrchAreaFldr = new JTextField(Settings.getSrchAreaFldr());
    private final JLabel lblDefaultLocalhost = new JLabel("HM-Test-Server: 10.28.2.162:8000");

    /**
     * Create the frame.
     */
    public Options() {
    	setAlwaysOnTop(true);
        createFrame();
        formatFrame();
        addSettings();
        addTests();
        addButtons();
        addActionListeners();
    }

    private void addActionListeners() {
        btnSave.addActionListener(new SettingsSaveAction(this));

        btnTestWaypoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                TestData.createAirdrop();
                TestData.createSric();
                TestData.createSearchArea();
            }
        });

        btnTestZone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                TestData.createEmergentZone();
                TestData.createNoFlightZone();
                TestData.createSearchAreaZone();
            }
        });

        btnTestTarget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                TestData.createAirdropTarget();
                TestData.createSricTarget();
                TestData.createIrTarget();
                TestData.createOffAxisTarget();
            }
        });

        btnTestObstacles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                RestClient.getObstacles();
                RestClient.getNextPosition();
                RestClient.getCurrentWindData();
            }
        });

        btnTestStopSric.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                RestClient.stopSRICTask();
            }
        });

        btnTestLogging.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                LOG.debug("TEST");
                LOG.error("TEST");
                LOG.warn("TEST");
                LOG.info("TEST");
            }
        });

        btnTestCollection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                TestData.createAirdrop();
                TestData.createSric();
                TestData.createSearchArea();
                TestData.createAirdropTarget();
                TestData.createSricTarget();
                TestData.createIrTarget();
                TestData.createOffAxisTarget();
                TestData.createEmergentZone();
                TestData.createNoFlightZone();
                TestData.createSearchAreaZone();
            }
        });

        btnTestConn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                RestClient.testRestConnection();
            }
        });
    }

    private void addButtons() {
        pnlSave.add(Box.createHorizontalGlue());
        pnlSave.add(btnSave);
        pnlClose.add(Box.createHorizontalGlue());
    }

    private void addSettings() {
        final JPanel pnlMpip = new JPanel();
        pnlMpip.setLayout(new GridLayout(0, 2, 5, 5));
        pnlSettings.add(pnlMpip);
        pnlMpip.add(lblMpip);
        pnlMpip.add(txtMpip);
        final JPanel pnlDefaultLocalhost = new JPanel();
        pnlDefaultLocalhost.setLayout(new GridLayout(0, 1, 5, 5));
        pnlSettings.add(pnlDefaultLocalhost);
        pnlDefaultLocalhost.add(lblDefaultLocalhost);
        lblDefaultLocalhost.setForeground(Color.GRAY);
        final JPanel pnlRestTest = new JPanel();
        pnlRestTest.setLayout(new GridLayout(0, 1, 5, 5));
        pnlSettings.add(pnlRestTest);
        pnlRestTest.add(lblRestTest);
        lblRestTest.setForeground(Color.GRAY);
        final JPanel pnlImgFldr = new JPanel();
        pnlImgFldr.setLayout(new GridLayout(0, 2, 5, 5));
        pnlSettings.add(pnlImgFldr);
        pnlImgFldr.add(lblImgFldr);
        pnlImgFldr.add(txtImgFldr);
        final JPanel pnlRprtImgFldr = new JPanel();
        pnlRprtImgFldr.setLayout(new GridLayout(0, 2, 5, 5));
        pnlSettings.add(pnlRprtImgFldr);
        pnlRprtImgFldr.add(lblRprtImgFldr);
        pnlRprtImgFldr.add(txtRprtImgFldr);
        final JPanel pnlRprtSvFldr = new JPanel();
        pnlRprtSvFldr.setLayout(new GridLayout(0, 2, 5, 5));
        pnlSettings.add(pnlRprtSvFldr);
        pnlRprtSvFldr.add(lblRprtSvFldr);
        pnlRprtSvFldr.add(txtRprtSvFldr);
        final JPanel pnlRtgFldr = new JPanel();
        pnlRtgFldr.setLayout(new GridLayout(0, 2, 5, 5));
        pnlSettings.add(pnlRtgFldr);
        pnlRtgFldr.add(lblRtgFldr);
        pnlRtgFldr.add(txtRtgFldr);
        final JPanel pnlSvFldr = new JPanel();
        pnlSvFldr.setLayout(new GridLayout(0, 2, 5, 5));
        pnlSettings.add(pnlSvFldr);
        pnlSvFldr.add(lblSvFldr);
        pnlSvFldr.add(txtSvFldr);
        final JPanel pnlSrchAreaFldr = new JPanel();
        pnlSrchAreaFldr.setLayout(new GridLayout(0, 2, 5, 5));
        pnlSettings.add(pnlSrchAreaFldr);
        pnlSrchAreaFldr.add(lblSrchAreaFldr);
        pnlSrchAreaFldr.add(txtSrchAreaFldr);
    }

    private void addTests() {
        pnlTests.add(btnTestWaypoints);
        pnlTests.add(btnTestZone);
        pnlTests.add(btnTestTarget);
        pnlTests.add(btnTestObstacles);
        pnlTests.add(btnTestStopSric);
        pnlTests.add(btnTestLogging);
        pnlTests.add(btnTestCollection);
        pnlTests.add(btnTestConn);
    }

    private void createFrame() {
        setIconImage(Constants.SAM_ICON.getImage());
        setTitle(Constants.OPTIONS_TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(Constants.OPTIONS_DEFAULT_SIZE, Constants.OPTIONS_DEFAULT_SIZE);
        setContentPane(pnlMain);
    }

    private void formatFrame() {
        pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.PAGE_AXIS));
        pnlMain.add(pnlSettings);
        pnlSettings.setLayout(new GridLayout(9, 0, 5, 5));
        pnlMain.add(pnlSave);
        pnlMain.add(Box.createVerticalGlue());
        pnlSave.setLayout(new BoxLayout(pnlSave, BoxLayout.LINE_AXIS));
        pnlMain.add(pnlTests);
        pnlTests.setLayout(new GridLayout(2, 4, 5, 5));
        pnlMain.add(pnlClose);
        pnlClose.setLayout(new BoxLayout(pnlClose, BoxLayout.LINE_AXIS));
    }

    JLabel getLblRestTest() {
        return lblRestTest;
    }

    JTextField getTxtImgFldr() {
        return txtImgFldr;
    }

    JTextField getTxtMpip() {
        return txtMpip;
    }

    JTextField getTxtRprtImgFldr() {
        return txtRprtImgFldr;
    }

    JTextField getTxtRprtSvFldr() {
        return txtRprtSvFldr;
    }

    JTextField getTxtRtgFldr() {
        return txtRtgFldr;
    }

    JTextField getTxtSrchAreaFldr() {
        return txtSrchAreaFldr;
    }

    JTextField getTxtSvFldr() {
        return txtSvFldr;
    }
}
