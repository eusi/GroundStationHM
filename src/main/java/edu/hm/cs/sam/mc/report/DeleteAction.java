package edu.hm.cs.sam.mc.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

class DeleteAction implements ActionListener {

    private final ReportSheet reportSheet;

    DeleteAction(final ReportSheet reportSheet) {
        this.reportSheet = reportSheet;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (!"".equals(reportSheet.getCurrentLoadedPicture())
                && (JOptionPane
                        .showConfirmDialog(reportSheet,
                                "Are you sure to delete \"" + reportSheet.getCurrentLoadedPicture()
                                        + "\" from report?", "Really Deleting?",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {
            final File toDelete = new File(reportSheet.getCurrentLoadedPicture());
            if (toDelete.exists()) {
                toDelete.delete();
            }
            reportSheet.resetLoadedPicture();
            reportSheet.refreshImages();
        }

    }
}
