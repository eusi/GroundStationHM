package cs.hm.edu.sam.mc.routing;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import cs.hm.edu.sam.mc.misc.CONSTANTS;

/**
 * ReportSheet class. This module creates a report sheet from specific files.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class Routing extends JInternalFrame {
    private final JTextArea textFieldZone;

    /**
     * Create the frame.
     */
    public Routing() {
        setFrameIcon(new ImageIcon(Routing.class.getResource(CONSTANTS.ICON_DIR
                + "routing_icon_mini.png")));
        setTitle("Routing");
        setIconifiable(true);
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setResizable(true);
        setBounds(0, 0, 500, 400);

        final JPanel panel = new JPanel();
        final GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout.createSequentialGroup().addContainerGap()
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                        .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout.createSequentialGroup().addContainerGap()
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                        .addContainerGap()));

        final JLabel lblRouting = new JLabel("Routing");

        final JLabel lblZone = new JLabel("Create Zone");

        textFieldZone = new JTextArea();
        textFieldZone
                .setText("\"Lat\":123.23,\"Lng\":123.34,\n\"Lat\":123.23,\"Lng\":123.34,\n...");
        textFieldZone.setColumns(10);

        final JButton btnCreateZone = new JButton("create Zone");
        final GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_panel.createSequentialGroup()
                        .addGroup(
                                gl_panel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(lblRouting)
                                        .addComponent(lblZone)
                                        .addGroup(
                                                gl_panel.createParallelGroup(Alignment.TRAILING)
                                                        .addComponent(btnCreateZone)
                                                        .addComponent(textFieldZone,
                                                                GroupLayout.PREFERRED_SIZE, 211,
                                                                GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(253, Short.MAX_VALUE)));
        gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_panel.createSequentialGroup()
                        .addComponent(lblRouting)
                        .addGap(27)
                        .addComponent(lblZone)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(textFieldZone, GroupLayout.PREFERRED_SIZE, 120,
                                GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCreateZone)
                        .addContainerGap(139, Short.MAX_VALUE)));
        panel.setLayout(gl_panel);
        getContentPane().setLayout(groupLayout);

    }
}
