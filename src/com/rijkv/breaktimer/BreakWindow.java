package com.rijkv.breaktimer;

import java.awt.Component;
import java.awt.Robot;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.rijkv.breaktimer.filemanagement.FileManager;
import com.rijkv.breaktimer.components.*;

public class BreakWindow implements Runnable {

    private JFrame breakFrame;
    private ImagePanel contentPanel;

    private JLabel titleLabel;
    private JLabel timeLabel;
    private JLabel descriptionLabel;

    private volatile boolean isOpened = false;

    private final boolean FORCE_MODE = true;

    public BreakWindow() {
        contentPanel = new ImagePanel(FileManager.getBreakBackgroundImage());

        BoxLayout layout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
        contentPanel.setLayout(layout);
        
        titleLabel = new JLabel("TITLE");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(FileManager.getTextColor());
        titleLabel.setFont(FileManager.getBoldFont(128f));
        contentPanel.add(titleLabel);

        timeLabel = new JLabel("TIME");
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        timeLabel.setForeground(FileManager.getTextColor());
        timeLabel.setFont(FileManager.getBoldFont(40f));
        contentPanel.add(timeLabel);

        descriptionLabel = new JLabel("DESCRIPTION");
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setForeground(FileManager.getTextColor());
        descriptionLabel.setFont(FileManager.getFont(34f));
        contentPanel.add(descriptionLabel);

        breakFrame = new JFrame("Break");
        breakFrame.add(contentPanel);

        // Always on top
        if (FORCE_MODE) {
            breakFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            int state = breakFrame.getExtendedState() & ~JFrame.ICONIFIED & JFrame.NORMAL;
            breakFrame.setExtendedState(state);
            breakFrame.setAlwaysOnTop(true);
            breakFrame.toFront();
            breakFrame.requestFocus();
        }

        breakFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        breakFrame.setUndecorated(true);
    }

    public void UpdateTimeText(String timeText) {
        timeLabel.setText(timeText);
    }

    public void open(BreakInfo breakInfo) {
        titleLabel.setText(breakInfo.name);
        descriptionLabel.setText(breakInfo.description);

        breakFrame.setVisible(true);
        isOpened = true;

        FileManager.playSound("break_sound");

        new Thread(this).start();
    }

    public void close() {
        isOpened = false;
        breakFrame.setVisible(false);
    }

    // Runnable / Force mode

    public void run() {
        if (FORCE_MODE) {
            try {
                kill("explorer.exe"); // Kill explorer

                Robot robot = new Robot();
                int i = 0;
                while (isOpened) {
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