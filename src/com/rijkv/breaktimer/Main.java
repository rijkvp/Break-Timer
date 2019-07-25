package com.rijkv.breaktimer;

import java.util.Arrays;
import java.util.logging.*;

import org.jnativehook.GlobalScreen;


public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		
		if (Arrays.stream(args).anyMatch("startup"::equals))
		{
			BreakTimer breakTimer = new BreakTimer("Break Timer [STARTUP]");
			breakTimer.Hide();
		} else {
			BreakTimer breakTimer = new BreakTimer("Break Timer");
		}
	}

}
