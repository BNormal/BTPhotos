package com.utils;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListener implements NativeKeyListener {
	
	public void takePicture() {
		
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {

	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		String key = NativeKeyEvent.getKeyText(arg0.getKeyCode()).toLowerCase();
		if (key.equals("enter")) {
			takePicture();
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {

	}
}
