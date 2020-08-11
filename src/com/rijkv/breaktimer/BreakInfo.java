package com.rijkv.breaktimer;

import java.time.Duration;

import org.json.simple.JSONObject;

public class BreakInfo {
	public String name;
	public Duration interval;
	public Duration duration;
	
	public BreakInfo(JSONObject jsonObject)
	{
		name = (String)jsonObject.get("name");
		interval = Duration.parse((CharSequence) jsonObject.get("interval"));
		duration = Duration.parse((CharSequence) jsonObject.get("duration"));
	}
}