package com.rijkv.breaktimer.filemanagement;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.rijkv.breaktimer.BreakInfo;


public final class FileManager {
	private static final String ASSETS_FOLDER = "./assets";
	private static final String CONFIG_FOLDER = ASSETS_FOLDER + "/config";
	private static final String PASSIVE_PROCESSES_PATH = CONFIG_FOLDER + "/passive_processes.ini";
	private static final String BREAK_CONFIG_PATH = CONFIG_FOLDER + "/break_config.json";
	
	private static ArrayList<String> passiveProcesses = new ArrayList<String>();
	private static ArrayList<BreakInfo> breakInfos = new ArrayList<BreakInfo>();
	
	private static boolean configLoaded = false;
	
	private static void LoadConfig()
	{
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

			@SuppressWarnings("unchecked") //Using legacy API
			Iterator<JSONObject> iterator = jsonArray.iterator();
			
			while (iterator.hasNext()) {
				breakInfos.add(new BreakInfo(iterator.next()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		configLoaded = true;
	}
	
	public static ArrayList<String> getPassiveProcesses()
	{
		if (!configLoaded) {
			LoadConfig();
		}
		return passiveProcesses;
	}
	
	public static ArrayList<BreakInfo> getBreakConfig()
	{
		if (!configLoaded) {
			LoadConfig();
		}
		return breakInfos;
	}
}
