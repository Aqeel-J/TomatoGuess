package com.aqeel.tomatoguess.components;

import javax.swing.*;
import java.awt.*;

public class ShadowPanelUI extends JPanel {

    private int shadowSize = 14;     // thickness of shadow
    private int arc = 20;            // corner roundness

    private Color cardColor = Color.WHITE;
    private Color shadowColor = new Color(0, 0, 0); // default black shadow

    public ShadowPanelUI() {
        setOpaque(false);
        setPreferredSize(new Dimension(420, 520)); // adjust if needed
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw shadow (soft blur effect)
        for (int i = 0; i < shadowSize; i++) {

            float opacity = (float) (shadowSize - i) / (shadowSize * 12);

            g2.setColor(new Color(
                    shadowColor.getRed(),
                    shadowColor.getGreen(),
                    shadowColor.getBlue(),
                    (int) (opacity * 255)
            ));

            g2.fillRoundRect(
                    i,
                    i,
                    width - i * 2,
                    height - i * 2,
                    arc,
                    arc
            );
        }

        // Draw main card
        g2.setColor(cardColor);
        g2.fillRoundRect(
                shadowSize / 2,
                shadowSize / 2,
                width - shadowSize,
                height - shadowSize,
                arc,
                arc
        );

        g2.dispose();
    }

    /* ===============================
       Customization Methods
       =============================== */

    public void setCardColor(Color color) {
        this.cardColor = color;
        repaint();
    }

    public void setShadowColor(Color color) {
        this.shadowColor = color;
        repaint();
    }

    public void setShadowSize(int size) {
        this.shadowSize = size;
        repaint();
    }

    public void setArc(int arcRadius) {
        this.arc = arcRadius;
        repaint();
    }
}