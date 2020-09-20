package com.rijkv.breaktimer;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.image.BufferedImage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

public final class FileManager {
	// Paths
	private static final String ASSETS_FOLDER = "./assets";
	private static final String CONFIG_FOLDER = ASSETS_FOLDER + "/config";
	private static final String PASSIVE_PROCESSES_PATH = CONFIG_FOLDER + "/passive_processes.ini";
	private static final String BREAK_CONFIG_PATH = CONFIG_FOLDER + "/break_config.json";

	// Config
	private static ArrayList<String> passiveProcesses = new ArrayList<String>();
	private static ArrayList<BreakInfo> breakInfos = new ArrayList<BreakInfo>();

	// Resources
	private static ArrayList<BufferedImage> backgrounds = new ArrayList<>();
	private static Font font;
	private static Font boldFont;
	private static Random random = new Random();

	static {
		// Load Passive procceses .ini file
		try (BufferedReader br = new BufferedReader(new FileReader(PASSIVE_PROCESSES_PATH))) {
			String line;
			while ((line = br.readLine()) != null) {
				passiveProcesses.add(line);
			}
		} catch (final FileNotFoundException e) {
			showMessageDialog(null, e.toString(), "FileNotFoundException", ERROR_MESSAGE);
			e.printStackTrace();
		} catch (final IOException e) {
			showMessageDialog(null, e.toString(), "IOException", ERROR_MESSAGE);
			e.printStackTrace();
		}

		// Load the break config .json file
		final JSONParser parser = new JSONParser();
		try {
			final Object obj = parser.parse(new FileReader(BREAK_CONFIG_PATH));

			final JSONArray jsonArray = (JSONArray) obj;

			@SuppressWarnings("unchecked") // Using legacy API
			final Iterator<JSONObject> iterator = jsonArray.iterator();

			while (iterator.hasNext()) {
				breakInfos.add(new BreakInfo(iterator.next()));
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// Load resources
		try {
			Files.list(Paths.get(ASSETS_FOLDER + "/backgrounds")).filter(Files::isRegularFile).forEach(path -> {
				BufferedImage bg = null;
				try {
					bg = ImageIO.read(path.toFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
				backgrounds.add(bg);
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(ASSETS_FOLDER + "/font/regular.ttf")).deriveFont(12f);
			final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final FontFormatException e) {
			e.printStackTrace();
		}

		try {
			boldFont = Font.createFont(Font.TRUETYPE_FONT, new File(ASSETS_FOLDER + "/font/bold.ttf")).deriveFont(12f);
			final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(boldFont);
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final FontFormatException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getPassiveProcesses() {
		return passiveProcesses;
	}

	public static ArrayList<BreakInfo> getBreakConfig() {
		return breakInfos;
	}

	public static BufferedImage getRandomBackground() {
		int index = random.nextInt(backgrounds.size());
		return backgrounds.get(index);
	}

	public static Font getFont() {
		return font;
	}

	public static Font getFont(final float size) {
		return font.deriveFont(size);
	}

	public static Font getBoldFont() {
		return boldFont;
	}

	public static Font getBoldFont(final float size) {
		return boldFont.deriveFont(size);
	}

	public static Color getTextColor() {
		return Color.BLACK;
	}

	public static void playSound(final String filename) {
		final String path = "./assets/audio/" + filename;
		AudioInputStream audioInputStream = null;
		final File soundFile = new File(path).getAbsoluteFile();
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (final UnsupportedAudioFileException e1) {
			e1.printStackTrace();
			return;
		} catch (final IOException e1) {
			System.out.println("The sound file " + path + " couldn't be loaded!");
			return;
		}
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (final LineUnavailableException e) {
			e.printStackTrace();
			return;
		}
		try {
			clip.open(audioInputStream);
		} catch (final LineUnavailableException e) {
			e.printStackTrace();
			return;
		} catch (final IOException e) {
			e.printStackTrace();
			return;
		} catch (final Exception e) {
			e.printStackTrace();
			return;
		}
		clip.start();
	}

}
