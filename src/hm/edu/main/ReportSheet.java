package hm.edu.main;

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
 * ReportSheet class. This module creates a report sheet from specific files.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class ReportSheet extends JInternalFrame {
    private final JTextField textField;

    /**
     * Create the frame.
     */
    public ReportSheet() {
        setFrameIcon(new ImageIcon(
                ReportSheet.class
                        .getResource("/com/sun/javafx/webkit/prism/resources/missingImage.png")));
        setTitle("ReportSheet");
        setIconifiable(true);
        setClosable(true);
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

        final JButton btnCreate = new JButton("create");

        final JLabel lblReportsheet = new JLabel("ReportSheet");

        final JLabel lblStatus = new JLabel("Status");

        textField = new JTextField();
        textField.setEditable(false);
        textField.setColumns(10);
        final GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(gl_panel
                .createParallelGroup(Alignment.LEADING)
                .addGroup(
                        gl_panel.createSequentialGroup()
                                .addGroup(
                                        gl_panel.createParallelGroup(
                                                Alignment.LEADING)
                                                .addComponent(lblStatus)
                                                .addComponent(lblReportsheet)
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(
                                                                        btnCreate))
                                                .addGroup(
                                                        gl_panel.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(
                                                                        textField,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        427,
                                                                        GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(27, Short.MAX_VALUE)));
        gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
                Alignment.LEADING).addGroup(
                gl_panel.createSequentialGroup()
                        .addGap(24)
                        .addComponent(lblReportsheet)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(btnCreate)
                        .addGap(38)
                        .addComponent(lblStatus)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(textField, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(194, Short.MAX_VALUE)));
        panel.setLayout(gl_panel);
        getContentPane().setLayout(groupLayout);

    }
}
