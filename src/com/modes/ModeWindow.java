package com.modes;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.MainMenu;
import com.modes.booth.BoothMode;
import com.modes.booth.CollagePage;
import com.modes.booth.EmailBooth;
import com.modes.gif.EmailGIF;
import com.modes.gif.GifMode;
import com.modes.snap.EmailSnap;
import com.modes.snap.SnapMode;
import com.utils.Email;
import com.utils.Utils;

import java.awt.CardLayout;

@SuppressWarnings("serial")
public class ModeWindow extends WindowInterface {
	
	private CardLayout cl = new CardLayout(0, 0);
	private JPanel cards = null;
	private Page mode;
	private Page currentScene = null;
	
	private ModeInterface photoPage = null;
	private EmailInterface emailPage = null;
	private CollagePage cp = null;
	private ProcessingInterface ems = null;

	private Email email = null;
	private BufferedImage image = null;
	private File gifImage = null;

	/**
	 * Create the frame.
	 */
	public ModeWindow(MainMenu menu, int mode) {
		this(menu, Page.values()[mode]);
	}
	
	public ModeWindow(MainMenu menu, Page mode) {
		super(menu);
		this.mode = mode;
	}
	
	public void start(GraphicsConfiguration graphicsConfiguration) {
		super.start(graphicsConfiguration);
		String modeName = mode.name();
		modeName = Utils.capitalizeWord(modeName.subSequence(0, modeName.length() - 4) + " " + modeName.subSequence(modeName.length() - 4, modeName.length()));
		setTitle("BTCameraController - " + modeName);
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
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModeWindow.class.getResource("/com/images/icon.png")));
		panelBackground.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelBackground.setBackground(Utils.backgroundColor);
		setContentPane(panelBackground);
		panelBackground.setLayout(null);
		panelBackground.setLayout(new BorderLayout(0, 0));
		cards = new JPanel();
		cards.setOpaque(false);
		cards.setLayout(cl);

		switch (mode) {
			case SNAPMODE:
				photoPage = new SnapMode(menu, this);
				emailPage = new EmailSnap(this);
				ems = new ProcessingInterface(this);

				cards.add(photoPage, mode.name());
				cards.add(emailPage, Page.SNAPEMAIL.name());
				cards.add(ems, Page.PROCESSMODE.name());
				break;
			case GIFMODE:
				photoPage = new GifMode(menu, this);
				emailPage = new EmailGIF(this);
				ems = new ProcessingInterface(this);

				cards.add(photoPage, mode.name());
				cards.add(emailPage, Page.GIFEMAIL.name());
				cards.add(ems, Page.PROCESSMODE.name());
			break;
			case BOOTHMODE:
				photoPage = new BoothMode(menu, this);
				cp = new CollagePage(menu, this);
				emailPage = new EmailBooth(this);
				ems = new ProcessingInterface(this);

				cards.add(photoPage, mode.name());
				cards.add(cp, Page.COLLAGEPAGE.name());
				cards.add(emailPage, Page.BOOTHEMAIL.name());
				cards.add(ems, Page.PROCESSMODE.name());
				break;
			default:
				break;
		}
		changeScene(mode);
		panelBackground.add(cards);
		addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	if (photoPage != null) {
            		photoPage.reset();
            		photoPage.removeGlobalKeyListener(photoPage.getGkl());
            		photoPage = null;
            	}
            }
        });
		setVisible(true);
	}
	
	public void resetMode() {
		photoPage.reset();
	}
	
	public void startProcess() {
		ems.start();
	}
	
	public void changeScene(Page page) {
		if (page.name().toLowerCase().contains("email"))
			emailPage.update();
		currentScene = page;
		cl.show(cards, page.name());
	}
	
	public Page getCurrentScene() {
		return currentScene;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public File getGifImage() {
		return gifImage;
	}
	public void setGifImage(File gifImage) {
		this.gifImage = gifImage;
	}
	public CollagePage getCollagePage() {
		return cp;
	}
	public void setCollagePage(CollagePage cp) {
		this.cp = cp;
	}
	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}
	public MainMenu getMenu() {
		return menu;
	}
	public void setMenu(MainMenu mm) {
		this.menu = mm;
	}
	public Page getMode() {
		return mode;
	}
	public void setMode(Page mode) {
		this.mode = mode;
	}
}
