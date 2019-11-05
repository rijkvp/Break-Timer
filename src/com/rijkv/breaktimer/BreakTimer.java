package com.rijkv.breaktimer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	private Settings settings = new Settings("Settings");
	private Countdown countdown = new Countdown();
		
	
	public BreakTimer()
	{
		contentPanel = new JPanel();
		
		panel = new JPanel();
		
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
       
        timeLabel = new JLabel("BREAK OVER ...:...");
        timeLabel.setFont(ResourceLoader.getDefaultBoldFont(32f));
        timeLabel.setForeground(ResourceLoader.getTextColor());

        panel.add(timeLabel);
        
        contentPanel.add(panel);
		
        settingsButton = new JButton("SETTINGS");
        settingsButton.setForeground(ResourceLoader.getTextColor());
        settingsButton.setBackground(ResourceLoader.getBGColor());
        settingsButton.setFont(ResourceLoader.getDefaultBoldFont(22f));
        settingsButton.setBorder(ResourceLoader.getDefaultButtonBorder());
        settingsButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  settings.Open();
          }
        });
        panel.add(settingsButton);
        
        JButton breakButton = new JButton("BREAK NOW");
        breakButton.setForeground(ResourceLoader.getTextColor());
        breakButton.setBackground(ResourceLoader.getBGColor());
        breakButton.setFont(ResourceLoader.getDefaultBoldFont(22f));
        breakButton.setBorder(ResourceLoader.getDefaultButtonBorder());
        breakButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  countdown.BreakNow();
          }
        });
        panel.add(breakButton);

        
        JButton quitButton = new JButton("QUIT");
        quitButton.setForeground(ResourceLoader.getTextColor());
        quitButton.setBackground(ResourceLoader.getBGColor());
        quitButton.setFont(ResourceLoader.getDefaultBoldFont(22f));
        quitButton.setBorder(ResourceLoader.getDefaultButtonBorder());
        quitButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  System.exit(0);
          }
        });
        panel.add(quitButton);
        
        
		mainFrame = new JFrame("Break Timer");
		mainFrame.setVisible(true);
		mainFrame.setSize(300, 250);
		mainFrame.add(contentPanel);
		mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		panel.setBackground(ResourceLoader.getBGColor());
		contentPanel.setBackground(ResourceLoader.getBGColor());

        countdown.Setup();
        countdown.SetLabel(timeLabel);
	}
	
	@SuppressWarnings("deprecation")
	public void Hide()
	{
		mainFrame.hide();
	}
}
