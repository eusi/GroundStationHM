package edu.hm.cs.sam.mc.images;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

@SuppressWarnings("serial")
class ImageToolbarAction extends AbstractAction {

    private final Icon displayPhoto;
    private final String filePath;
    private final ImageViewer imageViewer;

    ImageToolbarAction(final ImageViewer imageViewer, final Icon photo, final Icon thumb,
                       final String desc, final String filePath) {

        displayPhoto = photo;
        this.filePath = filePath;
        this.imageViewer = imageViewer;

        putValue(SHORT_DESCRIPTION, desc);

        putValue(LARGE_ICON_KEY, thumb);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        imageViewer.getLblPicture().setIcon(displayPhoto);
        imageViewer.getLblPicture().setToolTipText(getValue(SHORT_DESCRIPTION).toString());
        imageViewer.setTitle("Image Viewer: " + getValue(SHORT_DESCRIPTION).toString());
        imageViewer.getTxtPath().setText(filePath);
    }
}