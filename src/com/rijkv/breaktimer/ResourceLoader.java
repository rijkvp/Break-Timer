package com.rijkv.breaktimer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class ResourceLoader {
	private static Font defaultFont;
	private static Font defaultBoldFont;
	private static Color bgColor;
	private static Color textColor;
	
	static {
		try {
		    defaultFont = Font.createFont(Font.TRUETYPE_FONT, new File("./assets/font/RobotoCondensed-Regular.ttf")).deriveFont(12f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(defaultFont);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
		try {
		    defaultBoldFont = Font.createFont(Font.TRUETYPE_FONT, new File("./assets/font/RobotoCondensed-Bold.ttf")).deriveFont(12f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(defaultBoldFont);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
		bgColor = Settings.getBGColor();
		textColor = Settings.getFGColor();
	}
	
	public static Font getDefaultFont() {
		return defaultFont;
	}
	
	public static Font getDefaultFont(float size) {
		return defaultFont.deriveFont(size);
	}
	
	public static Font getDefaultBoldFont() {
		return defaultBoldFont;
	}
	
	public static Font getDefaultBoldFont(float size) {
		return defaultBoldFont.deriveFont(size);
	}
	
	public static Color getBGColor() {
		return bgColor;
	}
	
	public static Color getTextColor() {
		return textColor;
	}
	
	public static Border getDefaultButtonBorder()
	{
		return BorderFactory.createLineBorder(textColor, 2);
	}
}
