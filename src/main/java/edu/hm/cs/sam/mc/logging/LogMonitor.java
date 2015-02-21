package edu.hm.cs.sam.mc.logging;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import edu.hm.cs.sam.mc.misc.Constants;

/**
 * LogMonitor-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

@SuppressWarnings("serial")
public class LogMonitor extends JFrame {

    class LogCellRenderer extends JLabel implements ListCellRenderer<Object> {

        LogCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(final JList<?> list, final Object value,
                                                      final int index, final boolean isSelected, final boolean cellHasFocus) {

            final String entry = (String) value;
            setText(entry);

            if (isSelected) {
                setBackground(Constants.BLUE_SAM);
                setForeground(Color.white);
            } else if (entry.charAt(11) == 'E') { // E like Error
                setBackground(Constants.RED);
                setForeground(Constants.WHITE);
            } else if (entry.charAt(11) == 'W') { // W like Warning
                setBackground(Constants.RED_A100);
                setForeground(Constants.BLACK);
            } else {
                setBackground(Constants.WHITE);
                setForeground(Constants.BLACK);
            }
            return this;
        }
    }

    private final JPanel contentPane;
    private final JList<String> jList;
    private static DefaultListModel<String> defaultListModel;
    private final JScrollPane scrollPane;
    private final JLabel btnTitle;

    private final JButton btnScroll;

    /**
     * Create the frame.
     */
    public LogMonitor() {
    	setAlwaysOnTop(true);
        setIconImage(Constants.SAM_ICON.getImage());
        setTitle(Constants.LOG_TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 696, 421);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        btnTitle = new JLabel("Log Monitoring");

        btnScroll = new JButton("scroll down");
        btnScroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                onClickGoToBottom();
            }
        });

        jList = new JList<String>();
        defaultListModel = new DefaultListModel<String>();

        jList.setModel(defaultListModel);
        jList.setCellRenderer(new LogCellRenderer());

        scrollPane = new JScrollPane(jList);
        scrollPane.setViewportView(jList);

        final GroupLayout glContentPane = new GroupLayout(contentPane);
        glContentPane.setHorizontalGroup(glContentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(
                        glContentPane
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                glContentPane
                                .createParallelGroup(Alignment.LEADING)
                                .addGroup(
                                        glContentPane
                                        .createParallelGroup(
                                                Alignment.LEADING)
                                                .addComponent(btnTitle)
                                                .addComponent(btnScroll,
                                                        Alignment.TRAILING))
                                                        .addComponent(scrollPane, Alignment.TRAILING,
                                                                GroupLayout.DEFAULT_SIZE, 650,
                                                                Short.MAX_VALUE)).addContainerGap()));
        glContentPane.setVerticalGroup(glContentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(
                        glContentPane
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTitle)
                        .addGap(11)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 285,
                                Short.MAX_VALUE).addGap(18).addComponent(btnScroll)
                                .addContainerGap()));

        contentPane.setLayout(glContentPane);
    }

    /**
     * getter for defaultListModel
     *
     * @return defaultListModel.
     */
    public DefaultListModel<String> getDefaultListModel() {
        return defaultListModel;
    }

    /**
     * getter for jList.
     *
     * @return jList.
     */
    public JList<String> getjList() {
        return jList;
    }

    /**
     * jumps to end of list.
     */
    public void onClickGoToBottom() {
        final int lastIndex = jList.getModel().getSize() - 1;
        if (lastIndex >= 0) {
            jList.ensureIndexIsVisible(lastIndex);
        }
    }
}