package com.rijkv.breaktimer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.rijkv.breaktimer.components.ImagePanel;
import com.rijkv.breaktimer.filemanagement.ResourceLoader;
import com.rijkv.breaktimer.components.*;

public class Break implements Runnable {

	public boolean passiveMode = false;
	
	private JFrame breakFrame;
	private ImagePanel contentPanel;

	private JLabel label;
	private JLabel timeLabel;
	private JLabel breakText;
	private JButton skipButton;

	private Countdown refCountdown;

	private boolean isOpen = false;

	private final static String[] FONTS = { "Arial", "Arial Black", "Bahnschrift", "Castellar", "Elephant", "Gabriola",
			"Gill Sans", "Haettenschweiler", "Lucida Console", "Stencil", "Times New Roman", "Verdana", "Wide Latin" };

	private final static String[] TITLES = { "Time for a break!", "BREAK!", "Break.", "Just a normal break.",
			"Another break", "KAERB|BREAK" };
	private final static String[] DESCRIPTIONS = { "Walk away! NOW!! OR...", "Let's move away!",
			"Just walk away from your pc.", "Time for a drink!", "Don't look at this screen!" };
	private boolean didAddSkipButton = false;

	public Break(Countdown countdown) {
		refCountdown = countdown;

		BufferedImage myImage = null;
		try {
			myImage = ImageIO.read(new File("./assets/bg.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		contentPanel = new ImagePanel(myImage);

		BoxLayout layout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
		contentPanel.setLayout(layout);

		label = new JLabel("not set");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(label);

		timeLabel = new JLabel("not set");
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(timeLabel);

		breakText = new JLabel("not set");
		breakText.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(breakText);

		skipButton = new JButton("SKIP");
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ForceStop();
			}
		});
		skipButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(skipButton);

		breakFrame = new JFrame("Break");
		breakFrame.add(contentPanel);
		breakFrame.setSize(600, 400);

		// Always on top
		if (Settings.getForceMode() && !passiveMode) {
			breakFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			int state = breakFrame.getExtendedState() & ~JFrame.ICONIFIED & JFrame.NORMAL;
			breakFrame.setExtendedState(state);
			breakFrame.setAlwaysOnTop(true);
			breakFrame.toFront();
			breakFrame.requestFocus();
		}

		breakFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		breakFrame.setUndecorated(true);
	}

	private void ForceStop() {
		if (refCountdown.CanSkip() || passiveMode)
		{
			PlaySound("skip_sound");
			refCountdown.ForceStop();
		}
	}

	public void SetTime(int time) {
		timeLabel.setText(Countdown.formatHHMMSS(time));
		if (refCountdown.CanSkip() || passiveMode) {
			contentPanel.add(skipButton);
			if (!didAddSkipButton) {
				didAddSkipButton = true;
				contentPanel.doLayout();
				breakFrame.pack();
				breakFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
		} else {
			contentPanel.remove(skipButton);
		}
	}

	public void Open() {
		UpdateGUI();

		breakFrame.setVisible(true);
		isOpen = true;

		PlaySound("break_sound");

		new Thread(this).start();
	}

	@SuppressWarnings("deprecation")
	public void Close() {
		isOpen = false;
		breakFrame.hide();
	}
	public static void PlaySound(String filename)
	{
		String soundName = "./assets/" + filename +".wav";
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clip.start();
	}
	private void UpdateGUI() {
		GetColors();
		Font font = GetFont();
		String title = GetTitle();
		String description = GetDescription();

		label.setText(title);
		label.setForeground(ResourceLoader.getTextColor());
		label.setFont(font);
		label.setFont(label.getFont().deriveFont(128.0f));
		timeLabel.setForeground(ResourceLoader.getTextColor());
		timeLabel.setFont(font);
		timeLabel.setFont(timeLabel.getFont().deriveFont(40.0f));
		breakText.setText(description);
		breakText.setForeground(ResourceLoader.getTextColor());
		breakText.setFont(font);
		contentPanel.setBackground(new Color(0, 0, 0, 0));
		contentPanel.setFont(font);
		contentPanel.setBackground(ResourceLoader.getBGColor());
		contentPanel.setFont(font);
		breakText.setFont(label.getFont().deriveFont(34.0f));
		skipButton.setFont(font);
		skipButton.setBackground(ResourceLoader.getBGColor());
		skipButton.setForeground(ResourceLoader.getTextColor());
		skipButton.setFont(ResourceLoader.getDefaultBoldFont(26));
		skipButton.setBorder(ResourceLoader.getDefaultButtonBorder());
	}

	private String GetTitle() {
		if (!Settings.getRandomMode()) {
			return Settings.getBreakTitleText();
		} else {
			Random rand = new Random();
			int randomIndex = rand.nextInt(TITLES.length);
			return TITLES[randomIndex];
		}
	}

	private String GetDescription() {
		if (!Settings.getRandomMode()) {
			return Settings.getBreakText();
		} else {
			Random rand = new Random();
			int randomIndex = rand.nextInt(DESCRIPTIONS.length);
			return DESCRIPTIONS[randomIndex];
		}
	}

	private Font GetFont() {
		if (!Settings.getRandomMode()) { // TODO: Add custom break font setting back
			return ResourceLoader.getDefaultFont(46);
		} else {
			Random rand = new Random();
			int randomIndex = rand.nextInt(FONTS.length);
			String randomFontName = FONTS[randomIndex];

			return new Font(randomFontName, Font.PLAIN, 46);
		}
	}

	@SuppressWarnings("unused")
	private void GetColors() {
		// TODO: FIX RANDOM MODE FONTS
		Color bgColor;
		Color textColor;
		if (!Settings.getRandomMode()) {
			bgColor = ResourceLoader.getBGColor();
			textColor = ResourceLoader.getTextColor();
		} else {
			Random rand = new Random();

			float r = rand.nextFloat() / 2f;
			float g = rand.nextFloat() / 2f;
			float b = rand.nextFloat() / 2f;
			Color darkColor = new Color(r, g, b);

			float r_light = rand.nextFloat() / 2f + 0.5f; // 0.5-1
			float g_light = rand.nextFloat() / 2f + 0.5f;
			float b_light = rand.nextFloat() / 2f + 0.5f;
			Color lightColor = new Color(r_light, g_light, b_light);

			// 50% chance
			float random = rand.nextFloat();
			if (random > 0.5f) {
				bgColor = lightColor;
				textColor = darkColor;
			} else {
				bgColor = darkColor;
				textColor = lightColor;
			}
		}
	}

	public void run() {
		if (Settings.getForceMode() && !passiveMode) {
			try {
				kill("explorer.exe"); // Kill explorer

				Robot robot = new Robot();
				int i = 0;
				while (isOpen) {
					sleep(30L);
					focus();
					releaseKeys(robot);
					sleep(15L);
					focus();
					if (i++ % 10 == 0) {
						kill("taskmgr.exe");
					}
					focus();
					releaseKeys(robot);
				}
				Runtime.getRuntime().exec("explorer.exe"); // Restart explorer
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	private void releaseKeys(Robot robot) {
		robot.keyRelease(17);
		robot.keyRelease(18);
		robot.keyRelease(127);
		robot.keyRelease(524);
		robot.keyRelease(9);
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}

	private void kill(String string) {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + string).waitFor();
		} catch (Exception e) {
		}
	}

	private void focus() {
		this.breakFrame.requestFocus();
	}
}