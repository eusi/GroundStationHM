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

    /**
     * Create the frame.
     */
    public SRIC() {
        setFrameIcon(new ImageIcon(SRIC.class.getResource("/icons/sric_icon_mini.png")));
        setTitle("SRIC");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setBounds(0, 0, 500, 400);

        final JPanel panel = new JPanel();
        final GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel, GroupLayout.PREFERRED_SIZE, 465,
                                GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout.createSequentialGroup().addContainerGap()
                        .addComponent(panel, GroupLayout.PREFERRED_SIZE, 349, Short.MAX_VALUE)
                        .addContainerGap()));

        final JLabel lblWlan = new JLabel("WLAN");

        textFieldSSID = new JTextField();
        textFieldSSID.setColumns(10);

        final JLabel lblSsid = new JLabel("SSID");

        final JLabel labelPw = new JLabel("Passwort");

        textFieldPW = new JTextField();
        textFieldPW.setColumns(10);

        final JLabel lblName = new JLabel("Name");

        final JLabel lblPasswort = new JLabel("Passwort");

        final JLabel lblFtp = new JLabel("FTP");

        final JButton btnSend = new JButton("Send");

        final JLabel lblPicture = new JLabel("PicturePath");

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
        final GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(gl_panel
                .createParallelGroup(Alignment.TRAILING)
                .addGroup(
                        gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.LEADING)
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addComponent(lblName,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        60,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        ComponentPlacement.UNRELATED)
                                                                .addComponent(textFieldNAME,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        176,
                                                                        GroupLayout.PREFERRED_SIZE))
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addGroup(
                                                                        gl_panel.createParallelGroup(
                                                                                Alignment.LEADING)
                                                                                .addComponent(
                                                                                        lblPasswort,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        60,
                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(
                                                                                        lblPicture,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        68,
                                                                                        GroupLayout.PREFERRED_SIZE))
                                                                .addGap(2)
                                                                .addGroup(
                                                                        gl_panel.createParallelGroup(
                                                                                Alignment.LEADING)
                                                                                .addComponent(
                                                                                        textFieldPP,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        343,
                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(
                                                                                        textFieldPW2,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        176,
                                                                                        GroupLayout.PREFERRED_SIZE))))
                                .addGap(147))
                .addGroup(
                        gl_panel.createSequentialGroup()
                                .addComponent(lblFTP, GroupLayout.PREFERRED_SIZE, 29,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, 502, Short.MAX_VALUE)
                                .addComponent(lblFtp, GroupLayout.PREFERRED_SIZE, 29,
                                        GroupLayout.PREFERRED_SIZE).addContainerGap())
                .addGroup(
                        gl_panel.createSequentialGroup()
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.LEADING)
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(lblPort,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        60,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        ComponentPlacement.UNRELATED)
                                                                .addComponent(textFieldPORT,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        176, Short.MAX_VALUE))
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(lblHost,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        60,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        ComponentPlacement.UNRELATED)
                                                                .addComponent(textFieldHOST,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        176, Short.MAX_VALUE))
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(btnSend))
                                                .addComponent(lblWlan)
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(
                                                                        gl_panel.createParallelGroup(
                                                                                Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(
                                                                                        lblSsid,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)
                                                                                .addComponent(
                                                                                        labelPw,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        60,
                                                                                        Short.MAX_VALUE))
                                                                .addPreferredGap(
                                                                        ComponentPlacement.UNRELATED)
                                                                .addGroup(
                                                                        gl_panel.createParallelGroup(
                                                                                Alignment.LEADING)
                                                                                .addComponent(
                                                                                        textFieldSSID,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        176,
                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(
                                                                                        textFieldPW,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        176,
                                                                                        Short.MAX_VALUE))))
                                .addGap(314)));
        gl_panel.setVerticalGroup(gl_panel
                .createParallelGroup(Alignment.LEADING)
                .addGroup(
                        gl_panel.createSequentialGroup()
                                .addComponent(lblWlan)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.LEADING)
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addComponent(lblSsid)
                                                                .addPreferredGap(
                                                                        ComponentPlacement.UNRELATED)
                                                                .addComponent(labelPw))
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addComponent(textFieldSSID,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        ComponentPlacement.RELATED)
                                                                .addComponent(textFieldPW,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)))
                                .addGap(46)
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(lblFtp).addComponent(lblFTP))
                                .addGap(1)
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(lblHost)
                                                .addComponent(textFieldHOST,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(lblPort)
                                                .addComponent(textFieldPORT,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(lblName)
                                                .addComponent(textFieldNAME,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE))
                                .addGap(9)
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(lblPasswort)
                                                .addComponent(textFieldPW2,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(
                                        gl_panel.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(lblPicture)
                                                .addComponent(textFieldPP,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)).addGap(29)
                                .addComponent(btnSend).addGap(54)));
        panel.setLayout(gl_panel);
        getContentPane().setLayout(groupLayout);

    }
}
