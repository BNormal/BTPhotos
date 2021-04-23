package com.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.border.Border;

import com.jhlabs.image.ChromeFilter;
import com.jhlabs.image.ContrastFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.HalftoneFilter;

public class Utils {
	
	public static Color backgroundColor = Color.decode("#25274D");
	public static Color secondColor = Color.decode("#464866");
	public static Color thirdColor = Color.decode("#AAABB8");
	public static Color highlightColor = Color.decode("#2E9CCA");
	public static Color fifthColor = Color.decode("#29648A");
	public static Color textColor = Color.decode("#FFFFFF");
	public static CustomFont bubbleFont = new CustomFont("fonts/ALBAS.ttf", 25f);
	public static Border defaultBorder = BorderFactory.createDashedBorder(Utils.secondColor, 2, 4, 2, true);
	
	/**
	 * @param source the original image
	 * @return returns a cloned BufferedImage of the source
	 */
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	/**
	 * @param img the original image at full scale
	 * @param newWidth the new width of the image
	 * @param newHeight the new height of the image
	 * @return returns the transformed image
	 */
	public static BufferedImage resize(BufferedImage img, int newWidth, int newHeight) { 
	    Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
	/**
	 * @param image the original image needing a filter
	 * @param filter the type of filter to transform the image {@code FILTER.grayscale}
	 * @return returns the transformed image dependent on it's filter type
	 */
	public static BufferedImage getFilter(BufferedImage image, Filter filter) {
		if (filter == Filter.grayscale) {
			GrayscaleFilter gsf = new GrayscaleFilter();
			return gsf.filter(image, null);
		} else if (filter == Filter.chrome) {
			ChromeFilter cf = new ChromeFilter();
			return cf.filter(image, null);
		} else if (filter == Filter.halftone) {
			BufferedImage mask = null;
			try {
				mask = ImageIO.read(Utils.class.getResource("/com/images/HalftoneFilter2.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			HalftoneFilter htf = new HalftoneFilter();
			htf.setMask(mask);
			htf.setInvert(true);
			return htf.filter(image, null);
		} else if (filter == Filter.contrast) {
			ContrastFilter bf = new ContrastFilter();
			bf.setContrast((float) 1.5);
			return bf.filter(image, null);
		}
		return image;
	}
	
	/**
	 * @param buttonGroup the list of objects inside this group
	 * @return returns the selected object from a list of objects
	 */
	public static int getSelectedId(ButtonGroup buttonGroup) {
		int id = 0;
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected())
				return id;
			id++;
		}

		return -1;
	}
	
	/**
	 * @param colour takes a Color object and converts it into a string
	 * @return Color as a hex color as a string type
	 */
	public final static String toHexString(Color colour) {
		String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
		if (hexColour.length() < 6) {
			hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
		}
		return "#" + hexColour;
	}
	
	/**
	 * @param str takes the string and formats it
	 * @return Returns a string where the first letter of every word is capitalized
	 */
	public static String capitalizeWord(String str) {
		str = str.toLowerCase();
		String words[] = str.split("\\s");
		String capitalizeWord = "";
		for (String w : words) {
			String first = w.substring(0, 1);
			String afterfirst = w.substring(1);
			capitalizeWord += first.toUpperCase() + afterfirst + " ";
		}
		return capitalizeWord.trim();
	}
}
