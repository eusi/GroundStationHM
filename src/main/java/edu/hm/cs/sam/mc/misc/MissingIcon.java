package edu.hm.cs.sam.mc.misc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

/**
 * Icon class that creates an default icon when missing.
 */
class MissingIcon implements Icon {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    private final BasicStroke stroke = new BasicStroke(4);

    @Override
    public int getIconHeight() {
        return HEIGHT;
    }

    @Override
    public int getIconWidth() {
        return WIDTH;
    }

    @Override
    public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
        final Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + 1, y + 1, WIDTH - 2, HEIGHT - 2);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x + 1, y + 1, WIDTH - 2, HEIGHT - 2);

        g2d.setColor(Color.RED);

        g2d.setStroke(stroke);
        g2d.drawLine(x + 10, y + 10, (x + WIDTH) - 10, (y + HEIGHT) - 10);
        g2d.drawLine(x + 10, (y + HEIGHT) - 10, (x + WIDTH) - 10, y + 10);

        g2d.dispose();
    }
}
