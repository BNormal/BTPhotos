package com.cards;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import com.MainMenu;
import com.components.RadioButton;
import com.modes.Page;
import com.utils.Utils;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class BoothSettings extends JPanel {

	private MainMenu mm;
	private ButtonGroup boothGroup;
    private JButton btnRun;

	public BoothSettings(MainMenu mm) {
		this.mm = mm;
		initialize();
	}
	
	/**
	 * Draws the background to this JPanel
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage sprite = null;
		try {
			sprite = ImageIO.read(this.getClass().getResource("/com/images/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(sprite, 0, 0, null);
	}
	
	/**
	 * Loads all the components needed for this custom JPanel
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		setBackground(Utils.backgroundColor);
		setLayout(null);
		setBounds(100, 100, 700, 400);
		
		JPanel panelPBM = new JPanel();
		panelPBM.setOpaque(false);
		//panelPBM.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Utils.highlightColor), "Photo Modes", TitledBorder.CENTER, TitledBorder.TOP, null, Utils.textColor)); //changed for consistency
		panelPBM.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Utils.highlightColor), "Modes", TitledBorder.CENTER, TitledBorder.TOP, null, Utils.textColor));
		panelPBM.setBounds(10, 10, 330, 115);
		
		add(panelPBM);
		panelPBM.setLayout(null);
		
		RadioButton panelDefault = new RadioButton(BoothSettings.class.getResource("/com/images/snap.png"));
		panelDefault.setSelected(true);
		panelDefault.setBounds(50, 36, 50, 50);
		panelPBM.add(panelDefault);
		
		RadioButton paneGIF = new RadioButton(BoothSettings.class.getResource("/com/images/gif.png"));
		paneGIF.setBounds(140, 36, 50, 50);
		panelPBM.add(paneGIF);
		
		RadioButton panel2x4 = new RadioButton(BoothSettings.class.getResource("/com/images/strip.png"));
		panel2x4.setBounds(230, 36, 50, 50);
		panelPBM.add(panel2x4);
		
		boothGroup = new ButtonGroup();
		boothGroup.add(panelDefault);
		boothGroup.add(paneGIF);
		boothGroup.add(panel2x4);
		
		JLabel lblDefaultxAnd = new JLabel("Snap Mode");
		lblDefaultxAnd.setHorizontalAlignment(SwingConstants.CENTER);
		lblDefaultxAnd.setForeground(Utils.textColor);
		lblDefaultxAnd.setFont(new Font("Arial", Font.PLAIN, 11));
		lblDefaultxAnd.setBounds(45, 90, 60, 14);
		panelPBM.add(lblDefaultxAnd);
		
		JLabel lblGifBooth = new JLabel("GIF Mode");
		lblGifBooth.setHorizontalAlignment(SwingConstants.CENTER);
		lblGifBooth.setForeground(Utils.textColor);
		lblGifBooth.setFont(new Font("Arial", Font.PLAIN, 11));
		lblGifBooth.setBounds(140, 90, 50, 14);
		panelPBM.add(lblGifBooth);
		
		JLabel lblxOnly_1 = new JLabel("Booth Mode");
		lblxOnly_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblxOnly_1.setForeground(Utils.textColor);
		lblxOnly_1.setFont(new Font("Arial", Font.PLAIN, 11));
		lblxOnly_1.setBounds(225, 90, 60, 14);
		panelPBM.add(lblxOnly_1);
		
		/*JButton btnLogo = new JButton("Select Logo"); //I'm pretty sure we don't need this unless you want to put it in.
		btnLogo.setFocusable(false);
		btnLogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		})
		btnLogo.setBounds(10, 136, 150, 23);
		add(btnLogo);*/
		
		MonitorSettings monitorSettings = new MonitorSettings(mm);
		monitorSettings.setLocation(390, 5);
		MonitorSettings panelMonitor = (mm == null ? monitorSettings : mm.getMS());
		panelMonitor.setBounds(390, 5, 290, 350);
		add(panelMonitor);
		
		btnRun = new JButton("Run");
		btnRun.setFocusable(false);
		btnRun.setEnabled(false);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mm.getMS().start();
			}
		});
		btnRun.setBounds(130, 136, 89, 23);
		add(btnRun);
	}
	
	public int getMode() {
		return Utils.getSelectedId(boothGroup);
	}
	
	public Page getPageMode() {
		return Page.values()[Utils.getSelectedId(boothGroup)];
	}
	
	public void setRunEnabled(boolean enabled) {
		btnRun.setEnabled(enabled);
	}
}
