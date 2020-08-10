package com.rijkv.breaktimer.filemanagement;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class FileManager {
	private static final String CONFIG_FOLDER = "./configuration";
	private static final String PASSIVE_PROCESSES_PATH = CONFIG_FOLDER + "/passive_processes.ini";
	
	private static ArrayList<String> passiveProcesses = new ArrayList<String>();
	private static boolean configLoaded = false;
	
	private static void LoadConfig()
	{
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
		configLoaded = true;
		configLoaded = true;
	}
	
	public static ArrayList<String> getPassiveProcesses()
	{
		if (!configLoaded) {
			LoadConfig();
		}
		return passiveProcesses;
	}
}
