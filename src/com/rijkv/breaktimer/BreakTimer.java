package com.rijkv.breaktimer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		contentPanel = new JPanel();
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
       
        timeLabel = new JLabel("Break Over: ..s");
        timeLabel.setFont(timeLabel.getFont ().deriveFont (24.0f));
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
        
        JButton breakButton = new JButton("Break Now");
        breakButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  countdown.BreakNow();
          }
        });
        panel.add(breakButton);

        
        JButton quitButton = new JButton("Stop & Quit");
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
		

        countdown.Setup();
        countdown.SetLabel(timeLabel);
	}
	public static void main(String[] args) {
		BreakTimer breakTimer = new BreakTimer("Break Timer");
		breakTimer.SetSystemUILook();
	}
	public void SetSystemUILook()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	public static String SecoundsToTime()
	{
		
		return "null";
	}
}
