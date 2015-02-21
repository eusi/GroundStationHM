package edu.hm.cs.sam.mc.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import edu.hm.cs.sam.mc.misc.Settings;

class SaveAction implements ActionListener {

    final ReportSheet reportSheet;

    SaveAction(final ReportSheet reportSheet) {
        this.reportSheet = reportSheet;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (!"".equals(reportSheet.getCurrentLoadedPicture())) {
            final String oldFileName = reportSheet.getCurrentLoadedPicture();
            final String newFileName = createNewFileName();
            final File oldFile = new File(oldFileName);
            final File newFile = new File(Settings.getRprtImgFldr() + newFileName);
            oldFile.renameTo(newFile);
            reportSheet.resetLoadedPicture();
            reportSheet.refreshImages();
        }
    }

    private String createNewFileName() {
        final String number = reportSheet.getTxtNumber().getText().trim();
        final String type = reportSheet.getTxtType().getText().trim();
        final String latitude = reportSheet.getTxtLatitude().getText().trim();
        final String longitude = reportSheet.getTxtLongitude().getText().trim();
        final String orientation = reportSheet.getTxtOrientatation().getText().trim();
        final String shape = reportSheet.getTxtShape().getText().trim();
        final String color = reportSheet.getTxtColor().getText().trim();
        final String alphanumeric = reportSheet.getTxtAlphanumeric().getText().trim();
        final String alphanumericColor = reportSheet.getTxtAlphanumericColor().getText().trim();
        final String fileName = reportSheet.getTxtFileName().getText().trim();
        final String amplifyingText = reportSheet.getTxtAmplifyingText().getText().trim();
        final String isAutomatic = reportSheet.getLoadedPictureIsAutomatic() ? "1" : "0";
        final String extension = fileName.split("\\.(?=[^\\.]+$)")[1].trim();

        return number + "-" + type + "-" + latitude + "-" + longitude + "-" + orientation + "-"
                + shape + "-" + color + "-" + alphanumeric + "-" + alphanumericColor + "-"
                + fileName + "-" + amplifyingText + "-" + isAutomatic + "." + extension;
    }
}
