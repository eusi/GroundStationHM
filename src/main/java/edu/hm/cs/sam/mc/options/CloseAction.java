package edu.hm.cs.sam.mc.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CloseAction implements ActionListener {

    private final Options options;

    public CloseAction(final Options options) {
        this.options = options;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        options.dispose();
    }
}
