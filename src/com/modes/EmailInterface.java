package com.modes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.MainMenu;
import com.components.ImageBlock;
import com.components.ImageButton;
import com.utils.Utils;

@SuppressWarnings("serial")
public abstract class EmailInterface extends JPanel {
	
	protected ModeWindow userInterface = null;
	protected ImageBlock background;
	protected JTextField txtEmail;
	protected JLabel lblPhotos;
	protected JCheckBox cbxVerified;
	protected JButton btnEmail;
	protected JLabel lblPreview;
	protected ImageButton btnBack;
	
	public EmailInterface(ModeWindow ui) {
		this.userInterface = ui;
		initialize();
	}
	
	private void initialize() {
		setBounds(100, 100, 1280, 720);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));
		background = new ImageBlock("/com/images/background.png");
		add(background);
		background.setLayout(null);
		
		lblPreview = new JLabel("");
		lblPreview.setBounds(215, 120, 675, 450);
		background.add(lblPreview);
		
		btnEmail = new JButton("Email");
		btnEmail.setEnabled(false);
		btnEmail.setFont(Utils.bubbleFont.getFont());
		btnEmail.setBounds(1026, 219, 138, 66);
		background.add(btnEmail);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(922, 104, 332, 38);
		background.add(txtEmail);
		txtEmail.setColumns(10);
		
		lblPhotos = new JLabel("");
		lblPhotos.setBounds(1016, 248, 173, 14);
		background.add(lblPhotos);
		
		cbxVerified = new JCheckBox("I have verified that my email is correct.");
		cbxVerified.setOpaque(false);
		cbxVerified.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbxVerified.isSelected())
					btnEmail.setEnabled(true);
				else
					btnEmail.setEnabled(false);
			}
		});
		cbxVerified.setForeground(Color.WHITE);
		cbxVerified.setBounds(922, 158, 282, 23);
		background.add(cbxVerified);
		
		btnBack = new ImageButton(MainMenu.class.getResource("images/back.png"));
		btnBack.setFocusable(false);
		btnBack.setBackground(new Color(70, 72, 102));
		btnBack.setBounds(70, 200, 80, 200);
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				btnEmail.setEnabled(false);
				cbxVerified.setSelected(false);
				userInterface.changeScene(userInterface.getMode());
			}
		});
		
		background.add(btnBack);
	}
	
	public void update() {
		
	}
	
	public void reset() {
		btnEmail.setEnabled(false);
		cbxVerified.setSelected(false);
		txtEmail.setText("");
	}
}
