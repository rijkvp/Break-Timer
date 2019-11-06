package com.rijkv.breaktimer;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;


public class Main {

	public static void main(String[] args) {
		// No jnativehook logging
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		
		// Startup command line argument
		if (Arrays.stream(args).anyMatch("startup"::equals))
		{
			BreakTimer breakTimer = new BreakTimer();
			breakTimer.StartupMode();
		} 
		else 
		{
			@SuppressWarnings("unused")
			BreakTimer breakTimer = new BreakTimer();
		}
	}

}
