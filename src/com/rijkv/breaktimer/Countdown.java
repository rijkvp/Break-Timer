package com.rijkv.breaktimer;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
//import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

enum CountdownState
{ 
    Countdown, Break; 
} 

public class Countdown {	
	
	private CountdownState state = CountdownState.Countdown;
	private int countdown;
	private int inactiveTime;
	private final int maxInactiveTime = 5;
	private JLabel currentLabel;
	
	private Break breakWindow = new Break("Break", this);
	private Reminder reminder = new Reminder("Break Reminder");
	
	private int reminderTime = 20;
	private boolean didReminder = false;
	
	private MouseListener mouseListener;
	private KeyListener keyListener;
	
	public Countdown() {
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		// Mouse listener
		mouseListener = new MouseListener();
		GlobalScreen.addNativeMouseListener(mouseListener);
		GlobalScreen.addNativeMouseMotionListener(mouseListener);
		
		// Key listener
		keyListener = new KeyListener();
		GlobalScreen.addNativeKeyListener(keyListener);
	}
	
	public void SetLabel(JLabel label)
	{
		currentLabel = label;
	}
	public boolean CanSkip()
	{
		return (countdown < Settings.getBreakDuration() - Settings.getSkipTime());
	}
	
	private void Display()
	{
		switch(state)
		{
			case Break:
				currentLabel.setText("Break " + formatHHMMSS(countdown));
				break;
			case Countdown:
				currentLabel.setText("Break over " + formatHHMMSS(countdown));
				break;
			default:
				break;
		}
	}
	
	public void Setup()
	{
		state = CountdownState.Countdown;
		countdown = Settings.getTimeBetweenBreak();
		Start();
	}
	
	private void Start()
	{
		int delay = 1000;
        int period = 1000;
        final Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (countdown == 0) {
                	reminder.Close();
                    Switch();
                    time.cancel();
                    time.purge();
                } 
                else 
                {
                	if (state == CountdownState.Countdown)
                	{
                		if (keyListener.isKeyboardUsed() || mouseListener.isMouseUsed())
                			inactiveTime = 0;
                		else
                			inactiveTime++;
                		
                		if (!(inactiveTime > maxInactiveTime))
                			countdown--;
                	}
                	else
                	{
                		if (!keyListener.isKeyboardUsed() || !mouseListener.isMouseUsed())
                		{
                    		countdown--;
                		}
                	}
                	
                	
                	reminderTime = Integer.parseInt(Settings.getReminderTime());
                	if (countdown <= reminderTime)
                	{
                		reminder.SetTime(countdown);
                		if (!didReminder) 
                		{
                			didReminder = true;
                    		reminder.Open();
                		}
                	}
                	
                	Display();
                	
                	if (state == CountdownState.Break)
                		breakWindow.SetTime(countdown);
                }
            }
        }, delay, period);
	}
	public void BreakNow()
	{
		countdown = 0;
	}
	public void ForceStop()
	{
		countdown = 0;
	}
	private boolean CheckTime()
	{
		String input = Settings.getTimeRange();
		
		if (input == "-" || input == "" || input == null)
			return true;
		
		String[] split = input.split("-");
		if (split.length != 2)
		{
			System.out.println("UNABLE TO SPLIT TIME RANGE!!");
			return true;
		}
		
		String value1 = split[0];
		String value2 = split[1];
		
		
		LocalTime start = null;
		LocalTime stop = null;
		
		try
		{
			start = LocalTime.parse(value1);
		}
		catch (DateTimeParseException e)
		{
			System.out.println("UNABLE TO PARSE TIME!!! VALUE1: " + value1);
			return true;
		}
		try
		{
			stop = LocalTime.parse(value2);
		}
		catch (DateTimeParseException e)
		{
			System.out.println("UNABLE TO PARSE TIME!!! VALUE2: " + value2);
			return true;
		}
		
		Boolean inRange = (LocalTime.now().isAfter(start) && LocalTime.now().isBefore(stop));
		
		if(!inRange)
		{
			return false;
		}
		return true;
	}
	
	private void Switch()
	{
		didReminder = false;
		switch(state)
		{
			case Break:
				state = CountdownState.Countdown;
				countdown = Settings.getTimeBetweenBreak();
				breakWindow.Close();
				break;
			case Countdown:
				if (CheckTime()) {
					state = CountdownState.Break;
					countdown = Settings.getBreakDuration();
					breakWindow.Open();
					breakWindow.SetTime(countdown);
				} else {
					state = CountdownState.Countdown;
					countdown = Settings.getTimeBetweenBreak();
					breakWindow.Close();
					didReminder = true;
				}
				break;
			default:
				break;
		}
		Start();
	}
	
	public static String formatHHMMSS(long secondsCount){
	    int seconds = (int) (secondsCount % 60);
	    secondsCount -= seconds;
	    long minutesCount = secondsCount / 60;
	    long minutes = minutesCount % 60;
	    minutesCount -= minutes;
	    long hoursCount = minutesCount / 60;
	    if (hoursCount != 0)
	    	return String.format("%02d", hoursCount) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
	    else
	    	return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
	}
}
