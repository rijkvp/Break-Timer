package com.rijkv.breaktimer;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.rijkv.breaktimer.input.KeyListener;
import com.rijkv.breaktimer.input.MouseListener;

enum TimerState {
	CountingDown, Break,
}

public class BreakTimer {

	private final boolean isDebuging;

	private TimerState timerState;
	private HashMap<BreakInfo, Stopwatch> breaks = new HashMap<>();

	private MouseListener mouseListener;
	private KeyListener keyListener;
	private Stopwatch activityStopwatch = new Stopwatch();
	private long previousTime;

	private Stopwatch breakStopwatch = new Stopwatch();
	private Duration breakDuration;
	private String breakEndSoundPath;
	private BreakWindow breakWindow = new BreakWindow();

	public BreakTimer(boolean debug) {
		isDebuging = debug;

		// Load config
		var breaksList = FileManager.getBreakConfig();
		// Sort by interval
		Collections.sort(breaksList, (o1, o2) -> (int) o2.interval.toSeconds() - (int) o1.interval.toSeconds());

		for (BreakInfo breakInfo : breaksList) {
			breaks.put(breakInfo, new Stopwatch());
		}

		timerState = TimerState.CountingDown;

		// Setup Keyboard & Mouse listeners
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
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

	private void loop() {
		StartBreakStopwatches();
		activityStopwatch.start();
		previousTime = System.nanoTime();
		if (isDebuging)
			System.out.println("The break-timers have been started!");

		final Timer time = new Timer();
		final long timerPeroid = 100; // Execute every 100 miliseconds = 10 times per second
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				long timeDiff = (System.nanoTime() - previousTime) / 1000000;
				if (timeDiff > timerPeroid * 2) {
					if (isDebuging)
						System.out.println("The delay between updates is higher than it sould be! It was " + timeDiff
								+ "ms while it should be " + timerPeroid + "ms!");

					for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
						BreakInfo info = entry.getKey();
						Stopwatch stopwatch = entry.getValue();
						if (timeDiff >= info.duration.toMillis()) {
							if (isDebuging)
								System.out.println("Reset: " + info.name + " - break duration: "
										+ info.duration.toMillis() + "ms");
							stopwatch.stop();
							stopwatch.start();
						} else {
							if (isDebuging)
								System.out.println("Did not reset: " + info.name + " - break duration: "
										+ info.duration.toMillis() + "ms");
						}
					}
				}

				previousTime = System.nanoTime();

				if (keyListener.isKeyboardUsed() || mouseListener.isMouseUsed()) {
					activityStopwatch.stop();
					activityStopwatch.start();
				}
				boolean userIsActive = (activityStopwatch.elapsed() < 10 * 1000000000L);

				switch (timerState) {
					case CountingDown:
						if (!userIsActive) {
							// Pause all stopwatches
							for (var stopwatch : breaks.values()) {
								if (!stopwatch.isPaused()) {
									stopwatch.pause();
								}
							}
							return;
						} else {
							// Resume them
							for (var stopwatch : breaks.values()) {
								if (stopwatch.isPaused()) {
									stopwatch.resume();
								}
							}
						}
						// Check for reminders & play them
						boolean reminderPlayed = false;
						for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
							BreakInfo info = entry.getKey();
							Stopwatch stopwatch = entry.getValue();
							Duration elapsedTime = Duration.ofNanos(stopwatch.elapsed());
							Duration durationLeft = info.interval.minus(elapsedTime);
							if (reminderPlayed)
								break;
							for (var reminder : info.reminders) {
								if (reminder.isPlayed)
									continue;
								if (reminder.timeBefore.toMillis() < info.interval.toMillis()
										&& durationLeft.toMillis() <= reminder.timeBefore.toMillis()) {
									if (isDebuging) {
										System.out.println("PLAY " + reminder.soundPath + " FROM " + info.name);
									}
									FileManager.playSound(reminder.soundPath);
									reminder.isPlayed = true;
									reminderPlayed = true;
								}
							}
						}
						// Check for breaks
						boolean breakStarted = false;
						for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
							if (breakStarted)
								break;
							BreakInfo info = entry.getKey();
							Stopwatch stopwatch = entry.getValue();
							if (stopwatch.elapsed() >= info.interval.toNanos()) {
								timerState = TimerState.Break;

								// Stop all stopwatches smaller than this interval
								StopSmallerStopwatches(info.interval);

								breakStopwatch.start();
								breakEndSoundPath = info.endSoundPath;
								breakDuration = info.duration;
								breakWindow.open(info);
								breakStarted = true;

								if (isDebuging) {
									System.out.println("START BREAK " + info.name);
								}
							}
						}
						break;
					case Break:
						Duration elapsedTime = Duration.ofNanos(breakStopwatch.elapsed());
						Duration durationLeft = breakDuration.minus(elapsedTime);
						durationLeft = durationLeft.plusMillis(800);
						breakWindow.UpdateTimeText(formatDuration(durationLeft));
						// Check if the break is over
						if (breakStopwatch.elapsed() >= breakDuration.toNanos()) {
							timerState = TimerState.CountingDown;
							breakStopwatch.stop();
							if (breakEndSoundPath != null)
								FileManager.playSound(breakEndSoundPath);
							StartBreakStopwatches();
							breakWindow.close();
						}
						break;
				}

			}
		}, 0, timerPeroid);
	}

	public static String formatDuration(Duration duration) {
		long seconds = duration.getSeconds();
		long absSeconds = Math.abs(seconds);
		String formatted = String.format("%02d:%02d", (absSeconds % 3600) / 60, absSeconds % 60);
		return formatted;
	}

	private void StartBreakStopwatches() {
		// Start the stopwatches
		for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
			Stopwatch stopwatch = entry.getValue();
			if (!stopwatch.isRunning()) {
				stopwatch.start();
			} else if (stopwatch.isPaused()) {
				stopwatch.resume();
			}
		}
	}

	private void StopSmallerStopwatches(Duration interval) {
		for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
			BreakInfo info = entry.getKey();
			Stopwatch stopwatch = entry.getValue();
			if (info.interval.toSeconds() <= interval.toSeconds()) {
				stopwatch.stop();
				info.resetReminders(); // TODO: Bad function name
			} else {
				stopwatch.pause();
			}
		}
	}
}
