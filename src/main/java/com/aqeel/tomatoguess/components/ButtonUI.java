package com.aqeel.tomatoguess.components;

import com.aqeel.tomatoguess.resources.AppStrings;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JButton with a specific appearance. This class extends JButton to
 * create a customized button with a specified background color, size, and font
 * for a better user interface.
 *
 * @author Aqeel Jabir
 *
 */
public class ButtonUI extends JButton {

    private boolean loading = false;
    private int activeDot = 0;
    private Timer loaderTimer;

    private final int dotSize = 4;
    private final int spacing = 8;
    private final int dotCount = 4;

    /**
     * Constructs a new ButtonUI with the specified text, background color and
     * size (width and height).
     *
     * @param text    The text displayed on the button.
     * @param bgColor Sets the background color of the button.
     * @param size    Sets the preferred size (width and height) of the button.
     */
    public ButtonUI(String text, Color bgColor, Dimension size) {
        super(text);
        init(bgColor, size);
        setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
    }

    /**
     * Constructs a new ButtonUI with the specified icon, background color and
     * size (width and height).
     *
     * @param icon    The icon that's displayed on the button.
     * @param bgColor Sets the background color of the button.
     * @param size    Sets the preferred size (width and height) of the button.
     */
    public ButtonUI(Icon icon, Color bgColor, Dimension size) {
        super(icon);
        init(bgColor, size);
    }

    /**
     * Constructs a new ButtonUI with the specified icon.
     * Background color is set to transparent as default.
     *
     * @param icon thats displayed on the button.
     */
    public ButtonUI(Icon icon) {
        super(icon);
        init(new Color(0,0,0,0), null);
    }

    /**
     * Constructs a new ButtonUI with the specified text and foreground color.
     * Background color of the button is set to transparent as default.
     *
     * @param text    The text to be displayed on the button.
     * @param fgColor Sets the foreground color of the button.
     */
    public ButtonUI(String text, Color fgColor) {
        super(text);
        init(new Color(0,0,0,0), null);
        setForeground(fgColor);
        setBorder(null);
    }

    private void init(Color bgColor, Dimension size) {
        if (size != null) setPreferredSize(size);
        setBackground(bgColor);

        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setForeground(Color.WHITE);
    }

    /* =============================
       LOADING CONTROL
       ============================= */

    public void setLoading(boolean isLoading) {
        this.loading = isLoading;

        if (loading) {
            setText("");
            setIcon(null);
            startLoader();
        } else {
            stopLoader();
        }

        repaint();
    }

    private void startLoader() {
        loaderTimer = new Timer(250, e -> {
            activeDot = (activeDot + 1) % dotCount;
            repaint();
        });
        loaderTimer.start();
    }

    private void stopLoader() {
        if (loaderTimer != null) {
            loaderTimer.stop();
        }
    }

    /* =============================
       CUSTOM PAINT
       ============================= */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!loading) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int totalWidth = (dotCount * dotSize) + ((dotCount - 1) * spacing);
        int startX = (getWidth() - totalWidth) / 2;
        int y = getHeight() / 2 - dotSize / 2;

        for (int i = 0; i < dotCount; i++) {

            if (i == activeDot) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(new Color(255, 255, 255, 100));
            }

            int x = startX + i * (dotSize + spacing);
            g2.fillOval(x, y, dotSize, dotSize);
        }
    }
}
