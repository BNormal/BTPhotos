package com.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CustomFont {
	private Font font = null;
	
	public CustomFont(String location, float size) {
		try {
			URL url = new File(location).toURI().toURL();
			font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}

		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		genv.registerFont(font);
		font = font.deriveFont(size);
	}

	public Font getFont() {
		return font;
	}
	
}
