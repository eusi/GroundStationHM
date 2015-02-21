package edu.hm.cs.sam.mc.routing;

/**
 * Main Window for the Routing Tab
 * @author Stefan Hoelzl
 */
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.rest.RestClient;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;

@SuppressWarnings("serial")
public class Routes extends JPanel {
    RoutesTableModel routesTableModel = new RoutesTableModel();
    private static final Logger LOG = Logger.getLogger(Routes.class.getName());
    
    /**
     * Constructur which builds the GUI
     */
    public Routes() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        final JLabel heading = new JLabel("Select Routes");
        heading.setFont(heading.getFont().deriveFont(18f));
        this.add(heading);

        routesTableModel.init();
        final JTable routesTable = new JTable(routesTableModel);
        routesTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        routesTable.setDragEnabled(true);
        routesTable.setDropMode(DropMode.INSERT_ROWS);
        routesTable.setTransferHandler(new RouteTransferHandler(routesTable));
        this.add(new JScrollPane(routesTable));

        final JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        final JButton optimizeButton = new JButton("Optimize selected Routes");
        final ActionListener optimizeButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    routesTableModel.optimize(routesTable.getSelectedRows());
                    JOptionPane.showMessageDialog(null, "Routes optimized!");
                } catch (final NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR: Routes without Waypoints selected");
                    LOG.error("Routes without Waypoints selected", ex);
                }
            }
        };
        optimizeButton.addActionListener(optimizeButtonListener);
        buttons.add(optimizeButton);

        final JButton submitButton = new JButton("Submit to MissionPlanner");
        final ActionListener submitButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final List<LocationWp> wps = new ArrayList<LocationWp>();
                for (final Pair<SamType, Boolean> p : routesTableModel.getContentModel()) {
                    if ((p.getValue() == true) && (Data.getWaypoints(p.getKey()) != null)) {
                        for (final LocationWp lwp : Data.getWaypoints(p.getKey()).getWaypoints()) {
                            wps.add(lwp);
                        }
                    }
                }
                if (!wps.isEmpty()) {
                    LocationWp[] lwps = new LocationWp[wps.size()];
                    lwps = wps.toArray(lwps);
                    final Waypoints wayp = new Waypoints(lwps, SamType.UNKNOWN);
                    RestClient.sendWaypoints(wayp);
                    JOptionPane.showMessageDialog(null, wps.size() + " Waypoints submitted!");
                }
            }
        };
        submitButton.addActionListener(submitButtonListener);
        buttons.add(submitButton);

        this.add(buttons);
    }
}
