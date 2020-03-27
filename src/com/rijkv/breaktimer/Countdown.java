package com.rijkv.breaktimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	
	private Break breakWindow = new Break(this);
	private Reminder reminder = new Reminder(this);
	private GamePopup gamePopup = new GamePopup(this);
	
	private int reminderTime = 20;
	private boolean didReminder = false;
	
	private MouseListener mouseListener;
	private KeyListener keyListener;
	private boolean didDelay = false;
	
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
				currentLabel.setText("BREAK " + formatHHMMSS(countdown));
				break;
			case Countdown:
				currentLabel.setText("BREAK OVER " + formatHHMMSS(countdown));
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
	
	public void Delay(int seconds)
	{
		if (!didDelay)
		{			
			countdown += seconds;
			didDelay = true;
			didReminder = false;
		}
	}
	
	public boolean canDelay()
	{
		return !didDelay;
	}
	
	public void EnablePassiveMode()
	{
		breakWindow.passiveMode = true;
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
                	KillProcesses();
                	if (state == CountdownState.Countdown)
                	{
                		if (keyListener.isKeyboardUsed() || mouseListener.isMouseUsed())
                			inactiveTime = 0;
                		else
                			inactiveTime++;
                		
                		
                		if (CheckTime())
                    	{
                			if (inactiveTime <= maxInactiveTime)
                    			countdown--;
                			
                			if (inactiveTime >= Settings.getBreakDuration())
                				Reset();
                			
//                    		reminderTime = Integer.parseInt(Settings.getReminderTime());
//                    		if (countdown <= reminderTime)
//                        	{
//                        		reminder.SetTime(countdown);
//                        		if (!didReminder) 
//                        		{
//                        			didReminder = true;
//                            		//reminder.Open();
//                        		}
//                        	}
                    	}
                	}
                	else if (state == CountdownState.Break)
                	{
                		if (!keyListener.isKeyboardUsed() || !mouseListener.isMouseUsed())
                		{
                    		countdown--;
                		}

                		breakWindow.SetTime(countdown);
                	}
                	
                	Display();
                }
            }
        }, delay, period);
	}
	

	private void Reset()
	{
		state = CountdownState.Countdown;
		countdown = Settings.getTimeBetweenBreak();
		didReminder = false;
		breakWindow.Close();
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
				didDelay = false;
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
					// TODO: Actually close program if out of time range
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
	
	void KillProcesses()
	{
		String line;
		String pidInfo ="";

		Process p = null;
		try {
			p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

		try {
			while ((line = input.readLine()) != null) {
			    pidInfo+=line; 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final String[] GAME_PROCESSES = { "csgo.exe", "MinecraftLauncher.exe", "Seum.exe", "Cities.exe", "ravenfield.exe", "insurgency_x64.exe" };
		
		for(var process : GAME_PROCESSES)
		{
			if(pidInfo.contains(process))
			{
				ForceKillProcess(process);
				gamePopup.Open(process);
			}
		}
	}
	
	public static void ForceKillProcess(String processName)
	{
		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + processName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
