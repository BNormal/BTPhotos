package com.cards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import com.MainMenu;
import com.components.ImageBlock;
import com.github.sarxos.webcam.Webcam;
import com.utils.Filter;
import com.utils.ImageManager;
import com.utils.Utils;

import edsdk.api.CanonCamera;

@SuppressWarnings("serial")
public class CameraSettings extends JPanel {

	private MainMenu mm = null;
	private ImageBlock background;
	private JLabel lblSaveLocation;
	private JLabel lblPreview;
	private JButton btnTakeSnap;
	private DefaultComboBoxModel<String> modelCamera;
	private ArrayList<Camera> cameras = new ArrayList<Camera>();
	private JPanel filterPanel;
	private JPanel cameraResolutionPanel;
	private JPanel shutterSpeedPanel;
	private JPanel aperturePanel;
	private JComboBox<String> cameraResolution;
	private JComboBox<String> shutterSpeed;
	private JComboBox<String> aperture;
	private Filter filter = Filter.none;
	private int selectedCamera = -1;
	
	/**
	 * Create the panel.
	 */
	public CameraSettings(MainMenu mm) {
		this.mm = mm;
		initialize();
	}
	
	private void initialize() {
		setBounds(100, 100, 700, 400);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));
		setBackground(Utils.backgroundColor);
		
		//JPanel background = new JPanel();
		background = new ImageBlock("/com/images/background.png");
		background.setLayout(null);
		background.setOpaque(false);
		add(background);
		
		modelCamera = new DefaultComboBoxModel<String>(new String[] {"None"});
		
		JPanel CameraTitlePanel = new JPanel();
		CameraTitlePanel.setBounds(10, 42, 250, 200);
		CameraTitlePanel.setForeground(Utils.textColor);
		CameraTitlePanel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Utils.highlightColor), "Camera", TitledBorder.LEADING, TitledBorder.TOP, null, Utils.textColor));
		CameraTitlePanel.setOpaque(false);
		CameraTitlePanel.setLayout(null);
		background.add(CameraTitlePanel);

		lblPreview = new JLabel("No Camera Selected");
		lblPreview.setBounds(5, 13, 240, 180);
		lblPreview.setForeground(Utils.textColor);
		lblPreview.setHorizontalTextPosition(SwingConstants.CENTER);
		lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreview.setOpaque(false);
		CameraTitlePanel.add(lblPreview);
		
		btnTakeSnap = new JButton("Take Snap");
		btnTakeSnap.setEnabled(false);
		btnTakeSnap.setFocusable(false);
		btnTakeSnap.setBounds(10, 252, 99, 23);
		btnTakeSnap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewTest();
			}
		});
		background.add(btnTakeSnap);
		
		JComboBox<String> cbxCameraList = new JComboBox<String>(modelCamera);
		cbxCameraList.setBounds(10, 11, 250, 20);
		cbxCameraList.setFocusable(false);
		cbxCameraList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeCamera(cbxCameraList.getSelectedIndex() - 1);
			}
		});
		cbxCameraList.setFocusable(false);
		background.add(cbxCameraList);
		
		// Filter Panel
		filterPanel = new JPanel();
		filterPanel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Utils.highlightColor), "Filters", TitledBorder.LEADING, TitledBorder.TOP, null, Utils.textColor));
		filterPanel.setOpaque(false);
		filterPanel.setVisible(false);
		filterPanel.setBounds(482, 20, 193, 57);
		background.add(filterPanel);
		
		
		// Filters
		JComboBox<String> filterTypes = new JComboBox<String>();
		String filters[] = new String[Filter.values().length];
		for (int i = 0; i < filters.length; i++) {
			filters[i] = Utils.capitalizeWord(Filter.values()[i].name());
		}
		filterTypes.setModel(new DefaultComboBoxModel<String>(filters));
		filterTypes.setFocusable(false);
		filterTypes.setSelectedIndex(0);
		filterTypes.setBounds(20, 20, 160, 22);
		filterTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = filterTypes.getSelectedIndex();
				if (index > -1 && cameras.size() > selectedCamera) {
					filter = Filter.values()[index];
				}
			}
		});
		filterPanel.setLayout(null);
		filterPanel.add(filterTypes);
		

		// Camera Resolution Panel
		cameraResolutionPanel = new JPanel();
		cameraResolutionPanel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Utils.highlightColor), "Camera Resolution", TitledBorder.LEADING, TitledBorder.TOP, null, Utils.textColor));
		cameraResolutionPanel.setOpaque(false);
		cameraResolutionPanel.setVisible(false);
		cameraResolutionPanel.setBounds(482, 80, 193, 57);
		background.add(cameraResolutionPanel);
		
		// Camera Resolution
		cameraResolution = new JComboBox<String>();
		cameraResolution.setModel(new DefaultComboBoxModel<String>(new String[] {"None"}));
		cameraResolution.setFocusable(false);
		cameraResolution.setSelectedIndex(0);
		cameraResolution.setBounds(20, 20, 160, 22);
		cameraResolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = cameraResolution.getSelectedIndex();
				if (index > -1 && cameras.size() > selectedCamera) {
					// Disabled cause I don't want to break the Canon Camera
					cameras.get(selectedCamera).setImageQuality(index);
				}
			}
		});
		cameraResolutionPanel.setLayout(null);
		cameraResolutionPanel.add(cameraResolution);
		
		// Shutter Speed Panel
		shutterSpeedPanel = new JPanel();
		shutterSpeedPanel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Utils.highlightColor), "Shutter Speed", TitledBorder.LEADING, TitledBorder.TOP, null, Utils.textColor));
		shutterSpeedPanel.setOpaque(false);
		shutterSpeedPanel.setVisible(false);
		shutterSpeedPanel.setBounds(482, 140, 193, 57);
		background.add(shutterSpeedPanel);
		
		// Shutter Speed
		shutterSpeed = new JComboBox<String>();
		shutterSpeed.setModel(new DefaultComboBoxModel<String>(new String[] {"None"}));
		shutterSpeed.setFocusable(false);
		shutterSpeed.setSelectedIndex(0);
		shutterSpeed.setBounds(20, 20, 160, 22);
		shutterSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = shutterSpeed.getSelectedIndex();
				if (index > -1 && cameras.size() > selectedCamera) {
					Camera camera = cameras.get(selectedCamera);
					if (camera instanceof ExternalCamera) {
						// Disabled cause I don't want to break the Canon Camera
						((ExternalCamera) camera).setShutterSpeed(index);
					}
				}
			}
		});
		shutterSpeedPanel.setLayout(null);
		shutterSpeedPanel.add(shutterSpeed);
		
		// Aperture Speed Panel
		aperturePanel = new JPanel();
		aperturePanel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Utils.highlightColor), "Aperture", TitledBorder.LEADING, TitledBorder.TOP, null, Utils.textColor));
		aperturePanel.setOpaque(false);
		aperturePanel.setVisible(false);
		aperturePanel.setBounds(482, 200, 193, 57);
		background.add(aperturePanel);
		
		// Aperture 
		aperture = new JComboBox<String>();
		aperture.setModel(new DefaultComboBoxModel<String>(new String[] {"None"}));
		aperture.setFocusable(false);
		aperture.setSelectedIndex(0);
		aperture.setBounds(20, 20, 160, 22);
		aperture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = aperture.getSelectedIndex();
				if (index > -1 && cameras.size() > selectedCamera) {
					Camera camera = cameras.get(selectedCamera);
					if (camera instanceof ExternalCamera) {
						// Disabled cause I don't want to break the Canon Camera
						((ExternalCamera) camera).setApertures(index);
					}
				}
			}
		});
		aperturePanel.setLayout(null);
		aperturePanel.add(aperture);
		
		JButton btnSaveLocation = new JButton("Save Location");
		btnSaveLocation.setFocusable(false);
		btnSaveLocation.setBounds(10, 286, 99, 23);
		btnSaveLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(".")); // start at application current directory
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File yourFolder = fc.getSelectedFile();
					setSaveLocation(yourFolder.getAbsolutePath());
					mm.saveProperties();
				}
			}
		});
		background.add(btnSaveLocation);
		
		lblSaveLocation = new JLabel("...");
		lblSaveLocation.setForeground(Color.WHITE);
		lblSaveLocation.setBounds(123, 286, 554, 23);
		background.add(lblSaveLocation);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFocusable(false);
		btnRefresh.setBounds(161, 253, 99, 23);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadCameras();
			}
		});
		background.add(btnRefresh);
	}
	
	public void loadCameras() {
		Thread loopThread = new Thread() {
			@Override
			public void run() {
				ExternalCamera ec = null;
				if (cameras.size() > 0) {
					for (Camera cam : cameras) {
						if (cam instanceof ExternalCamera) {
							ec = (ExternalCamera) cam;
							break;
						}
					}
				}
				cameras.clear();
				for (Webcam camera: Webcam.getWebcams()) {
					cameras.add(new InternalCamera(camera));
				}
				if (ec == null) {
					try {
						String path = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
						File file = new File(path + "\\EDSDK_64");
						if (file.exists())
							ec = new ExternalCamera(new CanonCamera());
						else
							System.err.println("Missing Canon Camera files: EDSDK_64\\EDSDK.dll or EDSDK\\EDSDK.dll");
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
				if (ec != null) {
					if (ec.isConnected)
						cameras.add(ec);
					else {
						//ec.exit();
						ec = null;
					}
				}
				modelCamera.removeAllElements();
				modelCamera.addElement("None");
				for (Camera camera : cameras) {
					if (camera.isConnected()) {
						modelCamera.addElement(camera.getName());
					}
				}
			}
		};
		loopThread.start();
	}
	
	public void setSaveLocation(String exportLocation) {
		ImageManager.saveLocation = exportLocation;
		lblSaveLocation.setText(exportLocation);
	}

	private void previewTest() {
		Thread pictureThread = new Thread() {
			@Override
			public void run() {
				btnTakeSnap.setEnabled(false);
				changePreviewText("Taking picture - please wait...", false);
				BufferedImage image = getSnap();
				if (image != null) {
					lblPreview.setIcon(new ImageIcon(Utils.getFilter(Utils.copyImage(Utils.resize(image, lblPreview.getWidth(), lblPreview.getHeight())), filter)));
					changePreviewText("", false);
				} else {
					changePreviewText("Failed to take picture", false);
				}
				btnTakeSnap.setEnabled(true);
			}
		};
		pictureThread.start();
	}
	
	public void changePreviewText(String text, boolean removePicture) {
		lblPreview.setText(text);
		if (removePicture)
			lblPreview.setIcon(null);
	}
	
	public BufferedImage getSnap() {
		if (selectedCamera >= 0)
			return cameras.get(selectedCamera).takePicture();
		return null;
	}
	
	public void changeCamera(int selection) {
		Camera camera = null;
		if (selection >= 0) {
			if (selectedCamera >= 0)
				cameras.get(selectedCamera).close();
			mm.getMS().setWarning("");
			camera = cameras.get(selection);
			changePreviewText("Ready", true);
			btnTakeSnap.setEnabled(true);
		} else {
			btnTakeSnap.setEnabled(false);
			changePreviewText("No Camera Selected", true);
		}
		selectedCamera = selection;
		if (camera != null) {
			cameraResolutionPanel.setVisible(true);
			cameraResolution.setModel(new DefaultComboBoxModel<String>(camera.getImageQualities()));
			cameraResolution.setSelectedIndex(camera.getCurrentQualityId());
			filterPanel.setVisible(true);
		} else {
			filterPanel.setVisible(false);
			cameraResolutionPanel.setVisible(false);
		}
		if (camera instanceof ExternalCamera) {
			ExternalCamera ec = (ExternalCamera) camera;
			shutterSpeed.setModel(new DefaultComboBoxModel<String>(ec.getShutterSpeeds()));
			shutterSpeed.setSelectedIndex(ec.getShutterSpeedId());
			aperture.setModel(new DefaultComboBoxModel<String>(ec.getApertures()));
			aperture.setSelectedIndex(ec.getApertureId());
			shutterSpeedPanel.setVisible(true);
			aperturePanel.setVisible(true);
		} else {
			shutterSpeedPanel.setVisible(false);
			aperturePanel.setVisible(false);
		}
	}
	
	public Camera getCurrentCam() {
		if (selectedCamera < 0)
			return null;
		return cameras.get(selectedCamera);
	}
	
	public void close() {
		if (selectedCamera < 0)
			return;
		cameras.get(selectedCamera).close();
	}
	
	public void open() {
		//openCam = false;
	}
}
