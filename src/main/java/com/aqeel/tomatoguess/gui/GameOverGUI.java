package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.database.BonusesHandler;
import com.aqeel.tomatoguess.database.ScoreboardHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.BonusRewarder;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppDimen;
import com.aqeel.tomatoguess.resources.AppIcons;
import com.aqeel.tomatoguess.resources.AppStrings;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameOverGUI extends JPanel and provides the user interface for the game over
 * screen.
 *
 * It displays the player's final score, level, and stars achieved in the game.
 *
 * The class also handles the 'Back' button click, updating the scoreboard, and
 * bonus information.
 *
 * It integrates audio feedback for a more engaging experience.
 *
 * @author Aqeel Jabir
 */

public class GameOverGUI extends JPanel implements ActionListener {

    // Class Variables.
    App app;
    TomatoEngine tomatoEngine;
    AudioManager audioManager;
    BonusRewarder bonusRewarder;

    // Results-related variables.
    int level;
    int score;
    int stars;

    // UI Components.
    ButtonUI btnBack;
    JLabel levelArea, scoreArea, starsArea;
    JPanel starsPanel;

    /**
     * Constructor for GameOverGUI class.
     *
     * @param app The main application instance.
     */
    public GameOverGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.audioManager = app.getAudioManager();
        this.bonusRewarder = app.getBonusRewarder();
        initComponents(); // Initializing UI components.
    }

    /**
     * Sets the game results informaton for display.
     *
     * @param level The final level reached.
     * @param score The final score achieved.
     * @param stars The final stars earned.
     */
    public void setGameResult(int level, int score, int stars) {
        this.level = level;
        this.score = score;
        this.stars = stars;

        levelArea.setText(String.valueOf(level));
        scoreArea.setText(String.valueOf(score));
        starsArea.setText(String.valueOf(stars));
    }

    /**
     * Initializes and set up the UI components for the game over panel.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(0, 0, 0, 0);
        gbcMain.gridwidth = GridBagConstraints.REMAINDER;
        gbcMain.fill = GridBagConstraints.VERTICAL;

        // Title label for the game over screen.
        JLabel title = new JLabel("Game Over!");
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 34));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        add(title, gbcMain);

        gbcMain.insets = new Insets(0, 0, 40, 0);

        // Panel for displaying stars.
        starsPanel = new JPanel();
        starsPanel.setOpaque(false);
        add(starsPanel, gbcMain);

        // Display three stroke stars by default.
        for (int i = 0; i < 3; i++) {
            JLabel strokeStar = new JLabel(AppIcons.strokeStarIcon);
            starsPanel.add(strokeStar);
        }

        gbcMain.insets = new Insets(0, 0, 10, 0);

        // Panel for displaying game details (level and stars).
        JPanel gameDetail = new JPanel(new GridBagLayout());
        gameDetail.setBorder(new EmptyBorder(0, 0, 0, 15));
        gameDetail.setOpaque(false);
        add(gameDetail, gbcMain);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 0);
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Icon representing the level.
        JLabel levelIconArea = new JLabel(AppIcons.filledBarIcon);
        gameDetail.add(levelIconArea, gbc);

        // Label displaying the reached level.
        levelArea = new JLabel("0");
        levelArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 16));
        levelArea.setForeground(AppColors.NEUTRAL_COLOUR);
        gameDetail.add(levelArea, gbc);

        gbc.insets = new Insets(0, 60, 0, 0);

        // Icon representing the stars.
        JLabel starIconArea = new JLabel(AppIcons.filledStarsIcon);
        gameDetail.add(starIconArea, gbc);

        gbc.insets = new Insets(0, 15, 0, 0);

        // Label displaying the total stars earned.
        starsArea = new JLabel("0");
        starsArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 16));
        starsArea.setForeground(AppColors.NEUTRAL_COLOUR);
        gameDetail.add(starsArea, gbc);

        gbcMain.insets = new Insets(0, 0, 0, 0);

        // Label indicating the player's score.
        JLabel urScore = new JLabel("Your Score");
        urScore.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        urScore.setForeground(AppColors.SECONDARY_COLOUR);
        add(urScore, gbcMain);

        // Label displaying player's final score.
        scoreArea = new JLabel("0", SwingConstants.CENTER);
        scoreArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 30));
        scoreArea.setForeground(AppColors.NEUTRAL_COLOUR);
        add(scoreArea, gbcMain);

        gbcMain.insets = new Insets(40, 0, 20, 0);

        // Back button to return to the main menu.
        btnBack = new ButtonUI("Back", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnBack.addActionListener(this);
        add(btnBack, gbcMain);
    }

    /**
     * Handles action event triggered by the Back button.
     *
     * @param e The ActionEvent generated by the player's interaction with the
     *          components.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnBack)) {
            // Action to handle the Back button click.
            audioManager.playButtonSoundEffects();

            // Update the scoreboard and bonus information if the player scored.
            if (score != 0) {

                // Retrieve player information from TomatoEngine.
                String email = tomatoEngine.getPlayerEmail();
                String name = tomatoEngine.getPlayerName();
                String country = tomatoEngine.getCountry();
                long totalPlayTime = tomatoEngine.getTotalPlayTime();

                // Create a document with player information for scoreboard entry.
                Document document = new Document("email", email)
                        .append("playerName", name)
                        .append("level", level)
                        .append("score", score)
                        .append("totalStars", stars)
                        .append("totalPlayTime", totalPlayTime)
                        .append("country", country);

                // Add the player's score to the scoreboard in the database.
                ScoreboardHandler.addScore(document);

                // Create an update document for bonus information.
                Document update = new Document("$set", new Document("life", bonusRewarder.getExtraChance())
                        .append("skips", bonusRewarder.getGameSkips())
                        .append("hints", bonusRewarder.getAnswerHints())
                        .append("time", bonusRewarder.getExtraTime()));

                // Update the player's bonus information in the database.
                BonusesHandler.updateBonuses(email, update);
            }
            // Return to the main menu panel.
            app.getPanel(AppStrings.MAIN_MENU_PANEL);
        }
    }
}
