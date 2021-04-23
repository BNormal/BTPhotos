package com.tests;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.utils.Utils;

import edsdk.api.CanonCamera;
import edsdk.utils.CanonConstants.EdsAv;
import edsdk.utils.CanonConstants.EdsISOSpeed;
import edsdk.utils.CanonConstants.EdsImageQuality;
import edsdk.utils.CanonConstants.EdsSaveTo;
import edsdk.utils.CanonConstants.EdsTv;


public class CanonTest {

	private JFrame frame;
	private BufferedImage sprite = null;
	private JPanel panel;
	private CanonCamera slr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CanonTest window = new CanonTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CanonTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		slr = new CanonCamera();
		/*slr.beginLiveView();
		slr.downloadLiveView();
		slr.endLiveView();*/
		boolean isOpen = slr.openSession();
		if (isOpen) {
			System.out.println("Artist: " + slr.getArtist());
			System.out.println("Body ID Ex: " + slr.getBodyIDEx());
			System.out.println("Copyright: " + slr.getCopyright());
			System.out.println("Current Folder: " + slr.getCurrentFolder());
			System.out.println("Current Storage: " + slr.getCurrentStorage());
			System.out.println("Firmware Version: " + slr.getFirmwareVersion());
			System.out.println("Structure: " + slr.getHardDriveDirectoryStructure());
			System.out.println("Owner Name: " + slr.getOwnerName());
			System.out.println("Product Name: " + slr.getProductName());
			// Use the blocking variant to get a result immediately.
			//slr.shoot();
			//slr.beginLiveView();
			EdsAv edsa;
			System.out.println("\nAPERTURE VALUES: " + slr.getApertureValue().description());
			for (int i = 0; i < slr.getAvailableApertureValues().length; i++) {
				edsa = slr.getAvailableApertureValues()[i];
				System.out.println(edsa.value() + " : " + edsa + " : " + edsa.name() + " : " + edsa.description());
			}
			System.out.println("\nSHUTTER SPEEDS: " + slr.getShutterSpeed().description());
			EdsTv edst;
			for (int i = 0; i < slr.getAvailableShutterSpeeds().length; i++) {
				edst = slr.getAvailableShutterSpeeds()[i];
				System.out.println(edst.value() + " : " + edst + " : " + edst.name() + " : " + edst.description());
			}
			/*System.out.println("\nSHOOTING MODES:");
			for (int i = 0; i < slr.getAvailableShootingModes().length; i++) {
				System.out.println(slr.getAvailableShootingModes()[i].value());
			}*/
			EdsImageQuality edsi;
			System.out.println("\nIMAGE QUALITIES: " + slr.getImageQuality().description());
			for (int i = 0; i < slr.getAvailableImageQualities().length; i++) {
				edsi = slr.getAvailableImageQualities()[i];
				System.out.println(edsi.value() + " : " + edsi + " : " + edsi.name() + " : " + edsi.description());
			}
			System.out.println("\nISO SPEEDS: " + slr.getISOSpeed().description());
			
			EdsISOSpeed edss;
			for (int i = 0; i < slr.getAvailableISOSpeeds().length; i++) {
				edss = slr.getAvailableISOSpeeds()[i];
				System.out.println(edss.value() + " : " + edss + " : " + edss.name() + " : " + edss.description());
			}
			File file = new File("");
			try {
				file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			file = slr.shoot(EdsSaveTo.kEdsSaveTo_Host, 5, file)[0];
			System.out.println("File: " + file.getAbsolutePath());
			try {
				sprite = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Use async handlers to continue your code immediately 
			//slr.shootAsync().whenDone(f -> System.out.println(f));

			// close session is always blocking.
			// because commands are queued this won't be executed 
			// until the above slr.shoot() finished it's work. 
			//slr.closeSession();
		}

		frame = new JFrame();
		frame.setBounds(100, 100, 1440, 960);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	//slr.endLiveView();
		    	slr.closeSession();
		    	//System.exit(0);
		    }
		});
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (sprite != null) {
					sprite = Utils.copyImage(Utils.resize(sprite, panel.getWidth(), panel.getHeight()));
					g.drawImage(sprite, 0, 0, null);
				}
			};
		};
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		/*Thread loopThread = new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sprite = slr.downloadLiveView();
					panel.repaint();
				}
			}
		};
		loopThread.start();*/
	}

}
