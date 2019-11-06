package com.rijkv.breaktimer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Reminder {

	private JFrame reminderFrame;
	
	private JPanel contentPanel;
	private JPanel panel;
	private JLabel label;
	private JButton delay1Button;
	private JButton delay2Button;
	private JButton delay3Button;
	
	private Countdown countdown;
	
	public Reminder(Countdown cd) {		
		countdown = cd;		
		
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
        
        delay1Button = new JButton("DELAY 1 MINUTE ");
        delay1Button.setMargin(new Insets(4, 8, 4, 8));
        delay1Button.setBackground(ResourceLoader.getBGColor());
        delay1Button.setForeground(ResourceLoader.getTextColor());
        delay1Button.setFont(ResourceLoader.getDefaultBoldFont(16));
        delay1Button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
          	  countdown.Delay(60);
          	  Close();
            }
        });
        
        delay2Button = new JButton("DELAY 3 MINUTES");
        delay2Button.setMargin(new Insets(4, 8, 4, 8));
        delay2Button.setBackground(ResourceLoader.getBGColor());
        delay2Button.setForeground(ResourceLoader.getTextColor());
        delay2Button.setFont(ResourceLoader.getDefaultBoldFont(16));
        delay2Button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
          	  countdown.Delay(60 * 3);
          	  Close();
            }
        });
        
        delay3Button = new JButton("DELAY 5 MINUTES");
        delay3Button.setMargin(new Insets(4, 8, 4, 8));
        delay3Button.setBackground(ResourceLoader.getBGColor());
        delay3Button.setForeground(ResourceLoader.getTextColor());
        delay3Button.setFont(ResourceLoader.getDefaultBoldFont(16));
        delay3Button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
          	  countdown.Delay(60 * 5);
          	  Close();
            }
        });
        
		contentPanel.add(label);
		contentPanel.add(delay1Button);
		contentPanel.add(delay2Button);
		contentPanel.add(delay3Button);
		
        reminderFrame = new JFrame("Break Reminder");
		reminderFrame.add(contentPanel);
		reminderFrame.setSize(300, 200);
		
		reminderFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	public void SetTime(final int secondsLeft)
	{
		label.setText("BREAK OVER " + secondsLeft + "...");
	}
	
	public void Open()
	{
		if (countdown.canDelay())
		{
			contentPanel.add(delay1Button);
			contentPanel.add(delay2Button);
			contentPanel.add(delay3Button);
		}
		else
		{
			contentPanel.remove(delay1Button);
			contentPanel.remove(delay2Button);
			contentPanel.remove(delay3Button);
		}
		
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
