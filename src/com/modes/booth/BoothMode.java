package com.modes.booth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.MainMenu;
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
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class BoothMode extends ModeInterface {
	
	private JLabel lblMainPhoto;
	private BoothThread boothThread;
	private int selectedImage = -1;
	private ButtonGroup imageGroup;
	private ButtonGroup filterGroup;
	private RadioButton[] imageButtons = new RadioButton[6];
	private RadioButton[] filterButtons = new RadioButton[5];
	
	public BoothMode(MainMenu mm, ModeWindow ui) {
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
					boothThread = new BoothThread();
					boothThread.start();
				}
			};
			addGlobalKeyListener(gkl);
		}
		
		lblMainPhoto = new JLabel("");
		lblMainPhoto.setBackground(Utils.secondColor);
		lblMainPhoto.setHorizontalAlignment(SwingConstants.CENTER);
		lblMainPhoto.setHorizontalTextPosition(SwingConstants.CENTER);
		lblMainPhoto.setForeground(Utils.textColor);
		lblMainPhoto.setFont(Utils.bubbleFont.getFont());
		lblMainPhoto.setOpaque(true);
		lblMainPhoto.setBounds(455, 11, 675, 450);
		background.add(lblMainPhoto);
		
		ImageButton btnNext = new ImageButton(MainMenu.class.getResource("images/next.png"));
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (images.size() >= 6) {
					CollagePage cp = userInterface.getCollagePage();
					cp.reset();
					for(int i = 0; i < images.size(); i++)
						cp.addImage(images.get(i).getFilteredImage());
					changeScene(Page.COLLAGEPAGE);
				}
					//display warning missing image
			}
		});
		btnNext.setFocusable(false);
		btnNext.setBackground(Utils.secondColor);
		btnNext.setBounds(1156, 161, 80, 200);
		background.add(btnNext);
		
		JPanel panelImages = new JPanel();
		Border border = BorderFactory.createDashedBorder(Utils.secondColor, 2, 4, 2, true);
		panelImages.setBorder(border);
		panelImages.setBackground(Utils.secondColor);
		panelImages.setOpaque(false);
		panelImages.setBounds(50, 11, 383, 450);
		background.add(panelImages);
		panelImages.setLayout(null);
		
		imageGroup = new ButtonGroup();
		for (int i = 0; i < imageButtons.length; i++) {
			imageButtons[i] = new RadioButton();
			imageButtons[i].setOpaque(true);
			imageButtons[i].setHighlighted(false);
			imageButtons[i].setEnabled(false);
			imageButtons[i].setBackground(Utils.secondColor);
			imageButtons[i].setBounds(10 + (195 * (i % 2)), 11 + (159 * (i / 2)), 168, 112);
			imageButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					selectedImage = Utils.getSelectedId(imageGroup);
					if (images.size() >= 6) {
						Thread actionChangeThread = new Thread() {
							@Override
							public void run() {
								setEnabledButtons(false);
								lblMainPhoto.setText("Rendering Photo...");
								lblMainPhoto.setIcon(null);
								lblMainPhoto.setOpaque(true);
								
								BufferedImage image = Utils.copyImage(Utils.resize(images.get(selectedImage).getOriginalImage(), filterButtons[0].getWidth(), filterButtons[0].getHeight()));
								filterButtons[images.get(selectedImage).getSelectedFilter()].setSelected(true);
								changeMainImage();
								filterButtons[0].setImage(image);
								filterButtons[1].setImage(Utils.getFilter(image, Filter.grayscale));
								filterButtons[2].setImage(Utils.getFilter(image, Filter.chrome));
								filterButtons[3].setImage(Utils.getFilter(image, Filter.halftone));
								filterButtons[4].setImage(Utils.getFilter(image, Filter.contrast));
								
								lblMainPhoto.setOpaque(false);
								lblMainPhoto.setText("");
								setEnabledButtons(true);
							}
						};
						actionChangeThread.start();
					}
				}
			});
			panelImages.add(imageButtons[i]);
			imageGroup.add(imageButtons[i]);
		}
		
		JPanel panelFilters = new JPanel();
		panelFilters.setBorder(border);
		panelFilters.setBackground(Utils.secondColor);
		panelFilters.setOpaque(false);
		panelFilters.setBounds(50, 516, 1080, 147);
		background.add(panelFilters);
		panelFilters.setLayout(null);
		
		JLabel lblTitle = new JLabel("Photo Filters");
		lblTitle.setForeground(Utils.textColor);
		lblTitle.setFont(Utils.bubbleFont.getFont());
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(50, 465, 1080, 45);
		background.add(lblTitle);
		
		filterGroup = new ButtonGroup();
		for (int i = 0; i < filterButtons.length; i++) {
			filterButtons[i] = new RadioButton();
			filterButtons[i].setHighlighted(false);
			filterButtons[i].setEnabled(false);
			filterButtons[i].setOpaque(true);
			filterButtons[i].setBackground(Utils.secondColor);
			filterButtons[i].setBounds(40 + (208 * i), 20, 168, 112);
			filterButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (images.size() >= 6) {
						Thread actionChangeThread = new Thread() {
							@Override
							public void run() {
								setEnabledButtons(false);
								lblMainPhoto.setText("Rendering Photo...");
								lblMainPhoto.setIcon(null);
								lblMainPhoto.setOpaque(true);
								
								int index = Utils.getSelectedId(filterGroup);
								images.get(selectedImage).setSelectedFilter(index);
								//BufferedImage image = images.get(selectedImage).getFilteredImage();
								imageButtons[selectedImage].setImage(images.get(selectedImage).getFilteredImage());
								changeMainImage();
								
								lblMainPhoto.setOpaque(false);
								lblMainPhoto.setText("");
								setEnabledButtons(true);
							}
						};
						actionChangeThread.start();
					}
				}
			});
			panelFilters.add(filterButtons[i]);
			filterGroup.add(filterButtons[i]);
		}
	}

	public void changeMainImage() {
		lblMainPhoto.setIcon(new ImageIcon(Utils.copyImage(Utils.resize(images.get(selectedImage).getFilteredImage(), lblMainPhoto.getWidth(), lblMainPhoto.getHeight()))));
	}
	
	@Override
	public void reset() {
		images.clear();
		lblMainPhoto.setIcon(null);
		lblMainPhoto.setOpaque(true);
		imageGroup.clearSelection();
		filterGroup.clearSelection();
		for (int i = 0; i < imageButtons.length; i++) {
			imageButtons[i].setImage(null);
			if (i < filterButtons.length)
				filterButtons[i].setImage(null);
		}
	}
	
	public void setEnabledButtons(boolean enabled) {
		for (int i = 0; i < filterButtons.length; i++) {
			filterButtons[i].setEnabled(enabled);
		}
		for (int i = 0; i < imageButtons.length; i++) {
			imageButtons[i].setEnabled(enabled);
		}
	}
	
	public class BoothThread extends Thread {
		@Override
		public void run() {
			lblMainPhoto.setText("Taking photo...");
			//lblMainPhoto.setOpaque(true);
			
			if (images.size() >= 6 && selectedImage < 0) { // replacing a single picture
				takingPicture = false;
				return;
			}
			BufferedImage rawImage = mainMenu.getCS().getSnap();
			if (rawImage == null) {
				takingPicture = false;
				return;
			}
			try {
				String location = ImageManager.saveLocation + "/raw/";
				ImageManager.locationExists(location, true);
				File outputfile = new File(location + "IMG_" + String.format("%04d", ImageManager.getImageId(location)) + ".jpg");
				ImageIO.write(rawImage, "jpg", outputfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int size = images.size();
			if (size < 6) { // taking picture for first time
				images.add(new FilteredImage(rawImage));
				lblMainPhoto.setOpaque(false);
				lblMainPhoto.setText("");
				lblMainPhoto.setIcon(new ImageIcon(Utils.copyImage(Utils.resize(rawImage, lblMainPhoto.getWidth(), lblMainPhoto.getHeight()))));
				
				rawImage = Utils.copyImage(Utils.resize(rawImage, filterButtons[0].getWidth(), filterButtons[0].getHeight()));
				imageButtons[size].setImage(rawImage);
				if (images.size() >= 6) {
					for (int i = 0; i < imageButtons.length; i++) {
						imageButtons[i].setEnabled(true);
						if (i < filterButtons.length)
							filterButtons[i].setEnabled(true);
					}
					filterButtons[0].setImage(rawImage);
					filterButtons[1].setImage(Utils.getFilter(rawImage, Filter.grayscale));
					filterButtons[2].setImage(Utils.getFilter(rawImage, Filter.chrome));
					filterButtons[3].setImage(Utils.getFilter(rawImage, Filter.halftone));
					filterButtons[4].setImage(Utils.getFilter(rawImage, Filter.contrast));
					selectedImage = 5;
					imageButtons[5].setSelected(true);
					filterButtons[0].setSelected(true);
				}
			} else { // replacing a single picture
				images.get(selectedImage).setOriginalImage(rawImage);
				imageButtons[selectedImage].setImage(images.get(selectedImage).getFilteredImage());
				lblMainPhoto.setIcon(new ImageIcon(Utils.copyImage(Utils.resize(rawImage, lblMainPhoto.getWidth(), lblMainPhoto.getHeight()))));
				filterButtons[0].setImage(rawImage);
				filterButtons[1].setImage(Utils.getFilter(rawImage, Filter.grayscale));
				filterButtons[2].setImage(Utils.getFilter(rawImage, Filter.chrome));
				filterButtons[3].setImage(Utils.getFilter(rawImage, Filter.halftone));
				filterButtons[4].setImage(Utils.getFilter(rawImage, Filter.contrast));
			}
			lblMainPhoto.setText("");
			takingPicture = false;
		}
	}
}
