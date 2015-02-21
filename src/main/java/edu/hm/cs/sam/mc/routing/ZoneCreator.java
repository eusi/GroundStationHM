package edu.hm.cs.sam.mc.routing;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Zone;
import edu.hm.cs.sam.mc.rest.RestClient;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;

/**
 * View to create zones
 * @author Stefan Hoelzl
 *
 */

@SuppressWarnings("serial")
public class ZoneCreator extends JPanel {

    private final JComboBox<SamType> typeSelector = new JComboBox<SamType>();
    private final JButton colorSelector = new JButton("Change Color");
    private final WaypointTableModel wpTableModel = new WaypointTableModel();
    private final ModTable wpTable = new ModTable(wpTableModel);

    public ZoneCreator() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        final JLabel heading = new JLabel("Create Zones");
        heading.setFont(heading.getFont().deriveFont(18f));
        this.add(heading);

        final JPanel configurator = new JPanel();
        configurator.setLayout(new FlowLayout());
        this.add(configurator);

        for (final SamType st : SamType.values()) {
            if (st.toString().startsWith("ZONE_")) {
                typeSelector.addItem(st);
            }
        }
        final SamType type = (SamType) typeSelector.getSelectedItem();
        wpTableModel.setSamType(type);
        final ActionListener typeChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final SamType type = ((SamType) ((JComboBox<SamType>) e.getSource())
                        .getSelectedItem());
                final Zone zone = Data.getZones(type);
                if (zone == null) {
                    colorSelector.setBackground(Color.GREEN);
                } else {
                    colorSelector.setBackground(zone.getColor());
                }
                wpTableModel.setSamType(type);
            }
        };
        typeSelector.addActionListener(typeChangeListener);
        configurator.add(typeSelector);

        final Zone z = Data.getZones(type);
        Color c = Color.GREEN;
        if (z != null) {
            c = z.getColor();
        }
        colorSelector.setBackground(c);
        final ActionListener colorChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final Color newColor = JColorChooser.showDialog(null, "Select new Color",
                        ((JButton) e.getSource()).getBackground());
                final SamType type = ((SamType) typeSelector.getSelectedItem());
                Zone zone = Data.getZones(type);
                if (zone == null) {
                    final LocationWp[] lwps = {};
                    zone = new Zone(lwps, type, newColor);
                    Data.setZones(zone);
                }
                zone.setColor(newColor);
                ((JButton) e.getSource()).setBackground(newColor);
            }
        };
        colorSelector.addActionListener(colorChangeListener);
        configurator.add(colorSelector);

        wpTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wpTable.setRowSelectionAllowed(false);
        wpTable.setCellSelectionEnabled(true);
        this.add(new JScrollPane(wpTable));

        final JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        this.add(buttons);

        final JButton clearButton = new JButton("Clear");
        final ActionListener clearButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final SamType type = ((SamType) typeSelector.getSelectedItem());
                Data.clearZone(type);
                wpTableModel.setSamType(type);
            }
        };
        clearButton.addActionListener(clearButtonListener);
        buttons.add(clearButton);


        final JButton submitButton = new JButton("Submit to MissionPlanner");
        final ActionListener submitButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final SamType type = ((SamType) typeSelector.getSelectedItem());
                final Zone z = Data.getZones(type);
                if (z == null) {
                    JOptionPane.showMessageDialog(null, "Zone " + type.toString()
                            + " nicht vorhanden!");
                    return;
                }
                RestClient.sendZone(z);
            }
        };
        submitButton.addActionListener(submitButtonListener);
        buttons.add(submitButton);

    }
}
