package edu.hm.cs.sam.mc.sric;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by Frederic on 07.11.2014.
 */
public class SRICWindow {
    private JPanel sricPanel;
    private JTextField sricSSID;
    private JTextField sricWEPPassword;
    private JButton sendFirstConfigButton;
    private JTextField sricReadFTPAddress;
    private JTextField sricReadUsername;
    private JTextField sricReadPassword;
    private JButton readListButton;
    private JTextField sricWriteUsername;
    private JTextField sricWritePassword;
    private JTextField sricWritePicture;
    private JTextField sricWriteFTPAddress;
    private JButton sricWriteExecuteButtons;
    private JTextField sricReadFolder;
    private JTextField sricWriteFolder;
    private JComboBox fileList;
    private JButton loadFileButton;
    private JTextField logitude;
    private JTextField latitude;
    private JTextField height;
    private JButton setPosButton;
    private JButton cancelButton;
    private JFileChooser picChooser = new JFileChooser();
    private String fileName;

    public SRICWindow() {
        sricWritePicture.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int ret = picChooser.showOpenDialog(sricPanel);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    sricWritePicture.setText(picChooser.getSelectedFile().getAbsolutePath());
                    fileName = picChooser.getSelectedFile().getName();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //unused
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //unused
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //unused
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //unused
            }
        });
    }

    public JPanel getMainPanel() {
        return sricPanel;
    }

    public String getLongitude() {
        return logitude.getText();
    }

    public void setLongitude(String lng) {
        logitude.setText(lng);
    }

    public String getLatitude() {
        return latitude.getText();
    }

    public void setLatitude(String lat) {
        latitude.setText(lat);
    }

    public String getHeight() {
        return height.getText();
    }

    public void setHeight(String h) {
        height.setText(h);
    }

    public void setWEP(String in) {
        sricWEPPassword.setText(in);
    }

    public void setReadAddress(String in) {
        sricReadFTPAddress.setText(in);
    }

    public void setWriteAddress(String in) {
        sricWriteFTPAddress.setText(in);
    }

    public void setWritePicture(String in) {
        sricWritePicture.setText(in);
    }

    public String getSSID() {
        return sricSSID.getText();
    }

    public void setSSID(String in) {
        sricSSID.setText(in);
    }

    public String getWEPPassword() {
        return sricWEPPassword.getText();
    }

    public String getReadFTPAddress() {
        return sricReadFTPAddress.getText();
    }

    public String getReadUsername() {
        return sricReadUsername.getText();
    }

    public void setReadUsername(String in) {
        sricReadUsername.setText(in);
    }

    public String getReadPassword() {
        return sricReadPassword.getText();
    }

    public void setReadPassword(String in) {
       sricReadPassword.setText(in);
    }

    public String getReadFilename() {
        return fileList.getSelectedItem().toString();
    }

    public String getWriteFTPAddress() {
        return sricWriteFTPAddress.getText();
    }

    public String getWriteUsername() {
        return sricWriteUsername.getText();
    }

    public void setWriteUsername(String in) {
        sricWriteUsername.setText(in);
    }

    public String getWritePassword() {
        return sricWritePassword.getText();
    }

    public void setWritePassword(String in) {
        sricWritePassword.setText(in);
    }

    public String getWritePicturePath() {
        return sricWritePicture.getText();
    }

    public String getReadFolder() {
        return sricReadFolder.getText();
    }

    public void setReadFolder(String in) {
        sricReadFolder.setText(in);
    }

    public String getWriteFolder() {
        return sricWriteFolder.getText();
    }

    public void setWriteFolder(String in) {
        sricWriteFolder.setText(in);
    }

    public String getWritePictureFileName() {
        return fileName;
    }

    public void setFileList(String[] list) {
        fileList.removeAllItems();
        for (String item : list) {
            fileList.addItem(item);
        }
    }

    public void setSetPosActionListener(final ActionListener al) {
        setPosButton.addActionListener(al);
    }

    public void setWifiConfigActionListener(final ActionListener al) {
        sendFirstConfigButton.addActionListener(al);
    }

    public void setReadFileListActionListener(final ActionListener al) {
        readListButton.addActionListener(al);
    }

    public void setReadExecuteActionListener(final ActionListener al) {
        loadFileButton.addActionListener(al);
    }

    public void setWriteExecuteActionListener(final ActionListener al) {
        sricWriteExecuteButtons.addActionListener(al);
    }

    public void setCancelActionListener(final ActionListener al) {
        cancelButton.addActionListener(al);
    }
}