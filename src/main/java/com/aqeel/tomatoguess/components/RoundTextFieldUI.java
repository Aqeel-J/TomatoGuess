package com.aqeel.tomatoguess.components;

import com.aqeel.tomatoguess.resources.AppColors;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Custom JTextField with rounded appearance and custom styling. This class
 * extends JTextField to create a text field with rounded corners, custom
 * background, foreground, size and border.
 *
 * @author Aqeel Jabir
 *
 */
public class RoundTextFieldUI extends JTextField {

    private final int arcWidth = 10;
    private final int arcHeight = 10;
    private int borderSpacingTB = 5;
    private int borderSpacingLR = 10;

    private Color borderColor = new Color(80, 80, 80);
    private final Color normalBorderColor = new Color(80, 80, 80);
    private final Color focusBorderColor = AppColors.ACCENT_COLOUR;

    /**
     * Constructs a new RoundTextFieldGUI with specified background color, foreground
     * color, size (width and height).
     *
     * @param bgColor The background color of the text field.
     * @param fgColor The foreground color of the text field.
     * @param size    The preferred size (width and height) of the text field.
     */
    public RoundTextFieldUI(Color bgColor, Color fgColor, Dimension size) {
        super();
        setOpaque(false);
        setBackground(bgColor);
        setForeground(fgColor);
        setPreferredSize(size);
        setHorizontalAlignment(SwingConstants.LEFT);
        setCaretColor(borderColor);

        setBorder(new EmptyBorder(borderSpacingTB, borderSpacingLR, borderSpacingTB, borderSpacingLR));
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                borderColor = focusBorderColor;
                repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                borderColor = normalBorderColor;
                repaint();
            }
        });
    }

    /**
     * Overrides the default paint behavior of the JTextField to render a
     * rounded rectangle as the background of the component.
     * This method is automatically called by Swing to paint the component.
     * Antialiasing is applied for smoother graphics.
     *
     * @param g The Graphics context to paint on.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        super.paintComponent(g);
    }

    /**
     * Overrides the default border-painting behavior of the JTextField to draw a
     * rounded rectangle
     * border with the specified color and corner radius.
     *
     * @param g The Graphics context to paint the border on.
     */
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(borderColor);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
    }
}
