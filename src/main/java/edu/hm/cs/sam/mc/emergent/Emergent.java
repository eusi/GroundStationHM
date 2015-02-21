package edu.hm.cs.sam.mc.emergent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.ir.enuminterfaces.ColorEnum;
import edu.hm.cs.sam.mc.ir.enuminterfaces.EmergentGuiInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.GUIInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.TasksEnum;
import edu.hm.cs.sam.mc.ir.mainground.EmergentComponent;
import edu.hm.cs.sam.mc.ir.mainground.GroundAirPublisher;
import edu.hm.cs.sam.mc.ir.mainground.GroundAirSubscriber;
import edu.hm.cs.sam.mc.ir.mainground.PubSubControl;
import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Window;
import edu.hm.sam.location.SamType;


/**
 * @author Roland Widmann
 */
public class Emergent extends Window implements GUIInterface {

    private static final long serialVersionUID = 1L;
    private static boolean isActivated = false;
    private static final String TITLEONLINE = "Emergent Target - Emergent status: ONLINE";
    private static final String TITLEOFFLINE = "Emergent Target - Emergent status: OFFLINE";
    private static final String EMERGENTSTART = "Emergent Task Start";
    private static final String EMERGENTSTOP = "Emergent Task Stop";

    private static final Logger LOG = Logger.getLogger(Emergent.class.getName());

    private final JLabel txtEmergentLongitude = new JLabel();
    private final JLabel txtEmergentLatitude = new JLabel();

    private final JButton btnEmergentCalculate = new JButton("Calculate Waypoints");
    private final JButton btnEmergent = new JButton(EMERGENTSTART);

    private final JPanel contentPane = getMainPanel();
    private final JPanel pnlEmergentNorth = new JPanel(new BorderLayout());
    private final JPanel pnlEmergent = new JPanel(new BorderLayout());

    private final DefaultListModel<String> listModelConsole = new DefaultListModel<>();
    private final JList<String> lstConsole = new JList<String>(listModelConsole);

    private final JScrollPane scrollPaneConsole = new JScrollPane(lstConsole);
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final EmergentGuiInterface emergentTask = new EmergentComponent(this);
    private Thread emergentTh = new Thread(emergentTask);

    private GroundAirPublisher publisher;
    private GroundAirSubscriber subscriber;
    private boolean emergentTaskActive = false;


    public Emergent(final String title, final Icon icon) {
        super(title, icon);
        setSize(500, 325);
        setFrameIcon(Constants.EMERGENT_MINI_ICON);

        publisher = PubSubControl.getPublisher();
        subscriber = PubSubControl.getSubscriber();

        makeContentPane();
        makeButtonStyle();
        makeTabEmergent();
        tabbedPane.addTab("Emergent", pnlEmergent);
        catchTargetCoords();
        makeConsole();
        addActionListener();
        initGroundComponents();
        isActivated = true;
    }

    private void makeContentPane() {
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tabbedPane,BorderLayout.NORTH);
        contentPane.add(scrollPaneConsole, BorderLayout.CENTER);
    }

    private void makeButtonStyle(){
        btnEmergentCalculate.setFont(new Font("Arial", Font.BOLD, 24));  
        btnEmergent.setFont(new Font("Arial", Font.BOLD, 24));
    }

    private void makeTabEmergent(){
        pnlEmergentNorth.add(txtEmergentLongitude,BorderLayout.NORTH);
        pnlEmergentNorth.add(txtEmergentLatitude,BorderLayout.SOUTH);
        txtEmergentLatitude.setEnabled(false);
        txtEmergentLongitude.setEnabled(false);
        pnlEmergent.add(pnlEmergentNorth,BorderLayout.NORTH);
        pnlEmergent.add(btnEmergentCalculate,BorderLayout.CENTER);
        pnlEmergent.add(btnEmergent,BorderLayout.SOUTH);
    }

    private void catchTargetCoords(){
        if(Data.getTargets(SamType.TARGET_EMERGENT) != null){
            setStaticTargetCoordinates(Data.getTargets(SamType.TARGET_EMERGENT).getWaypoint().getLat(),
                    Data.getTargets(SamType.TARGET_EMERGENT).getWaypoint().getLng());
            btnEmergent.setEnabled(true);
        }else{
            setStaticTargetCoordinates(0.0,0.0);
            btnEmergent.setEnabled(false);
        }
    }

    private void initGroundComponents() {

        if(!PubSubControl.isPubActivated()) {
            PubSubControl.initPublisher();
            publisher = PubSubControl.getPublisher();
            publisher.start();
        }
        if(!PubSubControl.isSubActivated()) {
            PubSubControl.initSubscriber(this);
            subscriber = PubSubControl.getSubscriber();
            subscriber.start();
        } else {
            subscriber.setEmGui(this);
        }
        setDroneConnectionColor(ColorEnum.COLORRED);
    }

    private void startTask() {
        if(getTitle() == TITLEONLINE || 
                (JOptionPane.showConfirmDialog(null,"Emergent - Service not available!\n\tStart anyway?","Emergent-Service Offline", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)){
            emergentTh = new Thread(emergentTask);
            emergentTh.start();
            emergentTaskActive = true;
        }
        manageTaskButton();
    }
    
    private void stopTask(){
        if (emergentTaskActive && 
                (JOptionPane.showConfirmDialog(null,"Cancelling the task can cause the task finally to fail!\n\tCancel Emergent-Task anyway?","---- Emergent-Task Cancel ----", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)){
            emergentTask.stop();
        }
    }

    private void addActionListener() {
        btnEmergent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if(emergentTaskActive){
                    stopTask();
                }else{
                    startTask();
                }
 
            }
        });

        btnEmergentCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                waypointOperationEmergent();
            }
        });

        MouseListener refreshTargetCoords = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //not used
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //not used
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //not used
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                catchTargetCoords();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //not used
            }
        };

        txtEmergentLatitude.addMouseListener(refreshTargetCoords);
        txtEmergentLongitude.addMouseListener(refreshTargetCoords);
    }

    private boolean isEmpty(final String txt) {
        return txt.isEmpty();
    }

    @Override
    public void loadProperties() {
        //window status
    }

    private void makeConsole() {
        lstConsole.setForeground(Color.GREEN);
        lstConsole.setBackground(Color.BLACK);
        lstConsole.setModel(listModelConsole); // ?
    }

    @Override
    public void printConsole(final String text) {
        listModelConsole.addElement(text);
        lstConsole.ensureIndexIsVisible(listModelConsole.size() - 1);
        LOG.info(text);
    }

    @Override
    public void saveProperties() {
        //window status
    }

    public void setDroneConnectionColor(final ColorEnum setColor) {
        if (setColor == ColorEnum.COLORGREEN) {
            setTitle(TITLEONLINE);
        } else {
            setTitle(TITLEOFFLINE);
        }
    }

    @Override
    public void setMissionButtonEnabled(final TasksEnum task, final boolean enable) {
        if (task == TasksEnum.EMERGENT) {
            btnEmergent.setEnabled(enable);
        }
    }

    private void waypointOperationEmergent() {

        if (!isEmpty(txtEmergentLongitude.getText().trim())
                && !isEmpty(txtEmergentLatitude.getText().trim())) {
            try {
                emergentTask.calcWaypoints();
            } catch (final NumberFormatException e) {
                printConsole("Emergent SearchArea coordinates malformed");
            }
        }
    }

    @Override
    public void setStaticTargetCoordinates(double lat, double lng) {
        txtEmergentLatitude.setText("Lat: " + lat);
        txtEmergentLongitude.setText("Lng: " + lng);
        if(lat == 0.0 && lng == 0.0){
            btnEmergent.setEnabled(false);
        }else{
            btnEmergent.setEnabled(true);
        }
    }


    @Override
    public void setDynamicTargetCoordinates(double lat, double lng) {
        //Only needed in Static
    }

    public static boolean isActivated() {
        return isActivated;
    }

    private void manageTaskButton() {
        if(emergentTaskActive){
            btnEmergent.setText(EMERGENTSTOP);
        }else {
            btnEmergent.setText(EMERGENTSTART);
        }
    }

    @Override
    public void taskCancelled(TasksEnum task,boolean success) {
        emergentTaskActive = false;
        manageTaskButton();
        if (!success){
            printConsole("Emergent Task canceled!");
        }
    }

    @Override
    public void setStaticTaskActive(boolean taskActive) {
        // not needed
        
    }

    @Override
    public void setDynamicTaskActive(boolean taskActive) {
        // not needed
        
    }

    @Override
    public void setEmergentTaskActive(boolean taskActive) {
        emergentTaskActive = true;
        manageTaskButton();
    }

}
