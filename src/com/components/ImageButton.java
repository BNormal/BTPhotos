package com.components;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.utils.Utils;

public class ImageButton extends JPanel {
	
	private static final long serialVersionUID = -7620090255292767618L;
	private BufferedImage image;
	boolean hovered = false;
	boolean clicked = false;
	private ImageListener imageListener;

	public ImageButton(URL loc) {
		super();
		try {
			setOpaque(false);
			image = ImageIO.read(loc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageListener = new ImageListener();
		addMouseListener(imageListener);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage sprite = Utils.copyImage(Utils.resize(image, getWidth(), getHeight()));
		//sprite = sprite.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
		if (clicked) {
			float scaleFactor = 1.5f;
			RescaleOp op = new RescaleOp(scaleFactor, 0, null);
			sprite = op.filter(sprite, sprite);
		} else if (hovered) {
			float scaleFactor = 0.7f;
			RescaleOp op = new RescaleOp(scaleFactor, 0, null);
			sprite = op.filter(sprite, sprite);
		}
		g.drawImage(sprite, 0, 0, null);
	}
	
	public boolean isHovered() {
		return hovered;
	}

	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

	public ImageListener getImageListener() {
		return imageListener;
	}

	public void setImageListener(ImageListener imageListener) {
		this.imageListener = imageListener;
	}
	
	public class ImageListener extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			//hovered = true;
			//repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//hovered = false;
			//repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			clicked = true;
			repaint();
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			clicked = false;
			repaint();
		}
	}
}
