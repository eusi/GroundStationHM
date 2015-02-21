package edu.hm.cs.sam.mc.ir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import edu.hm.cs.sam.mc.ir.enuminterfaces.DynamicGuiInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.GUIInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.GroundGuiInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.StaticGuiInterface;
import edu.hm.cs.sam.mc.ir.enuminterfaces.TasksEnum;
import edu.hm.cs.sam.mc.ir.mainground.DynamicIRComponent;
import edu.hm.cs.sam.mc.ir.mainground.GroundAirPublisher;
import edu.hm.cs.sam.mc.ir.mainground.GroundAirSubscriber;
import edu.hm.cs.sam.mc.ir.mainground.PubSubControl;
import edu.hm.cs.sam.mc.ir.mainground.StaticIRComponent;
import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Window;
import edu.hm.sam.location.SamType;

/**
 * @author Roland Widmann
 */

public class Ir extends Window implements GUIInterface {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(Ir.class.getName());
    private static final String ENABLEFALLBACK = "Enable Fallback";
    private static final String TITLEONLINE = "Infrared (IR) - IR-Service status: ONLINE";
    private static final String TITLEOFFLINE = "Infrared (IR) - IR-Service status: OFFLINE";
    private static final String IRSTATICSTART = "IR Static Start";
    private static final String IRDYNAMICSTART = "IR DYNAMIC Start";
    private static final String IRSTATICSTOP = "IR Static Stop";
    private static final String IRDYNAMICSTOP = "IR DYNAMIC Stop";
    private static boolean isActivated = false;


    private final JPanel pnlStatNorth = new JPanel(new BorderLayout());
    private final JPanel pnlDynNorth = new JPanel(new BorderLayout());
    private final JPanel contentPane = getMainPanel();
    private final JPanel pnlStatic = new JPanel(new BorderLayout());
    private final JPanel pnlDynamic = new JPanel(new BorderLayout());

    private final JLabel txtIRStaticLongitude = new JLabel();
    private final JLabel txtIRStaticLatitude = new JLabel();
    private final JLabel txtIRDynamicLongitude = new JLabel();
    private final JLabel txtIRDynamicLatitude = new JLabel();

    private final JButton btnIrStatic = new JButton(IRSTATICSTART);
    private final JButton btnIrDynamic = new JButton(IRDYNAMICSTART);
    private final JButton btnRStaticCalculate = new JButton("Calculate IR-Static Waypoints");
    private final JButton btnIRDynamicCalculate = new JButton("Calculate IR-Dynamic Waypoints");
    private final JButton btnIRStaticFallBack = new JButton(ENABLEFALLBACK);
    private final JButton btnIRDynamicFallBack = new JButton(ENABLEFALLBACK);

    private final DefaultListModel<String> listModelConsole = new DefaultListModel<>();
    private final JList<String> lstConsole = new JList<String>(listModelConsole);

    private final JScrollPane scrollPaneConsole = new JScrollPane(lstConsole);
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final Font fntButtonFont = new Font("Arial", Font.BOLD, 24);

    private boolean staticTaskActive = false;
    private boolean dynamicTaskActive = false;

    private GroundAirPublisher publisher;
    private GroundAirSubscriber subscriber;
    private StaticGuiInterface staticIRTask;
    private DynamicGuiInterface dynamicIRTask;
    private Thread staticIRT;
    private Thread dynamicIRT;
    private boolean staticFallBackActive = false;
    private boolean dynamicFallBackActive = false;
    private boolean staticWPcalced = false;
    private boolean dynamicWPcalced = false;

    public Ir(final String title, final Icon icon) {
        super(title, icon);
        setSize(510, 325);
        setFrameIcon(Constants.IR_MINI_ICON);

        publisher = PubSubControl.getPublisher();
        subscriber = PubSubControl.getSubscriber();
        btnIrDynamic.setEnabled(false);
        btnIrStatic.setEnabled(false);


        makeContentPane();
        makeButtonStyle();
        makeTabStatic();
        makeTabDynamic();
        makeTabs();
        catchTargetCoords();
        makeConsole();
        addActionListener();
        addMouseListener();
        manageTaskButton();
        initGroundComponents();
        isActivated = true;
    }

    private void makeContentPane() {
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tabbedPane, BorderLayout.NORTH);
        contentPane.add(scrollPaneConsole, BorderLayout.CENTER);
    }

    private void makeButtonStyle() {
        btnRStaticCalculate.setFont(fntButtonFont);
        btnIRDynamicCalculate.setFont(fntButtonFont);
        btnIrDynamic.setFont(fntButtonFont);
        btnIrStatic.setFont(fntButtonFont);
        btnIRDynamicFallBack.setFont(fntButtonFont);
        btnIRStaticFallBack.setFont(fntButtonFont);
    }

    private void makeTabStatic() {
        pnlStatNorth.add(txtIRStaticLongitude, BorderLayout.NORTH);
        pnlStatNorth.add(txtIRStaticLatitude, BorderLayout.SOUTH);
        pnlStatic.add(pnlStatNorth, BorderLayout.NORTH);
        pnlStatic.add(btnRStaticCalculate, BorderLayout.CENTER);

        JPanel staticSouthSouth = new JPanel(new FlowLayout());
        pnlStatic.add(staticSouthSouth, BorderLayout.SOUTH);

        staticSouthSouth.add(btnIrStatic, BorderLayout.SOUTH);
        staticSouthSouth.add(btnIRStaticFallBack, BorderLayout.SOUTH);
    }

    private void makeTabDynamic() {
        pnlDynNorth.add(txtIRDynamicLongitude, BorderLayout.NORTH);
        pnlDynNorth.add(txtIRDynamicLatitude, BorderLayout.SOUTH);
        pnlDynamic.add(pnlDynNorth, BorderLayout.NORTH);
        pnlDynamic.add(btnIRDynamicCalculate, BorderLayout.CENTER);

        JPanel pnlDynamicSouthSouth = new JPanel(new FlowLayout());
        pnlDynamicSouthSouth.add(btnIrDynamic, BorderLayout.SOUTH);
        pnlDynamicSouthSouth.add(btnIRDynamicFallBack, BorderLayout.SOUTH);

        pnlDynamic.add(pnlDynamicSouthSouth, BorderLayout.SOUTH);
    }

    private void makeTabs() {
        tabbedPane.addTab("Static", pnlStatic);
        tabbedPane.addTab("Dynamic", pnlDynamic);
    }

    private void catchTargetCoords() {
        if (Data.getTargets(SamType.TARGET_IR_STATIC) != null) {
            setStaticTargetCoordinates(Data
                    .getTargets(SamType.TARGET_IR_STATIC).getWaypoint()
                    .getLat(), Data.getTargets(SamType.TARGET_IR_STATIC)
                    .getWaypoint().getLng());
            //btnIrStatic.setEnabled(true);
        } else {
            setStaticTargetCoordinates(0.0, 0.0);
            btnIrStatic.setEnabled(false);
        }

        if (Data.getTargets(SamType.TARGET_IR_DYNAMIC) != null) {
            setDynamicTargetCoordinates(
                    Data.getTargets(SamType.TARGET_IR_DYNAMIC).getWaypoint()
                    .getLat(),
                    Data.getTargets(SamType.TARGET_IR_DYNAMIC).getWaypoint()
                    .getLng());
        } else {
            setDynamicTargetCoordinates(0.0, 0.0);
        }
    }

    private void makeConsole() {
        lstConsole.setForeground(Color.GREEN);
        lstConsole.setBackground(Color.BLACK);
        lstConsole.setModel(listModelConsole);
    }

    private void initGroundComponents() {

        if (!PubSubControl.isPubActivated()) {
            PubSubControl.initPublisher();
            publisher = PubSubControl.getPublisher();
            publisher.start();
        }
        if (!PubSubControl.isSubActivated()) {
            PubSubControl.initSubscriber(this);
            subscriber = PubSubControl.getSubscriber();
            subscriber.start();
        } else {
            subscriber.setIrGui(this);
        }

        staticIRTask = new StaticIRComponent(this, publisher);
        dynamicIRTask = new DynamicIRComponent(this, publisher);
        setDroneConnectionColor(ColorEnum.COLORRED);
    }

    private void addActionListener() {
        btnIrStatic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if(staticTaskActive){
                    stopTask(TasksEnum.IRSTATIC);
                }else{
                    startTask(TasksEnum.IRSTATIC);
                }
            }
        });

        btnIrDynamic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if(dynamicTaskActive){
                    stopTask(TasksEnum.IRDYNAMIC);
                }else{
                    startTask(TasksEnum.IRDYNAMIC);
                }
            }
        });

        btnRStaticCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                waypointOperation(TasksEnum.IRSTATIC);
            }
        });

        btnIRDynamicCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                waypointOperation(TasksEnum.IRDYNAMIC);
            }
        });

        btnIRStaticFallBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fallBackOperations(TasksEnum.IRSTATIC);
            }
        });

        btnIRDynamicFallBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fallBackOperations(TasksEnum.IRDYNAMIC);
            }
        });
    }

    private void addMouseListener() {
        MouseListener refreshTargetCoords = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // not used
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // not used
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // not used
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                catchTargetCoords();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // not used
            }
        };

        txtIRStaticLatitude.addMouseListener(refreshTargetCoords);
        txtIRStaticLongitude.addMouseListener(refreshTargetCoords);
        txtIRDynamicLatitude.addMouseListener(refreshTargetCoords);
        txtIRDynamicLongitude.addMouseListener(refreshTargetCoords);
    }

    public GroundAirPublisher getPublisher() {
        return publisher;
    }

    public GroundAirSubscriber getSubscriber() {
        return subscriber;
    }

    @Override
    public void loadProperties() {
        // not mine
    }

    @Override
    public void printConsole(final String text) {
        listModelConsole.addElement(text);
        lstConsole.ensureIndexIsVisible(listModelConsole.size() - 1);
        LOG.info(text);
    }

    @Override
    public void saveProperties() {
        // not mine
    }

    @Override
    public void setDroneConnectionColor(final ColorEnum setColor) {
        if (setColor == ColorEnum.COLORGREEN) {
            setTitle(TITLEONLINE);
        } else {
            setTitle(TITLEOFFLINE);
        }
    }

    @Override
    public void setMissionButtonEnabled(final TasksEnum task,
            final boolean enable) {
        if (task == TasksEnum.IRDYNAMIC) {
            btnIrDynamic.setEnabled(enable);
        } else if (task == TasksEnum.IRSTATIC) {
            btnIrStatic.setEnabled(enable);
        }
    }

    private void stopTask(final TasksEnum taskToStop){
        if (taskToStop == TasksEnum.IRSTATIC && staticTaskActive &&
                (JOptionPane.showConfirmDialog(null,"Cancelling the task may cause the task finally to fail!\n\tCancel IR-Static-Task anyway?","---- IR-Static-Task Cancel ----", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)) {
            staticIRTask.stop();
        }else if (taskToStop == TasksEnum.IRDYNAMIC && dynamicTaskActive&&
                (JOptionPane.showConfirmDialog(null,"Cancelling the task may cause the task finally to fail!\n\tCancel IR-Dynamic-Task anyway?","---- IR-Dynamic-Task Cancel ----", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)) {
            dynamicIRTask.stop();
        }
    }

    private void startTask(final TasksEnum taskToStart) {

        if (getTitle() == TITLEONLINE
                || (JOptionPane.showConfirmDialog(null,
                        "IR - Service not available!\n\tStart anyway?",
                        "IR-Service Offline", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
            if (taskToStart == TasksEnum.IRSTATIC) {
                staticIRT = new Thread(staticIRTask);
                staticIRT.start();
                //                    staticTaskActive=true;
            }else if (taskToStart == TasksEnum.IRDYNAMIC) {
                dynamicIRT = new Thread(dynamicIRTask);
                dynamicIRT.start();
                //                    dynamicTaskActive=true;
            }
        }
        manageTaskButton();
    }

    private void manageTaskButton() {

        if(staticWPcalced){
            btnIrStatic.setEnabled(true);
            btnIRStaticFallBack.setEnabled(true);
        }else{
            btnIrStatic.setEnabled(false);
            btnIRStaticFallBack.setEnabled(false);
        }

        if(dynamicWPcalced){
            btnIrDynamic.setEnabled(true);
            btnIRDynamicFallBack.setEnabled(true);
        }else{
            btnIrDynamic.setEnabled(false);
            btnIRDynamicFallBack.setEnabled(false);
        }

        if(staticTaskActive && btnIrStatic.isEnabled()){
            btnIrStatic.setText(IRSTATICSTOP);
            btnIrDynamic.setEnabled(false);
            btnIRDynamicFallBack.setEnabled(false);
        }else {
            btnIrStatic.setText(IRSTATICSTART);
            if(dynamicWPcalced){
                btnIrDynamic.setEnabled(true);
                btnIRDynamicFallBack.setEnabled(true);
            }
        }

        if(dynamicTaskActive && btnIrDynamic.isEnabled()){
            btnIrDynamic.setText(IRDYNAMICSTOP);
            btnIrStatic.setEnabled(false);
            btnIRStaticFallBack.setEnabled(false);
        }else {
            btnIrDynamic.setText(IRDYNAMICSTART);
            if(staticWPcalced){
                btnIrStatic.setEnabled(true);
                btnIRStaticFallBack.setEnabled(true);
            }
        }

    }

    private void waypointOperation(final TasksEnum task) {
        if ((task == TasksEnum.IRSTATIC)) {
            ((GroundGuiInterface) staticIRTask).calcWaypoints();
            staticWPcalced = true;
        }
        if (task == TasksEnum.IRDYNAMIC) {
            dynamicIRTask.calcWaypoints();
            dynamicWPcalced = true;
        }
        manageTaskButton();
    }

    private void fallBackOperations(TasksEnum task) {
        if ((task == TasksEnum.IRSTATIC)) {
            if (staticFallBackActive) {
                btnIRStaticFallBack.setText(ENABLEFALLBACK);
                //#######ist von mir######
                staticIRTask.startFallBack();
            } else {
                btnIRStaticFallBack.setText("DISABLE Fallback");
                // TO Do Fallbacksignal Static senden
              //#######ist von mir######
                staticIRTask.stopFallBack();
            }
            staticFallBackActive = !staticFallBackActive;
        }

        if (task == TasksEnum.IRDYNAMIC) {
            if (dynamicFallBackActive) {
                btnIRDynamicFallBack.setText(ENABLEFALLBACK);
                //#######ist von mir######
                dynamicIRTask.startFallBack();
            } else {
                btnIRDynamicFallBack.setText("DISABLE Fallback");
                // TO Do Fallbacksignal Dynamic senden
                //#######ist von mir######
                dynamicIRTask.startFallBack();
            }
        }
        dynamicFallBackActive = !dynamicFallBackActive;
    }

    @Override
    public void setStaticTargetCoordinates(double lat, double lng) {
        txtIRStaticLatitude.setText("Lat: " + lat);
        txtIRStaticLongitude.setText("Lng: " + lng);

        //        if (lat == 0.0 && lng == 0.0) {
        //            btnIrStatic.setEnabled(false);
        //            btnIRStaticFallBack.setEnabled(false);
        //        } else  if(!dynamicTaskActive){
        //            btnIrStatic.setEnabled(true);
        //            btnIRStaticFallBack.setEnabled(true);
        //        }
        //        manageTaskButton();
    }

    @Override
    public void setDynamicTargetCoordinates(double lat, double lng) {
        txtIRDynamicLatitude.setText("Lat: " + lat);
        txtIRDynamicLongitude.setText("Lng: " + lng);

        //        if (lat == 0.0 && lng == 0.0) {
        //            btnIrDynamic.setEnabled(false);
        //            btnIRDynamicFallBack.setEnabled(false);
        //        } else if(!staticTaskActive){
        //            btnIrDynamic.setEnabled(true);
        //            btnIRDynamicFallBack.setEnabled(true);
        //        }
        //        manageTaskButton();
    }

    public static boolean isActivated() {
        return isActivated;
    }

    public boolean isStaticTaskActive() {
        return staticTaskActive;
    }

    public void setStaticTaskActive(boolean taskActive) {
        this.staticTaskActive = taskActive;
        manageTaskButton();
    }

    public boolean isDynamicTaskActive() {
        return dynamicTaskActive;
    }

    public void setDynamicTaskActive(boolean taskActive) {
        this.dynamicTaskActive = taskActive;
        manageTaskButton();
    }

    @Override
    public void taskCancelled(TasksEnum task, boolean success) {
        if (task == TasksEnum.IRSTATIC){
            staticTaskActive = false;
            manageTaskButton();
            if(!success){
                printConsole("IR-Static Task canceled!");
            }
            staticFallBackActive=false;
        }else if (task == TasksEnum.IRDYNAMIC){
            dynamicIRT = null;
            dynamicTaskActive = false;
            manageTaskButton();
            if(!success){
                printConsole("IR-Dynamic Task canceled!");
            }
            dynamicFallBackActive=false;
        }
        manageTaskButton();
    }

    @Override
    public void setEmergentTaskActive(boolean taskActive) {
        // TODO Auto-generated method stub

    }

}