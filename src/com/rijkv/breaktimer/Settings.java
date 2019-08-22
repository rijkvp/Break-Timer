package com.rijkv.breaktimer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("deprecation")
public class Settings {
	
	private JFrame settingsFrame;
	private JPanel contentPanel;
	private JPanel panel;
	private JButton saveButton;
	private JButton bgButton;
	private JButton fgButton;
	
	private Color bgColorSetting = Color.BLACK;
	private Color fgColorSetting = Color.WHITE;
	
	private JFormattedTextField timeTextField;
	private JFormattedTextField durationTextField;
	private JFormattedTextField skipTimeTextField;
	private JFormattedTextField timeRangeTextField;
	private JFormattedTextField reminderTimeTextField;
	private JFormattedTextField breakTitleTextField;
	private JFormattedTextField breakTextTextField;
	private JFormattedTextField fontNameTextField;
	private JCheckBox forceModeCheckBox;
	private JCheckBox randomModeCheckBox;
	
	static Preferences prefs;

	final static String TIME_BETWEEN_BREAK_NAME = "time_between_break";
	final static String BREAK_DURATION_NAME = "break_duration";
	final static String SKIP_TIME = "skip_time";
	final static String BG_COLOR = "bg_color";
	final static String FG_COLOR = "fg_color";
	final static String TIME_RANGE = "time_range";
	final static String REMINDER_TIME = "reminder_time";
	final static String BREAK_TITLE_TEXT = "break_title_text";
	final static String BREAK_TEXT = "break_text";
	final static String FONT_NAME = "font_name";
	final static String FORCE_MODE = "force_mode";
	final static String RANDOM_MODE = "random_mode";
	
	Color bgColor; 
	Color textColor;
	
	public Settings(String title) {
		SetupPrefs();
		
		bgColor = getBGColor();
		textColor = getFGColor();
		
		contentPanel = new JPanel();
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,2);
		gridLayout.setVgap(5);
		gridLayout.setHgap(10);
        panel.setLayout(gridLayout);
        contentPanel.add(panel);
        
        // CREATE TEXT FIELDS & CHECK BOXES
        timeTextField = CreateTextField(timeTextField, "Time between breaks");
        durationTextField = CreateTextField(durationTextField, "Beak duration");
        skipTimeTextField = CreateTextField(skipTimeTextField, "Skip time");
        reminderTimeTextField = CreateTextField(skipTimeTextField, "Reminder time");
        timeRangeTextField = CreateTextField(timeRangeTextField, "Time range");
        breakTitleTextField = CreateTextField(breakTitleTextField, "Break title text");
        breakTextTextField = CreateTextField(breakTextTextField, "Break text");
        fontNameTextField = CreateTextField(fontNameTextField, "Break font name");
        forceModeCheckBox = CreateCheckBox(forceModeCheckBox, "Force mode");
        randomModeCheckBox = CreateCheckBox(randomModeCheckBox, "Random mode");
        
        bgButton = CreateButton(bgButton, "Select background color");
        bgButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				bgColorSetting = JColorChooser.showDialog( settingsFrame,
	                       "Choose background color", bgColorSetting );
	   
	                 if (bgColorSetting == null )
	                	 bgColorSetting = Color.black;
			}
        });
        panel.add(bgButton);
        
        fgButton = CreateButton(fgButton, "Select foreground color");
        fgButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fgColorSetting = JColorChooser.showDialog( settingsFrame,
	                       "Choose foreground color", fgColorSetting );
	   
	                 if (fgColorSetting == null )
	                	 fgColorSetting = Color.white;
			}
        });
        panel.add(fgButton);
        
        saveButton = CreateButton(saveButton, "Save");
        saveButton.addActionListener(new ActionListener()
        {
			  public void actionPerformed(ActionEvent e)
			  {
				  	Save();
				  	Close();
			  }
        });
        panel.add(saveButton);
        
        panel.setBackground(bgColor);
        contentPanel.setBackground(bgColor);
        
		settingsFrame = new JFrame(title);
        
		settingsFrame.add(contentPanel);
		settingsFrame.setSize(450, 450);
		settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Load();
	}
	
	public void Open()
	{
		settingsFrame.setVisible(true);
	}
	
	public void Close()
	{
		settingsFrame.hide();
	}
	
	void SetupPrefs()
	{
		sun.util.logging.PlatformLogger platformLogger = sun.util.logging.PlatformLogger.getLogger("java.util.prefs");
		platformLogger.setLevel(sun.util.logging.PlatformLogger.Level.OFF);
		
		prefs = Preferences.userNodeForPackage(com.rijkv.breaktimer.Settings.class);
	}
	

	private void Load()
	{
		timeTextField.setText(prefs.get(TIME_BETWEEN_BREAK_NAME, "0"));
		durationTextField.setText(prefs.get(BREAK_DURATION_NAME, "0"));
		skipTimeTextField.setText(prefs.get(SKIP_TIME, "0"));
		timeRangeTextField.setText(prefs.get(TIME_RANGE, "-"));
		reminderTimeTextField.setText(prefs.get(REMINDER_TIME, "20"));
		bgColorSetting = Color.decode(prefs.get(BG_COLOR, "-1"));
		fgColorSetting = Color.decode(prefs.get(FG_COLOR, "-1"));
		breakTitleTextField.setText(prefs.get(BREAK_TITLE_TEXT, "Break"));
		breakTextTextField.setText(prefs.get(BREAK_TEXT, "Have a nice break!"));
		fontNameTextField.setText(prefs.get(FONT_NAME, "Arial"));
		forceModeCheckBox.setSelected(Boolean.parseBoolean(prefs.get(FORCE_MODE, "false")));
		randomModeCheckBox.setSelected(Boolean.parseBoolean(prefs.get(RANDOM_MODE, "false")));
	}
	
	private void Save()
	{
		if (isNumeric(timeTextField.getText()))
		{
			prefs.put(TIME_BETWEEN_BREAK_NAME, timeTextField.getText());
		} 
		if (isNumeric(durationTextField.getText()))
		{
			prefs.put(BREAK_DURATION_NAME, durationTextField.getText());
		} 
		if (isNumeric(skipTimeTextField.getText()))
		{
			prefs.put(SKIP_TIME, skipTimeTextField.getText());
		} 
		if (isNumeric(reminderTimeTextField.getText()))
		{
			prefs.put(REMINDER_TIME, reminderTimeTextField.getText());
		}
		prefs.put(BREAK_TITLE_TEXT, breakTitleTextField.getText());
		prefs.put(BREAK_TEXT, breakTextTextField.getText());
		prefs.put(FONT_NAME, fontNameTextField.getText());
		prefs.put(BG_COLOR, String.valueOf(bgColorSetting.getRGB()));
		prefs.put(FG_COLOR, String.valueOf(fgColorSetting.getRGB()));
		prefs.put(TIME_RANGE, timeRangeTextField.getText());
		prefs.put(FORCE_MODE, Boolean.toString(forceModeCheckBox.isSelected()));
		prefs.put(RANDOM_MODE, Boolean.toString(randomModeCheckBox.isSelected()));
	}
	
	// Utilities
	
	public static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
	}
	
	// UI Shortcuts
	
	private void ShowLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(textColor);
		panel.add(label);
	}
	
	private JButton CreateButton(JButton button, String text)
	{
		button = new JButton(text);
		button.setForeground(textColor);
		button.setBackground(bgColor);
		return button;
	}
	
	private JFormattedTextField CreateTextField(JFormattedTextField textField, String text)
	{
		ShowLabel(text);
		textField = new JFormattedTextField();
		textField.setForeground(textColor);
		textField.setBackground(bgColor);
		panel.add(textField);
		return textField;
	}
	
	private JCheckBox CreateCheckBox(JCheckBox checkBox, String text)
	{
		ShowLabel(text);
		checkBox = new JCheckBox();
		checkBox.setForeground(textColor);
		checkBox.setBackground(bgColor);
		panel.add(checkBox);
		return checkBox;
	}
	
	
	// Getters
	
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
		return prefs.get(TIME_RANGE, "");
	}
	
	public static String getReminderTime()
	{
		return prefs.get(REMINDER_TIME, "20");
	}
	
	public static int getBreakDuration()
	{
		return Integer.parseInt(prefs.get(BREAK_DURATION_NAME, "0"));
	}
	
	public static String getBreakTitleText()
	{
		return prefs.get(BREAK_TITLE_TEXT, "Break");
	}
	
	public static String getBreakText()
	{
		return prefs.get(BREAK_TEXT, "Have a nice break!");
	}
	
	public static String getFontName()
	{
		return prefs.get(FONT_NAME, "Arial");
	}
	
	public static boolean getForceMode()
	{
		return Boolean.parseBoolean(prefs.get(FORCE_MODE, "false"));
	}
	
	public static boolean getRandomMode()
	{
		return Boolean.parseBoolean(prefs.get(RANDOM_MODE, "false"));
	}
}