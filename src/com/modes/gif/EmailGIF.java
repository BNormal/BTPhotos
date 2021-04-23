package com.modes.gif;

import com.modes.EmailInterface;
import com.modes.Page;
import com.modes.ModeWindow;
import com.utils.Email;

import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;

@SuppressWarnings("serial")
public class EmailGIF extends EmailInterface {

	/**
	 * Create the panel.
	 */
	public EmailGIF(ModeWindow ui) {
		super(ui);
		initialize();
	}
	
	private void initialize() {
		btnEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Email email = new Email(txtEmail.getText());
				if (email.verifyEmail()) {
					btnEmail.setEnabled(false);
					if (userInterface.getGifImage() != null) {
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
		try {
			if (userInterface.getGifImage() != null)
				lblPreview.setIcon(new ImageIcon(userInterface.getGifImage().toURI().toURL()));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}
}
