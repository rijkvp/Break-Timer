package com.rijkv.breaktimer;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.scene.input.KeyCode;
import sun.applet.Main;


public class Break {
	
	private JFrame breakFrame;
	private JPanel contentPanel;
	private JPanel panel;
	
	private JLabel label;
	private JLabel timeLabel;
	
	public Break(String title) {
		contentPanel = new JPanel();
		contentPanel.setBounds(0, 400, 400, 400);
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        contentPanel.add(panel);
        
        label = new JLabel("Break Time!");
        label.setFont(label.getFont ().deriveFont (128.0f));
		panel.add(label);
        
		timeLabel = new JLabel("not set");
		timeLabel.setFont(timeLabel.getFont ().deriveFont (40.0f));
		panel.add(timeLabel);
		
		
		breakFrame.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	        }

	        @Override
	        public void keyPressed(KeyEvent e) {
	            if (e.getKeyCode() == 127)
	            {
	            	Close();
	            }
	        }

	        @Override
	        public void keyReleased(KeyEvent e) {
	        }
	    });
		
        breakFrame = new JFrame(title);
		breakFrame.add(contentPanel);
		breakFrame.setSize(600, 400);
		breakFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		breakFrame.setAlwaysOnTop(true);
		breakFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		breakFrame.setUndecorated(true);
	}
	public void SetTime(int time)
	{
		timeLabel.setText(Integer.toString(time) + " seconds...");
	}
	public void Open()
	{
		breakFrame.setVisible(true);
		final Runnable runnable =
			     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
			if (runnable != null) runnable.run();
		PlaySound(Settings.getBreakSoundPath());
		
	}
	
	@SuppressWarnings("deprecation")
	public void Close()
	{
		breakFrame.hide();
	}
	
	
	public static synchronized void PlaySound(final String path) {
	  new Thread(new Runnable() {
	    public void run() {
	      try {
	        Clip clip = AudioSystem.getClip();
	        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
	          Main.class.getResourceAsStream(path));
	        clip.open(inputStream);
	        clip.start(); 
	      } catch (Exception e) {
	    	System.out.println("path: " + path);
	        System.err.println(e.getMessage());
	      }
	    }
	  }).start();
	}
}
