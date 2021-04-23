package com.components;

import java.awt.Point;

public class CollageLayout {

	private int size = 0;
	private Point[] locations;

	public CollageLayout(int size) {
		this.size = size;
		locations = new Point[size];
	}
	
	public CollageLayout(Point... locations) {
		this.locations = locations;
		this.size = locations.length;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public Point[] getLocations() {
		return locations;
	}

	public void setLocations(Point[] locations) {
		this.locations = locations;
	}
}
