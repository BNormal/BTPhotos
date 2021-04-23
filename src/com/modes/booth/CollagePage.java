package com.modes.booth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.MainMenu;
import com.components.CollageLayout;
import com.components.ImageBlock;
import com.components.ImageButton;
import com.components.RadioButton;
import com.modes.Page;
import com.modes.ModeWindow;
import com.utils.CustomFont;
import com.utils.ImageManager;
import com.utils.Utils;

@SuppressWarnings("serial")
public class CollagePage extends JPanel {

	@SuppressWarnings("unused")
	private MainMenu mm;
	private ModeWindow ui;
	private ButtonGroup layoutGroup = new ButtonGroup();
	private ImageBlock dragBox;
	private ImageBlock mainPhoto;
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private RadioButton[] layouts = new RadioButton[3];
	private ImageBlock[] imageBoxes = new ImageBlock[6];
	private ImageBlock[] layoutBoxes = new ImageBlock[6];
	private BufferedImage collageImage = null;
	private int moveX = 0;
	private int moveY = 0;
	
	public CollagePage(MainMenu mm, ModeWindow ui) {
		this.ui = ui;
		this.mm = mm;
		initialize();
	}
	
	private void initialize() {
		setBounds(100, 100, 1280, 720);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(null);
		
		dragBox = new ImageBlock();
		dragBox.setBounds(0, 0, 1, 1);
		dragBox.setVisible(false);
		dragBox.setOpaque(true);
		add(dragBox);
		
		Border border = BorderFactory.createDashedBorder(Utils.secondColor, 2, 4, 2, true);
		CustomFont font = new CustomFont("fonts/ALBAS.ttf", 30f);
		
		JPanel layoutsPanel = new JPanel();
		layoutsPanel.setBorder(border);
		layoutsPanel.setOpaque(false);
		layoutsPanel.setBounds(87, 34, 637, 158);
		add(layoutsPanel);
		layoutsPanel.setLayout(null);
		
		for (int i = 0; i < layouts.length; i++) {
			layouts[i] = new RadioButton(MainMenu.class.getResource("images/layout_" + (i + 1) + ".png"));
			layouts[i].setOpaque(true);
			layouts[i].setBounds(9 + 210 * i, 9, 200, 140);
			layouts[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					collageImage = generateCollage();
					if (collageImage != null)
						mainPhoto.setImage(collageImage);
				}
			});
			layoutGroup.add(layouts[i]);
			layoutsPanel.add(layouts[i]);
		}
		layouts[0].setSelected(true);
		
		ImageButton btnNext = new ImageButton(MainMenu.class.getResource("images/next.png"));
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (collageImage != null) {
					ui.setImage(collageImage);
					ui.changeScene(Page.BOOTHEMAIL);
				}
					//display warning missing image
			}
		});
		btnNext.setFocusable(false);
		btnNext.setBackground(Utils.secondColor);
		btnNext.setBounds(1144, 383, 80, 200);
		add(btnNext);
		
		mainPhoto = new ImageBlock();
		mainPhoto.setBackground(Utils.secondColor);
		mainPhoto.setBounds(819, 26, 405, 270);
		add(mainPhoto);
		
		for (int i = 0; i < imageBoxes.length; i++) {
			imageBoxes[i] = new ImageBlock();
			imageBoxes[i].setId(i);
			imageBoxes[i].setBackground(Utils.secondColor);
			imageBoxes[i].setBounds(33 + (178 * (i % 2)), 279 + (123 * (i / 2)), 168, 112);
			imageBoxes[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent evt) {
					ImageBlock box = (ImageBlock) evt.getSource();
					dragBox.setId(box.getId());
					dragBox.setSize(box.getSize());
					dragBox.setIcon(box.getIcon());
					dragBox.setVisible(true);
					moveX = evt.getX();
					moveY = evt.getY();
					moveBox(evt);
				}
				public void mouseReleased(MouseEvent evt) {
					dragBox.setVisible(false);
					handleCollision(dragBox);
				}
			});
			imageBoxes[i].addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent evt) {
					moveBox(evt);
				}
			});
			add(imageBoxes[i]);
		}
		for (int i = 0; i < layoutBoxes.length; i++) {
			layoutBoxes[i] = new ImageBlock();
			//layoutBoxes[i].setId(i);
			layoutBoxes[i].setForeground(Utils.textColor);
			layoutBoxes[i].setText((i + 1) + "");
			layoutBoxes[i].setHorizontalTextPosition(SwingConstants.CENTER);
			layoutBoxes[i].setHorizontalAlignment(SwingConstants.CENTER);
			layoutBoxes[i].setCustomFont(font);
			layoutBoxes[i].setBackground(Utils.secondColor);
			layoutBoxes[i].setBounds(429 + (178 * (i % 2)), 279 + (123 * (i / 2)), 168, 112);
			add(layoutBoxes[i]);
		}
		
		ImageBlock background = new ImageBlock("/com/images/background.png");
		background.setBounds(0, 0, 1280, 720);
		
		background.setLayout(null);
		background.setBackground(Utils.backgroundColor);
		add(background);
	}
	
	public void handleCollision(ImageBlock box) {
		Rectangle r1 = box.getBounds();
		Rectangle r2 = null;
		float scale = 2.5f;
		for (int i = 0; i < layoutBoxes.length; i++) {
			r2 = layoutBoxes[i].getBounds();
			r2.setBounds((int) (r2.getX() + r2.getWidth() / (scale * 2.0f)), (int) (r2.getY() + r2.getHeight() / (scale * 2.0f)), (int) (r2.getWidth() / scale), (int) (r2.getHeight() / scale));
			if (!(r1.getX() + r1.getWidth() < r2.getX() || r1.getX() > r2.getX() + r2.getWidth() || r1.getY() + r1.getHeight() < r2.getY() || r1.getY() > r2.getY() + r2.getHeight())) {
				layoutBoxes[i].setId(box.getId());
				layoutBoxes[i].setIcon(box.getIcon());
				//layoutBoxes[i].setText("");
				collageImage = generateCollage();
				if (collageImage != null)
					mainPhoto.setImage(collageImage);
				return;
			}
		}
	}
	
	public void moveBox(MouseEvent mouse) {
		int x = (mouse.getXOnScreen() - ui.getX()) - moveX;
		int y = (mouse.getYOnScreen() - ui.getY() - 50) - moveY;
		if (x < 0)
			x = 0;
		else if (x > getWidth() - dragBox.getWidth())
			x = getWidth() - dragBox.getWidth();
		if (y < 0)
			y = 0;
		else if (y > getHeight() - dragBox.getHeight())
			y = getHeight() - dragBox.getHeight();
		dragBox.setLocation(x, y);
	}

	public BufferedImage generateCollage() {
		int layoutId = Utils.getSelectedId(layoutGroup);
		CollageLayout cl = ImageManager.layouts.get(layoutId);
		for (int i = 0; i < 6; i++) {
			if (i < cl.getSize())
				layoutBoxes[i].setVisible(true);
			else
				layoutBoxes[i].setVisible(false);
		}
		for (int i = 0; i < cl.getSize(); i++)
			if (layoutBoxes[i].getId() == -1)
				return null;
		int sizeX = 1920;
		int sizeY = 1080;
		BufferedImage source = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
		Graphics g = source.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, sizeX, sizeY);
		for (int i = 0; i < cl.getSize(); i++) {
			g.drawImage(Utils.copyImage(Utils.resize(images.get(layoutBoxes[i].getId()), 640, 360)), (int) cl.getLocations()[i].getX(), (int) cl.getLocations()[i].getY(), null);
		}
		return source;
	}
	
	public ArrayList<BufferedImage> getImages() {
		return images;
	}
	
	public void setImages(ArrayList<BufferedImage> images) {
		this.images = images;
	}

	public void reset() {
		mainPhoto.setImage(null);
		for (int i = 0; i < layoutBoxes.length; i++) {
			layoutBoxes[i].setId(-1);
			layoutBoxes[i].setIcon(null);
		}
		images.clear();
	}
	
	public void addImage(BufferedImage image) {
		imageBoxes[images.size()].setImage(image);
		images.add(image);
	}
}
