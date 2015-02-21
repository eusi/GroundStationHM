package edu.hm.cs.sam.mc.sric;

import edu.hm.cs.sam.mc.control.GroundStationClient;
import edu.hm.cs.sam.mc.misc.*;
import edu.hm.cs.sam.mc.rest.RestClient;
import edu.hm.sam.control.ControlMessage;
import edu.hm.sam.control.SubscriptionHandler;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;
import edu.hm.sam.sric.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * SRIC class. This module's job is to collect WLAN/FTP data in mask and send
 * that data to a target.
 *
 * @author Frederic Schuetze
 */
@SuppressWarnings("serial")
public class SRIC extends edu.hm.cs.sam.mc.misc.Window {

    /**
     * Beginn der Nachrichen die vom Flieger kommen.
     */
    private static final String PLANE_START_MSG = "Nachricht vom Flieger: ";

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SRIC.class.getName());

    /**
     * Nachricht, die erscheint sobald es einen Timeout gibt.
     */
    private static final String TIMEOUT_MESSAGE = "SRIC-TIMEOUT: Odroid nicht verfügbar";

    /**
     * Timeout in millisekunden.
     */
    private static final long TIMEOUT = 5000;

    /**
     * Window.
     */
    private final SRICWindow window = new SRICWindow();

    /**
     * Hilfsvariable für Timeout.
     */
    private boolean operationcomplete = false;

    /**
     * Konstruktor.
     */
    public SRIC(String title, Icon icon) {
        super(title, icon);
        getMainPanel().setLayout(new BorderLayout());
        getMainPanel().add(BorderLayout.CENTER,window.getMainPanel());
        //setContentPane(window.getMainPanel());
        pack();
        window.setSetPosActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSRICPosition();
            }
        });
        window.setWifiConfigActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendWifiConfiguration();
            }
        });
        window.setReadFileListActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readRemoteFileList();
            }
        });
        window.setReadExecuteActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readFile();
            }
        });
        window.setWriteExecuteActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeFile();
            }
        });
        window.setCancelActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelAll();
            }
        });
        loadSettings();
        addSubscriptionHandler();
    }

    /**
     * Laden des SRIC Waypoints.
     */
    public void loadSettings() {
        if (Data.getTargets(SamType.TARGET_SRIC) != null &&
                Data.getTargets(SamType.TARGET_SRIC).getWaypoint() != null) {
            LocationWp wp = Data.getTargets(SamType.TARGET_SRIC).getWaypoint();
            window.setLatitude(String.format("%f", wp.getLat()));
            window.setLongitude(String.format("%f", wp.getLng()));
        }
    }

    /**
     * Methode in der die SubscriptionHandler gesetzt werden.
     */
    private void addSubscriptionHandler() {
        GroundStationClient.getInstance().addSubscriptionHandler(ControlMessage.Service.SRIC,
                WifiConfigOutput.TOPIC, new SendWifiSubscriptionHandler(this));
        GroundStationClient.getInstance().addSubscriptionHandler(ControlMessage.Service.SRIC,
                GetFileListOutput.TOPIC, new FileListSubscriptionHandler(this));
        GroundStationClient.getInstance().addSubscriptionHandler(ControlMessage.Service.SRIC,
                LoginFileDownloadOutput.TOPIC, new ReadFileSubscriptionHandler(this));
        GroundStationClient.getInstance().addSubscriptionHandler(ControlMessage.Service.SRIC,
                ImageUploadOutput.TOPIC, new WriteFileSubscriptionHandler(this));
    }

    /**
     * Speichern der Position der SRIC Antenne.
     */
    private void setSRICPosition() {
        try {
            double lng = Double.parseDouble(window.getLongitude()),
                    lat = Double.parseDouble(window.getLatitude()),
                    height = Double.parseDouble(window.getHeight());
            LocationWp wp = new LocationWp(lng, lat, height, MavCmd.LOITER_UNLIM, 0, 0, 0, 0, 0);
            LocationWp[] wps = {wp};
            Data.setWaypoints(new Waypoints(wps, SamType.TASK_SRIC));
            LOG.info("SRIC Position erfolgreich gesetzt");
        } catch (NumberFormatException e) {
            LOG.error("Position von SRIC hat falsches Format, bitte Double als Eingabe benutzen");
        }
    }

    /**
     * Methode zum Starten des Timeouts.
     */
    private void startTimeoutCounter() {
        operationcomplete = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(TIMEOUT);
                    if (!operationcomplete) {
                        LOG.error(TIMEOUT_MESSAGE);
                        JOptionPane.showMessageDialog(null, TIMEOUT_MESSAGE, TIMEOUT_MESSAGE, JOptionPane.OK_OPTION);
                    }
                } catch (InterruptedException e) {
                    writeLog(e);
                }
            }
        }).start();
    }

    /**
     * Senden von SSID und Passwort.
     */
    private void sendWifiConfiguration() {
        WifiConfigInput input = new WifiConfigInput();
        input.setPassword(window.getWEPPassword());
        input.setSsid(window.getSSID());

        startTimeoutCounter();
        GroundStationClient.getInstance().sendAction(ControlMessage.Service.SRIC, input.TOPIC, input);
    }

    /**
     * Lesen der Liste an Files die auf dem FTP-Server liegen.
     */
    private void readRemoteFileList() {
        GetFileListInput input = new GetFileListInput();
        input.setFtpAddress(window.getReadFTPAddress());
        input.setFolder(window.getReadFolder());
        input.setUsername(window.getReadUsername());
        input.setPassword(window.getReadPassword());

        startTimeoutCounter();
        GroundStationClient.getInstance().sendAction(ControlMessage.Service.SRIC, input.TOPIC, input);
    }

    /**
     * Lesen des ausgewählten Files.
     */
    private void readFile() {
        LoginFileDownloadInput input = new LoginFileDownloadInput();
        input.setFilename(window.getReadFilename());

        startTimeoutCounter();
        GroundStationClient.getInstance().sendAction(ControlMessage.Service.SRIC, input.TOPIC, input);
    }

    /**
     * Schreiben eines beliebigen Files auf den FTP-Server.
     */
    private void writeFile() {
        ImageUploadInput input = new ImageUploadInput();
        input.setFtpAddress(window.getWriteFTPAddress());
        input.setFolder(window.getWriteFolder());
        input.setUsername(window.getWriteUsername());
        input.setPassword(window.getWritePassword());
        input.setFilename(window.getWritePictureFileName());
        input.setPicture(readFile(window.getWritePicturePath()));

        startTimeoutCounter();
        GroundStationClient.getInstance().sendAction(ControlMessage.Service.SRIC, input.TOPIC, input);
    }

    private void cancelAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestClient.stopSRICTask();
                LOG.info("Kreisen wurde beendet");
            }
        }).start();
    }

    /**
     * Hilfsmethode um ein File in ein ByteArray zu lesen.
     * @param path Filepath.
     * @return File in Bytes.
     */
    private byte[] readFile(String path) {
        try {
            return FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            writeLog(e);
            return new byte[0];
        }
    }

    /**
     * Diese Methode musste ich implementieren da Sonar sonst meckert.
     * @param files ...
     */
    private void setFileList(String[] files) {
        window.setFileList(files);
    }

    /**
     * Speichert ein ByteArray in ein File.
     * @param file Byte Array.
     */
    private void saveFile(byte[] file) {
        try {
            File outFile = new File(Constants.SRIC_LOGIN_FILE_PATH + window.getReadFilename());
            FileUtils.writeByteArrayToFile(outFile, file);
            if (Desktop.isDesktopSupported()) {
                // Try to Open
                Desktop.getDesktop().open(outFile);
            }
        } catch (IOException e) {
            writeLog(e);
        }
    }

    /**
     * Diese Methode musste ich implementieren da Sonar sonst meckert.
     * @param e ...
     */
    private void writeLog(Exception e) {
        LOG.error(e.getMessage());
    }

    @Override
    public void loadProperties() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(Constants.SRIC_LOGIN_FILE_PATH + "config.txt"));
            window.setSSID(br.readLine());
            window.setWEP(br.readLine());
            window.setReadAddress(br.readLine());
            window.setReadFolder(br.readLine());
            window.setReadUsername(br.readLine());
            window.setReadPassword(br.readLine());
            window.setWriteAddress(br.readLine());
            window.setWriteFolder(br.readLine());
            window.setWriteUsername(br.readLine());
            window.setWritePassword(br.readLine());
            window.setWritePicture(br.readLine());
            br.close();
        } catch (FileNotFoundException e) {
            writeLog(e);
        } catch (IOException e) {
            writeLog(e);
        }
    }

    @Override
    public void saveProperties() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.SRIC_LOGIN_FILE_PATH + "config.txt"));
            bw.write(window.getSSID() + "\n");
            bw.write(window.getWEPPassword() + "\n");
            bw.write(window.getReadFTPAddress() + "\n");
            bw.write(window.getReadFolder() + "\n");
            bw.write(window.getReadUsername() + "\n");
            bw.write(window.getReadPassword() + "\n");
            bw.write(window.getWriteFTPAddress() + "\n");
            bw.write(window.getWriteFolder() + "\n");
            bw.write(window.getWriteUsername() + "\n");
            bw.write(window.getWritePassword() + "\n");
            bw.write(window.getWritePicturePath() + "\n");
            bw.close();
        } catch (IOException e) {
            writeLog(e);
        }
    }

    // Subscription Handler

    /**
     * Subscription Handler für das Empfangen des Responds bei SendWifiConfiguration.
     */
    private class SendWifiSubscriptionHandler extends SubscriptionHandler<WifiConfigOutput> {

        /**
         * Aeusere Klasse.
         */
        private SRIC parent;

        /**
         * Konstruktor.
         * @param parent Aeusere Klasse.
         */
        public SendWifiSubscriptionHandler(SRIC parent) {
            this.parent = parent;
        }

        @Override
        public void handleSubscription(WifiConfigOutput subscription) {
            LOG.info(PLANE_START_MSG + subscription.getStatusMessage());
            operationcomplete = true;
            JOptionPane.showMessageDialog(null, subscription.getStatusMessage(), subscription.TOPIC, JOptionPane.OK_OPTION);
        }
    }

    /**
     * Subscription Handler für das Empfangen des Responds bei ReadFileList.
     */
    private class FileListSubscriptionHandler extends SubscriptionHandler<GetFileListOutput> {

        /**
         * Aeusere Klasse.
         */
        private SRIC parent;

        /**
         * Konstruktor.
         * @param parent Aeusere Klasse.
         */
        public FileListSubscriptionHandler(SRIC parent) {
            this.parent = parent;
        }

        @Override
        public void handleSubscription(GetFileListOutput subscription) {
            operationcomplete = true;
            if (!subscription.isSuccess()) {
                LOG.error(PLANE_START_MSG + subscription.getStatusMessage());
                JOptionPane.showMessageDialog(null, subscription.getStatusMessage(), subscription.TOPIC, JOptionPane.OK_OPTION);
            } else {
                LOG.info(PLANE_START_MSG + subscription.getStatusMessage());
                parent.setFileList(subscription.getFileList());
            }
        }
    }

    /**
     * Subscription Handler für das Empfangen des Responds bei ReadFile.
     */
    private class ReadFileSubscriptionHandler extends SubscriptionHandler<LoginFileDownloadOutput> {

        /**
         * Aeusere Klasse.
         */
        private SRIC parent;

        /**
         * Konstruktor.
         * @param parent Aeusere Klasse.
         */
        public ReadFileSubscriptionHandler(SRIC parent) {
            this.parent = parent;
        }

        @Override
        public void handleSubscription(LoginFileDownloadOutput subscription) {
            operationcomplete = true;
            if (!subscription.isSuccess()) {
                LOG.error(PLANE_START_MSG + subscription.getStatusMessage());
                JOptionPane.showMessageDialog(null, subscription.getStatusMessage(), subscription.TOPIC, JOptionPane.OK_OPTION);
            } else {
                LOG.info(PLANE_START_MSG + subscription.getStatusMessage());
                parent.saveFile(subscription.getLoginFile());
            }
        }
    }

    /**
     * Subscription Handler für das Empfangen des Responds bei WriteFile.
     */
    private class WriteFileSubscriptionHandler extends SubscriptionHandler<ImageUploadOutput> {

        /**
         * Aeusere Klasse.
         */
        private SRIC parent;

        /**
         * Konstruktor.
         * @param parent Aeusere Klasse.
         */
        public WriteFileSubscriptionHandler(SRIC parent) {
            this.parent = parent;
        }

        @Override
        public void handleSubscription(ImageUploadOutput subscription) {
            operationcomplete = true;
            if (!subscription.isSuccess()) {
                LOG.error(PLANE_START_MSG + subscription.getStatusMessage());
                JOptionPane.showMessageDialog(null, subscription.getStatusMessage(), subscription.TOPIC, JOptionPane.OK_OPTION);
            } else {
                LOG.info(PLANE_START_MSG + subscription.getStatusMessage());
                JOptionPane.showMessageDialog(null, subscription.getStatusMessage(), subscription.TOPIC, JOptionPane.OK_OPTION);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RestClient.stopSRICTask();
                        LOG.info("Kreisen wurde beendet");
                    }
                }).start();
            }
        }
    }
}