package edu.hm.cs.sam.mc.searcharea.task;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.control.GroundStationClient;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.ObstacleStatic;
import edu.hm.cs.sam.mc.misc.Obstacles;
import edu.hm.cs.sam.mc.misc.Window;
import edu.hm.cs.sam.mc.misc.Zone;
import edu.hm.cs.sam.mc.rest.RestClient;
import edu.hm.sam.control.SubscriptionHandler;
import edu.hm.sam.control.ControlMessage.Service;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;
import edu.hm.sam.searchareaservice.LowFlightRouteCalc;

/**
 * GUI-Class for the Search-Area-Routing. Displays tabs for high and low flight,
 * which offer calculations and transferring to the MissionPlanner
 *
 * @author Maximilian Bayer
 */
public class SearchArea extends Window implements ActionListener {
    private static final Logger LOG = Logger.getLogger(SearchArea.class.getName());
    private static final long serialVersionUID = -7765276267076116900L;
    private TextArea logHfTextArea;
    private TextArea aoiPointsTextArea;
    private TextArea warningsTextArea;
    private JTextField widthTextField;
    private JTextField altHfTextField;
    private JTextField altLfTextField;
    private JButton calcWaypointsButton;
    private JButton calcRouteButton;
    private JTabbedPane mainTabbedPane;
    private WaypointCalculator calc;
    private JPanel tab1;
    private JPanel tab2;
    private SpringLayout layout_tab1;
    private SpringLayout layout_tab2;

    /**
     * Create the frame.
     */
    public SearchArea(final String title, final Icon icon) {
        super(title, icon);
        buildFrame();
        buildTabForHighFlight();
        buildTabForLowFlight();

        final GroundStationClient client = GroundStationClient.getInstance();
        // add subscription Handler for low flight calculation
        client.addSubscriptionHandler(Service.SEARCHAREA, "LOWFLIGHTCALC",
                new SubscriptionHandler<LowFlightRouteCalc>() {

                    @Override
                    public void handleSubscription(final LowFlightRouteCalc subscription) {
                        final List<LocationWp> waypoints = subscription.getWaypoints();
                        String points = "";
                        for(LocationWp wp: waypoints) {
                        	points += wp.getLat() + " " + wp.getLng() +"\n";
                        }
                        aoiPointsTextArea.setText(points);
                    }
                });
    }

    /**
     * Method for Button handling.
     *
     * @param e triggered ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if ("calculateHigh".equals(e.getActionCommand())) {
        	Zone searchAreaZone = null;
        	// try to get and parse altitude
        	if(altHfTextField.getText().isEmpty()) {
        		logHfTextArea.setText("Please enter altitude!\n");
        		return;
        	}
        	double altitude = 0;
        	try {
        		altitude = Double.parseDouble(altHfTextField.getText());
        	} catch(NumberFormatException pe) {
        		logHfTextArea.setText("Altitude could not be read properly!\n");
        		return;
        	}
        	// try to get and parse width of track
        	if(widthTextField.getText().isEmpty()) {
        		logHfTextArea.setText("Please enter width!\n");
        		return;
        	}
        	double width = 0;
        	try {
        		// is needed in km
        		width = (Double.parseDouble(widthTextField.getText()))/1000;
        	} catch(NumberFormatException pe) {
        		logHfTextArea.setText("Width could not be read properly!\n");
        		return;
        	}
        	// try to get search area zone
        	try {
	        	searchAreaZone = Data.getZones(SamType.ZONE_SEARCH_AREA);
	        	LOG.info("SearchArea Routing: HighFlight received zone: "+(searchAreaZone.getWaypoints().length-1)+" corners");
	        	logHfTextArea.setText(logHfTextArea.getText()+"Search Area Zone received! "+(searchAreaZone.getWaypoints().length-1)+" corners\n");
        	} catch(NullPointerException e1) {
        		logHfTextArea.setText(logHfTextArea.getText()+"Zone not received!\n");
        		return;
        	}
        	if(searchAreaZone != null) {
	        	// get corners from zone
	        	final LocationWp[] waypoints = searchAreaZone.getWaypoints();
	        	// calculate waypoints with lawn mowing pattern
	        	calc = new WaypointCalculator();
	        	// MP provides areas with first point == last point which is just bad
	        	List<LocationWp> temp = Arrays.asList(waypoints).subList(1, waypoints.length);
	        	List<LocationWp> resultListHF = calc.lawnMowingPattern(temp, width, altitude);
		        logHfTextArea.setText(logHfTextArea.getText()+"Success: Route with "+resultListHF.size()+" waypoints calculated! (altitude: "+altitude+"m)\n");
	        	// convert to array
		        final LocationWp[] waypointsHF = new LocationWp[resultListHF.size()];
		        resultListHF.toArray(waypointsHF);
		        final Waypoints wpHF = new Waypoints(waypointsHF, SamType.TASK_SEARCH_AREA);
		        // set route
		        Data.setWaypoints(wpHF);
		        logHfTextArea.setText(logHfTextArea.getText()+"Route exported!\n");
        	}
        } else if ("calculateLow".equals(e.getActionCommand())) {
        	// try to get altitude
        	double altitude = 0;
        	try {
        		altitude = Double.parseDouble(altLfTextField.getText());
        	} catch(NumberFormatException pe) {
        		warningsTextArea.setText("Altitude could not be read properly!\n");
        		return;
        	}
        	// try to get obstacles
        	try {
        		RestClient.getObstacles();
	        	Obstacles obstacles = Data.getCurrentObstacles();
	        	ObstacleStatic[] staticObstacles = obstacles.getoStaticlist();
	        	LOG.info("SearchArea Routing: LowFlight received "+staticObstacles.length+" static obstacles");
        	} catch(NullPointerException e3) {
        		warningsTextArea.setText(warningsTextArea.getText()+"Obstacles not received!\n");
        	}
        	// try to read points
            if(!aoiPointsTextArea.getText().isEmpty()) {
                List<LocationWp> coordsLow = new ArrayList<LocationWp>();
                final LineNumberReader lnr = new LineNumberReader(new StringReader(aoiPointsTextArea.getText()));
                String line;
                try {
                    while ((line = lnr.readLine()) != null) {
                        final String[] coordString = line.split(" ");
                        final double lat = Double.parseDouble(coordString[0]);
                        final double lon = Double.parseDouble(coordString[1]);
                        final LocationWp c = new LocationWp(lat, lon, altitude, MavCmd.WAYPOINT, 1, 0, 0, 0, 0);
                        coordsLow.add(c);
                    }
                } catch (final IOException e1) {
                	warningsTextArea.setText("Coordinates could not be read properly!\n");
                    return;
                }catch(NumberFormatException ne) {
                    warningsTextArea.setText(warningsTextArea.getText()+"Coordinates could not be read properly!\n");
                    return;
                }
	        	LOG.info("SearchArea Routing: LowFlight read "+coordsLow.size()+" points");
                calc = new WaypointCalculator();
                List<LocationWp> resultListLF = calc.nearestNeighbour(coordsLow);
            	warningsTextArea.setText("Success: Route with "+resultListLF.size()+" waypoints calculated!\n");
                LOG.info("Altitude low flight: "+resultListLF.get(0).getAlt());
    	        final LocationWp[] waypointsLF = new LocationWp[resultListLF.size()];
    	        resultListLF.toArray(waypointsLF);
    	        final Waypoints wpLF = new Waypoints(waypointsLF, SamType.TASK_SEARCH_AREA);
    	        Data.setWaypoints(wpLF);
            	warningsTextArea.setText(warningsTextArea.getText()+"Route exported!\n");
            } else {
            	warningsTextArea.setText(warningsTextArea.getText()+"No Coordinates given!\n");
            	return;
            }
        }
    }

    /**
     * Builds the overall frame for the search area task.
     */
    private void buildFrame() {
        mainTabbedPane = new JTabbedPane();
        getMainPanel().setLayout(new BorderLayout());
        getMainPanel().add(BorderLayout.CENTER, mainTabbedPane);
        mainTabbedPane.setBackground(Color.LIGHT_GRAY);
        tab1 = new JPanel();
        tab2 = new JPanel();
        layout_tab1 = new SpringLayout();
        layout_tab2 = new SpringLayout();
        tab1.setLayout(layout_tab1);
        tab2.setLayout(layout_tab2);
        mainTabbedPane.addTab("High Flight", tab1);
        mainTabbedPane.addTab("Low Flight", tab2);
    }

    /**
     * Initialize all Labels, Buttons, TextArea, FileChooser on HighFlightTab.
     */
    private void buildTabForHighFlight() {
        // TextArea for corner points
        logHfTextArea = new TextArea();
        layout_tab1.putConstraint(SpringLayout.NORTH, logHfTextArea, 30, SpringLayout.NORTH, tab1);
        layout_tab1.putConstraint(SpringLayout.WEST, logHfTextArea, 10, SpringLayout.WEST, tab1);
        logHfTextArea.setEditable(false);
        tab1.add(logHfTextArea);
        // Label for track width
        final JLabel widthLabel = new JLabel("Width of track in m:");
        layout_tab1.putConstraint(SpringLayout.NORTH, widthLabel, 35, SpringLayout.SOUTH, logHfTextArea);
        layout_tab1.putConstraint(SpringLayout.WEST, widthLabel, 10, SpringLayout.WEST, tab1);
        tab1.add(widthLabel);
        // input for width
        widthTextField = new JTextField();
        layout_tab1.putConstraint(SpringLayout.EAST, widthLabel, -6, SpringLayout.WEST, widthTextField);
        layout_tab1.putConstraint(SpringLayout.WEST, widthTextField, 135, SpringLayout.WEST, tab1);
        layout_tab1.putConstraint(SpringLayout.EAST, widthTextField, -293, SpringLayout.EAST, tab1);
        layout_tab1.putConstraint(SpringLayout.NORTH, widthTextField, -3, SpringLayout.NORTH,
                widthLabel);
        tab1.add(widthTextField);
        // Label for altitude
        final JLabel altLabel = new JLabel("Altitude in m: ");
        layout_tab1.putConstraint(SpringLayout.NORTH, altLabel, 85, SpringLayout.SOUTH, logHfTextArea);
        layout_tab1.putConstraint(SpringLayout.WEST, altLabel, 10, SpringLayout.WEST, tab1);
        tab1.add(altLabel);
        // input for altitude
        altHfTextField = new JTextField();
        layout_tab1.putConstraint(SpringLayout.EAST, altLabel, -6, SpringLayout.WEST, altHfTextField);
        layout_tab1.putConstraint(SpringLayout.EAST, altHfTextField, 0, SpringLayout.EAST, widthTextField);
        layout_tab1.putConstraint(SpringLayout.WEST, altHfTextField, 135, SpringLayout.WEST, tab1);
        layout_tab1.putConstraint(SpringLayout.NORTH, altHfTextField, -3, SpringLayout.NORTH,altLabel);
        tab1.add(altHfTextField);
        // Calculate Button
        calcWaypointsButton = new JButton("<html>Calculate & Export<br>Waypoints<html>");
        layout_tab1.putConstraint(SpringLayout.NORTH, calcWaypointsButton, 226, SpringLayout.NORTH,
                tab1);
        layout_tab1.putConstraint(SpringLayout.WEST, calcWaypointsButton, 208, SpringLayout.WEST, tab1);
        layout_tab1.putConstraint(SpringLayout.SOUTH, calcWaypointsButton, -11, SpringLayout.SOUTH,
                altLabel);
        layout_tab1.putConstraint(SpringLayout.EAST, calcWaypointsButton, -140, SpringLayout.EAST, tab1);
        tab1.add(calcWaypointsButton);
        calcWaypointsButton.setVerticalTextPosition(AbstractButton.CENTER);
        calcWaypointsButton.setHorizontalTextPosition(AbstractButton.LEADING);
        calcWaypointsButton.setActionCommand("calculateHigh");
        calcWaypointsButton.addActionListener(this);
    }

    /**
     * Builds the tab for the lower Flight and adds all necessary buttons,
     * textAreas etc.
     */
    private void buildTabForLowFlight() {
        // Label für Punkt-Eingabe
        final JLabel lblCornerpointsInDecimalxy = new JLabel("AoI-WPs in decimal°(x1 y1) :");
        layout_tab2.putConstraint(SpringLayout.NORTH, lblCornerpointsInDecimalxy, 10,
                SpringLayout.NORTH, tab2);
        layout_tab2.putConstraint(SpringLayout.WEST, lblCornerpointsInDecimalxy, 10,
                SpringLayout.WEST, tab2);
        lblCornerpointsInDecimalxy.setBounds(38, 57, 296, 14);
        tab2.add(lblCornerpointsInDecimalxy);

        // TextArea for waypoints
        aoiPointsTextArea = new TextArea();
        layout_tab2.putConstraint(SpringLayout.NORTH, aoiPointsTextArea, 6, SpringLayout.SOUTH,
                lblCornerpointsInDecimalxy);
        layout_tab2.putConstraint(SpringLayout.WEST, aoiPointsTextArea, 0, SpringLayout.WEST,
                lblCornerpointsInDecimalxy);
        layout_tab2.putConstraint(SpringLayout.NORTH, logHfTextArea, 6, SpringLayout.SOUTH,
                lblCornerpointsInDecimalxy);
        layout_tab2.putConstraint(SpringLayout.WEST, logHfTextArea, 0, SpringLayout.WEST,
                lblCornerpointsInDecimalxy);
        tab2.add(aoiPointsTextArea);
        // Label for altitude
        final JLabel altLabelLF = new JLabel("Altitude in m:");
        layout_tab2.putConstraint(SpringLayout.NORTH, altLabelLF, 15, SpringLayout.SOUTH, aoiPointsTextArea);
        layout_tab2.putConstraint(SpringLayout.WEST, altLabelLF, 10, SpringLayout.WEST, tab2);
        tab2.add(altLabelLF);
        // input for altitude
        altLfTextField = new JTextField();
        layout_tab2.putConstraint(SpringLayout.NORTH, altLfTextField, 12, SpringLayout.SOUTH, aoiPointsTextArea);
        layout_tab2.putConstraint(SpringLayout.WEST, altLfTextField, 94, SpringLayout.WEST, tab2);
        tab2.add(altLfTextField);
        // Calculate Button
        calcRouteButton = new JButton("Calculate & Export Route");
        layout_tab2.putConstraint(SpringLayout.EAST, altLfTextField, 27, SpringLayout.WEST, calcRouteButton);
        layout_tab2.putConstraint(SpringLayout.NORTH, calcRouteButton, 54, SpringLayout.SOUTH,
                aoiPointsTextArea);
        layout_tab2.putConstraint(SpringLayout.WEST, calcRouteButton, 95, SpringLayout.WEST,
                lblCornerpointsInDecimalxy);
        layout_tab2.putConstraint(SpringLayout.SOUTH, calcRouteButton, 107, SpringLayout.SOUTH, aoiPointsTextArea);
        layout_tab2.putConstraint(SpringLayout.EAST, calcRouteButton, 143, SpringLayout.EAST, lblCornerpointsInDecimalxy);
        calcRouteButton.setBounds(38, 314, 184, 36);
        calcRouteButton.setVerticalTextPosition(AbstractButton.CENTER);
        calcRouteButton.setHorizontalTextPosition(AbstractButton.LEADING);
        calcRouteButton.setActionCommand("calculateLow");
        calcRouteButton.addActionListener(this);
        tab2.add(calcRouteButton);
        // TextArea for Warnings
        warningsTextArea = new TextArea();
        layout_tab2.putConstraint(SpringLayout.NORTH, warningsTextArea, 120, SpringLayout.SOUTH,
        		aoiPointsTextArea);
        layout_tab2.putConstraint(SpringLayout.WEST, warningsTextArea, 0, SpringLayout.WEST,
        		aoiPointsTextArea);
        layout_tab2.putConstraint(SpringLayout.SOUTH, warningsTextArea, 222, SpringLayout.SOUTH, aoiPointsTextArea);
        warningsTextArea.setEditable(false);
        tab2.add(warningsTextArea);
    }

    public JInternalFrame getFrame() {
        return this;
    }

    @Override
    public void loadProperties() {

    }

    @Override
    public void saveProperties() {

    }
}
