package com;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.cards.BoothSettings;
import com.cards.CameraSettings;
import com.cards.MonitorSettings;
import com.components.ImageBlock;
import com.components.ImageButton;
import com.utils.FrameDragListener;
import com.utils.ImageManager;
import com.utils.Utils;

import java.awt.Cursor;
import javax.swing.JTabbedPane;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.UIManager;


public class MainMenu {

	private JFrame mainFrame; // Main JFrame for this class
	private Properties prop; // Properties variable for loading and saving settings for the program
	private String propFileName = "booth.properties"; // Properties file name
	private BoothSettings booth = null; // Handles the Radio button for the photo shoot mode features
	private CameraSettings cs = null; // Handles the cameras attached to the computer
	private MonitorSettings ms = null; // Handles the monitors and which window will go on which screen
	
	/**
	 * Simple test run to skip splash launcher (testing purposes only)
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu menu = new MainMenu();
					menu.load();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Default constructor for this class
	 */
	public MainMenu() {
		initialize();
	}
	
	/**
	 * Loads the images, properties, layouts for the software
	 */
	public void load() {
		loadProperties();
		ImageManager.loadLayouts();
		cs.loadCameras();
		mainFrame.setVisible(true);
	}
	
	/**
	 * Loads the properties
	 */
	public void loadProperties() {
		prop = new Properties();
		if (!(new File(propFileName)).exists())
			return;
		try {
			InputStream inputStream = new FileInputStream(propFileName);
			prop.load(inputStream);
			String saveLocation = prop.getProperty("save");
			if (saveLocation != null) {
				File file = new File(saveLocation);
				if (file.exists())
					cs.setSaveLocation(saveLocation);
				else
					cs.setSaveLocation(".");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the properties
	 */
	public void saveProperties() {
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(propFileName);
			prop.setProperty("save", (getSaveLocation() == null || getSaveLocation().equals("")) ? "." : getSaveLocation());
			prop.store(outputStream, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs code to close cameras on exit
	 */
	public void exit() {
		cs.close();
		System.exit(0);
	}
	
	/**
	 * Initialize the contents of the JFrame.
	 */
	private void initialize() {
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		logger.setUseParentHandlers(false);
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		
		mainFrame = new JFrame();
		mainFrame.setTitle("BTCameraController");
		mainFrame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		mainFrame.getContentPane().setBackground(Utils.backgroundColor);
		mainFrame.getContentPane().setLayout(null);
		
		ImageIcon tabBoothIcon = new ImageIcon(this.getClass().getResource("/com/images/booth-icon.png"));
		ImageIcon tabCamIcon = new ImageIcon(this.getClass().getResource("/com/images/camera-icon.png"));
		FrameDragListener frameDragListener = new FrameDragListener(mainFrame);
		
		ImageBlock panelbg = new ImageBlock("/com/images/background.png");
		panelbg.setBackground(Utils.backgroundColor);
		panelbg.setBounds(0, 0, 720, 465);
		mainFrame.getContentPane().add(panelbg);
		panelbg.setLayout(null);

		ms = new MonitorSettings(this);
		booth = new BoothSettings(this);
		cs = new CameraSettings(this);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 40, 700, 400);
		panelbg.add(tabbedPane);
		tabbedPane.setFocusable(false);
		//tabbedPane.addTab("Modes", tabBoothIcon, booth); //temp renaming to "Booth" for presentation
		tabbedPane.addTab("Booth", tabBoothIcon, booth);
		tabbedPane.addTab("Camera", tabCamIcon, cs);

		ImageButton imageButton = new ImageButton(MainMenu.class.getResource("images/exit-icon.png"));
		imageButton.setBounds(674, 10, 36, 36);
		panelbg.add(imageButton);

		JPanel panelMove = new JPanel();
		panelMove.setBounds(0, 0, 670, 50);
		panelbg.add(panelMove);
		panelMove.setOpaque(false);
		panelMove.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		panelMove.setLayout(null);

		ImageIcon icon = new ImageIcon(MainMenu.class.getResource("/com/images/icon.png"));
		mainFrame.setIconImage(icon.getImage());
		JLabel lblTitle = new JLabel("BTCameraController");
		lblTitle.setIcon(new ImageIcon(MainMenu.class.getResource("/com/images/ico.png")));
		lblTitle.setForeground(Utils.textColor);
		lblTitle.setBounds(10, 5, 650, 25);
		panelMove.add(lblTitle);
		panelMove.addMouseListener(frameDragListener);
		panelMove.addMouseMotionListener(frameDragListener);
		imageButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				exit();
			}
		});
		mainFrame.setBounds(100, 100, 720, 465);
		mainFrame.setUndecorated(true);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public String getSaveLocation() {
		return ImageManager.saveLocation;
	}

	public MonitorSettings getMS() {
		return ms;
	}

	public void setMS(MonitorSettings ms) {
		this.ms = ms;
	}

	public BoothSettings getBooth() {
		return booth;
	}

	public void setBooth(BoothSettings booth) {
		this.booth = booth;
	}

	public CameraSettings getCS() {
		return cs;
	}

	public void setCS(CameraSettings cs) {
		this.cs = cs;
	}
}
