package com.aqeel.tomatoguess.components;

import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppStrings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Custom JLabel class for displaying error messages with specific styling.
 * This JLabel is designed for presenting textual error messages with a left
 * alignment, a specified font, and a preferred size.
 *
 * @author Aqeel Jabir
 */

public class TextErrorMessage extends JLabel {

    /**
     * Constructs a new TextErrorMessage with specified text.
     *
     * @param text The error message to be displayed.
     */
    public TextErrorMessage(String text) {
        super(text);
        setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 10));
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(new EmptyBorder(0, 5, 0, 0));
        setPreferredSize(new Dimension(200, 20));
        setForeground(AppColors.DANGER_COLOUR);
    }
}
