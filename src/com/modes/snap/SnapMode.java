package com.modes.snap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.MainMenu;
import com.components.ImageBlock;
import com.components.ImageButton;
import com.components.RadioButton;
import com.modes.ModeInterface;
import com.modes.Page;
import com.modes.ModeWindow;
import com.utils.Filter;
import com.utils.FilteredImage;
import com.utils.GlobalKeyListener;
import com.utils.ImageManager;
import com.utils.Utils;

import java.awt.Color;
import javax.swing.JLabel;

import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SnapMode extends ModeInterface {
	
	private ImageBlock photoPanel;
	private RadioButton[] filterButtons = new RadioButton[5];
	private SnapThread processThread;
	private ButtonGroup filterGroup;
	private FilteredImage mainImage;

	public SnapMode(MainMenu mm, ModeWindow ui) {
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
					takingPicture = true;
					processThread = new SnapThread();
					processThread.start();
				}
			};
			addGlobalKeyListener(gkl);
		}
		photoPanel = new ImageBlock();
		photoPanel.setFont(Utils.bubbleFont.getFont());
		photoPanel.setHorizontalTextPosition(SwingConstants.CENTER);
		photoPanel.setHorizontalAlignment(SwingConstants.CENTER);
		photoPanel.setForeground(Utils.textColor);
		photoPanel.setBackground(Utils.secondColor);
		photoPanel.setBounds(295, 20, 675, 450);
		background.add(photoPanel);
		
		filterGroup = new ButtonGroup();
		for (int i = 0; i < filterButtons.length; i++) {
			filterButtons[i] = new RadioButton(null);
			filterButtons[i].setVisibleBG(true);
			filterButtons[i].setHighlighted(false);
			filterButtons[i].setEnabled(false);
			filterButtons[i].setBackground(Utils.secondColor);
			filterButtons[i].setBounds(10 + 259 * i, 528, 215, 132);
			filterButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (images.size() > 0) {
						int index = Utils.getSelectedId(filterGroup);
						Thread actionChangeThread = new Thread() {
							@Override
							public void run() {
								setEnabledFilters(false);
								photoPanel.setText("Rendering Photo...");
								photoPanel.setImage(null);
								photoPanel.setOpaque(true);
								mainImage.setSelectedFilter(index);
								images.get(0).setSelectedFilter(index);
								BufferedImage image = mainImage.getFilteredImage();
								photoPanel.setImage(image);
								photoPanel.setOpaque(false);
								photoPanel.setText("");
								setEnabledFilters(true);
							}
						};
						actionChangeThread.start();
					}
				}
			});
			background.add(filterButtons[i]);
			filterGroup.add(filterButtons[i]);
		}
		
		ImageBlock logo = new ImageBlock("/com/images/logo2.png");
		logo.setOpaque(false);
		logo.setBackground(Utils.secondColor);
		logo.setBounds(10, 20, 175, 50);
		background.add(logo);
		
		ImageButton nextButtonPanel = new ImageButton(MainMenu.class.getResource("images/next.png"));
		nextButtonPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (images.size() > 0) {
					userInterface.setImage(images.get(0).getFilteredImage());
					changeScene(Page.SNAPEMAIL);
				}
					//display warning missing image
			}
		});
		nextButtonPanel.setFocusable(false);
		nextButtonPanel.setBackground(Utils.secondColor);
		nextButtonPanel.setBounds(1080, 160, 80, 200);
		background.add(nextButtonPanel);
		
		JLabel lblTitle = new JLabel("PHOTO FILTERS");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(Utils.bubbleFont.getFont());
		lblTitle.setBounds(497, 483, 270, 35);
		background.add(lblTitle);
		setVisible(true);
	}
	
	public class SnapThread extends Thread {
		@Override
		public void run() {
			photoPanel.setText("Taking photo...");
			photoPanel.setImage(null);
			photoPanel.setOpaque(true);
			BufferedImage image = mainMenu.getCS().getSnap();
			if (image == null) {
				photoPanel.setText("Failed to take photo");
				return;
			}
			try {
				String location = ImageManager.saveLocation + "/raw/";
				ImageManager.locationExists(location, true);
				File outputfile = new File(location + "IMG_" + String.format("%04d", ImageManager.getImageId(location)) + ".jpg");
				ImageIO.write(image, "jpg", outputfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			images.clear();
			images.add(new FilteredImage(image));
			
			mainImage = new FilteredImage(Utils.copyImage(Utils.resize(image, photoPanel.getWidth(), photoPanel.getHeight())));
			
			BufferedImage filteredImage = Utils.copyImage(Utils.resize(image, filterButtons[0].getWidth(), filterButtons[0].getHeight()));
			/******* original *******/
			filterButtons[0].setEnabled(true);
			filterButtons[0].setSelected(true);
			filterButtons[0].setImage(filteredImage);
			
			/******* gray scale *******/
			filterButtons[1].setEnabled(true);
			filterButtons[1].setImage(Utils.getFilter(filteredImage, Filter.grayscale));

			/******* chrome *******/
			filterButtons[2].setEnabled(true);
			filterButtons[2].setImage(Utils.getFilter(filteredImage, Filter.chrome));

			/******* halftone *******/
			filterButtons[3].setEnabled(true);
			filterButtons[3].setImage(Utils.getFilter(filteredImage, Filter.halftone));
			
			/******* contrast *******/
			filterButtons[4].setEnabled(true);
			filterButtons[4].setImage(Utils.getFilter(filteredImage, Filter.contrast));

			photoPanel.setImage(mainImage.getFilteredImage());
			photoPanel.setOpaque(false);
			
			photoPanel.setText("");
			takingPicture = false;
		}
	}
	
	public void setEnabledFilters(boolean enabled) {
		for (int i = 0; i < filterButtons.length; i++) {
			filterButtons[i].setEnabled(enabled);
		}
	}
	
	@Override
	public void reset() {
		images.clear();
		photoPanel.setOpaque(true);
		photoPanel.setImage(null);
		filterGroup.clearSelection();
		for (int i = 0; i < filterButtons.length; i++)
			filterButtons[i].clear();
	}
}
