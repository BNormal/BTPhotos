package com.modes;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.utils.CustomFont;
import com.utils.ImageManager;
import com.utils.Utils;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;

@SuppressWarnings("serial")
public class ProcessingInterface extends JPanel {

	private int dotCount = 9;
	private boolean sending = true;
	private ModeWindow ui;
	private TickThread tickThread;
	private ProcessThread processThread;
	private JLabel lblDots;
	private JLabel lblSend;
	/**
	 * Create the panel.
	 */
	public ProcessingInterface(ModeWindow ui) {
		this.ui = ui;
		initialize();
	}

	public void start() {
		tickThread = new TickThread();
		tickThread.start();
	}
	
	private void initialize() {
		setBounds(100, 100, 1280, 720);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));
		JPanel panelBackground = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				BufferedImage sprite = null;
				try {
					sprite = Utils.copyImage(Utils.resize((BufferedImage) ImageIO.read(this.getClass().getResource("/com/images/background.png")), getWidth(), getHeight()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(sprite, 0, 0, null);
			}
		};
		add(panelBackground);
		panelBackground.setLayout(null);

		CustomFont font = new CustomFont("fonts/ALBAS.ttf", 40f);
		
		lblSend = new JLabel("Emailing your photo now!");
		lblSend.setForeground(Color.WHITE);
		lblSend.setHorizontalAlignment(SwingConstants.CENTER);
		lblSend.setBounds(364, 198, 494, 162);
		lblSend.setFont(font.getFont());
		panelBackground.add(lblSend);
		
		lblDots = new JLabel(". . . . . . . . .");
		lblDots.setForeground(Color.WHITE);
		lblDots.setHorizontalAlignment(SwingConstants.CENTER);
		lblDots.setFont(font.getFont());
		lblDots.setBounds(364, 358, 494, 65);
		panelBackground.add(lblDots);
	}
	
	public String getSaveLocation() {
		switch(ui.getMode()) {
			case GIFMODE: return ImageManager.saveLocation + "\\gif\\";
			case BOOTHMODE: return ImageManager.saveLocation + "\\collage\\";
			default: return ImageManager.saveLocation + "\\filtered\\";
		}
	}
	
	public String getSaveType() {
		switch(ui.getMode()) {
			case GIFMODE: return ".gif";
			default: return ".jpg";
		}
	}
	
	public class ProcessThread extends Thread {
		@Override
		public void run() {
			String type = getSaveType();
			String location = getSaveLocation();
			ImageManager.locationExists(location, true);
			String fileName = "IMG_" + String.format("%04d", (ImageManager.getImageId(location) - (type.equals(".jpg") ? 0 : 1))) + type;
			File outputfile = new File(location + fileName);
			if (type.equals(".jpg")) {
				try {
					ImageIO.write(ui.getImage(), type.substring(1), outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ui.getEmail().addAttachment(location + fileName);
			ui.getEmail().send();
			sending = false;
		}
	}
	
	public class TickThread extends Thread {
		@Override
		public void run() {
			processThread = new ProcessThread();
			processThread.start();
			while (sending || dotCount >= 9) {
				try {
					sleep(1000);
					String dots = "";
					dotCount -= 2;
					if (dotCount < 0)
						dotCount = 9;
					for (int i = 0; i < dotCount; i++) {
						if (i == dotCount - 1)
							dots += ".";
						else
							dots += ". ";
					}
					lblDots.setText(dots);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lblSend.setText("Photo sent!");
			lblDots.setText("");
			try {
				sleep(2000);
				ui.resetMode();
				ui.setImage(null);
				ui.changeScene(ui.getMode());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
