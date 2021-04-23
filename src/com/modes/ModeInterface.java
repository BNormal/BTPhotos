package com.modes;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jnativehook.GlobalScreen;

import com.MainMenu;
import com.components.ImageBlock;
import com.utils.FilteredImage;
import com.utils.GlobalKeyListener;
import com.utils.Utils;


@SuppressWarnings("serial")
public abstract class ModeInterface extends JPanel  {

	protected MainMenu mainMenu = null;
	protected ModeWindow userInterface = null;
	protected ArrayList<FilteredImage> images = new ArrayList<FilteredImage>();
	protected boolean takingPicture = false;
	protected ImageBlock background = null;
	protected GlobalKeyListener gkl = null;

	public ModeInterface(MainMenu mainMenu, ModeWindow userInterface) {
		this.userInterface = userInterface;
		this.mainMenu = mainMenu;
		initialize();
	}
	
	private void initialize() {	
		setBounds(100, 100, 1280, 720);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));
		
		background = new ImageBlock("/com/images/background.png");
		background.setLayout(null);
		background.setBackground(Utils.backgroundColor);
		add(background);
	}
	
	protected void addGlobalKeyListener(GlobalKeyListener gkl) {
		if (gkl != null)
			GlobalScreen.addNativeKeyListener(gkl);
	}
	
	protected void removeGlobalKeyListener(GlobalKeyListener gkl) {
		if (gkl != null)
			GlobalScreen.removeNativeKeyListener(gkl);
	}
	
	public GlobalKeyListener getGkl() {
		return gkl;
	}

	public void setGkl(GlobalKeyListener gkl) {
		this.gkl = gkl;
	}
	
	public void changeScene(Page page) {
		userInterface.changeScene(page);
	}
	
	public boolean isTakingPicture() {
		return takingPicture;
	}

	public void setTakingPicture(boolean takingPicture) {
		this.takingPicture = takingPicture;
	}
	
	public void reset() {
		
	}

}
