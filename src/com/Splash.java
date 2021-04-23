package com;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Splash {

	private JFrame frmSplash;
	private JLabel lblLoading;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Splash window = new Splash();
		long startTime = System.currentTimeMillis();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frmSplash.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		Thread startup = new Thread() {
			public void run() {
				MainMenu menu = new MainMenu();
				while(true) {
					if (System.currentTimeMillis() - startTime > 4000) {
						menu.load();
						window.frmSplash.setVisible(false);
						break;
					} else if (System.currentTimeMillis() - startTime > 3000) {
						window.lblLoading.setText("Finished!");
					} else {
						byte[] array = new byte[7]; // length is bounded by 7
					    new Random().nextBytes(array);
					    String generatedString = new String(array, Charset.forName("UTF-8"));
						window.lblLoading.setText("Loading file " + generatedString + ".mp4");
					}
				}
			}
		};

		startup.start();
	}

	/**
	 * Create the application.
	 */
	public Splash() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		frmSplash = new JFrame();
		frmSplash.setBounds(100, 100, 350, 100);
		frmSplash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSplash.setUndecorated(true);
		frmSplash.setLocationRelativeTo(null);
		ImageIcon icon = new ImageIcon(MainMenu.class.getResource("/com/images/icon.png"));
		frmSplash.setIconImage(icon.getImage());
		
		JPanel imagePanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					Image logo = null;
					logo = ImageIO.read(MainMenu.class.getResource("images/splash2.png"));
					g.drawImage(logo, 0, 0, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		frmSplash.getContentPane().add(imagePanel, BorderLayout.CENTER);
		imagePanel.setLayout(null);
		
		lblLoading = new JLabel("");
		lblLoading.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoading.setFont(new Font("Arial", Font.PLAIN, 11));
		lblLoading.setForeground(Color.decode("#27e8a7"));
		lblLoading.setBounds(100, 46, 228, 14);
		imagePanel.add(lblLoading);
	}
}
