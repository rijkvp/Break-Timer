package com.rijkv.breaktimer;

import java.awt.*;

import javax.swing.*;

public class Reminder {
	
	private JPanel contentPanel;
	private JPanel panel;
	
	private JLabel label;
	
	private JFrame reminderFrame;

	public Reminder(String title) {		
		Color bgColor = Settings.getBGColor();
		Color textColor = Settings.getFGColor();
		
		contentPanel = new JPanel();
		contentPanel.setBackground(bgColor);
		contentPanel.setBounds(0, 400, 400, 400);
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        panel.setBackground(bgColor);
        contentPanel.add(panel);
        
        label = new JLabel("not set", SwingConstants.CENTER);
        label.setForeground(textColor);
        Font font = new Font("Arial", Font.BOLD, 28);
        label.setFont(font);
        
		contentPanel.add(label);
		
        reminderFrame = new JFrame(title);
		reminderFrame.add(contentPanel);
		reminderFrame.setSize(300, 80);
		
		reminderFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	public void SetTime(final int secondsLeft)
	{
		label.setText("Break over " + secondsLeft + "s");
	}
	public void Open()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double xpos = screenSize.getWidth() / 2;
		double ypos = screenSize.getHeight() / 2;
		
		reminderFrame.setLocation((int)xpos - (reminderFrame.getSize().width / 2), (int)ypos - (reminderFrame.getSize().height/ 2));
		reminderFrame.setVisible(true);
	}
	@SuppressWarnings("deprecation")
	public void Close()
	{
		reminderFrame.hide();
	}
}
