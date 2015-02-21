package edu.hm.cs.sam.mc.offaxis;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.hm.cs.sam.mc.misc.Window;

/**
 * Airdrop class for its task.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class OffAxis extends Window {

    /**
     * Create the frame.
     */
    public OffAxis(final String title, final Icon icon) {
        super(title, icon);

        final JPanel panel = new JPanel();
        final GroupLayout groupLayout = new GroupLayout(getMainPanel());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout.createSequentialGroup().addContainerGap()
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                        .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout.createSequentialGroup().addContainerGap()
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                        .addContainerGap()));

        final JLabel lblAirdrop = new JLabel("Off-Axis");

        final GroupLayout glPanel = new GroupLayout(panel);
        glPanel.setHorizontalGroup(glPanel.createParallelGroup(Alignment.LEADING).addGroup(
                glPanel.createSequentialGroup().addComponent(lblAirdrop)
                        .addContainerGap(153, Short.MAX_VALUE)));
        glPanel.setVerticalGroup(glPanel.createParallelGroup(Alignment.LEADING).addGroup(
                glPanel.createSequentialGroup().addGap(24).addComponent(lblAirdrop)
                        .addContainerGap(311, Short.MAX_VALUE)));
        panel.setLayout(glPanel);
        getContentPane().setLayout(groupLayout);

    }

    @Override
    public void loadProperties() {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveProperties() {
        // TODO Auto-generated method stub

    }
}
