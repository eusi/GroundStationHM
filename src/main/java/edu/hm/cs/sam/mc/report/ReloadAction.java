package edu.hm.cs.sam.mc.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

class ReloadAction implements ActionListener {

    private final ReportSheet reportSheet;

    public ReloadAction(final ReportSheet reportSheet) {
        this.reportSheet = reportSheet;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if ("".equals(reportSheet.getCurrentLoadedPicture())) {
            reportSheet.refreshImages();
        } else {
            JOptionPane.showMessageDialog(reportSheet,
                    "Please save your picture before reloading the images!", "Picture loaded",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
