package com.rijkv.breaktimer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.prefs.Preferences;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.text.PlainDocument;

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
		contentPanel.setBounds(0, 0, 400, 400);
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        contentPanel.add(panel);
        
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        
        ShowLabel("Settings");
        ShowLabel("Time between breaks");
        timeTextField = new JFormattedTextField(formatter);
        panel.add(timeTextField);
        ShowLabel("Break duration");
        durationTextField = new JFormattedTextField(formatter);
        panel.add(durationTextField);
        
        saveButton = new JButton("Save & Close");
        saveButton.addActionListener(new ActionListener()
        {
			  @SuppressWarnings("deprecation")
			  public void actionPerformed(ActionEvent e)
			  {
				  	Save();
				  	settingsFrame.hide();
			  }
        });
        panel.add(saveButton);
        
		settingsFrame = new JFrame(title);
		settingsFrame.add(contentPanel);
		settingsFrame.setSize(600, 800);
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
	private void Load()
	{
		timeTextField.setText(prefs.get(TIME_BETWEEN_BREAK_NAME, "0"));
		durationTextField.setText(prefs.get(BREAK_DURATION_NAME, "0"));
	}
	private void Save()
	{
		String value1 = timeTextField.getText();
		String value2 = durationTextField.getText();
		prefs.put(TIME_BETWEEN_BREAK_NAME, value1);
		prefs.put(BREAK_DURATION_NAME, value2);
	}
	private void ShowLabel(String text)
	{
		JLabel label = new JLabel(text);
		panel.add(label);
	}
}
