package edu.hm.cs.sam.mc.searcharea.viewer.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * @author Philipp Trepte
 * Represents a image within a container.
 */
public class ImagePanel extends JPanel {

	/** */
	private static final Logger LOG = Logger.getLogger(ImagePanel.class.getName());

	/** */
	private static final long serialVersionUID = 553101090125559812L;

	/** picture */
	private BufferedImage image;

	/**
	 * custom constructor.
	 * @param image picture
	 */
	public ImagePanel(final BufferedImage image) {
		setImage(image);
	}

	/**
	 * custom constructor.
	 * @param path picture path
	 */
	public ImagePanel(final String path) {
		try {
			final BufferedImage image = ImageIO.read(new File(path));
			setImage(image);
		} catch (final IOException e) {
			LOG.error("Could not read", e);
		}
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	/**
	 * Returns the image.
	 * @return image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Sets the image and the size of this container.
	 * @param image picture
	 */
	public void setImage(final BufferedImage image) {
		this.image = image;
		if (image != null) {
			final Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}
		repaint();
	}
}
