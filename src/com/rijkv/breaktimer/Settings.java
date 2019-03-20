package com.rijkv.breaktimer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.prefs.Preferences;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

import com.sun.xml.internal.ws.util.StringUtils;

public class Settings {
	
	private JFrame settingsFrame;
	private JPanel contentPanel;
	private JPanel panel;
	private JButton saveButton;
	
	private JFormattedTextField timeTextField;
	private JFormattedTextField durationTextField;
	
	static Preferences prefs;

	final static String TIME_BETWEEN_BREAK_NAME = "time_between_break";
	final static String BREAK_DURATION_NAME = "break_duration";
	
	public Settings(String title) {
		sun.util.logging.PlatformLogger platformLogger = sun.util.logging.PlatformLogger.getLogger("java.util.prefs");
		platformLogger.setLevel(sun.util.logging.PlatformLogger.Level.OFF);
		
		prefs = Preferences.userNodeForPackage(com.rijkv.breaktimer.Settings.class);
		
		contentPanel = new JPanel();
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        contentPanel.add(panel);

        ShowLabel("Time between breaks");
        timeTextField = new JFormattedTextField();
        panel.add(timeTextField);
        ShowLabel("Break duration");
        durationTextField = new JFormattedTextField();
        panel.add(durationTextField);
        
        saveButton = new JButton("Save & Close");
        saveButton.addActionListener(new ActionListener()
        {
			  public void actionPerformed(ActionEvent e)
			  {
				  	Save();
			  }
        });
        panel.add(saveButton);
        
        
        
		settingsFrame = new JFrame(title);
		
		settingsFrame.add(contentPanel);
		settingsFrame.setSize(300, 300);
		settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Load();
	}
	public void Open()
	{
		settingsFrame.setVisible(true);
	}
	public static int getTimeBetweenBreak()
	{
		return Integer.parseInt(prefs.get(TIME_BETWEEN_BREAK_NAME, "0"));
	}
	public static int getBreakDuration()
	{
		return Integer.parseInt(prefs.get(BREAK_DURATION_NAME, "0"));
	}
	public static String getBreakSoundPath()
	{
		return "C:\\Users\\rijkv\\Downloads\\Explosion.wav";
	}
	private void Load()
	{
		timeTextField.setText(prefs.get(TIME_BETWEEN_BREAK_NAME, "0"));
		durationTextField.setText(prefs.get(BREAK_DURATION_NAME, "0"));
	}
	@SuppressWarnings("deprecation")
	private void Save()
	{
		String value1 = timeTextField.getText();
		String value2 = durationTextField.getText();
		if (isNumeric(value1))
		{
			prefs.put(TIME_BETWEEN_BREAK_NAME, value1);
		} else {
			return;
		}
		if (isNumeric(value2))
		{
			prefs.put(BREAK_DURATION_NAME, value2);
		} else {
			return;
		}
	  	settingsFrame.hide();
	}
	public static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
	private void ShowLabel(String text)
	{
		JLabel label = new JLabel(text);
		panel.add(label);
	}
}
