package edu.hm.cs.sam.mc.searcharea.viewer.view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Philipp Trepte
 * Represents a image for the slide show.
 */
public class PreviewImagePanel extends JPanel {

	/** */
	private static final long serialVersionUID = -2307162056037660355L;

	private final JPanel numberPanel = new JPanel();
	private final ImagePanel imagePanel;
	private final JLabel numberLabel = new JLabel();

	/**
	 * custom constructor.
	 * @param number current number
	 * @param maxNumber maximal number
	 * @param imagePanel image
	 */
	public PreviewImagePanel(final int number, final int maxNumber, final ImagePanel imagePanel) {
		this.imagePanel = imagePanel;

		final String picNumber = String.valueOf(number) + " of " + maxNumber;
		setPicNumber(picNumber);
		this.numberPanel.add(numberLabel);

		final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		this.add(numberPanel);
		this.add(imagePanel);
	}

	/**
	 * Returns the image.
	 * @return image.
	 */
	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	/**
	 * Sets the new picture number.
	 * @param picNumber picture number
	 */
	public void setPicNumber(final String picNumber) {
		this.numberLabel.setText(picNumber);
	}
}