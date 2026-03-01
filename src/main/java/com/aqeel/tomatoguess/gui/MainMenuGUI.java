package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.database.BonusesHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.BonusRewarder;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppDimen;
import com.aqeel.tomatoguess.resources.AppImages;
import com.aqeel.tomatoguess.resources.AppStrings;
import com.aqeel.tomatoguess.utilities.ImageScaler;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents the GUI for the main menu of the game.
 *
 * This panel provides options for starting a new game, accessing the
 * scoreboard, adjusting settings, and quitting the game.
 *
 * @author Aqeel Jabir
 */
public class MainMenuGUI extends JPanel implements ActionListener {

    App app;
    TomatoEngine tomatoEngine;
    BonusRewarder bonusRewarder;
    AudioManager audioManager;

    // UI Components.
    ButtonUI btnNewGame, btnScoreboard, btnSettings, btnQuitGame;

    /**
     * Constructs a new instance of MainMenuGUI class.
     *
     * @param app The main application instance.
     */
    public MainMenuGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.bonusRewarder = app.getBonusRewarder();
        this.audioManager = app.getAudioManager();
        initComponents();
    }

    /**
     * Initializes and sets up the components of the main menu panel.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbcMainMenu = new GridBagConstraints();
        gbcMainMenu.insets = new Insets(0, 0, 40, 0);
        gbcMainMenu.gridwidth = GridBagConstraints.REMAINDER;
        gbcMainMenu.fill = GridBagConstraints.CENTER;

        // Displays the game icon.
        ImageScaler imageScaler = new ImageScaler();
        imageScaler.setSize(150, 200);
        ImageIcon appLogo = imageScaler.getScaledImage(AppImages.appLogo);

        JLabel gameIconDisplay = new JLabel(appLogo, SwingConstants.CENTER);
        add(gameIconDisplay, gbcMainMenu);

        gbcMainMenu.insets = new Insets(10, 0, 0, 0);

        // Create buttons for main menu.
        btnNewGame = new ButtonUI("New Game", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnNewGame.addActionListener(this);
        add(btnNewGame, gbcMainMenu);

        btnScoreboard = new ButtonUI("Score Board", AppColors.SECONDARY_COLOUR, AppDimen.BUTTON_SIZE);
        btnScoreboard.addActionListener(this);
        add(btnScoreboard, gbcMainMenu);

        btnSettings = new ButtonUI(AppStrings.SETTINGS_PANEL, AppColors.SECONDARY_COLOUR, AppDimen.BUTTON_SIZE);
        btnSettings.addActionListener(this);
        add(btnSettings, gbcMainMenu);

        btnQuitGame = new ButtonUI("Quit Game", AppColors.SECONDARY_COLOUR, AppDimen.BUTTON_SIZE);
        btnQuitGame.addActionListener(this);
        add(btnQuitGame, gbcMainMenu);
    }

    /**
     * Handles the actions performed when buttons are clicked.
     *
     * @param e The ActionEvent representing the button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnNewGame)) {
            // Starts a new game.
            audioManager.playButtonSoundEffects();

            tomatoEngine.startNewGame();

            // Setup bonus information for new game.
            String email = tomatoEngine.getPlayerEmail();
            Document bonusDoc = BonusesHandler.getBonuses(email);
            if (bonusDoc != null) {
                bonusRewarder.setExtraChance(bonusDoc.getInteger("life"));
                bonusRewarder.setGameSkips(bonusDoc.getInteger("skips"));
                bonusRewarder.setAnswerHints(bonusDoc.getInteger("hints"));
                bonusRewarder.setExtraTime(bonusDoc.getInteger("time"));

                // Set the bonus
                app.gamePanel.setBonuses(
                        bonusRewarder.getExtraChance(),
                        bonusRewarder.getGameSkips(),
                        bonusRewarder.getAnswerHints(),
                        bonusRewarder.getExtraTime());
            }

            // Update the UI and start the game.
            app.gamePanel.setNextGame();
            app.gamePanel.setChances();
            app.gamePanel.resetComponents();
            app.gamePanel.startTimer(System.currentTimeMillis());

            app.setExtendedState(JFrame.MAXIMIZED_BOTH);
            app.setShape(null);
            app.getPanel(AppStrings.GAME_PANEL);

        } else if (e.getSource().equals(btnScoreboard)) {
            // Navigates to the scoreboard GUI.
            audioManager.playButtonSoundEffects();
            app.scoreBoardPanel.updateScores();
            app.getPanel(AppStrings.SCOREBOARD_PANEL);

        } else if (e.getSource().equals(btnSettings)) {
            // Navigates to the settings GUI
            audioManager.playButtonSoundEffects();
            app.getPanel(AppStrings.SETTINGS_PANEL);

        } else if (e.getSource().equals(btnQuitGame)) {
            // Quit the game.
            audioManager.playButtonSoundEffects();
            audioManager.stopBackgroundMusic();
            System.exit(0);
        }
    }
}
