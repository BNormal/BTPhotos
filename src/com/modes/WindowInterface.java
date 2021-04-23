package com.modes;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;

import com.MainMenu;
import com.utils.Utils;

@SuppressWarnings("serial")
public abstract class WindowInterface extends JFrame {
	
	protected MainMenu menu;
	protected String path;
	
	public WindowInterface(MainMenu menu) {
		this.menu = menu;
		initialize();
	}
	
	public void start(GraphicsConfiguration graphicsConfiguration) {
		setLocation(graphicsConfiguration.getBounds().x, getY());
	}
	
	private void initialize() {
		setBounds(100, 100, 1280, 720);
		getContentPane().setBackground(Utils.backgroundColor);
		getContentPane().setLayout(new BorderLayout(0, 0));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		//setUndecorated(true);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
