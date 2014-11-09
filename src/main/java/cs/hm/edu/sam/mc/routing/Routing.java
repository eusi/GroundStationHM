package cs.hm.edu.sam.mc.routing;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ReportSheet class. This module creates a report sheet from specific files.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class Routing extends JInternalFrame {

    /**
     * Create the frame.
     */
    public Routing() {
        setFrameIcon(new ImageIcon(
                Routing.class
                        .getResource("/icons/routing_icon_mini.png")));
        setTitle("Routing");
        setIconifiable(true);
        setClosable(true);
        setResizable(true);
        setBounds(0, 0, 500, 400);

        final JPanel panel = new JPanel();
        final GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
                Alignment.LEADING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 414,
                                Short.MAX_VALUE).addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
                Alignment.LEADING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 249,
                                Short.MAX_VALUE).addContainerGap()));

        final JLabel lblRouting = new JLabel("Routing");
        final GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addComponent(lblRouting)
        			.addContainerGap(433, Short.MAX_VALUE))
        );
        gl_panel.setVerticalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addComponent(lblRouting)
        			.addContainerGap(335, Short.MAX_VALUE))
        );
        panel.setLayout(gl_panel);
        getContentPane().setLayout(groupLayout);

    }
}
