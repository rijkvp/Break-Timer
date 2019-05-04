package com.rijkv.breaktimer;

import java.util.TimerTask;

import javax.swing.JLabel;

import java.time.*;
import java.util.Timer;

enum CountdownState
{ 
    Countdown, Break; 
} 

public class Countdown {	
	
	private CountdownState state = CountdownState.Countdown;
	private int countdown;
	private JLabel currentLabel;
	
	private Break breakWindow = new Break("Break", this);
	
	public Countdown() {
		
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
				currentLabel.setText("In Break: " + Integer.toString(countdown) + "s");
				break;
			case Countdown:
				currentLabel.setText("Break over: " + Integer.toString(countdown) + "s");
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
                    Switch();
                    time.cancel();
                    time.purge();
                } else {
                	countdown--;
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
		// Test values for now:
		LocalTime start = LocalTime.parse( "09:30:00" );
		LocalTime stop = LocalTime.parse( "19:15:00" );	
		
		Boolean inRange = (LocalTime.now().isAfter(start) && LocalTime.now().isBefore(stop));
		
		if(!inRange)
		{
			return false;
		}
		return true;
	}
	
	private void Switch()
	{
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
				}
				break;
			default:
				break;
		}
		Start();
	}
}
