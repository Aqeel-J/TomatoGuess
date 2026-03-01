package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.resources.AppImages;
import com.aqeel.tomatoguess.utilities.ImageScaler;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the welcome panel displayed during application startup.
 * This panel typically shows the application logo and a loading indicator.
 *
 * @author Aqeel Jabir
 */
public class WelcomeGUI extends JPanel {

    /**
     * Constructs a new WelcomeGUI instance.
     * Initializes and sets up the components for the welcome panel.
     */
    public WelcomeGUI() {
        initComponents();
    }

    /**
     * Initializes and sets up the components of the welcome panel.
     * Adds an application logo and a loading indicator to the panel.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 50, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ImageScaler imageScaler = new ImageScaler();
        imageScaler.setSize(250, 200);
        ImageIcon appIcon = imageScaler.getScaledImage(AppImages.appLogo);

        JLabel appIconLabel = new JLabel(appIcon);
        add(appIconLabel, gbc);

        JLabel loader = new JLabel(AppImages.loaderGif);
        add(loader);
    }
}
