package edu.hm.cs.sam.mc.routing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.cs.sam.mc.rest.RestClient;
import edu.hm.sam.location.SamType;
/**
 * View to manage Targets
 * @author Stefan Hoelzl
 *
 */
@SuppressWarnings("serial")
public class TargetManager extends JPanel {
	private final TargetTableModel tgtTableModel = new TargetTableModel();
    private final ModTable targetTable = new ModTable(tgtTableModel);
	public TargetManager()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        final JLabel heading = new JLabel("Manage Targets");
        heading.setFont(heading.getFont().deriveFont(18f));
        this.add(heading);
        
        targetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        targetTable.setRowSelectionAllowed(false);
        targetTable.setCellSelectionEnabled(true);
        this.add(new JScrollPane(targetTable));
        
        final JButton submitButton = new JButton("Submit to MissionPlanner");
        final ActionListener submitButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                int c = 0;
                for (final Pair<SamType, Boolean> p : tgtTableModel.getContentModel()) {
                	Target t = Data.getTargets(p.getKey());
                    if ((p.getValue() == true) && (t != null)) {
                        RestClient.sendTarget(t);
                        c++;
                    }
                }
                JOptionPane.showMessageDialog(null, c + " Targets submitted!");
            }
        };
        submitButton.addActionListener(submitButtonListener);
        this.add(submitButton);
	}
}
