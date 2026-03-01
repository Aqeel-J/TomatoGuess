package com.aqeel.tomatoguess.components;

import javax.swing.*;
import java.awt.*;

public class DotLoaderUI extends JPanel {

    private final int dotCount = 4;
    private int activeDot = 0;
    private final int dotSize = 10;
    private final int spacing = 15;

    private final Color activeColor;
    private final Color inactiveColor;

    private Timer timer;

    public DotLoaderUI(Color activeColor) {
        this.activeColor = activeColor;
        this.inactiveColor = new Color(activeColor.getRed(),
                activeColor.getGreen(),
                activeColor.getBlue(), 80);

        setOpaque(false);
        setPreferredSize(new Dimension(80, 30));

        startAnimation();
    }

    private void startAnimation() {
        timer = new Timer(300, e -> {
            activeDot = (activeDot + 1) % dotCount;
            repaint();
        });
        timer.start();
    }

    public void stopAnimation() {
        if (timer != null) {
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int totalWidth = (dotCount * dotSize) + ((dotCount - 1) * spacing);
        int startX = (getWidth() - totalWidth) / 2;
        int y = getHeight() / 2 - dotSize / 2;

        for (int i = 0; i < dotCount; i++) {
            if (i == activeDot) {
                g2.setColor(activeColor);
            } else {
                g2.setColor(inactiveColor);
            }

            int x = startX + i * (dotSize + spacing);
            g2.fillOval(x, y, dotSize, dotSize);
        }
    }
}