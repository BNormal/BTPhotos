package com.modes.snap;

import com.modes.EmailInterface;
import com.modes.Page;
import com.modes.ModeWindow;
import com.utils.Email;
import com.utils.Utils;

import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class EmailSnap extends EmailInterface {

	/**
	 * Create the panel.
	 */
	public EmailSnap(ModeWindow ui) {
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
	}
	
	public void update() {
		BufferedImage image = Utils.copyImage(Utils.resize(userInterface.getImage(), lblPreview.getWidth(), lblPreview.getHeight()));
		if (image != null)
			lblPreview.setIcon(new ImageIcon(image));
	}
}
