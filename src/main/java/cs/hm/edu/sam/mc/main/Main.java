package cs.hm.edu.sam.mc.main;

import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
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

import cs.hm.edu.sam.mc.emergency.Emergency;
import cs.hm.edu.sam.mc.images.ImageViewer;
import cs.hm.edu.sam.mc.ir.Ir;
import cs.hm.edu.sam.mc.misc.CONSTANTS;
import cs.hm.edu.sam.mc.misc.Data;
import cs.hm.edu.sam.mc.misc.RESTClient;
import cs.hm.edu.sam.mc.report.ReportSheet;
import cs.hm.edu.sam.mc.routing.Routing;
import cs.hm.edu.sam.mc.sric.SRIC;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JCheckBoxMenuItem;

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
    private JButton btnEmergency;
    private ReportSheet reportSheet;
    private SRIC sric;
    private Routing routing;
    private Ir ir;
    private Emergency emergency;
    private ImageViewer imageViewer;
    private JDesktopPane desktopPane;
    private JButton btnRouting;
    private JLabel lblCoor;
    private JCheckBoxMenuItem chckbxmntmGetLocation;
    
    private final String icondir = CONSTANTS.ICON_DIR;

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
        new Thready("currentLocation").start();
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
        
        chckbxmntmGetLocation = new JCheckBoxMenuItem("Get Location");
        mnFile.add(chckbxmntmGetLocation);
        mntmExit.setIcon(new ImageIcon(Main.class.getResource(icondir + "exit_icon.png")));
        mnFile.add(mntmExit);
        
        chckbxmntmGetLocation.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            		Data.setCurrentLocationIsActive( chckbxmntmGetLocation.isSelected() );
            }
          });
        
        menuBar.add(Box.createHorizontalGlue());
        
        JLabel lblLocation = new JLabel("Location: ");
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
        btnReportSheet.setIcon(new ImageIcon(Main.class.getResource(icondir + "report_icon.png")));
        toolBar.add(btnReportSheet);

        btnImageviewer = new JButton("ImageViewer");
        btnImageviewer.setIcon(new ImageIcon(Main.class.getResource(icondir + "image_icon.png")));
        toolBar.add(btnImageviewer);

        btnSric = new JButton("SRIC");
        btnSric.setIcon(new ImageIcon(Main.class.getResource(icondir + "sric_icon.png")));
        toolBar.add(btnSric);

        btnRouting = new JButton("Routing");
        btnRouting.setIcon(new ImageIcon(Main.class.getResource(icondir + "routing_icon.png")));
        toolBar.add(btnRouting);
        
        btnIr = new JButton("IR");
        btnIr.setIcon(new ImageIcon(Main.class.getResource(icondir + "ir_icon.png")));
        toolBar.add(btnIr);
        
        btnEmergency = new JButton("Emergency");
        btnEmergency.setIcon(new ImageIcon(Main.class.getResource(icondir + "emergency_icon.png")));
        toolBar.add(btnEmergency);

        contentPane.setLayout(gl_contentPane);
    }

    private void createEvents() {
        btnReportSheet.addActionListener(arg0 -> {
            if (reportSheet == null || reportSheet.isClosed()) {
                reportSheet = new ReportSheet(); // only one instace of this
                desktopPane.add(reportSheet);
                reportSheet.show();
            } else {
                reportSheet.moveToFront();
            }
        });

        btnImageviewer.addActionListener(arg0 -> {
            if (imageViewer == null || imageViewer.isClosed()) {
                imageViewer = new ImageViewer(); // only one instance of this
                desktopPane.add(imageViewer);
                imageViewer.show();
            } else {
                imageViewer.moveToFront();
            }
        });

        btnSric.addActionListener(arg0 -> {
            if (sric == null || sric.isClosed()) {
                sric = new SRIC(); // only one instance of this
                desktopPane.add(sric);
                sric.show();
            } else {
                sric.moveToFront();
            }
        });

        btnRouting.addActionListener(arg0 -> {
            if (routing == null || routing.isClosed()) {
                routing = new Routing(); // only one instance of this
                desktopPane.add(routing);
                routing.show();
            } else {
                routing.moveToFront();
            }
        });
        
        btnIr.addActionListener(arg0 -> {
          if (ir == null || ir.isClosed()) {
              ir = new Ir(); // only one instance of this
              desktopPane.add(ir);
              ir.show();
          } else {
              ir.moveToFront();
          }
      });
        
        btnEmergency.addActionListener(arg0 -> {
            if (emergency == null || emergency.isClosed()) {
            	emergency = new Emergency(); // only one instance of this
                desktopPane.add(emergency);
                emergency.show();
            } else {
            	emergency.moveToFront();
            }
        });
    }
    
    
	private class Thready extends Thread {
    	public Thready(String str) {
    		super(str);
    	}
     
    	public void run() {
    		while( true ) {
    			try 
    			{
        			if ( Data.isCurrentLocationActive() )
        			{
        				RESTClient.getCurrentPosition();
        				
            			if ( Data.getCurrentPosition() != null )
            			{
	        				lblCoor.setText("lng: "+ Data.getCurrentPosition().getLng() 
	        						+" , lat: "+  Data.getCurrentPosition().getLat()
	        						+" , alt: "+ Data.getCurrentPosition().getAlt());
            			}
        			}
        			
        			// sleep 2 sec
    				sleep( CONSTANTS.GET_CURRENT_POSITION_DELAY );
    			} 
    			catch (InterruptedException e) 
    			{
    			}
    		}
    	}
    }
}
