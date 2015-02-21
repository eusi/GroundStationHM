package edu.hm.cs.sam.mc.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

import edu.hm.cs.sam.mc.misc.Settings;

class SettingsSaveAction implements ActionListener {

    private final Options options;

    SettingsSaveAction(final Options options) {
        this.options = options;
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
        saveMp();
        saveImgFolder();
        saveRprtImgFldr();
        saveRprtSvFldr();
        saveRtgFldr();
        saveSvFldr();
        saveSrchAreaFldr();
    }

    private void saveImgFolder() {
        File save;
        if (!options.getTxtImgFldr().getText().equals(Settings.getImgFldr())) {
            save = new File(options.getTxtImgFldr().getText());
            if (!save.exists()) {
                if (JOptionPane.showConfirmDialog(options, "Should the folder "
                        + options.getTxtImgFldr().getText() + " be created",
                        "Folder does not exist", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    save.mkdirs();
                    Settings.setImgFldr(options.getTxtImgFldr().getText()
                            .endsWith(File.pathSeparator) ? options.getTxtImgFldr().getText()
                                    : options.getTxtImgFldr().getText() + "\\");
                    options.getTxtImgFldr().setText(Settings.getImgFldr());
                }
            } else if (save.isDirectory()) {
                Settings.setImgFldr(options.getTxtImgFldr().getText());
                options.getTxtImgFldr().setText(Settings.getImgFldr());
            }
        }
    }

    private void saveMp() {
        if (!options.getTxtMpip().getText().equals(Settings.getRestMp())) {
            Settings.setRestMP(options.getTxtMpip().getText());
            options.getLblRestTest().setText("Current REST Interface: " + Settings.getRestMpUrl());
        }
    }

    private void saveRprtImgFldr() {
        File save;
        if (!options.getTxtRprtImgFldr().getText().equals(Settings.getRprtImgFldr())) {
            save = new File(options.getTxtRprtImgFldr().getText());
            if (!save.exists()) {
                if (JOptionPane.showConfirmDialog(options, "Should the folder "
                        + options.getTxtRprtImgFldr().getText() + " be created",
                        "Folder does not exist", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    save.mkdirs();
                    Settings.setRprtImgFldr(options.getTxtRprtImgFldr().getText()
                            .endsWith(File.pathSeparator) ? options.getTxtRprtImgFldr().getText()
                            : options.getTxtRprtImgFldr().getText() + "\\");
                    options.getTxtRprtSvFldr().setText(Settings.getRprtImgFldr());
                }
            } else if (save.isDirectory()) {
                Settings.setRprtImgFldr(options.getTxtRprtImgFldr().getText());
                options.getTxtRprtSvFldr().setText(Settings.getRprtImgFldr());
            }
        }
    }

    private void saveRprtSvFldr() {
        File save;
        if (!options.getTxtRprtSvFldr().getText().equals(Settings.getRprtSvFldr())) {
            save = new File(options.getTxtRprtSvFldr().getText());
            if (!save.exists()) {
                if (JOptionPane.showConfirmDialog(options, "Should the folder "
                        + options.getTxtRprtSvFldr().getText() + " be created",
                        "Folder does not exist", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    save.mkdirs();
                    Settings.setRprtSvFldr(options.getTxtRprtSvFldr().getText()
                            .endsWith(File.pathSeparator) ? options.getTxtRprtSvFldr().getText()
                            : options.getTxtRprtSvFldr().getText() + "\\");
                    options.getTxtRprtSvFldr().setText(Settings.getRprtSvFldr());
                }
            } else if (save.isDirectory()) {
                Settings.setRprtSvFldr(options.getTxtRprtSvFldr().getText());
                options.getTxtRprtSvFldr().setText(Settings.getRprtSvFldr());
            }
        }
    }

    private void saveRtgFldr() {
        File save;
        if (!options.getTxtRtgFldr().getText().equals(Settings.getRtgFldr())) {
            save = new File(options.getTxtRtgFldr().getText());
            if (!save.exists()) {
                if (JOptionPane.showConfirmDialog(options, "Should the folder "
                        + options.getTxtRtgFldr().getText() + " be created",
                        "Folder does not exist", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    save.mkdirs();
                    Settings.setRtgFldr(options.getTxtRtgFldr().getText()
                            .endsWith(File.pathSeparator) ? options.getTxtRtgFldr().getText()
                            : options.getTxtRtgFldr().getText() + "\\");
                    options.getTxtRtgFldr().setText(Settings.getRtgFldr());
                }
            } else if (save.isDirectory()) {
                Settings.setRtgFldr(options.getTxtRtgFldr().getText());
                options.getTxtRtgFldr().setText(Settings.getRtgFldr());
            }
        }
    }

    private void saveSrchAreaFldr() {
        File save;
        if (!options.getTxtSrchAreaFldr().getText().equals(Settings.getSrchAreaFldr())) {
            save = new File(options.getTxtSrchAreaFldr().getText());
            if (!save.exists()) {
                if (JOptionPane.showConfirmDialog(options, "Should the folder "
                        + options.getTxtSrchAreaFldr().getText() + " be created",
                        "Folder does not exist", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    save.mkdirs();
                    Settings.setSrchAreaFldr(options.getTxtSrchAreaFldr().getText()
                            .endsWith(File.pathSeparator) ? options.getTxtSrchAreaFldr().getText()
                                    : options.getTxtSrchAreaFldr().getText() + "\\");
                    options.getTxtSrchAreaFldr().setText(Settings.getSrchAreaFldr());
                }
            } else if (save.isDirectory()) {
                Settings.setSrchAreaFldr(options.getTxtSrchAreaFldr().getText());
                options.getTxtSrchAreaFldr().setText(Settings.getSrchAreaFldr());
            }
        }
    }

    private void saveSvFldr() {
        File save;
        if (!options.getTxtSvFldr().getText().equals(Settings.getSvFldr())) {
            save = new File(options.getTxtSvFldr().getText());
            if (!save.exists()) {
                if (JOptionPane.showConfirmDialog(options, "Should the folder "
                        + options.getTxtSvFldr().getText() + " be created",
                        "Folder does not exist", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    save.mkdirs();
                    Settings.setSvFldr(options.getTxtSvFldr().getText()
                            .endsWith(File.pathSeparator) ? options.getTxtSvFldr().getText()
                                    : options.getTxtSvFldr().getText() + "\\");
                    options.getTxtSvFldr().setText(Settings.getSvFldr());
                }
            } else if (save.isDirectory()) {
                Settings.setSvFldr(options.getTxtSvFldr().getText());
                options.getTxtSvFldr().setText(Settings.getSvFldr());
            }
        }
    }
}
