package com.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

import com.utils.Utils;

public class RadioButton extends JRadioButton {
	
	private static final long serialVersionUID = -7620090255292767618L;
	private BufferedImage image = null;
	boolean hovered = false;
	boolean clicked = false;
	boolean visibleBG = false;
	boolean highlighted = false;
	private ImageListener imageListener;
	private Color highlight = new Color(Utils.highlightColor.getRed(), Utils.highlightColor.getGreen(), Utils.highlightColor.getBlue(), 60);
	
	public RadioButton() {
		super();
		setOpaque(false);
		highlighted = true;
		BufferedImage bufimage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		setIcon(new ImageIcon(bufimage));
		imageListener = new ImageListener();
		addMouseListener(imageListener);
	}
	
	public RadioButton(URL loc) {
		super();
		try {
			setOpaque(false);
			highlighted = true;
			BufferedImage bufimage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			setIcon(new ImageIcon(bufimage));
			if (loc != null)
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
		Graphics2D g2 = (Graphics2D) g;
		if (visibleBG) {
			g2.setColor(getBackground());
			g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
		}
		if (image != null) {
			BufferedImage sprite = Utils.copyImage(image);
			// sprite = sprite.getScaledInstance(getWidth(), getHeight(),
			// Image.SCALE_DEFAULT);
			if (clicked && highlighted) {
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
		if (isSelected()) {
			g2.setColor(highlight);
			g2.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
		}
		if (isSelected()) {
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Utils.highlightColor);
			//g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			g2.setStroke(new BasicStroke(2));
		    g2.setRenderingHints(rh);
			g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
		}
	}
	
	public boolean isHovered() {
		return hovered;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		if (image != null && image.getWidth() == getWidth() && image.getHeight() == getHeight())
			this.image = image;
		else if (image != null)
			this.image = Utils.copyImage(Utils.resize(image, getWidth(), getHeight()));
		else
			this.image = image;
		repaint();
	}

	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

	public boolean isVisibleBG() {
		return visibleBG;
	}

	public void setVisibleBG(boolean visibleBG) {
		this.visibleBG = visibleBG;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean hasHighlight) {
		this.highlighted = hasHighlight;
	}

	public ImageListener getImageListener() {
		return imageListener;
	}

	public void setImageListener(ImageListener imageListener) {
		this.imageListener = imageListener;
	}
	
	public void clear() {
		image = null;
		setEnabled(false);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		setImage(image);
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
