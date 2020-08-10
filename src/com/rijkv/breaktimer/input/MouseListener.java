package com.rijkv.breaktimer.input;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class MouseListener implements NativeMouseInputListener {
	
	boolean mouseUse = false;
	
	public void nativeMouseClicked(NativeMouseEvent e) {
		mouseUse = true;
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		mouseUse = true;
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		mouseUse = true;
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		mouseUse = true;
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		mouseUse = true;
	}
	
	public boolean isMouseUsed()
	{
		if (mouseUse)
		{
			mouseUse = false;
			return true;
		}
		else
		{
			return false;
		}
	}
}