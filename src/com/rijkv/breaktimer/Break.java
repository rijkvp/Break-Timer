package com.rijkv.breaktimer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Break {
	
	private JFrame breakFrame;
	private JPanel contentPanel;
	private JPanel panel;
	
	private JLabel label;
	private JLabel timeLabel;
	
	private Color bgColor = Color.BLACK;
	private Color textColor = Color.WHITE;
	
	private Countdown refCountdown;
	
	public Break(String title, Countdown countdown) {
		refCountdown = countdown;		
		
		contentPanel = new JPanel();
		contentPanel.setBounds(0, 400, 400, 400);
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        contentPanel.add(panel);
        
        label = new JLabel("Break", SwingConstants.CENTER);
        label.setFont(label.getFont ().deriveFont (128.0f));
        label.setForeground(textColor);
		panel.add(label);
        
		timeLabel = new JLabel("not set", SwingConstants.CENTER);
		timeLabel.setForeground(textColor);
		timeLabel.setFont(timeLabel.getFont ().deriveFont (40.0f));
		panel.add(timeLabel);		
		
        breakFrame = new JFrame(title);
		breakFrame.add(contentPanel);
		breakFrame.setSize(600, 400);
		breakFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		breakFrame.setAlwaysOnTop(true);
		breakFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		breakFrame.setUndecorated(true);
		
		panel.setBackground(bgColor);
		contentPanel.setBackground(bgColor);
		
		breakFrame.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) { }

	        @Override
	        public void keyPressed(KeyEvent e) {
	            if (e.getKeyCode() == 127)
	            {
	            	ForceStop();
	            }
	        }

	        @Override
	        public void keyReleased(KeyEvent e) { }
	    });
	}
	private void ForceStop()
	{
		if (refCountdown.CanSkip())
			refCountdown.ForceStop();
	}
	public void SetTime(int time)
	{
		timeLabel.setText(Integer.toString(time) + " seconds left");
	}
	public void Open()
	{
		breakFrame.setVisible(true);
		//final Runnable runnable =
		//	     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
		//	if (runnable != null) runnable.run();
		
	}
	
	@SuppressWarnings("deprecation")
	public void Close()
	{
		breakFrame.hide();
	}
}
