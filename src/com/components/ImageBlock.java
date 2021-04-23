package com.components;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.utils.CustomFont;
import com.utils.Utils;

@SuppressWarnings("serial")
public class ImageBlock extends JLabel {

	private int id = -1;

	private BufferedImage image = null;
	
	public ImageBlock() {
		setOpaque(true);
		image = null;
	}
	
	public ImageBlock(String path) {
		setOpaque(true);
		try {
			image = ImageIO.read(this.getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setIcon(new ImageIcon(image));
	}
	
	public void setCustomFont(CustomFont cf) {
		setFont(cf.getFont());
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		if (image != null)
			setIcon(new ImageIcon(Utils.copyImage(Utils.resize(image, width, height))));
	}
	
	public void setImage(BufferedImage image) {
		if (image == null)
			setIcon(null);
		else
			setIcon(new ImageIcon(Utils.copyImage(Utils.resize(image, getWidth(), getHeight()))));
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
