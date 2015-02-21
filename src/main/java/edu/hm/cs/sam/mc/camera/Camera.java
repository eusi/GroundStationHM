package edu.hm.cs.sam.mc.camera;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.control.GroundStationClient;
import edu.hm.cs.sam.mc.misc.Window;
import edu.hm.sam.cameraservice.CameraMessage;
import edu.hm.sam.cameraservice.CameraSettingsMessage;
import edu.hm.sam.control.ControlMessage.Service;
import edu.hm.sam.control.SubscriptionHandler;

/**
 * @author Deutsch, Alexander (adeutsch@hm.edu)
 * @version 03.01.2015 - 16:40:10
 */
@SuppressWarnings("serial")
public class Camera extends Window {

    private static final Logger LOG = Logger.getLogger(Camera.class.getName());

    private JToggleButton tglbtnAutophoto;
    private JToggleButton tglbtnAutofocus;
    /**
     * Create the application.
     */

    private JTextField textFieldZoom;

    private JTextField textFieldIntervall;
    private JTextField textFieldISO;
    private JTextField textFieldResWidth;
    private JTextField textFieldResHeight;
    private JTextField textFieldShutterSpeed;
    private JComboBox<String> focusModeSelectionList;
    private final String[] focusModes = { "FOCUS_MODE_AUTO", "FOCUS_MODE_INFINITY",
            "FOCUS_MODE_MACRO", "FOCUS_MODE_FIXED", "FOCUS_MODE_EDOF", "FOCUS_MODE_CONTINOUS_VIDEO" };

    public Camera(final String title, final Icon icon) {
        super(title, icon);
        try {
            initialize();
        } catch (final Exception e) {
            LOG.error("Error", e);
        }
    }

    /**
     * @param data
     * @param format
     * @return
     */
    private BufferedImage bitmapToImage(final byte[] data, final String format) {
        BufferedImage bufferedImage = null;
        ImageReader rdr = null;
        try (final ByteArrayInputStream inb = new ByteArrayInputStream(data)) {
            rdr = ImageIO.getImageReadersByFormatName(format).next();
            final ImageInputStream iis = ImageIO.createImageInputStream(inb);
            rdr.setInput(iis);
            bufferedImage = rdr.read(0);
        } catch (final IOException iox) {
            LOG.error("Error", iox);
        }
        return bufferedImage;
    }

    /**
     * Initialize the contents of the frame.
     */

    private void initialize() {

        final GridLayout gridLayout = new GridLayout(9, 4);
        getMainPanel().setLayout(gridLayout);

        final GroundStationClient client = GroundStationClient.getInstance();

        client.addSubscriptionHandler(Service.CAMERA, "PICTURE",
                new SubscriptionHandler<CameraMessage>() {

                    @Override
            public void handleSubscription(final CameraMessage subscription) {
                final byte[] imageData = subscription.getImageData();
                bitmapToImage(imageData, "jpeg");

            }
        });

        final JLabel lblModes = new JLabel("Modes:");
        lblModes.setHorizontalAlignment(SwingConstants.LEFT);
        getMainPanel().add(lblModes);

        getMainPanel().add(new JLabel(""));

        tglbtnAutophoto = new JToggleButton("AutoPhoto");
        tglbtnAutophoto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Integer.parseInt(textFieldIntervall.getText());
                    final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                    cameraSettingsMessage
                    .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingCaptureMode);
                    cameraSettingsMessage.setBoolValue(tglbtnAutophoto.isSelected());
                    send(cameraSettingsMessage);
                } catch (final Exception ex) {// meldung ausgeben
                    JOptionPane.showMessageDialog(null, "Ungueltige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungueltige Eingabe", ex);
                }

            }
        });
        getMainPanel().add(tglbtnAutophoto);

        tglbtnAutofocus = new JToggleButton("AutoFocus");
        tglbtnAutofocus.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                    cameraSettingsMessage
                    .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingAutofocusMode);
                    cameraSettingsMessage.setBoolValue(tglbtnAutofocus.isSelected());
                    send(cameraSettingsMessage);
                } catch (final Exception ex) {
                    // show error message
                    JOptionPane.showMessageDialog(null, "Ungueltige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungueltige Eingabe", ex);
                }

            }
        });
        getMainPanel().add(tglbtnAutofocus);

        // Zoom

        final JLabel lblNewLabel = new JLabel("Zoom:");
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);

        getMainPanel().add(lblNewLabel);
        getMainPanel().add(new JLabel(""));

        textFieldZoom = new JTextField();
        textFieldZoom.setHorizontalAlignment(SwingConstants.LEFT);
        textFieldZoom.setText("1");

        getMainPanel().add(textFieldZoom);
        textFieldZoom.setColumns(10);

        final JButton btnSetzoom = new JButton("SetZoom");

        btnSetzoom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final int i = Integer.parseInt(textFieldZoom.getText());
                    if (!((i >= 30) || (i < 0))) {
                        final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                        cameraSettingsMessage
                        .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingZoom);
                        cameraSettingsMessage.setIntValue1(i);
                        send(cameraSettingsMessage);
                    } else {
                        JOptionPane.showMessageDialog(null, "Werte zwischen 0 und 30 verwenden",
                                "Fehler", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (final Exception ex) {// meldung ausgeben
                    JOptionPane.showMessageDialog(null, "Ungueltige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungueltige Eingabe", ex);
                }
            }
        });
        getMainPanel().add(btnSetzoom);

        // Intervall

        final JLabel lblIntervall = new JLabel("Intervall:");
        lblIntervall.setHorizontalAlignment(SwingConstants.LEFT);

        getMainPanel().add(lblIntervall);

        getMainPanel().add(new JLabel(""));

        textFieldIntervall = new JTextField();
        textFieldIntervall.setHorizontalAlignment(SwingConstants.LEFT);
        textFieldIntervall.setText("1000");

        getMainPanel().add(textFieldIntervall);
        textFieldIntervall.setColumns(10);

        final JButton btnSetintervall = new JButton("SetIntervall");

        btnSetintervall.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final int i = Integer.parseInt(textFieldIntervall.getText());
                    final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                    cameraSettingsMessage
                    .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingFrequency);
                    cameraSettingsMessage.setIntValue1(i);
                    send(cameraSettingsMessage);
                } catch (final Exception ex) {// meldung ausgeben
                    JOptionPane.showMessageDialog(null, "Ungueltige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungueltige Eingabe", ex);
                }

            }
        });
        getMainPanel().add(btnSetintervall);

        // ISO

        final JLabel lblISO = new JLabel("ISO:");
        lblISO.setHorizontalAlignment(SwingConstants.LEFT);

        getMainPanel().add(lblISO);
        getMainPanel().add(new JLabel(""));

        textFieldISO = new JTextField();
        textFieldISO.setHorizontalAlignment(SwingConstants.LEFT);
        textFieldISO.setText("1600");

        getMainPanel().add(textFieldISO);
        textFieldISO.setColumns(10);

        final JButton btnSetISO = new JButton("SetISO");
        btnSetISO.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final String isoValue = textFieldISO.getText();
                    final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                    cameraSettingsMessage
                    .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingISO);
                    cameraSettingsMessage.setStringValue1(isoValue);
                    send(cameraSettingsMessage);
                } catch (final Exception ex) {//
                    JOptionPane.showMessageDialog(null, "Ungueltige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungueltige Eingabe", ex);
                }

            }
        });

        getMainPanel().add(btnSetISO);

        final JLabel lblRes = new JLabel("Resolution:");
        lblRes.setHorizontalAlignment(SwingConstants.LEFT);
        getMainPanel().add(lblRes);

        textFieldResWidth = new JTextField();
        textFieldResWidth.setHorizontalAlignment(SwingConstants.LEFT);
        textFieldResWidth.setText("5184");
        getMainPanel().add(textFieldResWidth);
        textFieldResWidth.setColumns(10);

        textFieldResHeight = new JTextField();
        textFieldResHeight.setHorizontalAlignment(SwingConstants.LEFT);
        textFieldResHeight.setText("3888");
        getMainPanel().add(textFieldResHeight);
        textFieldResHeight.setColumns(10);

        final JButton btnSetRes = new JButton("SetResolution");
        btnSetRes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final int width = Integer.parseInt(textFieldResWidth.getText());
                    final int height = Integer.parseInt(textFieldResHeight.getText());

                    final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                    cameraSettingsMessage
                    .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingResolution);
                    cameraSettingsMessage.setIntValue1(width);
                    cameraSettingsMessage.setIntValue2(height);
                    send(cameraSettingsMessage);

                } catch (final Exception ex) {
                    // show error message
                    JOptionPane.showMessageDialog(null, "Ungültige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungueltige Eingabe", ex);
                }

            }
        });

        getMainPanel().add(btnSetRes);

        // Shutter Speed

        final JLabel lblShutterSpeed = new JLabel("Shutter-Speed:");
        lblShutterSpeed.setHorizontalAlignment(SwingConstants.LEFT);
        getMainPanel().add(lblShutterSpeed);

        getMainPanel().add(new JLabel(""));

        textFieldShutterSpeed = new JTextField();
        textFieldShutterSpeed.setHorizontalAlignment(SwingConstants.LEFT);
        textFieldShutterSpeed.setText("30");
        getMainPanel().add(textFieldShutterSpeed);
        textFieldShutterSpeed.setColumns(10);

        final JButton btnSetShutterSpeed = new JButton("SetShutterSpeed");
        btnSetShutterSpeed.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final int speed = Integer.parseInt(textFieldShutterSpeed.getText());
                    final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                    cameraSettingsMessage
                    .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingShutterSpeed);
                    cameraSettingsMessage.setIntValue1(speed);

                } catch (final Exception ex) {
                    // show error message
                    JOptionPane.showMessageDialog(null, "Ungültige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungueltige Eingabe", ex);
                }

            }
        });

        getMainPanel().add(btnSetShutterSpeed);

        // Shutter Mode

        final JLabel lblShutterMode = new JLabel("Shutter-Mode:");
        lblShutterMode.setHorizontalAlignment(SwingConstants.LEFT);
        getMainPanel().add(lblShutterMode);
        getMainPanel().add(new JLabel(""));

        focusModeSelectionList = new JComboBox<String>(focusModes);
        getMainPanel().add(focusModeSelectionList);
        focusModeSelectionList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                // TODO Auto-generated method stub
                final String focusMode = (String) focusModeSelectionList.getSelectedItem();
                final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                cameraSettingsMessage
                .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingAutofocusMode);
                cameraSettingsMessage.setStringValue1(focusMode);
            }
        });

        getMainPanel().add(new JLabel(""));

        // Compression rate

        final JLabel lblcomprRate = new JLabel("JPEG Quality:");
        lblcomprRate.setHorizontalAlignment(SwingConstants.LEFT);
        getMainPanel().add(lblcomprRate);

        getMainPanel().add(new JLabel(""));

        final JTextField textcomprRate = new JTextField();
        textcomprRate.setText("100");
        getMainPanel().add(textcomprRate);
        textcomprRate.setColumns(10);

        final JButton btnSetcompRate = new JButton("Set JPEG Quality");
        btnSetcompRate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final int speed = Integer.parseInt(textFieldShutterSpeed.getText());
                    final CameraSettingsMessage cameraSettingsMessage = new CameraSettingsMessage();
                    cameraSettingsMessage
                    .setCameraSetting(CameraSettingsMessage.CameraSetting.CameraSettingJPEGQuality);
                    cameraSettingsMessage.setIntValue1(speed);

                } catch (final Exception ex) {
                    // show error message
                    JOptionPane.showMessageDialog(null, "Ungültige Eingabe", "Fehler",
                            JOptionPane.WARNING_MESSAGE);
                    LOG.error("Ungueltige Eingabe", ex);
                }

            }
        });

        getMainPanel().add(btnSetcompRate);

    }

    @Override
    public void loadProperties() {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveProperties() {
        // TODO Auto-generated method stub

    }

    private void send(final CameraSettingsMessage message) {
        final GroundStationClient gst = GroundStationClient.getInstance();
        gst.sendAction(Service.CAMERA, "SPICTURE", message);
    }
}