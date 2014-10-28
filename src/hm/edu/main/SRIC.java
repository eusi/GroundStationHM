package hm.edu.main;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.ImageIcon;

/**
* SRIC class.
* 
* This module's job is to collect WLAN/FTP data in mask and send that data
* to a target. 
*  
* @author Christoph Friegel
* @version 0.1
*/

@SuppressWarnings("serial")
public class SRIC extends JInternalFrame {
	private JTextField textFieldSSID;
	private JTextField textFieldPW;
	private JTextField textFieldHOST;
	private JTextField textFieldPORT;
	private JTextField textFieldNAME;
	private JTextField textFieldPW2;
	private JTextField textFieldPP;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SRIC frame = new SRIC();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SRIC() {
		setFrameIcon(new ImageIcon(SRIC.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		setTitle("SRIC");
		setClosable(true);
		setIconifiable(true);
		setBounds(0, 0, 500, 400);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 465, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 349, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JLabel lblWLAN = new JLabel("WLAN");
		
		textFieldSSID = new JTextField();
		textFieldSSID.setColumns(10);
		
		JLabel lblSsid = new JLabel("SSID");
		
		JLabel labelPW = new JLabel("Passwort");
		
		textFieldPW = new JTextField();
		textFieldPW.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		
		JLabel lblPasswort = new JLabel("Passwort");
		
		JLabel lblFtp = new JLabel("FTP");
		
		JButton btnSend = new JButton("Send");
		
		JLabel lblPicture = new JLabel("PicturePath");
		
		JLabel lblHost = new JLabel("Host");
		
		JLabel lblPort = new JLabel("Port");
		
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
		
		JLabel lblFTP = new JLabel("FTP");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textFieldNAME, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPasswort, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPicture, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
							.addGap(2)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(textFieldPP, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldPW2, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))))
					.addGap(147))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblFTP, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 502, Short.MAX_VALUE)
					.addComponent(lblFtp, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPort, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textFieldPORT, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblHost, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textFieldHOST, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnSend))
						.addComponent(lblWLAN)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblSsid, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelPW, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(textFieldSSID, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldPW, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))))
					.addGap(314))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblWLAN)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblSsid)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(labelPW))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(textFieldSSID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldPW, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(46)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFtp)
						.addComponent(lblFTP))
					.addGap(1)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblHost)
						.addComponent(textFieldHOST, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort)
						.addComponent(textFieldPORT, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(textFieldNAME, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPasswort)
						.addComponent(textFieldPW2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPicture)
						.addComponent(textFieldPP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addComponent(btnSend)
					.addGap(54))
		);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);

	}
}
