package com.rijkv.breaktimer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BreakTimer {
	
	private JFrame mainFrame;
	
	
	private JPanel contentPanel;
	private JPanel panel;
	
	private JButton settingsButton;
	
	private JLabel timeLabel;
	
	private Settings settings = new Settings("Settings");
	
	private Countdown countdown = new Countdown();
	
	
	
	public BreakTimer(String title)
	{
		Color bgColor = Settings.getBGColor();
		Color textColor = Settings.getFGColor();
		
		contentPanel = new JPanel();
		
		panel = new JPanel();
		
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
       
        timeLabel = new JLabel("Break Over: ..s");
        timeLabel.setFont(timeLabel.getFont ().deriveFont (24.0f));
        timeLabel.setForeground(textColor);

        panel.add(timeLabel);
        
        contentPanel.add(panel);
		
        settingsButton = new JButton("Settings");
        settingsButton.setForeground(textColor);
        settingsButton.setBackground(bgColor);
        settingsButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  settings.Open();
          }
        });
        panel.add(settingsButton);
        
        JButton breakButton = new JButton("Break Now");
        breakButton.setForeground(textColor);
        breakButton.setBackground(bgColor);
        breakButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  countdown.BreakNow();
          }
        });
        panel.add(breakButton);

        
        JButton quitButton = new JButton("Stop & Quit");
        quitButton.setForeground(textColor);
        quitButton.setBackground(bgColor);
        quitButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  System.exit(0);
          }
        });
        panel.add(quitButton);
        
        
		mainFrame = new JFrame(title);
		mainFrame.setVisible(true);
		mainFrame.setSize(300, 250);
		mainFrame.add(contentPanel);
		mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		panel.setBackground(bgColor);
		contentPanel.setBackground(bgColor);

        countdown.Setup();
        countdown.SetLabel(timeLabel);
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if (Arrays.stream(args).anyMatch("startup"::equals))
		{
			BreakTimer breakTimer = new BreakTimer("Break Timer [STARTUP]");
			breakTimer.Hide();
		} else {
			BreakTimer breakTimer = new BreakTimer("Break Timer");
		}
	}
	@SuppressWarnings("deprecation")
	public void Hide()
	{
		mainFrame.hide();
	}
	public static String SecoundsToTime()
	{
		
		return "null";
	}
}
