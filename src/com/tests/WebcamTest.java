package com.tests;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.sarxos.webcam.Webcam;
import com.utils.Utils;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WebcamTest {

	private JFrame frame;
	private Webcam webcam = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WebcamTest window = new WebcamTest();
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
	public WebcamTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblPhoto = new JLabel("");
		lblPhoto.setBounds(88, 11, 240, 180);
		frame.getContentPane().add(lblPhoto);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (webcam != null) {
					Thread snapThread = new Thread() {
						@Override
						public void run() {
							if (!webcam.isOpen() && !webcam.getLock().isLocked()) {
								webcam.open();
								lblPhoto.setIcon(new ImageIcon(Utils.copyImage(Utils.resize(webcam.getImage(), lblPhoto.getWidth(), lblPhoto.getHeight()))));
								webcam.close();
							}
						}
					};
					snapThread.start();
				}
			}
		});
		btnNewButton.setBounds(160, 227, 89, 23);
		frame.getContentPane().add(btnNewButton);
		Thread initThread = new Thread() {
			@Override
			public void run() {
				webcam = Webcam.getDefault();
				if (webcam != null) {
					webcam.setViewSize(webcam.getViewSizes()[webcam.getViewSizes().length - 1]);
				}
			}
		};
		initThread.start();
	}
}
