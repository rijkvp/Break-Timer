package com.rijkv.breaktimer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GamePopup {

	private JFrame popupFrame;
	
	private JPanel contentPanel;
	private JPanel panel;
	private JLabel label;
	private JButton ignoreButton;
	private JButton quitButton;
	
	private Countdown countdown;
	
	public GamePopup() {	
		contentPanel = new JPanel();
		contentPanel.setBackground(ResourceLoader.getBGColor());
		contentPanel.setBounds(0, 400, 400, 400);
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(1,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        panel.setBackground(ResourceLoader.getBGColor());
        contentPanel.add(panel);
        
        label = new JLabel("not set", SwingConstants.CENTER);
        label.setForeground(ResourceLoader.getTextColor());
        Font font = new Font(Settings.getFontName(), Font.BOLD, 28);
        label.setFont(font);
        
        ignoreButton = new JButton("IGNORE");
        ignoreButton.setMargin(new Insets(4, 8, 4, 8));
        ignoreButton.setBackground(ResourceLoader.getBGColor());
        ignoreButton.setForeground(ResourceLoader.getTextColor());
        ignoreButton.setFont(ResourceLoader.getDefaultBoldFont(16));
        ignoreButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	Close();
            }
        });
        
        quitButton = new JButton("QUIT");
        quitButton.setMargin(new Insets(4, 8, 4, 8));
        quitButton.setBackground(ResourceLoader.getBGColor());
        quitButton.setForeground(ResourceLoader.getTextColor());
        quitButton.setFont(ResourceLoader.getDefaultBoldFont(16));
        quitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	System.exit(0);
            }
        });
        
        
		contentPanel.add(label);
		contentPanel.add(ignoreButton);
		contentPanel.add(quitButton);
		
        popupFrame = new JFrame("GAME POPUP");
        popupFrame.add(contentPanel);
        popupFrame.setSize(500, 150);
		
        popupFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	public void Open(String processName)
	{		
		label.setText("KILLED " + processName + "!");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double xpos = screenSize.getWidth() / 2;
		double ypos = screenSize.getHeight() / 2;
		
		popupFrame.setLocation((int)xpos - (popupFrame.getSize().width / 2), (int)ypos - (popupFrame.getSize().height/ 2));
		popupFrame.setVisible(true);
	}
	
	@SuppressWarnings("deprecation")
	public void Close()
	{
		popupFrame.hide();
	}
}
