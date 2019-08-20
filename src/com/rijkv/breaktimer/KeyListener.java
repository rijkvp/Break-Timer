package com.rijkv.breaktimer;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {
	
	private boolean keyboardUse = false;
	
	public void nativeKeyPressed(NativeKeyEvent e) {
		keyboardUse = true;
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		keyboardUse = true;
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		keyboardUse = true;
	}
	
	public boolean isKeyboardUsed()
	{
		if (keyboardUse)
		{
			keyboardUse = false;
			return true;
		}
		else
		{
			return false;
		}
	}
}
