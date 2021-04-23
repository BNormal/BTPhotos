package com.utils;

import java.awt.image.BufferedImage;

public class FilteredImage {
	private BufferedImage original;
	private BufferedImage filtered;
	private int selectedFilter = 0;

	public FilteredImage(BufferedImage image) {
		filtered = original = image;
	}
	
	public void setOriginalImage(BufferedImage image) {
		original = image;
		setSelectedFilter(selectedFilter);
	}
	
	public BufferedImage getOriginalImage() {
		return original;
	}
	
	public BufferedImage getFilteredImage() {
		return filtered;
	}
	
	public int getSelectedFilter() {
		return selectedFilter;
	}

	public void setSelectedFilter(int selectFilter) {
		this.selectedFilter = selectFilter;
		filtered = Utils.getFilter(original, Filter.values()[selectFilter]);
	}
}
