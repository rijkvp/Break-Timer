package com.rijkv.breaktimer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.rijkv.breaktimer.filemanagement.FileManager;
import com.rijkv.breaktimer.input.KeyListener;
import com.rijkv.breaktimer.input.MouseListener;

enum TimerState {
	CountingDown,
    Break,
}

public class BreakTimer {
	
	private TimerState timerState;
	private HashMap<BreakInfo, Stopwatch> breaks = new HashMap<>();
	
	private MouseListener mouseListener;
	private KeyListener keyListener;
	
	private Stopwatch breakStopwatch = new Stopwatch();
	private Duration breakDuration;
	
	public BreakTimer()
	{
		// Load config
		var breaksList = FileManager.getBreakConfig();
		for(BreakInfo breakInfo : breaksList)
		{
			System.out.println(breakInfo.interval.getSeconds());
			breaks.put(breakInfo, new Stopwatch());
		}
		timerState = TimerState.CountingDown;
		
		// Setup Keyboard & Mouse listeners
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

		
		loop();
	}
	
	private void loop()
	{
		StartBreakStopwatches();
		
        final Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	System.out.println("UPDATE!");
            	switch(timerState)
            	{
            	case CountingDown:
            		// Check for breaks
            		for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
            		    BreakInfo info = entry.getKey();
            		    Stopwatch stopwatch = entry.getValue();
            		    if (stopwatch.elapsed() >= info.interval.toNanos())
            		    {
            		    	System.out.println("ELAPSED: " + stopwatch.elapsed() + "  INTERVAL: " + info.interval.toNanos());
            		    	System.out.println("BREAK!!! On " + info.name);
            		    	timerState = TimerState.Break;
            		    	StopStopwatches();
            		    	breakStopwatch.start();
            		    	breakDuration = info.duration;
            		    }
            		}
            		break;
            	case Break:
            		// Check if the break is over
            		if (breakStopwatch.elapsed() >= breakDuration.toNanos())
            		{
            			timerState = TimerState.CountingDown;
            			StopStopwatches();
            			StartBreakStopwatches();
            			System.out.println("BREAK OVER!");
            		}
            		break;
            	}
            	
            }
        }, 1000, 1000); // Update every second
	}
	
	private void StartBreakStopwatches()
	{
		// Start the stopwatches
		for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
		    BreakInfo info = entry.getKey();
		    Stopwatch stopwatch = entry.getValue();
		    stopwatch.start();
		}
	}
	
	private void StopStopwatches()
	{
		for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
		    BreakInfo info = entry.getKey();
		    Stopwatch stopwatch = entry.getValue();
		    stopwatch.stop();
		}
		breakStopwatch.stop();
	}
	
}
