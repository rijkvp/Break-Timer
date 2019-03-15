package com.rijkv.breaktimer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BreakTimer {
	
	private JFrame mainFrame;
	
	
	private JPanel contentPanel;
	private JPanel panel;
	
	private JButton settingsButton;
	
	private JLabel timeLabel;
	
	private Countdown countdown = new Countdown();
	
	private Settings settings = new Settings("Settings");
	
	public BreakTimer(String title)
	{
		contentPanel = new JPanel();
		contentPanel.setBounds(0, 0, 600, 400);
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        
        ShowLabel("TIME");
        timeLabel = new JLabel("NOT SET!!");
        panel.add(timeLabel);
        
        contentPanel.add(panel);
		
        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  settings.Open();
          }
        });
        panel.add(settingsButton);
        
        
		mainFrame = new JFrame(title);
		mainFrame.setVisible(true);
		mainFrame.setSize(600, 400);
		mainFrame.add(contentPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

        countdown.Setup();
        countdown.SetLabel(timeLabel);
	}
	public static void main(String[] args) {	
		BreakTimer breakTimer = new BreakTimer("Break Timer");
	}
	private void ShowLabel(String text)
	{
		JLabel label = new JLabel(text);
		panel.add(label);
	}
}
