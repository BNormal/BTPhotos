package com.cards;

import java.awt.image.BufferedImage;

public abstract class Camera {
	
	protected boolean isConnected;
	protected boolean takingPicture;

	public Camera() {
		isConnected = false;
		takingPicture = false;
	}
	
	public Camera(boolean isConnected) {
		this.isConnected = isConnected;
		takingPicture = false;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public String getName() {
		return "No camera connected";
	}
	
	public abstract String[] getImageQualities();
	
	public abstract void setImageQuality(int id);
	
	public abstract int getCurrentQualityId();

	public abstract BufferedImage takePicture();
	
	public abstract boolean ready();
	
	public abstract void close();
}
