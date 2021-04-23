package com.cards;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import com.MainMenu;
import com.components.Monitor;
import com.modes.ModeWindow;
import com.modes.SlideShow;
import com.utils.Utils;

import java.awt.Font;

import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Color;

@SuppressWarnings("serial")
public class MonitorSettings extends JPanel {
	
	private MainMenu menu = null;
	private boolean identifying = false;
	private ArrayList<Monitor> monitors = new ArrayList<Monitor>();
	private DefaultComboBoxModel<String> modelMonitors;
	private JTextArea txtLocation;
	private JButton btnLocation;
	private JScrollPane scrollPane;
	private JLabel lblWarning;
	
	/**
	 * Create the panel.
	 */
	public MonitorSettings(MainMenu menu) {
		this.menu = menu;
		initialize();
	}
	
	private void initialize() {
		setBackground(Utils.backgroundColor);
		setLayout(null);
		setOpaque(false);
		setBounds(100, 100, 290, 350);
		
		modelMonitors = new DefaultComboBoxModel<String>(new String[] {"None"});
		JComboBox<String> cbxMonitors = new JComboBox<String>(modelMonitors);
		ButtonGroup group = new ButtonGroup();
		
		txtLocation = new JTextArea();
		
		updateDevices();
		
		JPanel panelScreenSettings = new JPanel();
		panelScreenSettings.setOpaque(false);
		panelScreenSettings.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Utils.highlightColor), "Screen Settings", TitledBorder.CENTER, TitledBorder.TOP, null, Utils.textColor));
		panelScreenSettings.setOpaque(false);
		panelScreenSettings.setBounds(5, 5, 280, 340);
		add(panelScreenSettings);
		panelScreenSettings.setLayout(null);
		
		JRadioButton rdbtnNone = new JRadioButton("None");
		rdbtnNone.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnNone.setFocusable(false);
		rdbtnNone.setOpaque(false);
		rdbtnNone.setForeground(Utils.textColor);
		rdbtnNone.setBounds(10, 99, 80, 23);
		panelScreenSettings.add(rdbtnNone);
		rdbtnNone.setSelected(true);
		rdbtnNone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitors.get(cbxMonitors.getSelectedIndex()).setWindowID(-1);
				scrollPane.setVisible(false);
				btnLocation.setVisible(false);
				if (!hasAMode()) {
					menu.getBooth().setRunEnabled(false);
				}
			}
		});
		
		group.add(rdbtnNone);
		
		JRadioButton rdbtnSlideshow = new JRadioButton("Slideshow");
		rdbtnSlideshow.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnSlideshow.setFocusable(false);
		rdbtnSlideshow.setOpaque(false);
		rdbtnSlideshow.setForeground(Utils.textColor);
		rdbtnSlideshow.setBounds(98, 99, 80, 23);
		panelScreenSettings.add(rdbtnSlideshow);
		rdbtnSlideshow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitors.get(cbxMonitors.getSelectedIndex()).setWindowID(0);
				menu.getBooth().setRunEnabled(true);
				scrollPane.setVisible(true);
				btnLocation.setVisible(true);
			}
		});
		group.add(rdbtnSlideshow);
		
		JRadioButton rdbtnMain = new JRadioButton("Main");
		rdbtnMain.setEnabled(true);
		rdbtnMain.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnMain.setFocusable(false);
		rdbtnMain.setOpaque(false);
		rdbtnMain.setForeground(Utils.textColor);
		rdbtnMain.setBounds(186, 99, 80, 23);
		panelScreenSettings.add(rdbtnMain);
		rdbtnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getMainModeID() == -1)
					monitors.get(cbxMonitors.getSelectedIndex()).setWindowID(1);
				else
					rdbtnNone.setSelected(true);
				menu.getBooth().setRunEnabled(true);
				scrollPane.setVisible(true);
				btnLocation.setVisible(true);
				if (menu.getCS().getCurrentCam() == null) {
					lblWarning.setText("No camera selected!");
				} else {
					lblWarning.setText("");
				}
			}
		});
		group.add(rdbtnMain);
		
		cbxMonitors.setBounds(10, 21, 260, 20);
		panelScreenSettings.add(cbxMonitors);
		cbxMonitors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = cbxMonitors.getSelectedIndex();
				if (index > -1) {
					int windowID = monitors.get(index).getWindowID();
					txtLocation.setText(monitors.get(index).getPath());
					if (windowID == -1) {//nothing
						rdbtnNone.setSelected(true);
					} else if (windowID == 1) {//MainWindow
						rdbtnMain.setSelected(true);
					} else if (windowID == 0) {//SlideShow
						rdbtnSlideshow.setSelected(true);
					}
					int mainIndex = getMainModeID();
					if (mainIndex == -1 || mainIndex == index) {
						rdbtnMain.setEnabled(true);
					} else {
						rdbtnMain.setEnabled(false);
					}
				}
			}
		});
		cbxMonitors.setFocusable(false);
		
		JButton btnIdentify = new JButton("Identify");
		btnIdentify.setFocusable(false);
		btnIdentify.setBounds(10, 52, 90, 23);
		panelScreenSettings.add(btnIdentify);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFocusable(false);
		btnRefresh.setBounds(180, 52, 90, 23);
		panelScreenSettings.add(btnRefresh);
		
		btnLocation = new JButton("Change Load Images Location");
		btnLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = cbxMonitors.getSelectedIndex();
				if (index > -1) {
					JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(new File(".")); // start at application current directory
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File yourFolder = fc.getSelectedFile();
						monitors.get(index).setPath(yourFolder.getAbsolutePath());
						txtLocation.setText(monitors.get(index).getPath());
					}
				}
			}
		});
		btnLocation.setVisible(false);
		btnLocation.setBounds(10, 154, 256, 38);
		panelScreenSettings.add(btnLocation);
		
		scrollPane = new JScrollPane();
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBounds(10, 203, 256, 126);
		scrollPane.setVisible(false);
		panelScreenSettings.add(scrollPane);
		
		txtLocation.setEditable(false);
		txtLocation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(txtLocation);
		txtLocation.setForeground(Utils.textColor);
		txtLocation.setOpaque(false);
		txtLocation.setLineWrap(true);
		
		lblWarning = new JLabel("");
		lblWarning.setForeground(Color.RED);
		lblWarning.setHorizontalAlignment(SwingConstants.CENTER);
		lblWarning.setBounds(10, 129, 256, 14);
		panelScreenSettings.add(lblWarning);
		
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateDevices();
			}
		});
		btnIdentify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showScreenId();
			}
		});
	}
	
	public void setWarning(String warning) {
		lblWarning.setText(warning);
	}
	
	public void start() {
		GraphicsDevice gd = null;
		for (Monitor monitor : monitors) {
			int windowID = monitor.getWindowID();
			if (windowID != -1) {
				gd = monitor.getDevice();
				if (windowID == 0)
					monitor.setWindow(new SlideShow(menu));
				else if (windowID == 1)
					monitor.setWindow(new ModeWindow(menu, menu.getBooth().getPageMode()));
				monitor.getWindow().setPath(monitor.getPath());
				monitor.getWindow().start(gd.getDefaultConfiguration());
			}
		}
	}
	
	public boolean hasAMode() {
		for (int i = 0; i < monitors.size(); i++) {
			if (monitors.get(i).getWindowID() >= 0)
				return true;
		}
		return false;
	}
	
	public int getMainModeID() {
		for (int i = 0; i < monitors.size(); i++) {
			if (monitors.get(i).getWindowID() == 1)//MainWindow
				return i;
		}
		return -1;
	}
	
	public void updateDevices() {
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = g.getScreenDevices();
		monitors.clear();
		modelMonitors.removeAllElements();
		for (int i = 0; i < devices.length; i++) {
			Monitor slide = new Monitor(devices[i], 0);
			monitors.add(slide);
			modelMonitors.addElement("Screen " + slide.getId()  + ": " + slide.getDescription());
		}
		txtLocation.setText(monitors.get(0).getPath());
	}
	
	public static void showOnScreen(int screen, JFrame frame) {//example code, remove once done
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (screen > -1 && screen < gd.length) {
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
		} else if (gd.length > 0) {
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
		} else {
			throw new RuntimeException("No Screens Found");
		}
	}
	
	public void showScreenId() {
		setIdentifying(true);
	    for (Monitor monitor : monitors) {
	    	Thread loopThread = new Thread() {
				@Override
				public void run() {
					GraphicsDevice gd = monitor.getDevice();
					JFrame frame = new JFrame(gd.getDefaultConfiguration());
					frame.setLocation(frame.getX() + 20, getY() + 20);
					frame.setSize(200, 200);
					frame.getContentPane().setBackground(Utils.thirdColor);
					frame.setAlwaysOnTop(true);
					frame.setUndecorated(true);
					
					JLabel lblScreenId = new JLabel((monitor.getId()) + "");
					lblScreenId.setOpaque(false);
					lblScreenId.setFont(new Font("Arial", Font.BOLD, 72));
					lblScreenId.setHorizontalAlignment(SwingConstants.CENTER);
					
					frame.getContentPane().add(lblScreenId);
					frame.setVisible(true);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
					}
					frame.dispose();
					setIdentifying(false);
				}
	    	};
	    	loopThread.start();
	    }
	}

	public MainMenu getMenu() {
		return menu;
	}

	public void setMenu(MainMenu menu) {
		this.menu = menu;
	}

	public boolean isIdentifying() {
		return identifying;
	}

	public void setIdentifying(boolean identifying) {
		this.identifying = identifying;
	}
}
