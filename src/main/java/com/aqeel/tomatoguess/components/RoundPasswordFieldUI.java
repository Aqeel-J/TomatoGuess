package com.aqeel.tomatoguess.components;

import com.aqeel.tomatoguess.resources.AppColors;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Custom JPasswordField with rounded appearance and custom styling. This class
 * extends JPasswordField to create a password field with rounded corners,
 * custom background, foreground, size and border.
 *
 * @author Aqeel Jabir
 *
 */
public class RoundPasswordFieldUI extends JPasswordField {

    private final int arcWidth = 10;
    private final int arcHeight = 10;
    private int borderSpacingTB = 5;
    private int borderSpacingLR = 10;

    private Color borderColor = new Color(80, 80, 80);
    private final Color normalBorderColor = new Color(80, 80, 80);
    private final Color focusBorderColor = AppColors.ACCENT_COLOUR;

    /**
     * Constructs a new RoundPasswordFieldGUI with specified background color,
     * foreground color, size (width and height).
     *
     * @param bgColor The background color of the password field.
     * @param fgColor The foreground color of the password field.
     * @param size    The preferred size (width and height) of the password field.
     */
    public RoundPasswordFieldUI(Color bgColor, Color fgColor, Dimension size) {
        super();
        setOpaque(false);
        // setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
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
     * Overrides the default paint behavior of the JPasswordField to render a
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
     * Overrides the default border-painting behavior of the JPasswordField to draw a rounded rectangle
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
