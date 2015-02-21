package edu.hm.cs.sam.mc.airdrop;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.ir.mainground.Misc;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Settings;
import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.cs.sam.mc.misc.Window;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;

/**
 * Airdrop class for its task.
 *
 * @author Siegfried Ippisch
 * @version 0.1
 */

@SuppressWarnings("serial")
public class Airdrop extends Window {

    private static final String AIRDROP_FILE = "Airdrop.serialized";
    private static final int MAX_ITERATIONS = 1000000;

    private static final Logger LOG = Logger.getLogger(Airdrop.class.getName());

    private class AirdropParameter {
        private final String id;
        private final JTextField textField;
        private final JLabel label;

        AirdropParameter(String id, String text) {
            this.id = id;
            this.label = new JLabel(text);
            this.textField = new JTextField();
            this.textField.setColumns(10);
        }

        public AirdropParameter(String id, String text, double initial) {
            this(id, text);
            textField.setText(Double.toString(initial));
        }

        public String getId() {
            return id;
        }

        public JTextField getTextField() {
            return textField;
        }

        public JLabel getLabel() {
            return label;
        }

        public double getParameterValue() {
            return Double.parseDouble(textField.getText());
        }
    }

    private List<AirdropParameter> params;

    private final AirdropParameter paramMass;
    private final AirdropParameter paramGravity;
    private final AirdropParameter paramWindDir;
    private final AirdropParameter paramPlaneDir;
    private final AirdropParameter paramVWind;
    private final AirdropParameter paramVPlane;
    private final AirdropParameter paramHeight;
    private final AirdropParameter paramRoh;
    private final AirdropParameter paramCws;
    private final AirdropParameter paramCwv;
    private final AirdropParameter paramAs;
    private final AirdropParameter paramAv;
    private final AirdropParameter paramH;
    private final AirdropParameter paramBalaceDistance;
    private final AirdropParameter paramTriggerRadius;
    private final AirdropParameter paramDeathTime;
    private final AirdropParameter paramServo;
    private final AirdropParameter paramServoCloseValue;
    private final AirdropParameter paramServoOpenValue;

    final JButton btnGenerateWaypoints;
    final JButton btnCancel;
    final JLabel zResultLabel;

    private Thread calculateWaypointsThread;

    /**
     * Create the frame.
     */
    public Airdrop(final String title, final Icon icon) {
        super(title, icon);
        setBounds(0, 0, 650, 575);

        params = new ArrayList<Airdrop.AirdropParameter>();

        paramMass = new AirdropParameter("m", "Masse von Ei [g]", 106);
        params.add(paramMass);

        paramGravity = new AirdropParameter("g", "Schwerebeschläunigung [m/s^2]", 9.7803);
        params.add(paramGravity);

        paramWindDir = new AirdropParameter("wind_dir", "Windrichtung (0 = nach Norden, 90 = nach Osten, ...) [degree]");
        params.add(paramWindDir);

        paramPlaneDir = new AirdropParameter("plane_dir", "Flugrichtung (0 = nach Norden, 90 = nach Osten, ...) [degree]");
        params.add(paramPlaneDir);

        paramVWind = new AirdropParameter("v_wind", "Absolute Windgeschwindigkeit [m/s]");
        params.add(paramVWind);

        paramVPlane = new AirdropParameter("v_plane", "Absolute Fluggeschwindigkeit [m/s]", 15);
        params.add(paramVPlane);

        paramHeight = new AirdropParameter("z", "Flughöhe über Ziel [m]", 75);
        params.add(paramHeight);

        paramRoh = new AirdropParameter("roh", "Luftdichte [kg/m^3]", 1.2041);
        params.add(paramRoh);

        paramCws = new AirdropParameter("cws", "Wiederstandsbeiwert seitlich", 0.4);
        params.add(paramCws);

        paramCwv = new AirdropParameter("cwv", "Wiederstandsbeiwert vertikal", 0.06);
        params.add(paramCwv);

        paramAs = new AirdropParameter("As", "A seitlich [m^3]", 0.007603);
        params.add(paramAs);

        paramAv = new AirdropParameter("Av", "A vertikal [m^3]", 0.0012997);
        params.add(paramAv);

        paramH = new AirdropParameter("h", "Schrittweite für Integration", 0.000001);
        params.add(paramH);

        paramBalaceDistance = new AirdropParameter("balance", "Entfernung für Vorhaltepunkt und Nachhaltepunkt [m]", 200);
        params.add(paramBalaceDistance);

        paramTriggerRadius = new AirdropParameter("triggerRadius", "MissionPlanner Radius für Wegpunkt erreicht [m]", 30);
        params.add(paramTriggerRadius);

        paramDeathTime = new AirdropParameter("deathTime", "Totzeit durch den Haltemechanismus [sec]", 0.2);
        params.add(paramDeathTime);
        
        paramServo = new AirdropParameter("servo", "Servo", 6);
        params.add(paramServo);
        
        paramServoOpenValue = new AirdropParameter("servoOpenValue", "Servo Value für Öffnen", 1070);
        params.add(paramServoOpenValue);
        
        paramServoCloseValue = new AirdropParameter("servoCloseValue", "Servo Value für Schließen", 1600);
        params.add(paramServoCloseValue);

        final JPanel centerPanel = new JPanel();
        final GroupLayout groupLayout = new GroupLayout(getMainPanel());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                        groupLayout.createSequentialGroup().addContainerGap().addComponent(centerPanel, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE).addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                        groupLayout.createSequentialGroup().addContainerGap().addComponent(centerPanel, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE).addContainerGap()));

        btnGenerateWaypoints = new JButton("generate waypoints");
        btnGenerateWaypoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableGui(false);
                startGenerateWaypointsThread();
            }
        });

        btnCancel = new JButton("Abbrechen");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelGenerateWaypointsThread();
            }
        });

        zResultLabel = new JLabel();

        final GroupLayout glPanel = new GroupLayout(centerPanel);

        ParallelGroup groupWithLabels = glPanel.createParallelGroup(Alignment.LEADING);
        ParallelGroup groupWithTextFields = glPanel.createParallelGroup(Alignment.LEADING);
        SequentialGroup verticalGroup = glPanel.createSequentialGroup().addContainerGap();
        for (AirdropParameter param : params) {
            groupWithLabels.addComponent(param.getLabel());
            groupWithTextFields.addComponent(param.getTextField(), GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE);
            verticalGroup.addGroup(
                            glPanel.createParallelGroup(Alignment.BASELINE).addComponent(param.getLabel())
                                            .addComponent(param.getTextField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                            ComponentPlacement.RELATED);
        }

        glPanel.setHorizontalGroup(glPanel.createParallelGroup(Alignment.LEADING).addGroup(
                        glPanel.createSequentialGroup().addContainerGap().addGroup(groupWithLabels).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(groupWithTextFields)));

        glPanel.setVerticalGroup(glPanel.createParallelGroup(Alignment.LEADING).addGroup(verticalGroup));

        centerPanel.setLayout(glPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnGenerateWaypoints);
        bottomPanel.add(btnCancel);
        bottomPanel.add(zResultLabel);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    private void startGenerateWaypointsThread() {
        if (calculateWaypointsThread != null) {
            LOG.warn("GenerateWaypointsThread wurde gestartet, bevor die letzte Rechnung beendet wurde.");
            cancelGenerateWaypointsThread();
        }

        calculateWaypointsThread = new Thread() {
            public void run() {
                try {
                    generateWaypoints();
                    return;
                } catch (RuntimeException ex) {
                    throw ex;
                } finally {
                    enableGui(true);
                }
            }
        };
        calculateWaypointsThread.start();
    }

    private void cancelGenerateWaypointsThread() {
        LOG.debug("GenerateWaypointsThread wird gestoppt.");
        calculateWaypointsThread.interrupt();
    }

    private void enableGui(boolean enable) {
        btnGenerateWaypoints.setEnabled(enable);
        for (AirdropParameter param : params) {
            param.getTextField().setEnabled(enable);
        }
    }

    private void errorForUser(String message, Throwable t) {
        if (t == null) {
            LOG.error(message);
        } else {
            LOG.error(message, t);
        }
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void infoForUser(String message) {
        LOG.info(message);
        JOptionPane.showMessageDialog(null, message, "INFO", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Function generate a Waypoints-object for Airdrop task.
     *
     * @return Airdrop-Waypoints
     */
    private void generateWaypoints() {

        Target airdropTarget = Data.getTargets(SamType.TARGET_AIRDROP);
        if (airdropTarget == null || airdropTarget.getWaypoint() == null) {
            errorForUser("Zuerst muss im Routing Tab das Ziel gesetzt werden", null);
            return;
        }

        for (AirdropParameter param : params) {
            try {
                param.getParameterValue();
            } catch (NumberFormatException e) {
                errorForUser("Das Feld \"" + param.getLabel().getText() + "\" enthält keine gültige Eingabe", e);
                return;
            }
        }

        double x, y, z;
        int numIterations;
        try {
            double[] res = fk03Algorithm();
            x = res[0];
            y = res[1];
            z = res[2];
            numIterations = (int) res[3];
        } catch (RuntimeException e) {
            errorForUser("Fehler in der Berechnung! Es könnte an falschen Eingabeparametern liegen, einer fehlerhaften Implementierung, oder noch einem Fehler in den Formeln", e);
            return;
        }

        final LocationWp[] waypoints = createWaypoints(airdropTarget.getWaypoint(), x, y);
        final Waypoints wpp = new Waypoints(waypoints, SamType.TASK_AIRDROP);
        Data.setWaypoints(wpp);

        showResultMessage(z, numIterations, waypoints);
    }

    private void showResultMessage(double z, int numIterations, final LocationWp[] waypoints) {
        String message = String.format("%d Wegpunkte erstellt!\n\nDiese können nun im Routing Tab übertragen werden.\n\nBerechnung hat %d Iterationen gebraucht und ist auf z=%.5fm gekommen.",
                        waypoints.length, numIterations, z);
        String errorMessage = " Ergebniss kann falsch sein! Um die Laufzeit zu verkürzen kann die Schrittweite vergrößert werden.\n\n" + message;
        if (z < 0) {
            errorForUser("Berechnung frühzeitig angehalten!" + errorMessage, null);
        } else if (numIterations > MAX_ITERATIONS) {
            errorForUser("Berechnung hat maximale Iterationen überstiegen und wurde angehalten!" + errorMessage, null);
        } else {
            infoForUser(message + "\n\nFür genauere Ergebnisse, wenn z um einiges größer ist als 0, sollte die Schrittweite noch reduziert werden.");
        }
    }

    /**
     * Berechnet die Flugbahn des Ei's. (x,y,z) sind im Koordinatenssystem des
     * Flugzeuges gegeben. x geht in Flugrichtung; y geht nach rechts; z geht
     * nach unten
     * 
     * @return
     */
    private double[] fk03Algorithm() {
        double m = paramMass.getParameterValue();
        double g = paramGravity.getParameterValue();

        // 0 Grad = Rückenwind, 90 Grad = Wind von Links
        double a = (360 + paramWindDir.getParameterValue() - paramPlaneDir.getParameterValue()) % 360;
        double alpha = Math.toRadians(a);

        double vWind = paramVWind.getParameterValue();
        double vPlane = paramVPlane.getParameterValue();

        double ks = 0.5 * paramRoh.getParameterValue() * paramCws.getParameterValue() * paramAs.getParameterValue();
        double kv = 0.5 * paramRoh.getParameterValue() * paramCwv.getParameterValue() * paramAv.getParameterValue();

        double vx = vPlane + Math.cos(alpha) * vWind;
        double vy = Math.sin(alpha) * vWind;
        double vz = 0;

        double h = paramH.getParameterValue();

        double t = 0;
        double x = 0;
        double y = 0;
        double z = -paramHeight.getParameterValue();
        double numIterations = 0;
        while (z < 0) {
            double v = Math.sqrt(vx * vx + vy * vy + vz * vz);

            vx = vx - (ks / m) * v * vx * t;
            vy = vy - (ks / m) * v * vy * t;
            vz = vz - (kv / m) * v * vz * t + g * t;

            x += vx * t;
            y += vy * t;
            z += vz * t;

            t += h;

            numIterations++;
            if (numIterations % 50 == 0) {
                LOG.debug(String.format("Iteration %7.0f: (%3.5f, %3.5f, %3.5f)", numIterations, x, y, z));
                zResultLabel.setText(String.format("z=%3.5fm", z));
            }

            if (numIterations > MAX_ITERATIONS) {
                LOG.debug("Abbruch, da zu viele Iterationen");
                break;
            }

            if (Thread.currentThread().isInterrupted()) {
                LOG.debug("Abbruch, durch User");
                break;
            }
        }

        LOG.debug("Result: (" + x + ", " + y + ", " + z + ")");
        zResultLabel.setText(String.format("z=%3.5fm", z));

        return new double[] { x, y, z, numIterations };
    }

    /**
     * (xEggDistance, yEggDistance) ist die Richtung, in die das Ei fliegt im
     * Flugzeug Koordinatensystem.
     * 
     * @param target
     * @param xPlaneKoord
     * @param yPlaneKoord
     * @return
     */
    private LocationWp[] createWaypoints(LocationWp target, double xEggDistancePlaneCoords, double yEggDistancePlaneCoords) {
        double h = paramHeight.getParameterValue();
        double balaceDistance = paramBalaceDistance.getParameterValue();
        double planeDir = Math.toRadians(paramPlaneDir.getParameterValue());
        double triggerRadius = paramTriggerRadius.getParameterValue();
        double deathTimeMeters = paramVPlane.getParameterValue() * paramDeathTime.getParameterValue();
        
        int servo = (int) paramServo.getParameterValue();
        int servoOpenValue = (int) paramServoOpenValue.getParameterValue();
        int servoCloseValue = (int) paramServoCloseValue.getParameterValue();

        // Rotationsmatrix
        double planeToWorldRotation11 = Math.sin(planeDir);
        double planeToWorldRotation12 = Math.cos(planeDir);
        double planeToWorldRotation21 = planeToWorldRotation12;
        double planeToWorldRotation22 = -planeToWorldRotation11;

        // Berechne Drop Punkt (Mit beachtung der Totzeit)
        double xDropPlaneCoords = -xEggDistancePlaneCoords - deathTimeMeters;
        double yDropPlaneCoords = -yEggDistancePlaneCoords;
        double xDropWorldCoords = xDropPlaneCoords * planeToWorldRotation11 + yDropPlaneCoords * planeToWorldRotation21;
        double yDropWorldCoords = xDropPlaneCoords * planeToWorldRotation12 + yDropPlaneCoords * planeToWorldRotation22;

        // Berechne Servo Auslösepunkt, da für Wegpunkte immer ein Radius für
        // erreicht gilt
        double xDoSetServoOffsetWorldCoords = triggerRadius * planeToWorldRotation11;
        double yDoSetServoOffsetWorldCoords = triggerRadius * planeToWorldRotation12;

        // Berechne offset für vor und nachhaltepunkt
        double xDirWorldCoords = balaceDistance * Math.sin(planeDir);
        double yDirWorldCoords = balaceDistance * Math.cos(planeDir);

        LocationWp drop = createWpWithOffset(target, xDropWorldCoords, yDropWorldCoords, h);
        LocationWp dropTrigger = createWpWithOffset(drop, xDoSetServoOffsetWorldCoords, yDoSetServoOffsetWorldCoords, h);
        LocationWp before1 = createWpWithOffset(drop, -xDirWorldCoords * 1.00, -yDirWorldCoords * 1.00, h);
        LocationWp before2 = createWpWithOffset(drop, -xDirWorldCoords * 0.75, -yDirWorldCoords * 0.75, h);
        LocationWp before3 = createWpWithOffset(drop, -xDirWorldCoords * 0.50, -yDirWorldCoords * 0.50, h);
        LocationWp before4 = createWpWithOffset(drop, -xDirWorldCoords * 0.25, -yDirWorldCoords * 0.25, h);
        LocationWp after = createWpWithOffset(dropTrigger, xDoSetServoOffsetWorldCoords, yDoSetServoOffsetWorldCoords, h);
        
        LocationWp open = createServoWp(servo, servoOpenValue);
        LocationWp close = createServoWp(servo, servoCloseValue);

        return new LocationWp[] { before1, before2, before3, before4, drop, dropTrigger, open, after, close };
    }
    
    private LocationWp createWpWithOffset(LocationWp target, double x, double y, double h) {
        LocationWp wp = Misc.calcCoordsWithOffset(target.getLat(), target.getLng(), x, y);
        return new LocationWp(wp.getLat(), wp.getLng(), h, MavCmd.WAYPOINT, 1, 0, 0, 0, 0);
    }

    private LocationWp createServoWp(int servo, int servoValue) {
        return new LocationWp(0, 0, 0, MavCmd.DO_SET_SERVO, 1, servo, servoValue, 0, 0);
    }

    private AirdropParameter getParameterById(String id) {
        for (AirdropParameter param : params) {
            if (param.getId().equals(id)) {
                return param;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> readParameterMap() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(Settings.getSvFldr(), AIRDROP_FILE)))) {
            Map<String, String> parameterValues = (Map<String, String>) ois.readObject();
            ois.close();
            return parameterValues;
        } catch (IOException e) {
            LOG.error("Error reading ParameterMap", e);
        } catch (ClassNotFoundException e) {
            LOG.error("Error reading ParameterMap", e);
        }

        return Collections.emptyMap();
    }

    private void writeParameterMap(Map<String, String> parameterValues) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Settings.getSvFldr(), AIRDROP_FILE)))) {
            oos.writeObject(parameterValues);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            LOG.error("Error writing ParameterMap", e);
        }
    }

    @Override
    public void loadProperties() {
        Map<String, String> parameterValues = readParameterMap();

        for (Entry<String, String> entry : parameterValues.entrySet()) {
            AirdropParameter param = getParameterById(entry.getKey());

            if (param == null) {
                LOG.warn("Parameter konnte nicht geladen werden: " + entry.getValue());
            }

            param.getTextField().setText(entry.getValue());
        }
    }

    @Override
    public void saveProperties() {
        Map<String, String> parameterValues = new HashMap<String, String>();
        for (AirdropParameter param : params) {
            parameterValues.put(param.getId(), param.getTextField().getText());
        }

        writeParameterMap(parameterValues);
    }

}
