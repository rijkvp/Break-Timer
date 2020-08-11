package com.rijkv.breaktimer;

import java.time.Duration;
import java.util.ArrayList;

import org.json.simple.JSONObject;

class Reminder
{
	public Duration timeBefore;
	public boolean isPlayed;
	public String soundFile;
}

public class BreakInfo {
	public String name;
	public String description;
	public String soundPath;
	public Duration interval;
	public Duration duration;
	public ArrayList<Duration> reminders;

	public BreakInfo(JSONObject jsonObject)
	{
		name = (String)jsonObject.get("name");
		description = (String)jsonObject.get("description");
		soundPath = (String)jsonObject.get("soundPath");
		interval = Duration.parse((CharSequence) jsonObject.get("interval"));
		duration = Duration.parse((CharSequence) jsonObject.get("duration"));
		// TODO: Load reminders & play them
	}
}