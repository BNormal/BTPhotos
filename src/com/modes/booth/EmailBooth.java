package com.modes.booth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.modes.EmailInterface;
import com.modes.Page;
import com.modes.ModeWindow;
import com.utils.Email;
import com.utils.Utils;

@SuppressWarnings("serial")
public class EmailBooth extends EmailInterface {

	public EmailBooth(ModeWindow ui) {
		super(ui);
		initialize();
	}
	
	private void initialize() {
		btnEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Email email = new Email(txtEmail.getText());
				if (email.verifyEmail()) {
					btnEmail.setEnabled(false);
					if (userInterface.getImage() != null) {
						userInterface.setEmail(email);
						userInterface.changeScene(Page.PROCESSMODE);
						reset();
						userInterface.startProcess();
					}
				} else
					;//display error
			}
		});
		for (MouseListener ml : btnBack.getMouseListeners())
			btnBack.removeMouseListener(ml);
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				btnEmail.setEnabled(false);
				cbxVerified.setSelected(false);
				userInterface.changeScene(Page.COLLAGEPAGE);
			}
		});
	}
	
	public void update() {
		BufferedImage image = userInterface.getImage();
		if (image != null)
			lblPreview.setIcon(new ImageIcon(Utils.copyImage(Utils.resize(image, lblPreview.getWidth(), lblPreview.getHeight()))));
	}
}
