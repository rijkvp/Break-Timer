package com.rijkv.breaktimer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class Reminder {
	public Duration timeBefore;
	public String soundPath;

	public boolean isPlayed = false;

	public Reminder(JSONObject jsonObject)
	{
		timeBefore = Duration.parse((CharSequence) jsonObject.get("timeBefore"));
		soundPath = (String)jsonObject.get("soundPath");
	}
}

enum ExecutionMode {
	Normal,
	Passive,
	Always,
}

public class BreakInfo {
	public String name;
	public String description;
	public String startSoundPath;
	public String endSoundPath;
	public Duration interval;
	public Duration duration;
	public boolean resetOnSleep;
	public boolean resetWhenInactive;
	public ArrayList<Reminder> reminders;
	public ExecutionMode executionMode = ExecutionMode.Normal;

	public BreakInfo(JSONObject jsonObject) {
		name = (String) jsonObject.get("name");
		description = (String) jsonObject.get("description");
		startSoundPath = (String) jsonObject.get("startSoundPath");
		endSoundPath = (String) jsonObject.get("endSoundPath");
		interval = Duration.parse((CharSequence) jsonObject.get("interval"));
		duration = Duration.parse((CharSequence) jsonObject.get("duration"));
		executionMode = ExecutionMode.valueOf((String)jsonObject.get("executionMode"));
		resetOnSleep = (boolean)jsonObject.get("resetOnSleep");
		resetWhenInactive = (boolean)jsonObject.get("resetWhenInactive");

		reminders = new ArrayList<>();
		JSONArray reminderObjects = (JSONArray) jsonObject.get("reminders");
		@SuppressWarnings("unchecked") // Using legacy API
		Iterator<JSONObject> iterator = reminderObjects.iterator();

		while (iterator.hasNext()) {
			reminders.add(new Reminder(iterator.next()));
		}
	}

	public void resetReminders()
	{
		for(var reminder : reminders)
		{
			reminder.isPlayed = false;
		}
	}
}