package cs.hm.edu.sam.mc.sric;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import cs.hm.edu.sam.mc.misc.CONSTANTS;

/**
 * SRIC class. This module's job is to collect WLAN/FTP data in mask and send
 * that data to a target.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class SRIC extends JInternalFrame {
    private final JTextField textFieldSSID;
    private final JTextField textFieldPW;
    private final JTextField textFieldHOST;
    private final JTextField textFieldPORT;
    private final JTextField textFieldNAME;
    private final JTextField textFieldPW2;
    private final JTextField textFieldPP;
    private JTextField textField_1;
    private JTextField textField;

    /**
     * Create the frame.
     */
    public SRIC() {
        setFrameIcon(new ImageIcon(
                SRIC.class.getResource(CONSTANTS.ICON_DIR + "sric_icon_mini.png")));
        setTitle("SRIC");
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setIconifiable(true);
        setResizable(true);
        setBounds(0, 0, 605, 538);

        final JPanel panel = new JPanel();
        final GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 583, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(12, Short.MAX_VALUE))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 349, Short.MAX_VALUE)
        			.addContainerGap())
        );

        final JLabel lblWlan = new JLabel("WLAN");

        textFieldSSID = new JTextField();
        textFieldSSID.setColumns(10);

        final JLabel lblSsid = new JLabel("SSID");

        final JLabel labelPw = new JLabel("Password");

        textFieldPW = new JTextField();
        textFieldPW.setColumns(10);

        final JLabel lblName = new JLabel("Name");

        final JLabel lblPasswort = new JLabel("Password");

        final JButton btnSend = new JButton("Send");

        final JLabel lblPicture = new JLabel("Picture Path");

        final JLabel lblHost = new JLabel("Host");

        final JLabel lblPort = new JLabel("Port");

        textFieldHOST = new JTextField();
        textFieldHOST.setColumns(10);

        textFieldPORT = new JTextField();
        textFieldPORT.setColumns(10);

        textFieldNAME = new JTextField();
        textFieldNAME.setColumns(10);

        textFieldPW2 = new JTextField();
        textFieldPW2.setColumns(10);

        textFieldPP = new JTextField();
        textFieldPP.setColumns(10);

        final JLabel lblFTP = new JLabel("FTP");
        
        JLabel lblSricrouting = new JLabel("SRIC-Routing");
        
        JLabel lblLongitude = new JLabel("Longitude");
        
        JLabel lblLatitude = new JLabel("Latitude");
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        
        JButton btnCreateWaypoints = new JButton("Create Waypoints");
        
        JButton button = new JButton("Send");
        
        textField = new JTextField();
        textField.setColumns(10);
        final GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(textFieldNAME, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addComponent(lblPasswort, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(textFieldPW2, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addComponent(lblPicture, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(textFieldPP, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE)))
        			.addGap(147))
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblWlan)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_panel.createSequentialGroup()
        							.addContainerGap()
        							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(lblSsid, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        								.addComponent(labelPw, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)))
        						.addComponent(button, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
        					.addGap(18)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(textFieldSSID, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
        						.addComponent(textFieldPW, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))))
        			.addGap(314))
        		.addGroup(gl_panel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblPort, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(textFieldPORT, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
        			.addGap(314))
        		.addGroup(gl_panel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblHost, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(textFieldHOST, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
        			.addGap(314))
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addComponent(lblLongitude, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(10)
        					.addComponent(lblLatitude, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
        				.addComponent(lblSricrouting, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
        			.addGap(317))
        		.addGroup(gl_panel.createSequentialGroup()
        			.addComponent(btnCreateWaypoints)
        			.addContainerGap())
        		.addGroup(gl_panel.createSequentialGroup()
        			.addComponent(lblFTP, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        		.addGroup(gl_panel.createSequentialGroup()
        			.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(516, Short.MAX_VALUE))
        );
        gl_panel.setVerticalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addComponent(lblSricrouting)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblLongitude)
        				.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(11)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblLatitude)
        				.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btnCreateWaypoints)
        			.addGap(36)
        			.addComponent(lblWlan)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addComponent(lblSsid)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(labelPw)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(button))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addComponent(textFieldSSID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(textFieldPW, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addGap(30)
        			.addComponent(lblFTP)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
        				.addComponent(textFieldHOST, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblHost))
        			.addGap(8)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblPort)
        				.addComponent(textFieldPORT, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblName)
        				.addComponent(textFieldNAME, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblPasswort)
        				.addComponent(textFieldPW2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblPicture)
        				.addComponent(textFieldPP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btnSend)
        			.addGap(53))
        );
        panel.setLayout(gl_panel);
        getContentPane().setLayout(groupLayout);

    }
}
