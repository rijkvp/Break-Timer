package com.rijkv.breaktimer.filemanagement;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
import com.rijkv.breaktimer.BreakInfo;

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
	private static BufferedImage breakBackroundImage;
	private static Font font;
	private static Font boldFont;

	static {
		// Load Passive procceses .ini file
		try (BufferedReader br = new BufferedReader(new FileReader(PASSIVE_PROCESSES_PATH))) {
			String line;
			while ((line = br.readLine()) != null) {
				passiveProcesses.add(line);
			}
		} catch (FileNotFoundException e) {
			showMessageDialog(null, e.toString(), "FileNotFoundException", ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			showMessageDialog(null, e.toString(), "IOException", ERROR_MESSAGE);
			e.printStackTrace();
		}

		// Load the break config .json file
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(BREAK_CONFIG_PATH));

			JSONArray jsonArray = (JSONArray) obj;

			@SuppressWarnings("unchecked") // Using legacy API
			Iterator<JSONObject> iterator = jsonArray.iterator();

			while (iterator.hasNext()) {
				breakInfos.add(new BreakInfo(iterator.next()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load resources
		try {
			breakBackroundImage = ImageIO.read(new File(ASSETS_FOLDER + "/img/break_background.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
		    font = Font.createFont(Font.TRUETYPE_FONT, new File(ASSETS_FOLDER + "/font/regular.ttf")).deriveFont(12f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(font);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
		try {
		    boldFont = Font.createFont(Font.TRUETYPE_FONT, new File(ASSETS_FOLDER + "/font/bold.ttf")).deriveFont(12f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(boldFont);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
	}

	public static ArrayList<String> getPassiveProcesses() {
		return passiveProcesses;
	}

	public static ArrayList<BreakInfo> getBreakConfig() {
		return breakInfos;
	}

	public static BufferedImage getBreakBackgroundImage() {
		return breakBackroundImage;
	}

	public static Font getFont() {
		return font;
	}
	
	public static Font getFont(float size) {
		return font.deriveFont(size);
	}
	
	public static Font getBoldFont() {
		return boldFont;
	}
	
	public static Font getBoldFont(float size) {
		return boldFont.deriveFont(size);
	}

	public static Color getTextColor() {
		return Color.BLACK;
	}

    public static void playSound(String filename) {
        String soundName = "./assets/audio/" + filename + ".wav";
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
	

}
