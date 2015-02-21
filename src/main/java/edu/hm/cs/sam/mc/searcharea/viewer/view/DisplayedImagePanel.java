package edu.hm.cs.sam.mc.searcharea.viewer.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Philipp Trepte
 * Represents the current selected picture.
 */
public class DisplayedImagePanel extends ImagePanel {

	/** */
	private static final long serialVersionUID = -2703278224888221896L;
	/** start and end point of the rectangle */
	private Point start, end;
	/** rectangle */
	private Shape rec;
	/** sub image listener */
	private SubImageListener listener;
	/** */
	private boolean isClear = true;

	/**
	 * custom constructor.
	 * @param image the current image
	 */
	public DisplayedImagePanel(final BufferedImage image) {
		super(image);

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (getImage() != null) {
					rec = makeRectangle(start.x, start.y, e.getX(), e.getY());
					final Rectangle2D rectangle = (Rectangle2D)rec;
					listener.updateListener((int)rectangle.getX(), (int)rectangle.getY(),
							(int)rectangle.getWidth(), (int)rectangle.getHeight());
					start = null;
					end = null;

					repaint();
				}
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				if (getImage() != null) {
					isClear = false;
					start = new Point(e.getX(), e.getY());
					end = start;
					repaint();
				}
			}

			@Override
			public void mouseExited(final MouseEvent e) {
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
			}

			@Override
			public void mouseClicked(final MouseEvent e) {
			}
		});

		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(final MouseEvent e) {
			}

			@Override
			public void mouseDragged(final MouseEvent e) {
				if (getImage() != null) {
					end = new Point(e.getX(), e.getY());
					repaint();
				}
			}
		});
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		final Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final Stroke oldStroke = g2.getStroke();

		if (!isClear) {
			g2.setStroke(new BasicStroke(4));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

			if (rec != null) {
				g2.setPaint(Color.WHITE);
				g2.draw(rec);
				g2.setStroke(oldStroke);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.01f));
				g2.fill(rec);

				g2.setStroke(new BasicStroke(4));
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			}

			if (start != null && end != null) {
				final Shape r = makeRectangle(start.x, start.y, end.x, end.y);
				g2.draw(r);
			}
		} else {
			super.paintComponent(g2);
		}
	}

	/**
	 * Sets the flag to clear the drawing area.
	 * @param clear flag to clear the drawing area
	 */
	public void setClear(final boolean clear) {
		this.isClear = clear;
		repaint();
	}

	/**
	 * Sets the sub image listener.
	 * @param listener sub image listener
	 */
	public void setSubImageListener(final SubImageListener listener) {
		this.listener = listener;
	}

	/**
	 * Creates a 2d rectangle.
	 * @param x1 top left x-position
	 * @param y1 top left y-position
	 * @param x2 bottom right x-position
	 * @param y2 bottom right y-position
	 * @return 2d rectangle
	 */
	private Rectangle2D.Float makeRectangle(final int x1, final int y1, final int x2, final int y2) {
		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}
}