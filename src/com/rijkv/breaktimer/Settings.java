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
	private JButton bgButton;
	private JButton fgButton;
	
	private Color bgColor = Color.BLACK;
	private Color fgColor = Color.WHITE;
	
	private JFormattedTextField timeTextField;
	private JFormattedTextField durationTextField;
	private JFormattedTextField skipTimeTextField;
	private JFormattedTextField timeRangeTextField;
	
	static Preferences prefs;

	final static String TIME_BETWEEN_BREAK_NAME = "time_between_break";
	final static String BREAK_DURATION_NAME = "break_duration";
	final static String SKIP_TIME = "skip_time";
	final static String BG_COLOR = "bg_color";
	final static String FG_COLOR = "fg_color";
	final static String TIME_RANGE = "time_range";
	
	void SetupPrefs()
	{
		sun.util.logging.PlatformLogger platformLogger = sun.util.logging.PlatformLogger.getLogger("java.util.prefs");
		platformLogger.setLevel(sun.util.logging.PlatformLogger.Level.OFF);
		
		prefs = Preferences.userNodeForPackage(com.rijkv.breaktimer.Settings.class);
	}
	public Settings(String title) {
		SetupPrefs();
		
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
        ShowLabel("Skip time");
        skipTimeTextField = new JFormattedTextField();
        panel.add(skipTimeTextField);
        ShowLabel("Time Range");
        timeRangeTextField = new JFormattedTextField();
        panel.add(timeRangeTextField);
        
        bgButton = new JButton("Select background color");
        bgButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				bgColor = JColorChooser.showDialog( settingsFrame,
	                       "Choose background color", bgColor );
	   
	                 if (bgColor == null )
	                	 bgColor = Color.black;
			}
        });
        panel.add(bgButton);
        
        fgButton = new JButton("Select foreground color");
        fgButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fgColor = JColorChooser.showDialog( settingsFrame,
	                       "Choose foreground color", fgColor );
	   
	                 if (fgColor == null )
	                	 fgColor = Color.white;
			}
        });
        panel.add(fgButton);
        
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
		settingsFrame.setSize(300, 400);
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
	public static Color getFGColor()
	{
		return Color.decode(prefs.get(FG_COLOR, "-1"));
	}
	public static Color getBGColor()
	{
		return Color.decode(prefs.get(BG_COLOR, "-1"));
	}
	public static int getSkipTime()
	{
		return Integer.parseInt(prefs.get(SKIP_TIME, "0"));
	}
	public static String getTimeRange()
	{
		return prefs.get(TIME_RANGE, "00:00:00-23:59:59");
	}
	
	public static int getBreakDuration()
	{
		return Integer.parseInt(prefs.get(BREAK_DURATION_NAME, "0"));
	}
	private void Load()
	{
		timeTextField.setText(prefs.get(TIME_BETWEEN_BREAK_NAME, "0"));
		durationTextField.setText(prefs.get(BREAK_DURATION_NAME, "0"));
		skipTimeTextField.setText(prefs.get(SKIP_TIME, "0"));
		timeRangeTextField.setText(prefs.get(TIME_RANGE, "-"));
		bgColor = Color.decode(prefs.get(BG_COLOR, "-1"));
		fgColor = Color.decode(prefs.get(FG_COLOR, "-1"));
	}
	@SuppressWarnings("deprecation")
	private void Save()
	{
		String value1 = timeTextField.getText();
		String value2 = durationTextField.getText();
		String value3 = skipTimeTextField.getText();
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
		}
		if (isNumeric(value3))
		{
			prefs.put(SKIP_TIME, value3);
		} else {
			return;
		}
		prefs.put(TIME_RANGE, timeRangeTextField.getText());
		prefs.put(BG_COLOR, String.valueOf(bgColor.getRGB()));
		prefs.put(FG_COLOR, String.valueOf(fgColor.getRGB()));
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
