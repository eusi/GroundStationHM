package edu.hm.cs.sam.mc.control;

import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.hm.sam.control.*;

/**
 * Created by Andreas on 15.12.2014.
 */
public class ControlWindow {

    private JPanel controlPanel;
    private DefaultListModel smartphoneStatusListModel = new DefaultListModel();
    private JList<ControlMessage.Service> smartphoneStatusList;
    private DefaultListModel smartphoneNewListModel = new DefaultListModel();
    private JList<ControlMessage.Service> smartphoneNewList;
    private DefaultListModel odroidStatusListModel = new DefaultListModel();
    private JList<ControlMessage.Service> odroidStatusList;
    private DefaultListModel odroidNewListModel = new DefaultListModel();
    private JList<ControlMessage.Service> odroidNewList;

    private JButton configureButton;

    private JTextField taskStatusTextField;

    private JTextField lastHeartbeatMessageSmartphone;
    private JTextField lastHeartbeatMessageOdroid;


    private JComboBox<ControlMessage.Task> taskNewComboBox;
    private DefaultComboBoxModel taskNewComboBoxModel = new DefaultComboBoxModel();

    private JButton oToSButton;
    private JButton sToOButton;
    private JButton gToOButton;
    private JButton oToGButton;


    public ControlWindow() {
        smartphoneStatusList.setModel(smartphoneStatusListModel);
        smartphoneNewList.setModel(smartphoneNewListModel);
        odroidStatusList.setModel(odroidStatusListModel);
        odroidNewList.setModel(odroidNewListModel);
        taskNewComboBox.setModel(taskNewComboBoxModel);
    }

    public void setLastHeartbeatMessageSmartphone(long timestamp) {
        lastHeartbeatMessageSmartphone.setText(new SimpleDateFormat("HH:mm:ss").format(timestamp));
    }

    public void setLastHeartbeatMessageOdroid(long timestamp) {
        lastHeartbeatMessageOdroid.setText(new SimpleDateFormat("HH:mm:ss").format(timestamp));
    }

    public JPanel getMainPanel() {
        return controlPanel;
    }

    public void setConfigureActionListener(final ActionListener listener) {
        configureButton.addActionListener(listener);
    }
    public void setOToSButtonActionListener(final ActionListener listener) {
        oToSButton.addActionListener(listener);
    }
    public void setSToOButtonActionListener(final ActionListener listener) {
        sToOButton.addActionListener(listener);
    }
    public void setGToOButtonActionListener(final ActionListener listener) {
        gToOButton.addActionListener(listener);
    }
    public void setOToGButtonActionListener(final ActionListener listener) {
        oToGButton.addActionListener(listener);
    }
    public void setTaskNewComboBoxActionListener(final ActionListener listener) {
        taskNewComboBox.addActionListener(listener);
    }

    public void addTaskToTasklist(ControlMessage.Task task) {
        taskNewComboBoxModel.addElement(task);
    }

    public ControlMessage.Task getSelectedNewTask() {
        return (ControlMessage.Task)taskNewComboBox.getSelectedItem();
    }

    public void emptyNewServiceLists() {
        smartphoneNewListModel.removeAllElements();
        odroidNewListModel.removeAllElements();
    }

    public void addSmartphoneNewList(ControlMessage.Service service) {
        if(service != null){
            smartphoneNewListModel.addElement(service);
        }
    }
    public void addOdroidNewList(ControlMessage.Service service) {
        if(service != null) {
            odroidNewListModel.addElement(service);
        }
    }

    public ControlMessage.Service getSelectedSmartphoneNewList() {
        return smartphoneNewList.getSelectedValue();
    }
    public ControlMessage.Service getSelectedOdroidNewList() {
        return odroidNewList.getSelectedValue();
    }

    public void removeSelectedSmartphoneNewList() {
        int selectedIndex = smartphoneNewList.getSelectedIndex();
        if (selectedIndex != -1) {
            smartphoneNewListModel.remove(selectedIndex);
        }
    }
    public void removeSelectedOdroidNewList() {
        int selectedIndex = odroidNewList.getSelectedIndex();
        if (selectedIndex != -1) {
            odroidNewListModel.remove(selectedIndex);
        }
    }


    public void setTaskText(String text) {
        taskStatusTextField.setText(text);
    }


    public DefaultListModel getSmartphoneNewListModel() {
        return smartphoneNewListModel;
    }

    public DefaultListModel getOdroidNewListModel() {
        return odroidNewListModel;
    }




    public DefaultListModel getSmartphoneStatusListModel() {
        return smartphoneStatusListModel;
    }

    public DefaultListModel getOdroidStatusListModel() {
        return odroidStatusListModel;
    }

}
