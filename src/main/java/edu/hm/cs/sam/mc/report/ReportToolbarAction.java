package edu.hm.cs.sam.mc.report;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;

@SuppressWarnings("serial")
class ReportToolbarAction extends AbstractAction {

    private final Icon displayPhoto;
    private final String filePath;
    private final String fileName;
    private final ReportSheet reportSheet;

    ReportToolbarAction(final ReportSheet reportSheet, final Icon photo, final Icon thumb,
            final String desc, final String filePath) {

        displayPhoto = photo;
        this.filePath = filePath;
        this.reportSheet = reportSheet;
        fileName = desc.split("\\.(?=[^\\.]+$)")[0];

        putValue(SHORT_DESCRIPTION, desc);
        putValue(LARGE_ICON_KEY, thumb);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final List<String> targetInfos = new ArrayList<>();
        if (fileName.contains("-")) {
            final String[] tInfos = fileName.split("-");
            for (final String info : tInfos) {
                targetInfos.add(info.trim());
            }
        }
        setTxtFields(targetInfos);
        enableTextfields(targetInfos);
        reportSheet.setCurrentLoadedPicture(filePath);
        reportSheet.getLblPicture().setIcon(displayPhoto);
        reportSheet.getLblPicture().setToolTipText(getValue(SHORT_DESCRIPTION).toString());
        reportSheet.setTitle("Report Sheet: " + getValue(SHORT_DESCRIPTION).toString());
    }

    private void enableTextfields(final List<String> targetInfos) {
        final boolean enable;
        if (targetInfos.size() > 11) {
            enable = "0".equals(targetInfos.get(11));
            reportSheet.setLoadedPictureIsAutomatic(!enable);
        } else {
            enable = true;
        }
        reportSheet.getTxtNumber().setEnabled(true);
        reportSheet.getTxtType().setEnabled(enable);
        reportSheet.getTxtLatitude().setEnabled(enable);
        reportSheet.getTxtLongitude().setEnabled(enable);
        reportSheet.getTxtOrientatation().setEnabled(enable);
        reportSheet.getTxtShape().setEnabled(enable);
        reportSheet.getTxtColor().setEnabled(enable);
        reportSheet.getTxtAlphanumeric().setEnabled(enable);
        reportSheet.getTxtAlphanumericColor().setEnabled(enable);
        reportSheet.getTxtFileName().setEnabled(enable);
        reportSheet.getTxtAmplifyingText().setEnabled(enable);
    }

    private String getAlphanumeric(final List<String> targetInfos) {
        return targetInfos.size() > 7 ? targetInfos.get(7) : "";
    }

    private String getAlphanumericColor(final List<String> targetInfos) {
        return targetInfos.size() > 8 ? targetInfos.get(8) : "";
    }

    private String getAmplifyingText(final List<String> targetInfos) {
        return targetInfos.size() > 10 ? targetInfos.get(10) : "";
    }

    private String getColor(final List<String> targetInfos) {
        return targetInfos.size() > 6 ? targetInfos.get(6) : "";
    }

    private String getFileName(final List<String> targetInfos) {
        return targetInfos.size() > 9 ? targetInfos.get(9) : getValue(SHORT_DESCRIPTION).toString();
    }

    private String getLatitude(final List<String> targetInfos) {
        return targetInfos.size() > 2 ? targetInfos.get(2) : "";
    }

    private String getLongitude(final List<String> targetInfos) {
        return targetInfos.size() > 3 ? targetInfos.get(3) : "";
    }

    private String getNumber(final List<String> targetInfos) {
        return !targetInfos.isEmpty() ? targetInfos.get(0) : "";
    }

    private String getOrientation(final List<String> targetInfos) {
        return targetInfos.size() > 4 ? targetInfos.get(4) : "";
    }

    private String getShape(final List<String> targetInfos) {
        return targetInfos.size() > 5 ? targetInfos.get(5) : "";
    }

    private String getType(final List<String> targetInfos) {
        return targetInfos.size() > 1 ? targetInfos.get(1) : "";
    }

    private void setTxtFields(final List<String> targetInfos) {
        reportSheet.getTxtNumber().setText(getNumber(targetInfos));
        reportSheet.getTxtType().setText(getType(targetInfos));
        reportSheet.getTxtLatitude().setText(getLatitude(targetInfos));
        reportSheet.getTxtLongitude().setText(getLongitude(targetInfos));
        reportSheet.getTxtOrientatation().setText(getOrientation(targetInfos));
        reportSheet.getTxtShape().setText(getShape(targetInfos));
        reportSheet.getTxtColor().setText(getColor(targetInfos));
        reportSheet.getTxtAlphanumeric().setText(getAlphanumeric(targetInfos));
        reportSheet.getTxtAlphanumericColor().setText(getAlphanumericColor(targetInfos));
        reportSheet.getTxtFileName().setText(getFileName(targetInfos));
        reportSheet.getTxtAmplifyingText().setText(getAmplifyingText(targetInfos));
    }
}