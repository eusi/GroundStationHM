package hm.edu.main;

import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

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
    private ReportSheet reportSheet;
    private SRIC sric;
    private ImageViewerGUI imageViewer;
    private JDesktopPane desktopPane;

    /**
     * Launch the application.
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                final Main frame = new Main();
                frame.setVisible(true);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public Main() {
        initComponents();
        createEvents();
    }

    private void initComponents() {
        setTitle("MissionControl");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        final JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        final JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        final JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(e -> System.exit(0));
        mntmExit.setIcon(new ImageIcon(Main.class
                .getResource("/icons/full/message_error.gif")));
        mnFile.add(mntmExit);
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
                .addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 615,
                        Short.MAX_VALUE)
                .addComponent(desktopPane, Alignment.TRAILING,
                        GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(
                Alignment.LEADING).addGroup(
                gl_contentPane
                        .createSequentialGroup()
                        .addComponent(toolBar, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(desktopPane, GroupLayout.DEFAULT_SIZE,
                                479, Short.MAX_VALUE)));
        final GroupLayout gl_desktopPane = new GroupLayout(desktopPane);
        gl_desktopPane.setHorizontalGroup(gl_desktopPane.createParallelGroup(
                Alignment.LEADING).addGap(0, 615, Short.MAX_VALUE));
        gl_desktopPane.setVerticalGroup(gl_desktopPane.createParallelGroup(
                Alignment.LEADING).addGap(0, 318, Short.MAX_VALUE));
        desktopPane.setLayout(gl_desktopPane);

        btnReportSheet = new JButton("ReportSheet");
        btnReportSheet
                .setIcon(new ImageIcon(
                        Main.class
                                .getResource("/com/sun/java/swing/plaf/windows/icons/JavaCup32.png")));
        toolBar.add(btnReportSheet);

        btnImageviewer = new JButton("ImageViewer");
        btnImageviewer
                .setIcon(new ImageIcon(
                        Main.class
                                .getResource("/com/sun/java/swing/plaf/windows/icons/JavaCup32.png")));
        toolBar.add(btnImageviewer);

        btnSric = new JButton("SRIC");
        btnSric.setIcon(new ImageIcon(
                Main.class
                        .getResource("/com/sun/java/swing/plaf/windows/icons/JavaCup32.png")));
        toolBar.add(btnSric);
        contentPane.setLayout(gl_contentPane);
    }

    private void createEvents() {
        btnReportSheet.addActionListener(arg0 -> {
            if (reportSheet == null || reportSheet.isClosed()) {
                reportSheet = new ReportSheet(); // only one instace of this
                desktopPane.add(reportSheet);
                reportSheet.show();
            }
        });

        btnImageviewer.addActionListener(arg0 -> {
            if (imageViewer == null || imageViewer.isClosed()) {
                imageViewer = new ImageViewerGUI(); // only one instance of this
                desktopPane.add(imageViewer);
                imageViewer.show();
            }
        });

        btnSric.addActionListener(arg0 -> {
            if (sric == null || sric.isClosed()) {
                sric = new SRIC(); // only one instance of this
                desktopPane.add(sric);
                sric.show();
            }
        });
    }
}
