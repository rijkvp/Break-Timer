package com.rijkv.breaktimer;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.rijkv.breaktimer.input.KeyListener;
import com.rijkv.breaktimer.input.MouseListener;

enum TimerState {
	CountingDown {
        @Override
        public Duration getDuration() {
            return Duration.ofMinutes(30);
        }
        
        @Override
        public TimerState getNextState() {
        	return SmallBreak;
        }
    },
    SmallBreak {
        @Override
        public Duration getDuration() {
        	return Duration.ofMinutes(2);
        }
        
        @Override
        public TimerState getNextState() {
        	return CountingDown;
        }
    },
    BigBreak {
        @Override
        public Duration getDuration() {
        	return Duration.ofMinutes(8);
        }
        
        @Override
        public TimerState getNextState() {
        	return CountingDown;
        }
    };
	
	public abstract TimerState getNextState();
    public abstract Duration getDuration();
}

public class BreakTimer {
	
	private TimerState timerState;
	private Stopwatch stopwatch;
	
	private MouseListener mouseListener;
	private KeyListener keyListener;
	
	public BreakTimer()
	{
		timerState = TimerState.CountingDown;
		stopwatch = new Stopwatch();
		stopwatch.start();
		
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
        final Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	if (stopwatch.elapsed() > timerState.getDuration().getNano())
            	{
            		
            	}
            }
        }, 1000, 1000); // Update every second
	}
	
}
