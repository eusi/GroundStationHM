package edu.hm.cs.sam.mc.searcharea.viewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.misc.Window;
import edu.hm.cs.sam.mc.searcharea.viewer.controller.SearchAreaViewerController;
import edu.hm.cs.sam.mc.searcharea.viewer.model.PreviewImage;
import edu.hm.cs.sam.mc.searcharea.viewer.view.CharacteristicsListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.DisplayedImagePanel;
import edu.hm.cs.sam.mc.searcharea.viewer.view.FlightChangeListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.ImagePanel;
import edu.hm.cs.sam.mc.searcharea.viewer.view.PictureListListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.PreviewImagePanel;
import edu.hm.cs.sam.mc.searcharea.viewer.view.SaveClassificationListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.SaveFileListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.SavePictureToAoIListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.SubImageListener;
import edu.hm.sam.searchareaservice.ByteImage;
import edu.hm.sam.searchareaservice.FlightType;
import edu.hm.sam.searchareaservice.PictureCategoryType;
import edu.hm.sam.searchareaservice.TargetType;

/**
 * GUI-Class for the Search Area Viewer.
 * Displays pictures or targets with characteristics.
 * @author Philipp Trepte
 */
public class SearchAreaViewer extends Window {

	/** */
	private static final long serialVersionUID = -7374230059277883297L;

	/** maximal number of pictures for the slide show */
	private static final int MAX_NUMBER_OF_PICTURES = 3;
	private static final String INTERRUPTED_EXCEPTION = "InterruptedException";
	private static final String EXECUTION_EXCEPTION = "ExecutionException";

	private static final Logger log = Logger.getLogger(SearchAreaViewer.class.getName());

	/** list of the picture list types */
	private static final PictureCategoryType[] namePictureLists = {
		PictureCategoryType.AOIHF,
		PictureCategoryType.AOILF,
		PictureCategoryType.SEARCHAREA,
		PictureCategoryType.ADLC,
		PictureCategoryType.ACTIONABLEINTELL,
		PictureCategoryType.NORESULTS
	};

	/** */
	private static final TargetType[] targetTypeList = {
		TargetType.UNDEFINED,
		TargetType.QRC,
		TargetType.STANDARD,
	};

	public static final String TITLE = "Search Area Viewer";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private static final String STANDARD_TARGET = "StandardTarget";
	private static final String QRC_TARGET = "QRCTarget";
	private static final String SAVE_AOI = "SaveAoI";
	private static final String TARGET_INFO = "TargetInfo";
	private static final String TARGET_TYPE = "Target Type";

	private final int pictureHeight;
	private final int pictureWidth;
	private final int previewPictureHeight;
	private final int previewPictureWidth;

	private final GridBagLayout internalFrameLayout = new GridBagLayout();
	private final GridBagConstraints internalFrameConstraints = new GridBagConstraints();

	private final JPanel pictureListPanel = new JPanel();
	private final JPanel picturePanel = new JPanel();
	private final JPanel characteristicsPanel = new JPanel();

	private final JPanel shapePanel = new JPanel();
	private final JPanel backgroundColorPanel = new JPanel();
	private final JPanel letterOrientationPanel = new JPanel();
	private final JPanel letterPanel = new JPanel();
	private final JPanel letterColorPanel = new JPanel();
	private final JPanel targetTypePanel = new JPanel();

	private final JPanel targetInfoWithSaveAoIPanel = new JPanel();
	private final JPanel switchPanel = new JPanel();
	private final JPanel targetInfoPanel = new JPanel();
	private final JPanel saveAreaOfInterestPanel = new JPanel();
	private final JPanel targetPanel = new JPanel();
	private final JPanel standardTargetPanel = new JPanel();
	private final JPanel qrcTargetPanel = new JPanel();

	private final JTextField shapeTextField = new JTextField();
	private final JTextField backgroundColorTextField = new JTextField();
	private final JTextField letterOrientationTextField = new JTextField();
	private final JTextField letterTextField = new JTextField();
	private final JTextField letterColorTextField = new JTextField();
	private final JTextField latitudeTextField = new JTextField();
	private final JTextField longitudeTextField = new JTextField();
	private final JTextField qrcTargetTextField = new JTextField();
	private final JComboBox<TargetType> targetTypeComboBox = new JComboBox<TargetType>(targetTypeList);

	private final CardLayout cardLayout = new CardLayout();
	private final CardLayout switchLayout = new CardLayout();

	private boolean isStandardTarget = false;
	private boolean isQRCTarget = false;

	private final JComboBox<PictureCategoryType> comboBox = new JComboBox<PictureCategoryType>(namePictureLists);

	private final DefaultListModel<PreviewImagePanel> listModel = new DefaultListModel<PreviewImagePanel>();
	private final JList<PreviewImagePanel> imagePanelList = new JList<PreviewImagePanel>(listModel);
	private final List<PreviewImage> bufferedImages = new ArrayList<PreviewImage>();

	private final JButton prevPictureButton = new JButton();
	private final JButton nextPictureButton = new JButton();
	private final JButton saveForReportSheet = new JButton();
	private final JButton saveAreaOfInterest = new JButton();
	private final JButton saveClassification = new JButton();

	final JCheckBox checkBox = new JCheckBox();

	private int imageIndex = 1;

	private final DisplayedImagePanel currentImagePanel;

	private CharacteristicsListener characteristicsListener;
	private PictureListListener pictureListListener;
	private SaveFileListener saveFileListener;
	private SavePictureToAoIListener savePictureToAoIListener;
	private SaveClassificationListener saveClassificationListener;
	private FlightChangeListener flightChangeListener;
	private SubImageListener subImageListener;

	private boolean isTargetInfosChanged = false;

	private String currentLatitude = "";
	private String currentLongitude = "";
	private String currentShape = "";
	private String currentOrientation = "";
	private String currentLetter = "";
	private String currentBackgroundColor = "";
	private String currentLetterColor = "";

	private BufferedImage subImage;

	private final SearchAreaViewerController controller;

	/**
	 * Creates the frame.
	 */
	public SearchAreaViewer(final String title, final Icon icon)  {
		super(title, icon);

		getMainPanel().setLayout(internalFrameLayout);

		pictureListPanel.setSize(WIDTH, (int)(HEIGHT * 0.25));
		picturePanel.setSize(WIDTH, (int)(HEIGHT * 0.55));
		characteristicsPanel.setSize(WIDTH, (int)(HEIGHT * 0.2));

		// setze Breite und Höhe für das anzuzeigende Bild und die Vorschaubilder
		pictureHeight = (int)(picturePanel.getHeight() * 0.75);
		pictureWidth = (int)(picturePanel.getWidth() * 0.6);
		previewPictureHeight = (int)(pictureListPanel.getHeight() * 0.7);
		previewPictureWidth = (int)(pictureListPanel.getWidth() * 0.2);

		saveForReportSheet.setText("Save for Reportsheet");
		saveAreaOfInterest.setText("Save area of Interest");
		saveClassification.setText("Save classification");

		addTextFieldListener();

		final JPanel borderPanel = new JPanel();
		final BorderLayout border = new BorderLayout();
		borderPanel.setLayout(border);

		// BorderLayout.NORTH -----------------------------------------------------------------------------
		final GridBagLayout pictureListGridBag = new GridBagLayout();
		final GridBagConstraints northConstraints = new GridBagConstraints();
		pictureListPanel.setLayout(pictureListGridBag);

		prevPictureButton.setText("<");
		nextPictureButton.setText(">");
		prevPictureButton.setFont(new Font(prevPictureButton.getFont().getFontName(), prevPictureButton.getFont().getStyle(), 22));
		nextPictureButton.setFont(new Font(nextPictureButton.getFont().getFontName(), nextPictureButton.getFont().getStyle(), 22));
		prevPictureButton.setPreferredSize(new Dimension((int)(pictureListPanel.getWidth() * 0.06),
				previewPictureHeight));
		nextPictureButton.setPreferredSize(new Dimension((int)(pictureListPanel.getWidth() * 0.06),
				previewPictureHeight));
		prevPictureButton.setEnabled(false);
		nextPictureButton.setEnabled(false);

		saveForReportSheet.setEnabled(false);
		saveAreaOfInterest.setEnabled(false);
		saveClassification.setEnabled(false);

		controller = new SearchAreaViewerController(this);

		addListener();

		// BorderLayout.SOUTH ------------------------------------------------------------------------------
		imagePanelList.setModel(listModel);
		imagePanelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		imagePanelList.setVisibleRowCount(-1);
		imagePanelList.setLayoutOrientation(JList.VERTICAL_WRAP);

		final JPanel panelNorth = new JPanel();
		final FlowLayout northLayout = new FlowLayout(FlowLayout.CENTER, 5, 5);
		panelNorth.setLayout(northLayout);
		panelNorth.add(new JLabel("choose picture list: "));
		panelNorth.add(comboBox);
		northConstraints.anchor = GridBagConstraints.PAGE_START;
		northConstraints.gridx = 1;
		northConstraints.gridy = 0;
		northConstraints.insets = new Insets(10, 0, 10, 0);
		pictureListPanel.add(panelNorth, northConstraints);

		final JPanel panelWest = new JPanel();
		panelWest.add(prevPictureButton);
		northConstraints.anchor = GridBagConstraints.LINE_START;
		northConstraints.gridx = 0;
		northConstraints.gridy = 1;
		northConstraints.insets = new Insets(0, 0, 0, 0);
		pictureListPanel.add(panelWest, northConstraints);

		final JPanel panelCenter = new JPanel();
		panelCenter.add(imagePanelList);
		northConstraints.anchor = GridBagConstraints.CENTER;
		northConstraints.gridx = 1;
		northConstraints.gridy = 1;
		pictureListPanel.add(panelCenter, northConstraints);

		final JPanel panelEast = new JPanel();
		panelEast.add(nextPictureButton);
		northConstraints.anchor = GridBagConstraints.LINE_END;
		northConstraints.gridx = 2;
		northConstraints.gridy = 1;
		pictureListPanel.add(panelEast, northConstraints);
		// BorderLayout.SOUTH ------------------------------------------------------------------------------


		// BorderLayout.CENTER -----------------------------------------------------------------------------
		final BufferedImage im2 = null;
		currentImagePanel = new DisplayedImagePanel(im2);
		currentImagePanel.setSubImageListener(subImageListener);
		final Dimension size = new Dimension(pictureWidth, pictureHeight);
		currentImagePanel.setPreferredSize(size);
		currentImagePanel.setMinimumSize(size);
		currentImagePanel.setMaximumSize(size);
		currentImagePanel.setSize(size);
		currentImagePanel.setLayout(null);
		picturePanel.add(currentImagePanel);

		// BorderLayout.SOUTH ------------------------------------------------------------------------------
		switchPanel.setLayout(switchLayout);
		targetPanel.setLayout(cardLayout);

		final GridBagLayout tmpGridBagLayout = new GridBagLayout();
		final GridBagConstraints tmpBagConstraints = new GridBagConstraints();
		tmpBagConstraints.fill = GridBagConstraints.VERTICAL;
		targetInfoWithSaveAoIPanel.setLayout(tmpGridBagLayout);

		final TitledBorder targetInformationen = BorderFactory.createTitledBorder("Target information");
		final GridBagLayout southLayout = new GridBagLayout();
		final GridBagConstraints contraints = new GridBagConstraints();
		contraints.fill = GridBagConstraints.HORIZONTAL;
		targetInfoPanel.setBorder(targetInformationen);
		targetInfoPanel.setLayout(southLayout);

		final JPanel tmpPanel = new JPanel();
		final GridBagLayout tmpLayout = new GridBagLayout();
		final GridBagConstraints tmpConstraints = new GridBagConstraints();
		tmpConstraints.fill = GridBagConstraints.VERTICAL;
		tmpPanel.setLayout(tmpLayout);

		checkBox.setText("target infos not correct?");

		final GridLayout keyValueGrid = new GridLayout(1, 2, 15, 5);


		// Location Panel ----------------------------------
		final JPanel locationPanel = new JPanel();
		final TitledBorder locationBorder = BorderFactory.createTitledBorder("Location");
		final GridLayout locationGridLayout = new GridLayout(2, 1, 15, 5);
		locationPanel.setBorder(locationBorder);
		locationPanel.setLayout(locationGridLayout);

		final JPanel latitudePanel = new JPanel();
		latitudePanel.setLayout(keyValueGrid);

		latitudePanel.add(new JLabel("Latitude: "));
		latitudePanel.add(latitudeTextField);

		final JPanel longitudePanel = new JPanel();
		longitudePanel.setLayout(keyValueGrid);

		longitudePanel.add(new JLabel("Longitude: "));
		longitudePanel.add(longitudeTextField);

		locationPanel.add(latitudePanel);
		locationPanel.add(longitudePanel);
		// Location Panel ----------------------------------


		tmpConstraints.weighty = 0.3;
		tmpConstraints.gridx = 1;
		tmpConstraints.gridy = 0;
		tmpConstraints.gridheight = 1;
		tmpPanel.add(checkBox, tmpConstraints);

		tmpConstraints.weighty = 0.7;
		tmpConstraints.gridx = 1;
		tmpConstraints.gridy = 1;
		tmpPanel.add(locationPanel, tmpConstraints);


		// Charateristics Panel ----------------------------
		final GridLayout characteristicsGridLayout = new GridLayout(3, 2, 15, 5);
		characteristicsPanel.setLayout(characteristicsGridLayout);
		characteristicsPanel.setBorder(BorderFactory.createTitledBorder("Characteristics"));

		shapePanel.setLayout(keyValueGrid);
		backgroundColorPanel.setLayout(keyValueGrid);
		letterOrientationPanel.setLayout(keyValueGrid);
		letterPanel.setLayout(keyValueGrid);
		letterColorPanel.setLayout(keyValueGrid);
		targetTypePanel.setLayout(keyValueGrid);

		shapePanel.add(new JLabel("Shape:"));
		shapePanel.add(shapeTextField);

		backgroundColorPanel.add(new JLabel("Background color: "));
		backgroundColorPanel.add(backgroundColorTextField);

		letterOrientationPanel.add(new JLabel("Letter Orientation: "));
		letterOrientationPanel.add(letterOrientationTextField);

		letterPanel.add(new JLabel("Alphanumeric: "));
		letterPanel.add(letterTextField);

		letterColorPanel.add(new JLabel("Alphanumeric color: "));
		letterColorPanel.add(letterColorTextField);

		characteristicsPanel.add(shapePanel);
		characteristicsPanel.add(backgroundColorPanel);
		characteristicsPanel.add(letterOrientationPanel);
		characteristicsPanel.add(letterPanel);
		characteristicsPanel.add(letterColorPanel);

		standardTargetPanel.add(characteristicsPanel);
		// Charateristics Panel ----------------------------


		// QRC Panel ---------------------------------------
		final GridLayout qrcGridLayout = new GridLayout(1, 2, 15, 5);
		qrcTargetPanel.setLayout(qrcGridLayout);
		qrcTargetPanel.setBorder(BorderFactory.createTitledBorder("Quick Respons Code (QRC)"));

		final JPanel qrcDecodePanel = new JPanel();

		qrcTargetTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
		qrcTargetTextField.setPreferredSize(new Dimension(200, 20));
		qrcTargetTextField.setMinimumSize(new Dimension(200, 20));
		qrcTargetTextField.setMaximumSize(new Dimension(200, 20));
		qrcTargetTextField.setSize(new Dimension(200, 20));

		qrcDecodePanel.add(new JLabel("decoded message: "));
		qrcDecodePanel.add(qrcTargetTextField);

		qrcTargetPanel.add(qrcDecodePanel);
		// QRC Panel ---------------------------------------


		// Target Type Panel -------------------------------
		final TitledBorder typeBorder = BorderFactory.createTitledBorder("Target Type");
		final BoxLayout targetTypeLayout = new BoxLayout(targetTypePanel, BoxLayout.Y_AXIS);
		targetTypePanel.setLayout(targetTypeLayout);
		targetTypePanel.setBorder(typeBorder);

		final JLabel noteLabel = new JLabel("Warning: You have to set the target type ("
				+ TargetType.QRC.toString() + " or " + TargetType.STANDARD.toString() + ")!");

		targetTypePanel.add(Box.createRigidArea(new Dimension(5, 40)));

		noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		targetTypeComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		targetTypeComboBox.setPreferredSize(new Dimension(200, 20));
		targetTypeComboBox.setMinimumSize(new Dimension(200, 20));
		targetTypeComboBox.setMaximumSize(new Dimension(200, 20));
		targetTypeComboBox.setSize(new Dimension(200, 20));
		saveClassification.setAlignmentX(Component.CENTER_ALIGNMENT);

		targetTypePanel.add(noteLabel);
		targetTypePanel.add(Box.createVerticalStrut(10));
		targetTypePanel.add(targetTypeComboBox);
		targetTypePanel.add(Box.createVerticalStrut(10));
		targetTypePanel.add(saveClassification);
		// Target Type Panel -------------------------------


		targetPanel.add(standardTargetPanel, STANDARD_TARGET);
		targetPanel.add(qrcTargetPanel, QRC_TARGET);
		targetPanel.add(targetTypePanel, TARGET_TYPE);


		contraints.weightx = 0.3;
		contraints.gridx = 0;
		contraints.gridy = 1;
		contraints.gridwidth = 1;
		targetInfoPanel.add(tmpPanel, contraints);

		contraints.weightx = 0.7;
		contraints.gridx = 1;
		contraints.gridy = 1;
		targetInfoPanel.add(targetPanel, contraints);


		// Save Area Of Interest Panel ---------------------
		final BoxLayout saveAoILayout = new BoxLayout(saveAreaOfInterestPanel, BoxLayout.Y_AXIS);
		final TitledBorder saveAoIBorder = new TitledBorder("Save area of interest");
		saveAreaOfInterestPanel.setLayout(saveAoILayout);
		saveAreaOfInterestPanel.setBorder(saveAoIBorder);

		final JLabel hintLabel = new JLabel("Warning: You have to draw a rectangle to mark the area of interest!");

		saveAreaOfInterestPanel.add(Box.createRigidArea(new Dimension(5, 40)));

		hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveAreaOfInterest.setAlignmentX(Component.CENTER_ALIGNMENT);

		saveAreaOfInterestPanel.add(hintLabel);
		saveAreaOfInterestPanel.add(Box.createVerticalStrut(10));
		saveAreaOfInterestPanel.add(saveAreaOfInterest);
		// Save Area Of Interest Panel ---------------------


		tmpBagConstraints.gridx = 0;
		tmpBagConstraints.gridy = 0;
		tmpBagConstraints.gridheight = 2;
		tmpBagConstraints.gridwidth = 3;
		tmpBagConstraints.anchor = GridBagConstraints.PAGE_START;
		targetInfoWithSaveAoIPanel.add(targetInfoPanel, tmpBagConstraints);

		tmpBagConstraints.gridx = 2;
		tmpBagConstraints.gridy = 2;
		tmpBagConstraints.gridheight = 1;
		tmpBagConstraints.gridwidth = 1;
		tmpBagConstraints.anchor = GridBagConstraints.LAST_LINE_END;
		targetInfoWithSaveAoIPanel.add(saveForReportSheet, tmpBagConstraints);
		targetInfoWithSaveAoIPanel.setVisible(false);

		switchPanel.add(targetInfoWithSaveAoIPanel, TARGET_INFO);
		switchPanel.add(saveAreaOfInterestPanel, SAVE_AOI);
		//switchPanel.add(targetTypePanel, TARGET_TYPE);

		switchPanel.setVisible(false);

		borderPanel.add(pictureListPanel);
		borderPanel.add(picturePanel);
		borderPanel.add(switchPanel);

		border.addLayoutComponent(pictureListPanel, BorderLayout.NORTH);
		border.addLayoutComponent(picturePanel, BorderLayout.CENTER);
		border.addLayoutComponent(switchPanel, BorderLayout.SOUTH);

		internalFrameConstraints.gridx = 0;
		internalFrameConstraints.gridy = 0;
		this.getContentPane().add(borderPanel, internalFrameConstraints);


		final JMenuBar bar = new JMenuBar();
		final JMenu highFlight = new JMenu("High Flight");
		final JMenu lowFlight = new JMenu("Low Flight");
		final JMenuItem startLow = new JMenuItem("Start");
		final JMenuItem stopHigh = new JMenuItem("Stop");

		startLow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				flightChangeListener.updateListener(FlightType.LOW, true);
			}
		});

		stopHigh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				flightChangeListener.updateListener(FlightType.HIGH, false);
			}
		});

		highFlight.add(stopHigh);
		lowFlight.add(startLow);
		bar.add(highFlight);
		bar.add(lowFlight);

		setJMenuBar(bar);


		this.setVisible(true);
	}

	/**
	 * Add list cell renderer.
	 */
	private void addListCellRender() {
		imagePanelList.setCellRenderer(new ListCellRenderer<PreviewImagePanel>() {

			@Override
			public Component getListCellRendererComponent(final JList<? extends PreviewImagePanel> list,
					final PreviewImagePanel value, final int index, final boolean isSelected,
					final boolean cellHasFocus) {

				for (int i = 0; i < list.getModel().getSize(); i++) {
					final int tmpIndex = i + imageIndex;
					final String picNumber = tmpIndex + " of " + bufferedImages.size();
					list.getModel().getElementAt(i).setPicNumber(picNumber);
				}

				return value;
			}
		});
	}

	/**
	 * Add list selection listener.
	 */
	private void addListSelectionListener() {
		imagePanelList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(final ListSelectionEvent arg0) {
				if (imagePanelList.getSelectedIndex() >= 0) {
					final int index = imagePanelList.getSelectedIndex() + imageIndex - 1;
					final String picturePath = bufferedImages.get(index).getPicturePath();
					final BufferedImage bufferedImage = bitmapToImage(controller.getByteImage(picturePath).getImage(), "jpg");
					final BufferedImage image = scaleImage(bufferedImage, pictureWidth, pictureHeight);

					if (((PictureCategoryType)comboBox.getSelectedItem()).equals(PictureCategoryType.NORESULTS)) {
						switchLayout.show(switchPanel, SAVE_AOI);
					} else {
						saveForReportSheet.setEnabled(true);
						switchLayout.show(switchPanel, TARGET_INFO);
						if (isTargetInfosChanged && targetInfoPanel.isVisible() && arg0.getValueIsAdjusting()) {
							clearTextFields();
							isTargetInfosChanged = false;
						}
						if (isAoI()) {
							cardLayout.show(targetPanel, TARGET_TYPE);
						} else if (isStandardTarget && !isQRCTarget) {
							cardLayout.show(targetPanel, STANDARD_TARGET);
						} else if (isQRCTarget && !isStandardTarget) {
							cardLayout.show(targetPanel, QRC_TARGET);
						} else {
							// do nothing
						}
						checkBox.setVisible(!isAoI());
						checkBox.setEnabled(isADLC() && !isTargetInfosChanged);
						saveForReportSheet.setVisible(!isAoI());
						characteristicsListener.updateListener(index);

						// set current values of the target
						currentBackgroundColor = backgroundColorTextField.getText();
						currentLatitude = latitudeTextField.getText();
						currentLetter = letterTextField.getText();
						currentLetterColor = letterColorTextField.getText();
						currentLongitude = longitudeTextField.getText();
						currentOrientation = letterOrientationTextField.getText();
						currentShape = shapeTextField.getText();
					}
					showCurrentImage(image);
					switchPanel.setVisible(true);
				}
			}
		});
	}

	/**
	 * Add combo box listener.
	 */
	private void addComboBoxListener() {
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					log.debug(((PictureCategoryType)e.getItem()).toString());
					listModel.clear();
					bufferedImages.clear();
					saveAreaOfInterest.setEnabled(false);
					currentImagePanel.setImage(null);
					switchPanel.setVisible(false);
					imageIndex = 1;
					clearTextFields();
					cardLayout.removeLayoutComponent(targetPanel);
					switchLayout.removeLayoutComponent(switchPanel);
					saveForReportSheet.setEnabled(false);
					nextPictureButton.setEnabled(false);
					nextPictureButton.repaint();
					prevPictureButton.setEnabled(false);
					prevPictureButton.repaint();
					isTargetInfosChanged = false;
					final PictureCategoryType model = (PictureCategoryType)e.getItem();
					log.debug("picture category type: " + model.toString());
					pictureListListener.updateListener(model.toString());
				}
			}
		});
	}

	/**
	 * Add preview picture button listener.
	 */
	private void addPrevPictureButtonListener() {
		prevPictureButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				if (imageIndex < bufferedImages.size() - 1) {
					nextPictureButton.setEnabled(true);
				}
				prevPictureButton.setEnabled(imageIndex > 1);
				if (imageIndex > 1) {
					if (imageIndex == 2) {
						prevPictureButton.setEnabled(false);
					}
					new SwingWorker<PreviewImage, Void>() {

						@Override
						protected PreviewImage doInBackground() throws Exception {
							final int index = imageIndex - (MAX_NUMBER_OF_PICTURES - 1);
							final PreviewImage prevImage = bufferedImages.get(index);
							final PreviewImage image = new PreviewImage(prevImage.getNumber(),
									prevImage.getMaxNumber(), prevImage.getPicturePath());
							imageIndex--;
							return image;
						}

						@Override
						protected void done() {
							try {
								final PreviewImage prevImage = get();

								for (int i = imagePanelList.getModel().getSize() - 1; i >= 1; i--) {
									final ImagePanel imagePanel = imagePanelList.getModel().getElementAt(i).getImagePanel();
									final BufferedImage image =
											imagePanelList.getModel().getElementAt(i - 1).getImagePanel().getImage();
									imagePanel.setImage(image);
								}

								final ByteImage byteImage = controller.getByteImage(prevImage.getPicturePath());
								final BufferedImage bufferedImage = bitmapToImage(byteImage.getImage(), "jpg");
								final BufferedImage image = scaleImage(bufferedImage, previewPictureWidth, previewPictureHeight);
								final ImagePanel imagePanel = imagePanelList.getModel().getElementAt(0).getImagePanel();
								imagePanel.setImage(image);

								imagePanelList.repaint();
							} catch (final InterruptedException e) {
								log.error(INTERRUPTED_EXCEPTION, e);
							} catch (final ExecutionException e) {
								log.error(EXECUTION_EXCEPTION, e);
							}
						}

					}.execute();
				}
			}
		});
	}

	/**
	 * Add next picture button listener.
	 */
	private void addNextPictureButtonListener() {
		nextPictureButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				if (imageIndex > 0) {
					prevPictureButton.setEnabled(true);
				}
				if (imageIndex < bufferedImages.size() - 2) {
					nextPictureButton.setEnabled(true);
					if (imageIndex == bufferedImages.size() - 3) {
						nextPictureButton.setEnabled(false);
					}
					new SwingWorker<PreviewImage, Void>() {

						@Override
						protected PreviewImage doInBackground() throws Exception {
							final int index = imageIndex + MAX_NUMBER_OF_PICTURES - 1;
							final PreviewImage prevImage = bufferedImages.get(index);
							final PreviewImage image = new PreviewImage(prevImage.getNumber(),
									prevImage.getMaxNumber(), prevImage.getPicturePath());
							imageIndex++;
							return image;
						}

						@Override
						protected void done() {
							try {
								final PreviewImage prevImage = get();

								for (int i = 0; i < imagePanelList.getModel().getSize() - 1; i++) {
									final ImagePanel imagePanel = imagePanelList.getModel().getElementAt(i).getImagePanel();
									final BufferedImage image =
											imagePanelList.getModel().getElementAt(i + 1).getImagePanel().getImage();
									imagePanel.setImage(image);
								}

								final ByteImage byteImage = controller.getByteImage(prevImage.getPicturePath());
								final BufferedImage bufferedImage = bitmapToImage(byteImage.getImage(), "jpg");
								final BufferedImage image = scaleImage(bufferedImage, previewPictureWidth, previewPictureHeight);
								final ImagePanel imagePanel = imagePanelList.getModel().getElementAt(MAX_NUMBER_OF_PICTURES - 1).getImagePanel();
								imagePanel.setImage(image);

								imagePanelList.repaint();
							} catch (final InterruptedException e) {
								log.error(INTERRUPTED_EXCEPTION, e);
							} catch (final ExecutionException e) {
								log.error(EXECUTION_EXCEPTION, e);
							}
						}

					}.execute();
				} else {
					nextPictureButton.setEnabled(false);
				}
			}
		});
	}

	/**
	 * Add save for reportsheet listener.
	 */
	private void addSaveForReportSheetListener() {
		saveForReportSheet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				log.debug("User clicked to save the choosen image for the reportsheet");
				if (currentImagePanel.getImage() != null) {
					log.debug("User can only clicked the button if an image is selected");
					// Bild als Datei wobei der Dateiname alle Merkmale enthält
					final int index = imagePanelList.getSelectedIndex() + imageIndex - 1;
					new SwingWorker<Void, Void>() {

						@Override
						protected Void doInBackground() throws Exception {
							saveForReportSheet.setEnabled(false);
							log.debug("update save file listener");
							saveFileListener.updateListener(index);
							return null;
						}
					}.execute();
				}
			}
		});
	}

	/**
	 * Add save classification listener.
	 */
	private void addSaveClassificationListener() {
		saveClassification.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (currentImagePanel.getImage() != null) {
					final int index = imagePanelList.getSelectedIndex() + imageIndex - 1;
					new SwingWorker<Void, Void>() {

						@Override
						protected Void doInBackground() throws Exception {
							saveClassification.setEnabled(false);
							log.debug("update save classification listener");
							saveClassificationListener.updateListener(index,
									((TargetType)targetTypeComboBox.getSelectedItem()).toString());
							targetTypeComboBox.setSelectedItem(TargetType.UNDEFINED);
							return null;
						}
					}.execute();
				}
			}
		});
	}

	/**
	 * Add check box listener.
	 */
	private void addCheckBoxListener() {
		checkBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					disableFields(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED
						&& !isTargetInfosChanged){
					disableFields(true);
				}
			}
		});
	}

	/**
	 * Add save area of interest listener.
	 */
	private void addSaveAreaOfInterestListener() {
		saveAreaOfInterest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final int reply = JOptionPane.showConfirmDialog(SearchAreaViewer.this,
						"Would you like to save the area of interest?", "Save area of Interest", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					currentImagePanel.setClear(true);
					final int index = imagePanelList.getSelectedIndex() + imageIndex - 1;
					savePictureToAoIListener.updateListener(index, subImage);
				} else {
					// do something
				}
			}
		});
	}

	/**
	 * Add sub image listener.
	 */
	private void addSubImageListener() {
		subImageListener = new SubImageListener() {

			@Override
			public void updateListener(final int topLeftX, final int topLeftY, final int width, final int height) {
				if (currentImagePanel != null && currentImagePanel.getImage() != null) {
					saveAreaOfInterest.setEnabled(true);
					subImage = currentImagePanel.getImage().getSubimage(topLeftX, topLeftY, width, height);
				}
			}
		};
	}

	/**
	 * Add listener.
	 */
	private void addListener() {
		addListCellRender();
		addListSelectionListener();
		addComboBoxListener();
		addPrevPictureButtonListener();
		addNextPictureButtonListener();
		addSaveForReportSheetListener();
		addSaveClassificationListener();
		addCheckBoxListener();
		addSaveAreaOfInterestListener();
		addSubImageListener();
	}

	/**
	 * Checks if the current picture category the area of interest is.
	 * @return true is area of interest, false otherwise
	 */
	private boolean isAoI() {
		final String pictureType = ((PictureCategoryType)comboBox.getSelectedItem()).toString();
		return pictureType.equals(PictureCategoryType.AOIHF.toString()) ||
				pictureType.equals(PictureCategoryType.AOILF.toString());
	}

	/**
	 * Scales the image.
	 * @param originalImage original image
	 * @param width width of the image
	 * @param height height of the image
	 * @return the scaled image
	 */
	private BufferedImage scaleImage(final BufferedImage originalImage, final int width, final int height) {
		final BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
		final Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(originalImage, 0, 0, width, height, null);
		g2.dispose();
		return resizedImg;
	}

	/**
	 * Creates the image from a byte array.
	 * @param data image data
	 * @param format image format
	 * @return image
	 */
	private BufferedImage bitmapToImage(final byte [] data, final String format) {
		BufferedImage bufferedImage = null;
		ImageReader rdr = null;
		try (final ByteArrayInputStream inb = new ByteArrayInputStream(data)) {
			rdr = ImageIO.getImageReadersByFormatName(format).next();
			final ImageInputStream iis = ImageIO.createImageInputStream(inb);
			rdr.setInput(iis);
			bufferedImage = rdr.read(0);
		} catch (final IOException iox) {
			log.debug("image could not be loaded");
		}
		return bufferedImage;
	}

	/**
	 * Shows the current image.
	 * @param image current image
	 */
	private void showCurrentImage(final BufferedImage image) {
		new SwingWorker<BufferedImage, Void>() {

			@Override
			protected BufferedImage doInBackground() throws Exception {
				return image;
			}

			@Override
			protected void done() {
				try {
					currentImagePanel.setImage(get());
				} catch (final InterruptedException e) {
					log.error(INTERRUPTED_EXCEPTION, e);
				} catch (final ExecutionException e) {
					log.error(EXECUTION_EXCEPTION, e);
				}
			}

		}.execute();
	}

	/**
	 * Checks if the current picture category adlc is.
	 * @return true is adlc, false otherwise
	 */
	private boolean isADLC() {
		return ((PictureCategoryType)comboBox.getSelectedItem()).equals(PictureCategoryType.ADLC);
	}

	/**
	 * Clears all textfields.
	 */
	private void clearTextFields() {
		latitudeTextField.setText(null);
		longitudeTextField.setText(null);
		letterColorTextField.setText(null);
		letterOrientationTextField.setText(null);
		letterTextField.setText(null);
		backgroundColorTextField.setText(null);
		shapeTextField.setText(null);
	}

	/**
	 * Sets the visibility of the container with the standard target characteristics.
	 * @param isVisible true is visible, false otherwise
	 */
	public void setStandardTargetVisible(final boolean isVisible) {
		isStandardTarget = isVisible;
	}

	/**
	 * Sets the visibility of the container with the qrc target information.
	 * @param isVisible true is visible, false otherwise
	 */
	public void setQRCTargetVisible(final boolean isVisible) {
		isQRCTarget = isVisible;
	}

	/**
	 * Sets the characteristics listener.
	 * @param listener characteristics listener
	 */
	public void setCharacteristicsListener(final CharacteristicsListener listener) {
		this.characteristicsListener = listener;
	}

	/**
	 * Sets the save file listener.
	 * @param listener save file listener
	 */
	public void setSaveFileListener(final SaveFileListener listener) {
		this.saveFileListener = listener;
	}

	/**
	 * Sets the picture list listener.
	 * @param listener picture list listener
	 */
	public void setPictureListListener(final PictureListListener listener) {
		this.pictureListListener = listener;
	}

	/**
	 * Sets the save picture to aoi listener.
	 * @param listener save picture to aoi listener
	 */
	public void setSavePictureToAoIListener(final SavePictureToAoIListener listener) {
		this.savePictureToAoIListener = listener;
	}

	/**
	 * Sets the save classification listener.
	 * @param listener classification listener
	 */
	public void setSaveClassificationListener(final SaveClassificationListener listener) {
		this.saveClassificationListener = listener;
	}

	/**
	 * Sets the flight change listener.
	 * @param listener flight change listener
	 */
	public void setFlightChangeListener(final FlightChangeListener listener) {
		this.flightChangeListener = listener;
	}

	/**
	 * Set images to preview picture list.
	 * @param picturePaths list with picture paths
	 */
	public void setImageList(final List<String> picturePaths) {
		int count = 1;
		for (final String path : picturePaths) {
			setImageToList(count, count, path);
			count++;
		}
	}

	/**
	 * Set a image to preview picture list.
	 * @param number current picture number
	 * @param maxNumber maximal number
	 * @param picturePath picture path
	 */
	public void setImageToList(final int number, final int maxNumber, final String picturePath) {
		final PreviewImage prevImage = new PreviewImage(number, maxNumber, picturePath);
		bufferedImages.add(prevImage);

		if (listModel.getSize() < MAX_NUMBER_OF_PICTURES) {
			final BufferedImage bufferedImage = bitmapToImage(controller.getByteImage(picturePath).getImage(), "jpg");
			final BufferedImage img = scaleImage(bufferedImage, previewPictureWidth, previewPictureHeight);
			final ImagePanel panel = new ImagePanel(img);
			final PreviewImagePanel prev = new PreviewImagePanel(number, maxNumber, panel);
			listModel.addElement(prev);
		} else {
			imagePanelList.repaint();
		}
		if (bufferedImages.size() > listModel.getSize()) {
			nextPictureButton.setEnabled(true);
		}
	}

	/**
	 * Disable all fields if the target counts to adlc.
	 * @param isADLC true is adlc, false otherwise
	 */
	public void disableFields(final boolean isADLC) {
		longitudeTextField.setEnabled(!isADLC);
		latitudeTextField.setEnabled(!isADLC);
		backgroundColorTextField.setEnabled(!isADLC);
		letterColorTextField.setEnabled(!isADLC);
		letterOrientationTextField.setEnabled(!isADLC);
		letterTextField.setEnabled(!isADLC);
		shapeTextField.setEnabled(!isADLC);
		qrcTargetTextField.setEnabled(!isADLC);
		targetTypeComboBox.setEnabled(!isADLC);
	}

	/**
	 * Enables the container of the target information if the target is completed.
	 */
	public void targetIsCompleted() {
		targetInfoPanel.setEnabled(false);
		saveForReportSheet.setEnabled(false);
	}

	/**
	 * Checks if the input of the text fields has changed.
	 */
	private void checkTextFieldChanged() {
		if (latitudeTextField.getText().equals(currentLatitude) &&
				longitudeTextField.getText().equals(currentLongitude) &&
				shapeTextField.getText().equals(currentShape) &&
				letterOrientationTextField.getText().equals(currentOrientation) &&
				letterTextField.getText().equals(currentLetter) &&
				letterColorTextField.getText().equals(currentLetterColor) &&
				backgroundColorTextField.getText().equals(currentBackgroundColor)) {
			isTargetInfosChanged = false;
		} else {
			isTargetInfosChanged = true;
		}
		checkBox.setEnabled(!isTargetInfosChanged && isADLC());
	}

	/**
	 * Add latitude listener.
	 */
	private void addLatitudeListener() {
		latitudeTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void changedUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}
		});
	}

	/**
	 * Add longitude listener.
	 */
	private void addLongitudeListener() {
		longitudeTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void changedUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}
		});
	}

	/**
	 * Add shape listener.
	 */
	private void addShapeListener() {
		shapeTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void changedUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}
		});
	}

	/**
	 * Add letter color listener.
	 */
	private void addLetterColorListener() {
		letterColorTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void changedUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}
		});
	}

	/**
	 * Add letter orientation listener.
	 */
	private void addLetterOrientationListener() {
		letterOrientationTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void changedUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}
		});
	}

	/**
	 * Add letter listener.
	 */
	private void addLetterListener() {
		letterTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void changedUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}
		});
	}

	/**
	 * Add background color listener.
	 */
	private void addBackgroundColorListener() {
		backgroundColorTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}

			@Override
			public void changedUpdate(final DocumentEvent e) {
				checkTextFieldChanged();
			}
		});
	}

	/**
	 * Add target type listener.
	 */
	private void addTargetTypeListener() {
		//		targetTypeComboBox.getDocument().addDocumentListener(new DocumentListener() {
		//
		//			@Override
		//			public void removeUpdate(final DocumentEvent e) {
		//				isTargetTypeCorrect(targetTypeComboBox.getText());
		//			}
		//
		//			@Override
		//			public void insertUpdate(final DocumentEvent e) {
		//				isTargetTypeCorrect(targetTypeComboBox.getText());
		//			}
		//
		//			@Override
		//			public void changedUpdate(final DocumentEvent e) {
		//				isTargetTypeCorrect(targetTypeComboBox.getText());
		//			}
		//		});
		targetTypeComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					final String targetType = ((TargetType)targetTypeComboBox.getSelectedItem()).toString();
					saveClassification.setEnabled(targetType.equals(TargetType.QRC.toString()) ||
							targetType.equals(TargetType.STANDARD.toString()));
				}
			}
		});
	}

	/**
	 * Add textfield listener.
	 */
	private void addTextFieldListener() {
		addLatitudeListener();
		addLongitudeListener();
		addShapeListener();
		addLetterColorListener();
		addLetterOrientationListener();
		addLetterListener();
		addBackgroundColorListener();
		addTargetTypeListener();
	}

	/**
	 * Sets the latitude of the target.
	 * @param latitude latitude
	 */
	public void setLatitude(final String latitude) {
		latitudeTextField.setText(latitude);
	}

	/**
	 * Sets the longitude of the target.
	 * @param longitude longitude
	 */
	public void setLongitude(final String longitude) {
		longitudeTextField.setText(longitude);
	}

	/**
	 * Sets the letter of the target.
	 * @param letter letter
	 */
	public void setLetter(final String letter) {
		letterTextField.setText(letter);
	}

	/**
	 * Sets the background color of the target.
	 * @param bgColor background color
	 */
	public void setBGColor(final String bgColor) {
		backgroundColorTextField.setText(bgColor);
	}

	/**
	 * Sets the foreground color of the target.
	 * @param fgColor foreground color
	 */
	public void setFGColor(final String fgColor) {
		letterColorTextField.setText(fgColor);
	}

	/**
	 * Sets the orientation of the target.
	 * @param orientation orientation
	 */
	public void setOrientation(final String orientation) {
		letterOrientationTextField.setText(orientation);
	}

	/**
	 * Sets the shape of the target.
	 * @param shape shape
	 */
	public void setShape(final String shape) {
		shapeTextField.setText(shape);
	}

	/**
	 * Sets the decoded message.
	 * @param message decoded message
	 */
	public void setDecodedMessage(final String message) {
		qrcTargetTextField.setText(message);
	}

	/**
	 * Returns the latitude.
	 * @return latitdue
	 */
	public String getLatitutde() {
		return latitudeTextField.getText();
	}

	/**
	 * Returns the longitude.
	 * @return longitude
	 */
	public String getLongitude() {
		return longitudeTextField.getText();
	}

	/**
	 * Returns the letter.
	 * @return letter
	 */
	public String getLetter() {
		return letterTextField.getText();
	}

	/**
	 * Returns the background color.
	 * @return background color
	 */
	public String getBGColor() {
		return backgroundColorTextField.getText();
	}

	/**
	 * Returns the foreground color.
	 * @return foreground color
	 */
	public String getFGColor() {
		return letterColorTextField.getText();
	}

	/**
	 * Returns the orientation.
	 * @return orientation
	 */
	public String getOrientation() {
		return letterOrientationTextField.getText();
	}

	/**
	 * Returns the shape.
	 * @return shape
	 */
	public String getShape() {
		return shapeTextField.getText();
	}

	/**
	 * Returns the decoded message.
	 * @return decoded message
	 */
	public String getDecodedMessage() {
		return qrcTargetTextField.getText();
	}

	@Override
	public void saveProperties() {

	}

	@Override
	public void loadProperties() {

	}
}