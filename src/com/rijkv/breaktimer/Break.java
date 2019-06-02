package com.rijkv.breaktimer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Break implements Runnable {
	
	private JFrame breakFrame;
	private JPanel contentPanel;
	private JPanel panel;
	
	private JLabel label;
	private JLabel timeLabel;
	private JLabel breakText;
	
	private Color bgColor = Color.BLACK;
	private Color textColor = Color.WHITE;
	
	private Countdown refCountdown;
	
	private boolean isOpen = false;
	
	private boolean escPressed = false;
	private boolean ctrlPressed = false;
	private boolean shiftPressed = false;
	
	public Break(String title, Countdown countdown) {
		refCountdown = countdown;		
		
		bgColor = Settings.getBGColor();
		textColor = Settings.getFGColor();
		
		contentPanel = new JPanel();
		contentPanel.setBounds(0, 400, 400, 400);
		
		panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0,1);
		gridLayout.setVgap(10);
        panel.setLayout(gridLayout);
        contentPanel.add(panel);
        
        
        Font font = new Font(Settings.getFontName(), Font.PLAIN, 24);
        bgColor = Settings.getBGColor();
		textColor = Settings.getFGColor();
        
        label = new JLabel(Settings.getBreakTitleText(), SwingConstants.CENTER);
        label.setFont(font);
        label.setFont(label.getFont ().deriveFont (128.0f));
        label.setForeground(textColor);
		panel.add(label);		
        
		timeLabel = new JLabel("not set", SwingConstants.CENTER);
		timeLabel.setFont(font);
		timeLabel.setForeground(textColor);
		timeLabel.setFont(timeLabel.getFont ().deriveFont (40.0f));
		panel.add(timeLabel);		
		
		breakText = new JLabel(Settings.getBreakText(), SwingConstants.CENTER);
		breakText.setFont(font);
		breakText.setFont(label.getFont().deriveFont (34.0f));
		breakText.setForeground(textColor);
		panel.add(breakText);
		
        breakFrame = new JFrame(title);
		breakFrame.add(contentPanel);
		breakFrame.setSize(600, 400);
		
		breakFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Always on top 
		int sta = breakFrame.getExtendedState() & ~JFrame.ICONIFIED & JFrame.NORMAL;
		breakFrame.setExtendedState(sta);
		breakFrame.setAlwaysOnTop(true);
		breakFrame.toFront();
		breakFrame.requestFocus();
		
		breakFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		breakFrame.setUndecorated(true);
		
		panel.setBackground(bgColor);
		contentPanel.setBackground(bgColor);
		
		breakFrame.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) { }

	        @Override
	        public void keyPressed(KeyEvent e) {
	        	if (e.getKeyCode() == KeyEvent.VK_SHIFT)
	            {
	            	shiftPressed = true;
	            }
	            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
	            {
	            	escPressed = true;
	            }
	            if (e.getKeyCode() == KeyEvent.VK_CONTROL)
	            {
	            	ctrlPressed = true;
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
		escPressed = false;
		ctrlPressed = false;
		shiftPressed = false;
		
		bgColor = Settings.getBGColor();
		textColor = Settings.getFGColor();
		label.setForeground(textColor);
		timeLabel.setForeground(textColor);
		panel.setBackground(bgColor);
		contentPanel.setBackground(bgColor);
		
		breakFrame.setVisible(true);
		isOpen = true;
		new Thread(this).start();
	}
	public void run() {
	    try {
	        kill("explorer.exe"); // Kill explorer
	        
	        Robot robot = new Robot();
	        int i = 0;
	        while (isOpen) {
	        	if (ctrlPressed && escPressed && shiftPressed)
	            {
	            	ForceStop();
	            }
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
	@SuppressWarnings("deprecation")
	public void Close()
	{
		isOpen = false;
		breakFrame.hide();
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
	  //  this.frame.grabFocus();
	    this.breakFrame.requestFocus();
	  }
}
