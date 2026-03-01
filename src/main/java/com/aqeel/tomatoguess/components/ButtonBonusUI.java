package com.aqeel.tomatoguess.components;

import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.utilities.ImageScaler;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JButton class for displaying a bonus icon with a count label.
 * This button has a rounded appearance with an icon and a count label.
 *
 * @author Aqeel Jabir
 */

public class ButtonBonusUI extends JButton {

    /* The JLabel is used to display the bonus count on the ButtonBonusUI */
    JLabel countLabel;

    /**
     * Constructs a new ButtonBonusUI with the specified icon.
     *
     * @param icon to be displayed on the button.
     */
    public ButtonBonusUI(ImageIcon icon) {
        setBackground(AppColors.TRANSPARENT_COLOUR);
        setBorder(null);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        RoundPanelUI mainPanel = new RoundPanelUI(new Dimension(60, 60));
        mainPanel.setBackground(AppColors.PEACH_COLOUR);
        mainPanel.setArc(100, 100);
        mainPanel.setLayout(null);
        add(mainPanel);

        ImageScaler imageScaler = new ImageScaler();
        imageScaler.setSize(40, 40);
        ImageIcon scaledIcon = imageScaler.getScaledImage(icon);

        JLabel image = new JLabel(scaledIcon);
        image.setBounds(10, 10, 40, 40);
        mainPanel.add(image);

        RoundPanelUI countPanel = new RoundPanelUI(new Dimension(25, 25));
        countPanel.setArc(15, 15);
        countPanel.setBounds(45, 45, 15, 15);
        countPanel.setBackground(AppColors.NEUTRAL_COLOUR);
        mainPanel.add(countPanel);

        countLabel = new JLabel();
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setFont(new Font(getFont().getName(), Font.PLAIN, 10));
        countLabel.setForeground(AppColors.PRIMARY_COLOUR);
        countPanel.add(countLabel);
    }

    /**
     * Sets the bonus count and updates the count label.
     *
     * @param count of the bonus to be set.
     */
    public void setBonusCount(int count) {
        countLabel.setText(String.valueOf(count));
    }
}