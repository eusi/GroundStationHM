package edu.hm.cs.sam.mc.searcharea.viewer.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.control.GroundStationClient;
import edu.hm.cs.sam.mc.misc.Settings;
import edu.hm.cs.sam.mc.searcharea.viewer.SearchAreaViewer;
import edu.hm.cs.sam.mc.searcharea.viewer.model.AreaOfInterest;
import edu.hm.cs.sam.mc.searcharea.viewer.model.NoTargets;
import edu.hm.cs.sam.mc.searcharea.viewer.model.Picture;
import edu.hm.cs.sam.mc.searcharea.viewer.model.QRCTarget;
import edu.hm.cs.sam.mc.searcharea.viewer.model.SearchAreaTarget;
import edu.hm.cs.sam.mc.searcharea.viewer.model.SearchAreaTargetVisitor;
import edu.hm.cs.sam.mc.searcharea.viewer.model.StandardTarget;
import edu.hm.cs.sam.mc.searcharea.viewer.model.Targets;
import edu.hm.cs.sam.mc.searcharea.viewer.view.CharacteristicsListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.FlightChangeListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.PictureListListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.SaveClassificationListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.SaveFileListener;
import edu.hm.cs.sam.mc.searcharea.viewer.view.SavePictureToAoIListener;
import edu.hm.cs.sam.mc.secretmessage.SecretMessageController;
import edu.hm.sam.control.ControlMessage;
import edu.hm.sam.control.ControlMessage.Service;
import edu.hm.sam.control.SubscriptionHandler;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.searchareaservice.AreaOfInterestMessage;
import edu.hm.sam.searchareaservice.ByteImage;
import edu.hm.sam.searchareaservice.FlightType;
import edu.hm.sam.searchareaservice.PictureCategoryType;
import edu.hm.sam.searchareaservice.PictureMessage;
import edu.hm.sam.searchareaservice.QRCTargetMessage;
import edu.hm.sam.searchareaservice.SearchAreaTargetMessage;
import edu.hm.sam.searchareaservice.StandardTargetMessage;
import edu.hm.sam.searchareaservice.TargetTaskType;
import edu.hm.sam.searchareaservice.TargetType;
import edu.hm.sam.searchareaservice.VerifyFlightMessage;
import edu.hm.sam.searchareaservice.VerifyTargetMessage;

/**
 * @author Philipp Trepte
 * Represents the controller for the logic in the view.
 */
public class SearchAreaViewerController {

	private static final Logger log = Logger.getLogger(SearchAreaViewerController.class.getName());

	private static final String RESULTS = "RESULTS";
	private static final String NO_RESULTS = "NORESULTS";

	/** view */
	private final SearchAreaViewer view;

	/** model for the targets */
	private final Targets model;
	/** model for the no targets */
	private final NoTargets modelNoTargets;

	/** list with id's */
	private final List<Integer> idList = new ArrayList<Integer>();
	/** manually set waypoints for the low flight */
	private final List<LocationWp> wayPoints = new ArrayList<LocationWp>();

	/** current picture category */
	private String currentCategory = PictureCategoryType.AOIHF.toString();

	/** picture listener */
	private PictureListListener pictureListener = null;
	/** characteristics listener */
	private CharacteristicsListener characteristicsListener = null;
	/** save file listener */
	private SaveFileListener saveFileListener = null;
	/** save picture to AoI listener */
	private SavePictureToAoIListener savePictureToAoIListener = null;
	/** save classification listener */
	private SaveClassificationListener saveClassificationListener = null;
	/** flight change listener */
	private FlightChangeListener flightChangeListener = null;

	/** counter which counts the number of pictures */
	private int counter = 1;
	/** flag to signal that actionable intelligence task is done */
	private boolean isActionable = false;
	/** letters for the secret message */
	private String lettersForSecretMessage = "";

	/**
	 * custom constructor.
	 * @param view view
	 */
	public SearchAreaViewerController(final SearchAreaViewer view) {
		this.view = view;
		model = new Targets();
		modelNoTargets = new NoTargets();

		addListener();

		// Receive pictures from Smartphone
		final GroundStationClient client = GroundStationClient.getInstance();
		client.addSubscriptionHandler(Service.SEARCHAREA, RESULTS, new SubscriptionHandler<SearchAreaTargetMessage>() {
			@Override
			public void handleSubscription(final SearchAreaTargetMessage subscription) {
				log.debug("received target");
				synchronized (SearchAreaViewerController.class) {
					helpHandleSubscription(subscription, RESULTS);
				}
			}
		});

		client.addSubscriptionHandler(Service.SEARCHAREA, NO_RESULTS, new SubscriptionHandler<SearchAreaTargetMessage>() {
			@Override
			public void handleSubscription(final SearchAreaTargetMessage subscription) {
				log.debug("received picture");
				synchronized (SearchAreaViewerController.class) {
					helpHandleSubscription(subscription, NO_RESULTS);
				}
			}
		});
	}

	/**
	 * Handles the subscription.
	 * @param subscription subscription
	 */
	private void helpHandleSubscription(final SearchAreaTargetMessage subscription, final String topic) {
		final String picturePath =
				Settings.getSrchAreaFldr() + subscription.getID() + "_" + subscription.getTargetType() + ".jpg";

		saveImage(picturePath, subscription.getImage());

		final SearchAreaTarget target;
		if (topic.equals(NO_RESULTS)) {
			final PictureMessage picMsg = (PictureMessage)subscription;
			target = new Picture(picMsg.getID(), picturePath, picMsg.getLocation(),
					picMsg.getTargetType(), picMsg.getPictureCategoryType(), picMsg.getFlightType());
			modelNoTargets.addPicture(target);
		} else {
			if (subscription.getTargetType().equals(TargetType.STANDARD.toString())) {
				final StandardTargetMessage stdTargetMsg = (StandardTargetMessage)subscription;
				target = new StandardTarget(stdTargetMsg.getID(), picturePath, stdTargetMsg.getLocation(), stdTargetMsg.getTargetType(),
						stdTargetMsg.getPictureCategoryType(), stdTargetMsg.getLetter(), stdTargetMsg.getBGColor(),
						stdTargetMsg.getFGColor(), stdTargetMsg.getOrientation(), stdTargetMsg.getShape());
			} else if (subscription.getTargetType().equals(TargetType.QRC.toString())) {
				final QRCTargetMessage qrcTargetMsg = (QRCTargetMessage)subscription;
				target = new QRCTarget(qrcTargetMsg.getID(), picturePath, qrcTargetMsg.getLocation(), qrcTargetMsg.getTargetType(),
						qrcTargetMsg.getPictureCategoryType(), qrcTargetMsg.getMessage());
			} else {
				final AreaOfInterestMessage aoiMsg = (AreaOfInterestMessage)subscription;
				target = new AreaOfInterest(aoiMsg.getID(), picturePath, aoiMsg.getLocation(), aoiMsg.getTargetType(),
						aoiMsg.getPictureCategoryType());
			}
			model.addTarget(target);
		}

		if (currentCategory.equals(subscription.getPictureCategoryType())) {
			counter++;

			synchronized (idList) {
				idList.add(subscription.getID());
			}

			view.setImageToList(counter, counter, picturePath);
		}
	}

	/**
	 * Save the image to disk.
	 * @param picturePath picture path
	 */
	private void saveImage(final String picturePath, final ByteImage byteImage) {
		try {
			final BufferedImage img = ImageIO.read(new ByteArrayInputStream(byteImage.getImage()));

			final File outputFile = new File(picturePath);
			log.debug("AbsolutePath: " + outputFile.getAbsolutePath());
			if (!outputFile.exists()) {
				log.debug("create new file");
				outputFile.createNewFile();
			}
			ImageIO.write(img, "jpg", outputFile);
		} catch (final IOException e) {
			log.debug("image could not be saved: " + e.getMessage());
		}
	}

	/**
	 * Load the image as byte array from the disk.
	 * @param picturePath picture path
	 * @return image as byte array
	 */
	private ByteImage loadImageAsByte(final String picturePath) {
		final File imgPath = new File(picturePath);
		BufferedImage bufferedImage;
		byte[] byteArray;
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			bufferedImage = ImageIO.read(imgPath);

			if (bufferedImage != null) {
				ImageIO.write(bufferedImage, "jpg", baos);
				baos.flush();

				byteArray = baos.toByteArray();
			} else {
				byteArray = new byte[0];
			}

		} catch (final IOException e) {
			log.debug("image could not be loaded: " + e.getMessage());
			bufferedImage = null;
			byteArray = new byte[0];
		}

		return new ByteImage(byteArray);
	}

	/**
	 * Add characteristics listener.
	 */
	private void addCharacteristicsListener() {
		characteristicsListener = new CharacteristicsListener() {

			@Override
			public void updateListener(final int id) {
				final int listID = getIndex(id);
				if (listID > 0) {
					final SearchAreaTarget object = model.getTarget(listID - 1);
					if (object != null) {
						if (model.isTargetCompleted(listID - 1)) {
							view.targetIsCompleted();
						}
						final boolean isADLC = object.getPictureCategoryType().equals(PictureCategoryType.ADLC.toString());
						view.disableFields(isADLC || model.isTargetCompleted(listID - 1));
						view.setLatitude(String.valueOf(object.getLocation().getLat()));
						view.setLongitude(String.valueOf(object.getLocation().getLng()));
						if (object.getTargetType().equals(TargetType.STANDARD.toString())) {
							view.setStandardTargetVisible(true);
							view.setQRCTargetVisible(false);
							final StandardTarget standardTarget = (StandardTarget)object;
							view.setLetter(standardTarget.getLetter());
							view.setBGColor(standardTarget.getBGColor());
							view.setFGColor(standardTarget.getFGColor());
							view.setOrientation(standardTarget.getOrientation());
							view.setShape(standardTarget.getShape());
						} else if (object.getTargetType().equals(TargetType.QRC.toString())) {
							view.setStandardTargetVisible(false);
							view.setQRCTargetVisible(true);
							final QRCTarget qrcTarget = (QRCTarget)object;
							view.setDecodedMessage(qrcTarget.getMessage());
						} else {
							// do nothing
						}
					}
				}
			}
		};
	}

	/**
	 * Add picture listener.
	 */
	private void addPictureListener() {
		pictureListener = new PictureListListener() {

			@Override
			public void updateListener(final String listType) {
				counter = 1;
				currentCategory = listType;
				final SearchAreaTargetVisitor visitor = getVisitor(listType);

				final List<String> list = visitor.getList();
				synchronized (idList) {
					idList.clear();
					idList.addAll(visitor.getIDList());
				}
				view.setImageList(list);
			}
		};
	}

	/**
	 * Add save file listener.
	 */
	private void addSaveFileListener() {
		saveFileListener = new SaveFileListener() {

			@Override
			public void updateListener(final int id) {
				String fileName = Settings.getImgFldr();
				final int listID = getIndex(id);
				log.debug("listID: " + listID);
				if (listID > 0) {
					final SearchAreaTarget object = model.getTarget(listID - 1);
					if (object != null) {
						model.setToCompleted(listID - 1);
						view.disableFields(true);
						log.debug("object: " + object);

						final String targetTaskType;
						if (currentCategory.equals(PictureCategoryType.ACTIONABLEINTELL.toString())) {
							targetTaskType = TargetTaskType.AITASK.toString();
						} else if (currentCategory.equals(PictureCategoryType.ADLC.toString())) {
							targetTaskType = TargetTaskType.ADLCTASK.toString();
						} else if (currentCategory.equals(PictureCategoryType.SEARCHAREA.toString())) {
							targetTaskType = TargetTaskType.SATASK.toString();
						} else {
							targetTaskType = "";
						}
						final GroundStationClient client = GroundStationClient.getInstance();
						final VerifyTargetMessage verifyTargetMsg = new VerifyTargetMessage(true, targetTaskType);
						client.sendAction(ControlMessage.Service.SEARCHAREA, "VERIFYTARGET", verifyTargetMsg);

						final boolean isADLC = object.getPictureCategoryType().equals(PictureCategoryType.ADLC.toString());
						final double latitude = Double.parseDouble(view.getLatitutde());
						final double longitude = Double.parseDouble(view.getLongitude());
						object.setLocation(new LocationWp(latitude, longitude));
						if (object.getTargetType().equals(TargetType.STANDARD.toString())) {
							final StandardTarget stdTarget = (StandardTarget)object;

							stdTarget.setLetter(view.getLetter());
							stdTarget.setBGColor(view.getBGColor());
							stdTarget.setFGColor(view.getFGColor());
							stdTarget.setOrientation(view.getOrientation());
							stdTarget.setShape(view.getShape());

							lettersForSecretMessage = lettersForSecretMessage.concat(stdTarget.getLetter());
							final SecretMessageController secretMessageController = SecretMessageController.getInstance();
							secretMessageController.setNewLetters(lettersForSecretMessage);

							fileName += listID + "-" + stdTarget.getTargetType() + "-" +
									view.getLatitutde() + "-" + view.getLongitude() + "-" +
									view.getOrientation() + "-" + view.getShape() + "-" + view.getBGColor() + "-" +
									view.getLetter() + "-" + view.getFGColor() + "-" +
									listID + "-" + "-" + (isADLC ? 1 : 0) + ".jpg";
						} else if (object.getTargetType().equals(TargetType.QRC.toString())) {
							final QRCTarget qrcTarget = (QRCTarget)object;

							qrcTarget.setMessage(view.getDecodedMessage());

							fileName += listID + "-" + qrcTarget.getTargetType() + "-" +
									view.getLatitutde() + "-" + view.getLongitude() + "-" +
									"-" + "-" + "-" + "-" + "-" + "-" + "-" +
									listID + "-" + qrcTarget.getMessage() + "-" + (isADLC ? 1 : 0) + ".jpg";
						} else {
							// do nothing
						}

						log.debug("fileName#1: " + fileName);

						final String picturePath = object.getPicturePath();
						log.debug("picture path: " + picturePath);
						final ByteImage image = loadImageAsByte(picturePath);
						saveImage(fileName, image);
					}
				}
			}
		};
	}

	/**
	 * Add save picture to aoi listener.
	 */
	private void addSavePictureToAoIListener() {
		savePictureToAoIListener = new SavePictureToAoIListener() {

			@Override
			public void updateListener(final int id, final BufferedImage subImage) {
				final int listID = getIndex(id);
				if (listID > 0) {
					final Picture object = (Picture)modelNoTargets.getTarget(listID-1);
					if (object != null) {

						byte[] byteImage;
						try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
							ImageIO.write(subImage, "jpg", baos);
							baos.flush();
							byteImage = baos.toByteArray();
						} catch (final IOException iox) {
							byteImage = new byte[0];
						}

						// calculate location
						final LocationWp location = new LocationWp(listID + 100.0, listID + 50.0);

						final AreaOfInterestMessage aoi;
						if (object.getFlightType().toString().equals(FlightType.HIGH.toString())) {
							aoi = new AreaOfInterestMessage(new ByteImage(byteImage), location,
									TargetType.AOI.toString(), PictureCategoryType.AOIHF.toString());
							wayPoints.add(location);
						} else {
							aoi = new AreaOfInterestMessage(new ByteImage(byteImage), location,
									TargetType.AOI.toString(), PictureCategoryType.AOILF.toString());
						}
						synchronized (SearchAreaViewerController.class) {
							helpHandleSubscription(aoi, RESULTS);
						}
					}
				}
			}
		};
	}

	/**
	 * Add save classification listener.
	 */
	private void addSaveClassificationListener() {
		saveClassificationListener = new SaveClassificationListener() {

			@Override
			public void updateListener(final int id, final String targetType) {
				final int listID = getIndex(id);
				if (listID > 0) {
					final SearchAreaTarget object = model.getTarget(listID - 1);
					if (object != null) {

						final PictureCategoryType pictureType;
						final SearchAreaTargetVisitor visitor = getVisitor(PictureCategoryType.ACTIONABLEINTELL.toString());
						isActionable = visitor.getIDList().isEmpty();
						if (!isActionable) {
							pictureType = PictureCategoryType.SEARCHAREA;
						} else {
							pictureType = PictureCategoryType.ACTIONABLEINTELL;
						}

						final SearchAreaTarget newObject;
						final double latitude = Double.parseDouble(view.getLatitutde());
						final double longitude = Double.parseDouble(view.getLongitude());
						final LocationWp location = new LocationWp(latitude, longitude);

						if (targetType.equals(TargetType.QRC.toString())) {
							newObject = new QRCTarget(object.getID(), object.getPicturePath(), location,
									targetType, pictureType.toString(), view.getDecodedMessage());
						} else if (targetType.equals(TargetType.STANDARD.toString())) {
							newObject = new StandardTarget(object.getID(), object.getPicturePath(), location,
									targetType, pictureType.toString(), view.getLetter(), view.getBGColor(),
									view.getFGColor(), view.getOrientation(), view.getShape());
						} else {
							newObject = null;
						}
						model.setTarget(listID - 1, newObject);
						pictureListener.updateListener(currentCategory);
					}
				}
			}
		};
	}

	/**
	 * Add flight change listener.
	 */
	private void addFlightChangeListener() {
		flightChangeListener = new FlightChangeListener() {

			@Override
			public void updateListener(final FlightType flightType, final boolean isStarted) {
				final GroundStationClient client = GroundStationClient.getInstance();
				log.debug("way points size: " + wayPoints.size());
				final VerifyFlightMessage message = new VerifyFlightMessage(flightType.toString(), isStarted, wayPoints);
				client.sendAction(ControlMessage.Service.SEARCHAREA, "FLIGHT", message);
			}
		};
	}

	/**
	 * Add listener.
	 */
	private void addListener() {
		addCharacteristicsListener();
		addPictureListener();
		addSaveFileListener();
		addSavePictureToAoIListener();
		addSaveClassificationListener();
		addFlightChangeListener();

		this.view.setCharacteristicsListener(characteristicsListener);
		this.view.setSaveFileListener(saveFileListener);
		this.view.setPictureListListener(pictureListener);
		this.view.setSavePictureToAoIListener(savePictureToAoIListener);
		this.view.setSaveClassificationListener(saveClassificationListener);
		this.view.setFlightChangeListener(flightChangeListener);
	}

	/**
	 * Returns the index of the picture.
	 * @param id picture id
	 * @return index of the picture
	 */
	private int getIndex(final int id) {
		final int idListSize;
		synchronized (idList) {
			idListSize = idList.size();
		}
		final int listID;
		if (id >= 0 && id < idListSize) {
			synchronized (idList) {
				listID = idList.get(id);
			}
		} else {
			listID = -1;
		}
		return listID;
	}

	/**
	 * Returns the visitor.
	 * @param listType current list type
	 * @return visitor
	 */
	private SearchAreaTargetVisitor getVisitor(final String listType) {
		final SearchAreaTargetVisitor visitor = new SearchAreaTargetVisitor(listType);
		if (listType.equals(PictureCategoryType.NORESULTS.toString())) {
			for (final SearchAreaTarget object : modelNoTargets.getAllPicturesWithoutTargets()) {
				object.accept(visitor);
			}
		} else {
			for (final SearchAreaTarget object : model.getAllTargets()) {
				object.accept(visitor);
			}
		}
		return visitor;
	}

	/**
	 * Returns the image as byte array.
	 * @param picturePath picture path
	 * @return image as byte array
	 */
	public ByteImage getByteImage(final String picturePath) {
		return loadImageAsByte(picturePath);
	}
}