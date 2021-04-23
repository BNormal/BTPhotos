package com.utils;

import java.awt.Point;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import com.components.CollageLayout;

public class ImageManager {
	public static String saveLocation = ".";
	public static boolean updating = false;
	public static int imageId = 1;
	public static ArrayList<CollageLayout> layouts = new ArrayList<CollageLayout>();

	private static ArrayList<String> images = new ArrayList<String>();
	
	static {
		File directory = new File(saveLocation);
		saveLocation = directory.getAbsolutePath();
		saveLocation = saveLocation.substring(0, saveLocation.length() - 2);
	}
	
	public static void loadImages() {
		images.clear();
		File directory = new File(saveLocation);
        File[] fileList = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg");
            }
        });
        for (File file : fileList) {
        	images.add(file.getName());
        	//System.out.println(file.getName());
        }
	}
	
	public static boolean updateImages() {
		if (needsUpdate()) {
			updating = true;
			loadImages();
			updating = false;
			return true;
		}
		return false;
	}
	
	public static boolean needsUpdate() {
		int size = new File(saveLocation).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg");
            }
        }).length;
		if (size != images.size())
			return true;
		return false;
	}
	
	public static ArrayList<String> getImages() {
		return images;
	}
	
	public static int getSize() {
		return images.size();
	}
	
	public static boolean locationExists(String location, boolean create) {
		File dir = new File(location);
		if(dir.exists()){
			return true;
		} else if (create) {
			dir.mkdir();
		}
		return false;
	}
	
	public static int getImageId(String location) {
		String name = location + "/IMG_" + String.format("%04d", imageId) + ".jpg";
		//System.out.println(name);
		File file = new File(name);
		if (file.exists()) {
			imageId++;
			return getImageId(location);
		}
		/*File dir = new File(location);
		int id = 0;
	    for (File f : dir.listFiles()) {
	    	
	        String name = f.getName();
	        name = name.replaceAll("IMG_", "");
	        System.out.println(name);
	    }*/
		imageId++;
		return imageId - 1;
	}
	
	public static void loadLayouts() {
		//new Point(58, 58), new Point(436, 270), new Point(940, 130), new Point(1200, 440), new Point(890, 665), new Point(215, 505)
		layouts.add(new CollageLayout(new Point(0, 0), new Point(641, 0), new Point(1282, 0), new Point(0, 720), new Point(641, 720), new Point(1282, 720)));
		layouts.add(new CollageLayout(new Point(0, 0), new Point(641, 360), new Point(1282, 0), new Point(0, 720), new Point(1282, 720)));
		layouts.add(new CollageLayout(new Point(58, 58), new Point(215, 505), new Point(436, 270), new Point(1200, 440), new Point(940, 130), new Point(890, 665)));
	}

}
