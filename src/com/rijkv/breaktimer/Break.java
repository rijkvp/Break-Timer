package com.rijkv.breaktimer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.util.Random;


public class Break implements Runnable {
	
	private JFrame breakFrame;
	private JPanel contentPanel;
	private JPanel panel;
	
	private JLabel label;
	private JLabel timeLabel;
	private JLabel breakText;
	private JButton skipButton;
	
	private Countdown refCountdown;
	
	private boolean isOpen = false;
	
	
	private final static String[] FONTS = { "Arial", "Arial Black", "Bahnschrift", "Castellar",
			"Elephant", "Gabriola", "Gill Sans", "Haettenschweiler", "Lucida Console", "Stencil",
			"Times New Roman", "Verdana", "Wide Latin"};
	
	private final static String[] TITLES = { "Time for a break!", "BREAK!", "Break.", "Just a normal break.", "Another break", "KAERB|BREAK" };
	private final static String[] DESCRIPTIONS = { "Walk away! NOW!! OR...", "Let's move away!", "Just walk away from your pc.", "Time for a drink!", "Don't look at this screen!" };

	
	public Break(Countdown countdown) {
		refCountdown = countdown;		
				
		contentPanel = new JPanel();
		contentPanel.setBounds(0, 400, 400, 400);
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        contentPanel.add(panel);
                
       
        label = new JLabel("not set", SwingConstants.CENTER);
		panel.add(label);		
        
		timeLabel = new JLabel("not set", SwingConstants.CENTER);
		panel.add(timeLabel);		
		
		breakText = new JLabel("not set", SwingConstants.CENTER);
		panel.add(breakText);
		
		skipButton = new JButton("SKIP"); 
		skipButton.addActionListener(new ActionListener()
        {
	          public void actionPerformed(ActionEvent e)
	          {
	        	  ForceStop();
	          }
        });
		panel.add(skipButton);
				
        breakFrame = new JFrame("Break");
		breakFrame.add(contentPanel);
		breakFrame.setSize(600, 400);
		
		// Always on top 
		if (Settings.getForceMode())
		{
			breakFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			int state = breakFrame.getExtendedState() & ~JFrame.ICONIFIED & JFrame.NORMAL;
			breakFrame.setExtendedState(state);
			breakFrame.setAlwaysOnTop(true);
			breakFrame.toFront();
			breakFrame.requestFocus();
		}		
		
		breakFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		breakFrame.setUndecorated(true);
		
		panel.setBackground(ResourceLoader.getBGColor());
		contentPanel.setBackground(ResourceLoader.getBGColor());
	}
	
	private void ForceStop()
	{
		if (refCountdown.CanSkip())
			refCountdown.ForceStop();
	}
	
	public void SetTime(int time)
	{
		timeLabel.setText(Countdown.formatHHMMSS(time));
		if (refCountdown.CanSkip())
		{
			panel.add(skipButton);
		}
		else
		{
			panel.remove(skipButton);
		}
	}
	
	public void Open()
	{
		UpdateGUI();
		
		breakFrame.setVisible(true);
		isOpen = true;
		new Thread(this).start();
	}
	
	@SuppressWarnings("deprecation")
	public void Close()
	{
		isOpen = false;
		breakFrame.hide();
	}
	
	private void UpdateGUI()
	{
		GetColors();
		Font font = GetFont();
		System.out.println("NAME " + font.getName());
		String title = GetTitle();
		String description = GetDescription();
		
		label.setText(title);
		label.setForeground(ResourceLoader.getTextColor());
		label.setFont(font);
		label.setFont(label.getFont ().deriveFont (128.0f));
		timeLabel.setForeground(ResourceLoader.getTextColor());
		timeLabel.setFont(font);
		timeLabel.setFont(timeLabel.getFont ().deriveFont (40.0f));
		breakText.setText(description);
		breakText.setForeground(ResourceLoader.getTextColor());
		breakText.setFont(font);
		panel.setBackground(ResourceLoader.getBGColor());
		panel.setFont(font);
		contentPanel.setBackground(ResourceLoader.getBGColor());
		contentPanel.setFont(font);
		breakText.setFont(label.getFont().deriveFont (34.0f));
		skipButton.setFont(font);
		skipButton.setBackground(ResourceLoader.getBGColor());
		skipButton.setForeground(ResourceLoader.getTextColor());
		skipButton.setFont(ResourceLoader.getDefaultBoldFont(18));
	}
	
	private String GetTitle()
	{
		if (!Settings.getRandomMode())
		{
			return Settings.getBreakTitleText();
		}
		else
		{
			Random rand = new Random();
        	int randomIndex = rand.nextInt(TITLES.length);
        	return TITLES[randomIndex];
		}
	}
	
	private String GetDescription()
	{
		if (!Settings.getRandomMode())
		{
			return Settings.getBreakText();
		}
		else
		{
			Random rand = new Random();
			int randomIndex = rand.nextInt(DESCRIPTIONS.length);
        	return DESCRIPTIONS[randomIndex];
		}
	}
	
	private Font GetFont()
	{
		if (!Settings.getRandomMode())
		{        	// TODO: Add custom break font setting back
			return ResourceLoader.getDefaultFont(46);
        }
        else
        {
        	Random rand = new Random();
        	int randomIndex = rand.nextInt(FONTS.length);
        	String randomFontName = FONTS[randomIndex];
        	
        	return new Font(randomFontName, Font.PLAIN, 46);
        }
	}
	
	private void GetColors()
	{
		// TODO: FIX RANDOM MODE FONTS
		Color bgColor;
		Color textColor;
		if (!Settings.getRandomMode())
		{
			bgColor = ResourceLoader.getBGColor();
			textColor = ResourceLoader.getTextColor();
		}
		else
		{
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
			if (random > 0.5f)
			{
				bgColor = lightColor;
				textColor = darkColor;
			}
			else
			{
				bgColor = darkColor;
				textColor = lightColor;
			}
		}
	}
	
	public void run() {
		if (Settings.getForceMode())
		{
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