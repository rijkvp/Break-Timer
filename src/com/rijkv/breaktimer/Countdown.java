package com.rijkv.breaktimer;

import java.util.TimerTask;

import javax.swing.JLabel;

import java.util.Timer;

enum CountdownState
{ 
    Countdown, Break; 
} 

public class Countdown {	
	
	private CountdownState state = CountdownState.Countdown;
	private int countdown;
	private JLabel currentLabel;
	
	public Countdown() {
		
	}
	public void SetLabel(JLabel label)
	{
		currentLabel = label;
	}
	private void Display()
	{
		switch(state)
		{
			case Break:
				currentLabel.setText("IN BREAK: " + Integer.toString(countdown));
				break;
			case Countdown:
				currentLabel.setText("BREAK OVER: " + Integer.toString(countdown));
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
                if (countdown == 1) {
                    Switch();
                    time.cancel();
                    time.purge();
                } else {
                	countdown--;
                }
                Display();
            }
        }, delay, period);
	}
	private void Switch()
	{
		switch(state)
		{
			case Break:
				state = CountdownState.Countdown;
				countdown = Settings.getTimeBetweenBreak();
				break;
			case Countdown:
				state = CountdownState.Break;
				countdown = Settings.getBreakDuration();
				break;
			default:
				break;
		}
		Start();
	}
}
