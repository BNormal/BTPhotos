package com.modes.gif;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

import com.MainMenu;
import com.cards.Camera;
import com.components.ImageBlock;
import com.components.ImageButton;
import com.modes.ModeInterface;
import com.modes.Page;
import com.modes.ModeWindow;
import com.utils.AnimatedGifEncoder;
import com.utils.FilteredImage;
import com.utils.GlobalKeyListener;
import com.utils.ImageManager;
import com.utils.Utils;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class GifMode extends ModeInterface {
	
	private File gifImage = null;
	private JLabel lblPhoto;
	private JLabel lblMessage;
	private JLabel[] boxes = new JLabel[4];
	private GIFThread gifCreator = null;
	private boolean hasImage = false;

	public GifMode(MainMenu mm, ModeWindow ui) {
		super(mm, ui);
		initialize();
	}
	
	public void initialize() {
		if (gkl == null) {
			gkl = new GlobalKeyListener() {
				@Override
				public void takePicture() {
					if (mainMenu == null || takingPicture || userInterface.getCurrentScene() != userInterface.getMode())
						return;
					hasImage = false;
					takingPicture = true;
					gifCreator = new GIFThread();
					gifCreator.start();
				}
			};
			addGlobalKeyListener(gkl);
		}
		
		/* logo */
		ImageBlock logo = new ImageBlock("/com/images/logo2.png");
		logo.setOpaque(false);
		logo.setBackground(Utils.secondColor);
		logo.setBounds(10, 20, 175, 50);
		background.add(logo);
		
		lblPhoto = new JLabel("");
		lblPhoto.setBackground(Utils.secondColor);
		lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhoto.setHorizontalTextPosition(SwingConstants.CENTER);
		lblPhoto.setForeground(Utils.textColor);
		lblPhoto.setFont(Utils.bubbleFont.getFont());
		lblPhoto.setOpaque(true);
		lblPhoto.setBounds(295, 45, 675, 450);
		background.add(lblPhoto);
		
		lblMessage = new JLabel("");
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setHorizontalTextPosition(SwingConstants.CENTER);
		lblMessage.setForeground(Utils.textColor);
		lblMessage.setFont(Utils.bubbleFont.getFont());
		lblMessage.setOpaque(false);
		lblMessage.setBounds(295, 550, 675, 60);
		background.add(lblMessage);

		Color boxColor = new Color(Utils.secondColor.getRed(), Utils.secondColor.getGreen(), Utils.secondColor.getBlue(), Utils.secondColor.getAlpha() / 4);
		for (int i = 0; i < boxes.length; i++) {
			boxes[i] = new JLabel();
			boxes[i].setHorizontalAlignment(SwingConstants.CENTER);
			boxes[i].setBackground(boxColor);
			boxes[i].setOpaque(true);
			boxes[i].setBounds(295 + 171 * i, 506, 163, 14);
			background.add(boxes[i]);
		}
		
		ImageButton nextButtonPanel = new ImageButton(MainMenu.class.getResource("images/next.png"));
		nextButtonPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (gifImage != null && hasImage && !takingPicture) {
					userInterface.setGifImage(gifImage);
					changeScene(Page.GIFEMAIL);
				}
					//display warning missing image
			}
		});
		nextButtonPanel.setFocusable(false);
		nextButtonPanel.setBackground(Utils.secondColor);
		nextButtonPanel.setBounds(1080, 160, 80, 200);
		background.add(nextButtonPanel);
		
	}
	
	@Override
	public void reset() {
		images.clear();
		userInterface.setGifImage(null);
		gifImage = null;
		lblPhoto.setIcon(null);
		lblPhoto.setOpaque(true);
		Color boxColor = new Color(Utils.secondColor.getRed(), Utils.secondColor.getGreen(), Utils.secondColor.getBlue(), Utils.secondColor.getAlpha() / 4);
		for (int i = 0; i < boxes.length; i++)
			boxes[i].setBackground(boxColor);
		hasImage = false;
	}
	
	public void lostAndFoundSeedOnTheGroundSpeedOSonic(boolean test) {
		String location = ImageManager.saveLocation + "/gif/";
		ImageManager.locationExists(location, true);
		String path = location + "IMG_" + String.format("%04d", ImageManager.getImageId(location)) + ".gif";
		gifImage = new File(path);
		try {
			AnimatedGifEncoder encoder = new AnimatedGifEncoder();
			encoder.start(new FileOutputStream(gifImage));
			encoder.setDelay(1000);   // 1 frame per second
			encoder.setRepeat(0);
			encoder.setQuality(1);
			for (FilteredImage image : images) {
				encoder.addFrame(image.getOriginalImage());
			}
			encoder.finish(); 
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			lblPhoto.setIcon(new ImageIcon(gifImage.toURI().toURL()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		hasImage = true;
	}
	
	@SuppressWarnings("static-access")
	public void nextPictureMessage(Thread thread, String message, int seconds) throws InterruptedException {
		for (int i = 0; i < 1000 * seconds; i += 100) {
			lblMessage.setText(message + " " + ((1000 * seconds - i) / 1000 + 1));
			thread.sleep(100);
		}
		lblMessage.setText("");
		thread.sleep(100);
	}
	
	public class GIFThread extends Thread {
		
		@Override
		public void run() {
			Camera camera = mainMenu.getCS().getCurrentCam();
			if (camera == null) {
				takingPicture = false;
				return;
			}
			images.clear();
			//lblPhoto.setText("Starting");
			if (camera.ready()) {
				try {
					BufferedImage oneFramePhoto = null;
					ImageIcon icon = null;
					
					nextPictureMessage(this, "Taking pictures in", 5);
					lblMessage.setText("Taking picture...");
					oneFramePhoto = Utils.copyImage(Utils.resize(camera.takePicture(), lblPhoto.getWidth(), lblPhoto.getHeight()));
					icon = new ImageIcon(oneFramePhoto);
					lblPhoto.setIcon(icon);
					lblPhoto.setOpaque(false);
					images.add(new FilteredImage(oneFramePhoto));
					boxes[0].setBackground(Utils.secondColor);
					nextPictureMessage(this, "Taking next picture in", 5);

					oneFramePhoto = Utils.copyImage(Utils.resize(camera.takePicture(), lblPhoto.getWidth(), lblPhoto.getHeight()));
					icon = new ImageIcon(oneFramePhoto);
					lblPhoto.setIcon(icon);
					images.add(new FilteredImage(oneFramePhoto));
					boxes[1].setBackground(Utils.secondColor);
					nextPictureMessage(this, "Taking next picture in", 5);

					oneFramePhoto = Utils.copyImage(Utils.resize(camera.takePicture(), lblPhoto.getWidth(), lblPhoto.getHeight()));
					icon = new ImageIcon(oneFramePhoto);
					lblPhoto.setIcon(icon);
					images.add(new FilteredImage(oneFramePhoto));
					boxes[2].setBackground(Utils.secondColor);
					nextPictureMessage(this, "Taking next picture in", 5);

					oneFramePhoto = Utils.copyImage(Utils.resize(camera.takePicture(), lblPhoto.getWidth(), lblPhoto.getHeight()));
					icon = new ImageIcon(oneFramePhoto);
					lblPhoto.setIcon(icon);
					images.add(new FilteredImage(oneFramePhoto));
					boxes[3].setBackground(Utils.secondColor);
					
					lblMessage.setText("Generating GIF...");
					lostAndFoundSeedOnTheGroundSpeedOSonic(false);
					lblMessage.setText("Your GIF is done!");
					lblPhoto.setOpaque(false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			takingPicture = false;
		}
	}
}
