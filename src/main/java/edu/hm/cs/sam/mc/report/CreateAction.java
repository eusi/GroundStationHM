package edu.hm.cs.sam.mc.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.misc.Misc;
import edu.hm.cs.sam.mc.misc.Settings;

class CreateAction implements ActionListener {

    private static final Logger LOG = Logger.getLogger(CreateAction.class.getName());

    private final ReportSheet reportSheet;
    private final ColumnStorage numberColumn;
    private final ColumnStorage typeColumn;
    private final ColumnStorage latitudeColumn;
    private final ColumnStorage longitudeColumn;
    private final ColumnStorage orientationColumn;
    private final ColumnStorage shapeColumn;
    private final ColumnStorage colorColumn;
    private final ColumnStorage alphanumericColumn;
    private final ColumnStorage alphanumericColorColumn;
    private final ColumnStorage fileNameColumn;
    private final ColumnStorage amplifyingTextColumn;

    CreateAction(final ReportSheet reportSheet) {
        this.reportSheet = reportSheet;
        numberColumn = new ColumnStorage();
        numberColumn.add("number");
        typeColumn = new ColumnStorage();
        typeColumn.add("type");
        latitudeColumn = new ColumnStorage();
        latitudeColumn.add("latitude");
        longitudeColumn = new ColumnStorage();
        longitudeColumn.add("longitude");
        orientationColumn = new ColumnStorage();
        orientationColumn.add("orientation");
        shapeColumn = new ColumnStorage();
        shapeColumn.add("shape");
        colorColumn = new ColumnStorage();
        colorColumn.add("color");
        alphanumericColumn = new ColumnStorage();
        alphanumericColumn.add("alphanumeric");
        alphanumericColorColumn = new ColumnStorage();
        alphanumericColorColumn.add("alphanumeric color");
        fileNameColumn = new ColumnStorage();
        fileNameColumn.add("file name");
        amplifyingTextColumn = new ColumnStorage();
        amplifyingTextColumn.add("amplifying text");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if ("".equals(reportSheet.getCurrentLoadedPicture())) {
            final List<String> imageFiles = Misc.getImagesInDir(Settings.getRprtImgFldr(), LOG);
            final File reportFile = new File(Settings.getRprtSvFldr() + "SAM.txt");
            for (int i = 0; i < imageFiles.size(); i++) {
                final String fileName = imageFiles.get(i);
                addTargetInfos(fileName);
                final File sourceImage = new File(Settings.getRprtImgFldr() + fileName);
                final File destinationImage = new File(Settings.getRprtSvFldr()
                        + newFileName(fileName));
                Misc.copyFile(LOG, reportSheet, sourceImage, destinationImage, false);
            }
            PrintWriter report;
            BufferedWriter buffer;
            FileWriter fileWriter;
            try {
                reportFile.createNewFile();
                fileWriter = new FileWriter(reportFile);
                buffer = new BufferedWriter(fileWriter);
                report = new PrintWriter(buffer);
                try {
                    printLine(report, 0);
                    printStripline(report);
                    for (int index = 1; index <= imageFiles.size(); index++) {
                        printLine(report, index);
                    }
                } finally {
                    report.close();
                    buffer.close();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(reportSheet, "\"" + reportFile.getAbsolutePath()
                            + "\" was created", "Report sheet created",
                            JOptionPane.INFORMATION_MESSAGE);
                    reportSheet.getBtnOpen().setEnabled(true);
                }
            } catch (final IOException error) {
                LOG.error("report couldn't be created", error);
            }
        } else {
            JOptionPane.showMessageDialog(reportSheet,
                    "Please save your picture before creating the report sheet!", "Picture loaded",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addAlphanumeric(final String[] targetInfos) {
        alphanumericColumn.add(targetInfos.length > 7 ? targetInfos[7] : "");
    }

    private void addAlphanumericColor(final String[] targetInfos) {
        alphanumericColorColumn.add(targetInfos.length > 8 ? targetInfos[8] : "");
    }

    private void addAmplifyingText(final String[] targetInfos) {
        amplifyingTextColumn.add(targetInfos.length > 10 ? targetInfos[10] : "");
    }

    private void addColor(final String[] targetInfos) {
        colorColumn.add(targetInfos.length > 6 ? targetInfos[6] : "");
    }

    private void addFileName(final String[] targetInfos, final String fileName) {
        fileNameColumn.add(targetInfos.length > 9 ? targetInfos[9] : fileName);
    }

    private void addLatitude(final String[] targetInfos) {
        latitudeColumn.add(targetInfos.length > 2 ? targetInfos[2] : "");
    }

    private void addLongitude(final String[] targetInfos) {
        longitudeColumn.add(targetInfos.length > 3 ? targetInfos[3] : "");
    }

    private void addNumber(final String[] targetInfos) {
        numberColumn.add(targetInfos.length > 0 ? targetInfos[0] : "");
    }

    private void addOrientation(final String[] targetInfos) {
        orientationColumn.add(targetInfos.length > 4 ? targetInfos[4] : "");
    }

    private void addShape(final String[] targetInfos) {
        shapeColumn.add(targetInfos.length > 5 ? targetInfos[5] : "");
    }

    private void addTargetInfos(final String fileName) {
        final String[] targetInfos = getTargetInfos(fileName);
        addAlphanumeric(targetInfos);
        addAlphanumericColor(targetInfos);
        addAmplifyingText(targetInfos);
        addColor(targetInfos);
        addFileName(targetInfos, fileName);
        addLatitude(targetInfos);
        addLongitude(targetInfos);
        addNumber(targetInfos);
        addOrientation(targetInfos);
        addShape(targetInfos);
        addType(targetInfos);
    }

    private void addType(final String[] targetInfos) {
        typeColumn.add(targetInfos.length > 1 ? targetInfos[1] : "");
    }

    private String formatColumn(final int index, final ColumnStorage storage, final boolean isLast) {
        String cellEnd;
        if (!isLast) {
            cellEnd = " |";
        } else {
            cellEnd = "%n";
        }
        return String.format("%" + (storage.getLongestElementLength() + 1) + "s" + cellEnd,
                storage.get(index));
    }

    private String[] getTargetInfos(final String fileName) {
        String[] targetInfos = null;
        if (fileName.contains("-")) {
            targetInfos = fileName.split("-");
        }
        return targetInfos;
    }

    private String newFileName(final String fileName) {
        final String[] targetInfos = getTargetInfos(fileName);
        return ((targetInfos != null) && (targetInfos.length > 9)) ? targetInfos[9] : fileName;
    }

    private void printLine(final PrintWriter report, final int index) {
        report.print(formatColumn(index, numberColumn, false));
        report.print(formatColumn(index, typeColumn, false));
        report.print(formatColumn(index, latitudeColumn, false));
        report.print(formatColumn(index, longitudeColumn, false));
        report.print(formatColumn(index, orientationColumn, false));
        report.print(formatColumn(index, shapeColumn, false));
        report.print(formatColumn(index, colorColumn, false));
        report.print(formatColumn(index, alphanumericColumn, false));
        report.print(formatColumn(index, alphanumericColorColumn, false));
        report.print(formatColumn(index, fileNameColumn, false));
        report.print(formatColumn(index, amplifyingTextColumn, true));
    }

    private void printStripline(final PrintWriter report) {
        report.print(stripColumn(numberColumn, false));
        report.print(stripColumn(typeColumn, false));
        report.print(stripColumn(latitudeColumn, false));
        report.print(stripColumn(longitudeColumn, false));
        report.print(stripColumn(orientationColumn, false));
        report.print(stripColumn(shapeColumn, false));
        report.print(stripColumn(colorColumn, false));
        report.print(stripColumn(alphanumericColumn, false));
        report.print(stripColumn(alphanumericColorColumn, false));
        report.print(stripColumn(fileNameColumn, false));
        report.print(stripColumn(amplifyingTextColumn, true));
    }

    private String stripColumn(final ColumnStorage storage, final boolean isLast) {
        String line = "";
        for (int i = 0; i <= storage.getLongestElementLength(); i++) {
            line += "-";
        }
        if (!isLast) {
            line += "-+";
        } else {
            line = String.format("%s%n", line);
        }
        return line;
    }
}
