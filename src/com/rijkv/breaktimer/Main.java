package com.rijkv.breaktimer;

import java.util.Arrays;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if (Arrays.stream(args).anyMatch("startup"::equals))
		{
			BreakTimer breakTimer = new BreakTimer("Break Timer [STARTUP]");
			breakTimer.Hide();
		} else {
			BreakTimer breakTimer = new BreakTimer("Break Timer");
		}
	}

}
