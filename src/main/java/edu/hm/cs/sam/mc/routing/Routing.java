package edu.hm.cs.sam.mc.routing;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JSplitPane;

import edu.hm.cs.sam.mc.misc.Window;

/**
 * ReportSheet class. This module creates a report sheet from specific files.
 *
 * @author Stefan Hoelzl
 * @version 0.1
 */

@SuppressWarnings("serial")
public class Routing extends Window {
    final StoreManager sm;
    public Routing(final String title, final Icon icon) {
        super(title, icon);
        sm = new StoreManager();
        final ZoneCreator zones = new ZoneCreator();
        final Routes routes = new Routes();
        final TargetManager targets = new TargetManager();
        
        setSize(750,500);

        final JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, targets,
                zones);
        leftSplitPane.setResizeWeight(0.5);
        final JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                leftSplitPane, routes);
        mainSplitPane.setResizeWeight(1);
        getMainPanel().setLayout(new BorderLayout());
        getMainPanel().add(BorderLayout.CENTER, mainSplitPane);
    }
    
    @Override
    public void loadProperties() {
        sm.load();
    }

    @Override
    public void saveProperties() {
        sm.save();
    }
}
