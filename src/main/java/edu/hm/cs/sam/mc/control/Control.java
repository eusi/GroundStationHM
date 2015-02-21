package edu.hm.cs.sam.mc.control;
import com.intellij.uiDesigner.lw.BorderLayoutSerializer;

import javax.swing.*;
import java.awt.event.*;
import java.util.Map;
import java.awt.BorderLayout;


import edu.hm.cs.sam.mc.misc.Constants;
import edu.hm.cs.sam.mc.misc.Window;
import edu.hm.sam.Message;
import edu.hm.sam.control.ControlMessage;
import edu.hm.sam.control.HeartBeatMessage;
import edu.hm.sam.control.SubscriptionHandler;

/**
 * @author Opitz, Jan (jopitz@hm.edu)
 * @author Christoph Kapinos
 * @author Sebastian Schuster
 */
@SuppressWarnings("serial")
public class Control extends Window {


    private final ControlWindow window = new ControlWindow();
    private GroundStationClient groundStationClient = GroundStationClient.getInstance();

    /**
     *
     */
    public Control(final String title, final Icon icon) {
        super(title, icon);
        createFrame();
        fillTaskList();
        window.setConfigureActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configure();
            }
        });
        window.setTaskNewComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                changedSelection((ControlMessage.Task)comboBox.getSelectedItem());
            }
        });
        window.setOToSButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oToSClicked();
            }
        });
        window.setSToOButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sToOClicked();
            }
        });

        groundStationClient.addSubscriptionHandler(ControlMessage.Service.CONTROLLER, HeartBeatMessage.SUMMARY, new SubscriptionHandler<HeartBeatMessage>() {
            @Override
            public void handleSubscription(final HeartBeatMessage subscription) {
                if (!SwingUtilities.isEventDispatchThread()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            DefaultListModel list;
                            switch(subscription.getDevice()){
                                case SMARTPHONE:
                                    list = window.getSmartphoneStatusListModel();
                                    window.setLastHeartbeatMessageSmartphone(subscription.getTimestamp()*1000);
                                    break;
                                case ODROID:
                                    list = window.getOdroidStatusListModel();
                                    window.setLastHeartbeatMessageOdroid(subscription.getTimestamp()*1000);
                                    break;
                                default:
                                    //ToDO: Wenn kein Device gesetzt ist, was dann???
                                    list = window.getSmartphoneStatusListModel();
                                    break;
                            }


                            list.clear();
                            Map<ControlMessage.Service, HeartBeatMessage> summary = subscription.getSummary();
                            for(ControlMessage.Service service : summary.keySet()){
                                list.addElement(service + " : " + summary.get(service).getState());
                            }
                            window.setTaskText(subscription.getTask().toString());
                        }
                    });

                }
            }
        });
    }

    private void createFrame() {
        getMainPanel().setLayout(new BorderLayout());
        getMainPanel().add(BorderLayout.CENTER,window.getMainPanel());
    }

    public void fillTaskList() {
        for(ControlMessage.Task task : ControlMessage.Task.values()){
            window.addTaskToTasklist(task);
        }
        changedSelection(window.getSelectedNewTask());
    }

    private void configure() {
        ControlMessage controlMessage = new ControlMessage(window.getSelectedNewTask());
        collectServices(controlMessage, window.getSmartphoneNewListModel(), ControlMessage.Device.SMARTPHONE);
        collectServices(controlMessage, window.getOdroidNewListModel(), ControlMessage.Device.ODROID);
        groundStationClient.sendAction(ControlMessage.Service.CONTROLLER, "configure", controlMessage);
    }

    private void collectServices(ControlMessage  controlMessage, DefaultListModel model, ControlMessage.Device device) {
        for(int i = 0; i < model.size(); i++) {
            controlMessage.setAssignment((ControlMessage.Service) model.get(i), device);
        }
    }

    private void changedSelection(ControlMessage.Task selection){
        window.emptyNewServiceLists();
        for (ControlMessage.Service service: selection.getServices()) {
            window.addSmartphoneNewList(service);
        }
    }
    private void oToSClicked() {
        window.addSmartphoneNewList(window.getSelectedOdroidNewList());
        window.removeSelectedOdroidNewList();
    }
    private void sToOClicked() {
        window.addOdroidNewList(window.getSelectedSmartphoneNewList());
        window.removeSelectedSmartphoneNewList();
    }

    @Override
    public void loadProperties() {

    }

    @Override
    public void saveProperties() {

    }
}
