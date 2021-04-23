package com.cards;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import edsdk.api.CanonCamera;
import edsdk.utils.CanonConstants.EdsAv;
import edsdk.utils.CanonConstants.EdsImageQuality;
import edsdk.utils.CanonConstants.EdsSaveTo;
import edsdk.utils.CanonConstants.EdsTv;

public class ExternalCamera extends Camera {

	private CanonCamera camera;
	
	public ExternalCamera(CanonCamera camera) {
		super(false);
		this.camera = camera;
		initialize();
	}
	
	public void initialize() {
		if (!isConnected)
			isConnected = camera.openSession();
		if (isConnected) {
			
		} else {
			//exit();
		}
	}
	
	@Override
	public String getName() {
		if (isConnected)
			return camera.getProductName();
		else
			return super.getName();
	}
	
	public String[] getShutterSpeeds() {
		String[] shutterSpeeds = new String[camera.getAvailableShutterSpeeds().length];
		for (int i = 0; i < shutterSpeeds.length; i++) {
			shutterSpeeds[i] = camera.getAvailableShutterSpeeds()[i].description();
		}
		return shutterSpeeds;
	}
	
	public void setShutterSpeed(int id) {
		int size = camera.getAvailableShutterSpeeds().length;
		if (id >= size || id < 0)
			return;
		camera.setShutterSpeed(camera.getAvailableShutterSpeeds()[id]);
	}
	
	public int getShutterSpeedId() {
		int value = camera.getShutterSpeed().value();
		int i = 0;
		for (EdsTv shutterSpeed : camera.getAvailableShutterSpeeds()) {
			if (shutterSpeed.value() == value) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public String[] getApertures() {
		String[] apertures = new String[camera.getAvailableApertureValues().length];
		for (int i = 0; i < apertures.length; i++) {
			apertures[i] = camera.getAvailableApertureValues()[i].description();
		}
		return apertures;
	}
	
	public void setApertures(int id) {
		int size = camera.getAvailableApertureValues().length;
		if (id >= size || id < 0)
			return;
		camera.setApertureValue(camera.getAvailableApertureValues()[id]);
	}
	
	public int getApertureId() {
		int value = camera.getApertureValue().value();
		int i = 0;
		for (EdsAv aperture : camera.getAvailableApertureValues()) {
			if (aperture.value() == value) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public String[] getImageQualities() {
		String[] qualities = new String[camera.getAvailableImageQualities().length];
		for (int i = 0; i < qualities.length; i++) {
			qualities[i] = camera.getAvailableImageQualities()[i].description();
		}
		return qualities;
	}
	
	public void setImageQuality(int id) {
		int size = camera.getAvailableImageQualities().length;
		if (id >= size || id < 0)
			return;
		camera.setImageQuality(camera.getAvailableImageQualities()[id]);
	}
	
	public int getCurrentQualityId() {
		int value = camera.getImageQuality().value();
		int i = 0;
		for (EdsImageQuality quality : camera.getAvailableImageQualities()) {
			if (quality.value() == value) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public BufferedImage takePicture() {
		if (!ready())
			return null;
		takingPicture = true;
		BufferedImage image = null;
		File file = new File("");
		try {
			String path = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
			file = new File(path + "\\LAST_IMAGE_TAKEN.JPG");
			file = camera.shoot(EdsSaveTo.kEdsSaveTo_Host, 5, file)[0];
			System.out.println("File: " + file.getAbsolutePath());
			image = ImageIO.read(file);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		takingPicture = false;
		return image;
	}
	
	public boolean ready() {
		if (camera == null || !isConnected || takingPicture)
			return false;
		return true;
	}
	
	public void close() {
		//camera.closeSession();
	}
	
	@SuppressWarnings("static-access")
	public void exit() {
		camera.close();
		camera = null;
	}
}
