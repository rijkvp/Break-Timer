package com.rijkv.breaktimer;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class MouseListener implements NativeMouseInputListener {
	
	boolean mouseUse = false;
	
	public void nativeMouseClicked(NativeMouseEvent e) {
		//System.out.println("Mouse Clicked: " + e.getClickCount());
		mouseUse = true;
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		//System.out.println("Mouse Pressed: " + e.getButton());
		mouseUse = true;
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		//System.out.println("Mouse Released: " + e.getButton());
		mouseUse = true;
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		//System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
		mouseUse = true;
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		//System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
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
	
	public static void main(String[] args) {
		
	}
}