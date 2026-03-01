package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.components.RoundTextFieldUI;
import com.aqeel.tomatoguess.components.TextErrorMessage;
import com.aqeel.tomatoguess.database.PlayerHandler;
import com.aqeel.tomatoguess.database.PreferencesHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppDimen;
import com.aqeel.tomatoguess.resources.AppIcons;
import com.aqeel.tomatoguess.resources.AppStrings;
import com.aqeel.tomatoguess.utilities.IPAddressReader;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The SettingsGUI class represents the GUI for managing game settings.
 *
 * It allows the player to customize their name, toggle background music, and
 * adjust sound settings.
 *
 * This class extends JPanel and implements ActionListener to handle button
 * interactions.
 *
 * @author Aqeel Jabir
 */
public class SettingsGUI extends JPanel implements ActionListener {

    App app;
    TomatoEngine tomatoEngine;
    AudioManager audioManager;

    // UI Components.
    RoundTextFieldUI txtEditPlayerName;
    ButtonUI btnBack;
    JToggleButton btnEditName, btnToggleMusic, btnToggleSound;
    JLabel playerNameLabel, info_err;
    TextErrorMessage name_err;

    /**
     * Constructs a new instance for SettingsGUI class.
     *
     * @param app The main application instance.
     */
    public SettingsGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.audioManager = app.getAudioManager();
        initComponents();
    }

    /**
     * Sets up the initial settings based on the provided parameters.
     *
     * @param playerName  The current player's name.
     * @param musicStatus The status of background music.
     * @param soundStatus The status of sound effects.
     */
    public void setupSettings(String playerName, boolean musicStatus, boolean soundStatus) {
        // Set initial values for player name, music, and sound settings
        this.playerNameLabel.setText(playerName);
        this.txtEditPlayerName.setText(playerName);

        // Configure background music toggle button
        if (musicStatus) {
            btnToggleMusic.setSelected(false);
            btnToggleMusic.setBackground(AppColors.SECONDARY_COLOUR);
        } else {
            btnToggleMusic.setSelected(true);
            btnToggleMusic.setBackground(AppColors.ACCENT_COLOUR);
        }

        // Configure sound effects toggle button
        if (soundStatus) {
            btnToggleSound.setSelected(false);
            btnToggleSound.setBackground(AppColors.SECONDARY_COLOUR);
        } else {
            btnToggleSound.setSelected(true);
            btnToggleSound.setBackground(AppColors.ACCENT_COLOUR);
        }

    }

    /**
     * Initializes and configures the GUI components.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(AppStrings.SETTINGS_PANEL, SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 24));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        add(title, gbc);

        info_err = new JLabel("", SwingConstants.CENTER);
        info_err.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 10));
        info_err.setBorder(new EmptyBorder(0, 5, 0, 0));
        info_err.setPreferredSize(new Dimension(230, 20));
        info_err.setVisible(false);
        add(info_err, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel namePanel = new JPanel(new GridBagLayout());
        namePanel.setOpaque(false);
        add(namePanel, gbc);

        JPanel textNamePanel = new JPanel(new FlowLayout());
        textNamePanel.setOpaque(false);
        namePanel.add(textNamePanel, gbc);

        playerNameLabel = new JLabel();
        playerNameLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 14));
        playerNameLabel.setPreferredSize(new Dimension(200, 30));
        playerNameLabel.setVisible(true);
        textNamePanel.add(playerNameLabel);

        txtEditPlayerName = new RoundTextFieldUI(AppColors.TRANSPARENT_COLOUR, AppColors.NEUTRAL_COLOUR,
                AppDimen.TEXTFIELD_SIZE);
        txtEditPlayerName.setVisible(false);
        textNamePanel.add(txtEditPlayerName);

        btnEditName = new JToggleButton(AppIcons.penIcon);
        btnEditName.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEditName.setSelectedIcon(AppIcons.checkIcon);
        btnEditName.setPreferredSize(new Dimension(30, 30));
        btnEditName.setBorderPainted(false);
        btnEditName.addActionListener(this);
        textNamePanel.add(btnEditName, gbc);

        name_err = new TextErrorMessage("Error");
        name_err.setPreferredSize(new Dimension(230, 20));
        name_err.setVisible(false);
        namePanel.add(name_err);

        JPanel musicPanel = new JPanel();
        musicPanel.setOpaque(false);
        add(musicPanel, gbc);

        JLabel musicLabel = new JLabel("Music");
        musicLabel.setPreferredSize(new Dimension(200, 30));
        musicLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 14));
        musicPanel.add(musicLabel);

        btnToggleMusic = new JToggleButton(AppIcons.musicIcon);
        btnToggleMusic.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnToggleMusic.setSelectedIcon(AppIcons.noMusicIcon);
        btnToggleMusic.setPreferredSize(new Dimension(30, 30));
        btnToggleMusic.setBorderPainted(false);
        btnToggleMusic.addActionListener(this);
        musicPanel.add(btnToggleMusic);

        JPanel soundPanel = new JPanel();
        soundPanel.setOpaque(false);
        add(soundPanel, gbc);

        JLabel soundLabel = new JLabel("Sound");
        soundLabel.setPreferredSize(new Dimension(200, 30));
        soundLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 14));
        soundPanel.add(soundLabel);

        btnToggleSound = new JToggleButton(AppIcons.soundOnIcon);
        btnToggleSound.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnToggleSound.setSelectedIcon(AppIcons.muteIcon);
        btnToggleSound.setPreferredSize(new Dimension(30, 30));
        btnToggleSound.setBorderPainted(false);
        btnToggleSound.addActionListener(this);
        soundPanel.add(btnToggleSound);

        gbc.insets = new Insets(30, 0, 0, 0);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        add(btnPanel, gbc);

        btnBack = new ButtonUI("Back", AppColors.SECONDARY_COLOUR, AppDimen.BUTTON_SIZE);
        btnBack.addActionListener(this);
        btnPanel.add(btnBack);
    }

    /**
     * Handles action events triggered by buttons in the settings panel.
     *
     * @param e The ActionEvent representing the button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnBack)) {
            // Navigates back to the main menu
            audioManager.playButtonSoundEffects();
            app.getPanel(AppStrings.MAIN_MENU_PANEL);

        } else if (e.getSource().equals(btnEditName)) {
            // Toggle between editing and viewing player's name

            audioManager.playButtonSoundEffects();

            if (btnEditName.isSelected()) {
                btnEditName.setBackground(AppColors.ACCENT_COLOUR);
                txtEditPlayerName.setVisible(true);
                playerNameLabel.setVisible(false);

            } else {
                btnEditName.setBackground(AppColors.SECONDARY_COLOUR);
                txtEditPlayerName.setVisible(false);
                playerNameLabel.setVisible(true);

                String newPlayerName = txtEditPlayerName.getText().trim();
                String email = tomatoEngine.getPlayerEmail();

                if (!newPlayerName.isEmpty()) {
                    name_err.setText("");
                    name_err.setVisible(false);

                    if (!newPlayerName.trim().equals(tomatoEngine.getPlayerName())) {
                        Document update = new Document("$set", new Document("playerName", newPlayerName));
                        PlayerHandler.updatePlayer(email, update);

                        this.playerNameLabel.setText(newPlayerName);
                        this.txtEditPlayerName.setText(newPlayerName);
                        tomatoEngine.setPlayerName(newPlayerName);

                        System.out.println("Player name updated successfully.");
                    }

                } else {
                    name_err.setText("Name cannot be blank!");
                    name_err.setVisible(true);

                    btnEditName.setSelected(true);
                    btnEditName.setBackground(AppColors.ACCENT_COLOUR);
                    txtEditPlayerName.setVisible(true);
                    playerNameLabel.setVisible(false);
                }
            }

        } else if (e.getSource().equals(btnToggleMusic)) {
            // Toggle background music and update preferences
            audioManager.playButtonSoundEffects();

            String ip = IPAddressReader.getPrivateIpAddress();
            String email = tomatoEngine.getPlayerEmail();
            boolean setMusic;

            if (btnToggleMusic.isSelected()) {
                setMusic = false;
                audioManager.stopBackgroundMusic();
                btnToggleMusic.setBackground(AppColors.ACCENT_COLOUR);
            } else {
                setMusic = true;
                audioManager.playBackgroundMusic();
                btnToggleMusic.setBackground(AppColors.SECONDARY_COLOUR);
            }

            audioManager.setMusicStatus(setMusic);
            Document updateMusic = new Document("$set", new Document("music", setMusic));
            PreferencesHandler.updatePreferences(email, ip, updateMusic);

        } else if (e.getSource().equals(btnToggleSound)) {
            // Toggle sound effects and update preferences
            audioManager.playButtonSoundEffects();

            String ip = IPAddressReader.getPrivateIpAddress();
            String email = tomatoEngine.getPlayerEmail();
            boolean setSound;

            if (btnToggleSound.isSelected()) {
                setSound = false;
                btnToggleSound.setBackground(AppColors.ACCENT_COLOUR);
            } else {
                setSound = true;
                btnToggleSound.setBackground(AppColors.SECONDARY_COLOUR);
            }

            audioManager.setSoundStatus(setSound);
            Document updateSound = new Document("$set", new Document("sound", setSound));
            PreferencesHandler.updatePreferences(email, ip, updateSound);
        }
    }
}
