package edu.hm.cs.sam.mc.images;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.misc.Misc;
import edu.hm.cs.sam.mc.misc.Settings;

class CopyAction implements ActionListener {

    final ImageViewer imageViewer;
    private static final Logger LOG = Logger.getLogger(CopyAction.class.getName());

    CopyAction(final ImageViewer imageViewer) {
        this.imageViewer = imageViewer;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (!"...".equals(imageViewer.getTxtPath().getText())) {
            final String filename = imageViewer.getTxtPath().getText()
                    .replace(Settings.getImgFldr(), "");
            final File inF = new File(Settings.getImgFldr() + filename);
            final File outF = new File(Settings.getRprtImgFldr() + filename);
            Misc.copyFile(LOG, imageViewer, inF, outF, true);
        }
    }
}
