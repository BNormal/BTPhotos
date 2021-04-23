package com.cards;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import com.github.sarxos.webcam.Webcam;

public class InternalCamera extends Camera {
	
	private Webcam camera;
	
	public InternalCamera(Webcam camera) {
		super(true);
		this.camera = camera;
		if (camera != null) {
			camera.setViewSize(camera.getViewSizes()[camera.getViewSizes().length - 1]);
		}
	}
	
	@Override
	public String getName() {
		if (isConnected)
			return camera.getName();//.getProductName();
		else
			return super.getName();
	}
	
	public String[] getImageQualities() {
		String[] qualities = new String[camera.getViewSizes().length];
		for (int i = 0; i < qualities.length; i++) {
			qualities[i] = getPixelDescription(camera.getViewSizes()[i]);
		}
		return qualities;
	}
	
	public void setImageQuality(int id) {
		int size = camera.getViewSizes().length;
		if (id >= size || id < 0)
			return;
		camera.setViewSize(camera.getViewSizes()[id]);
	}
	
	public String getPixelDescription(Dimension d) {
		double megaPixels = (d.getWidth() * d.getHeight()) / 1000000.0;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		return df.format(megaPixels) + " MP - " + d.getWidth() + " x " + d.getHeight();
	}
	
	public int getCurrentQualityId() {
		int width = (int) camera.getViewSize().getWidth();
		int height = (int) camera.getViewSize().getHeight();
		int i = 0;
		for (Dimension d : camera.getViewSizes()) {
			if (d.getWidth() == width && d.getHeight() == height) {
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
		camera.open();
		BufferedImage image = camera.getImage();
		camera.close();
		takingPicture = false;
		return image;
	}
	
	public boolean ready() {
		if (camera == null || camera.isOpen() || camera.getLock().isLocked() || takingPicture)
			return false;
		return true;
	}
	
	public void close() {
		camera.close();
	}
}

