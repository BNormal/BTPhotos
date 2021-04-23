package com.components;

import java.awt.GraphicsDevice;
import com.modes.WindowInterface;
import com.utils.ImageManager;

public class Monitor {
	
	private int id;
	private GraphicsDevice device;
	private int windowID = -1;
	private WindowInterface window = null;
	private String path = ImageManager.saveLocation + "\\raw\\";
	
	public Monitor(GraphicsDevice device, int type) {
		this.device = device; // monitor attached to slide
		this.id = Integer.parseInt(device.getIDstring().charAt(device.getIDstring().length() - 1) + "") + 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GraphicsDevice getDevice() {
		return device;
	}

	public void setDevice(GraphicsDevice device) {
		this.device = device;
	}

	public int getWindowID() {
		return windowID;
	}

	public void setWindowID(int windowID) {
		this.windowID = windowID;
	}

	public WindowInterface getWindow() {
		return window;
	}

	public void setWindow(WindowInterface window) {
		this.window = window;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return device.getDisplayMode().getWidth() + " x " + device.getDisplayMode().getHeight() + ", Refresh Rate: " + device.getDisplayMode().getRefreshRate();
	}
}
