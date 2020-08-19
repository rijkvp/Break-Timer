package com.rijkv.breaktimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
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

	private boolean previousPassiveMode = false;
	private boolean passiveMode = false;

	public static final String OS = System.getProperty("os.name").toLowerCase();
	public static boolean RUNNING_WINDOWS;

	public BreakTimer(boolean debug) {
		isDebuging = debug;

		RUNNING_WINDOWS = OS.contains("win");

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
		startBreakStopwatches();
		activityStopwatch.start();
		previousTime = System.nanoTime();
		if (isDebuging)
			System.out.println("The break-timers have been started!");

		final Timer time = new Timer();
		final long timerPeroid = 300; // Execute every 300 miliseconds = 3,3 times per second

		// Run in a seperate thread because checking processes takes about 300ms
		time.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				CompletableFuture.runAsync(() -> { // Run acync because checking processes takes a while
					passiveMode = checkPassiveMode();
				});
			};
		}, 0, 800);

		// The main timer thread
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
							// Pause or reset all stopwatches
							for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
								BreakInfo info = entry.getKey();
								Stopwatch stopwatch = entry.getValue();
								if (activityStopwatch.elapsed() > info.duration.toNanos()) {
									if (isDebuging)
										System.out.println("Reset: " + info.name + "!");
									stopwatch.stop();
									stopwatch.start();
								} else if (!stopwatch.isPaused()) {
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
						// Pause or resume
						for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
							BreakInfo info = entry.getKey();
							Stopwatch stopwatch = entry.getValue();
							if (!passiveMode) {
								if (info.executionMode == ExecutionMode.Normal
										|| info.executionMode == ExecutionMode.Always) {
									// Resume
									if (stopwatch.isPaused()) {
										stopwatch.resume();
									}
								} else {
									// Pause
									if (!stopwatch.isPaused()) {
										stopwatch.pause();
									}
								}
							} else {
								if (info.executionMode == ExecutionMode.Passive
										|| info.executionMode == ExecutionMode.Always) {
									// Resume
									if (stopwatch.isPaused()) {
										stopwatch.resume();
									}
								} else {
									// Pauze
									if (!stopwatch.isPaused()) {
										stopwatch.pause();
									}
								}
							}

						}

						// Get the next break
						BreakInfo nextBreakInfo = null;
						Stopwatch nextStopwatch = null;
						Duration nextDurationLeft = Duration.ofMillis(Long.MAX_VALUE);
						for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
							BreakInfo info = entry.getKey();
							Stopwatch stopwatch = entry.getValue();
							Duration elapsedTime = Duration.ofNanos(stopwatch.elapsed());
							Duration durationLeft = info.interval.minus(elapsedTime);
							if (!passiveMode) {
								if (!(info.executionMode == ExecutionMode.Normal
										|| info.executionMode == ExecutionMode.Always)) {
									continue;
								}
							} else {
								if (!(info.executionMode == ExecutionMode.Passive
										|| info.executionMode == ExecutionMode.Always)) {
									continue;
								}
							}
							if (durationLeft.toMillis() < nextDurationLeft.toMillis()) {
								nextBreakInfo = info;
								nextStopwatch = stopwatch;
								nextDurationLeft = durationLeft;
							}
						}
						if (nextBreakInfo == null) {
							if (isDebuging)
								System.out.println("Couldn't find the next break!");
							return;
						}
						// Passive mode
						if (passiveMode && !previousPassiveMode) {
							// Passive mode enabled
							FileManager.playSound("enable_passive.wav");

							if (isDebuging) {
								System.out.println("NEXT BREAK: " + nextBreakInfo.name);
							}
						}
						if (!passiveMode && previousPassiveMode) {
							// Passive mode disabled
							FileManager.playSound("disable_passive.wav");

							if (isDebuging) {
								System.out.println("NEXT BREAK: " + nextBreakInfo.name);
							}
						}
						previousPassiveMode = passiveMode;

						// Check for reminders & play them
						for (var reminder : nextBreakInfo.reminders) {
							if (reminder.isPlayed)
								continue;
							if (reminder.timeBefore.toMillis() < nextBreakInfo.interval.toMillis()
									&& nextDurationLeft.toMillis() <= reminder.timeBefore.toMillis()) {
								if (isDebuging) {
									System.out.println("PLAY " + reminder.soundPath + " FROM " + nextBreakInfo.name);
								}
								FileManager.playSound(reminder.soundPath);
								reminder.isPlayed = true;
							}
						}
						// Check for breaks
						if (nextStopwatch.elapsed() >= nextBreakInfo.interval.toNanos()) {
							timerState = TimerState.Break;

							// Stop all stopwatches smaller than this interval
							resetCountdowns(nextBreakInfo.interval);

							breakStopwatch.start();
							breakEndSoundPath = nextBreakInfo.endSoundPath;
							breakDuration = nextBreakInfo.duration;
							breakWindow.open(nextBreakInfo);

							if (isDebuging) {
								System.out.println("START BREAK " + nextBreakInfo.name);
							}
						}
						break;
					case Break:
						Duration elapsedTime = Duration.ofNanos(breakStopwatch.elapsed());
						Duration durationLeft = breakDuration.minus(elapsedTime);
						durationLeft = durationLeft.plusMillis(800);
						breakWindow.updateTimeText(formatDuration(durationLeft));
						// Check if the break is over
						if (breakStopwatch.elapsed() >= breakDuration.toNanos()) {
							timerState = TimerState.CountingDown;
							breakStopwatch.stop();
							if (breakEndSoundPath != null)
								FileManager.playSound(breakEndSoundPath);
							startBreakStopwatches();
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

	private void startBreakStopwatches() {
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

	private void resetCountdowns(Duration interval) {
		for (Map.Entry<BreakInfo, Stopwatch> entry : breaks.entrySet()) {
			BreakInfo info = entry.getKey();
			Stopwatch stopwatch = entry.getValue();
			if (info.interval.toSeconds() <= interval.toSeconds()) {
				stopwatch.stop();
				info.resetReminders();
			} else {
				stopwatch.pause();
			}
		}
	}

	boolean checkPassiveMode() {
		if (!RUNNING_WINDOWS) // Only works on windows
			return false;

		String line;
		String pidInfo = "";

		Process p = null;
		try {
			p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

		try {
			while ((line = input.readLine()) != null) {
				pidInfo += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean foundProcess = false;
		for (var process : FileManager.getPassiveProcesses()) {
			if (pidInfo.contains(process)) {
				foundProcess = true;
			}
		}
		return foundProcess;
	}
}
