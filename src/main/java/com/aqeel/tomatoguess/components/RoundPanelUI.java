package com.aqeel.tomatoguess.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom JPanel with a rounded appearance. This class extends JPanel to create
 * a customized panel with rounded corners and a specified background color.
 *
 * @author Aqeel Jabir
 *
 */
public class RoundPanelUI extends JPanel {

    private int arcWidth = 0;
    private int arcHeight = 0;
    private Color backgroundColor;

    /**
     * Constructs a new RoundPanelGUI with default values. The panel is set to a
     * default size of 60x60 pixels.
     */
    public RoundPanelUI(Dimension size) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setPreferredSize(size);
    }

    /**
     * Sets the corner radius of the rounded panel.
     *
     * @param arcWidth  The width of the corner radius.
     * @param arcHeight The height of the corner radius.
     */
    public void setArc(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    /**
     * Sets the background color of the rounded panel.
     *
     * @param backgroundColor The background color to set.
     */
    public void setBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Overrides the default paint behavior to render a rounded rectangle with
     * specified background color and corner radius.
     * This method is automatically called by Swing to paint the component.
     *
     * @param g The Graphics context to paint on.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(backgroundColor);
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, arcWidth, arcHeight));
    }
}
