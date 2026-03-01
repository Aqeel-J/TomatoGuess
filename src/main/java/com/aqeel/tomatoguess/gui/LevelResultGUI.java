package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonBonusUI;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.BonusRewarder;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.*;
import com.aqeel.tomatoguess.utilities.ImageScaler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * LevelResultGUI extends JPanel and represents the GUI for displaying the
 * results of a game level, including wheather the player succeeded or failed.
 *
 * @author Aqeel Jabir
 */
public class LevelResultGUI extends JPanel implements ActionListener {

    // Class Variables.
    App app;
    TomatoEngine tomatoEngine;
    BonusRewarder bonusRewarder;
    AudioManager audioManager;

    // UI Components.
    ButtonUI btnClose, btnNextLevel, btnTryAgain;
    JLabel title, scoreArea, levelArea, starsArea;
    JPanel bodyPanel, starsPanel, heartPanel, levelPanel;
    GridBagConstraints gbcMain;

    /**
     * Constructor for LevelResultGUI class.
     *
     * @param app The main application instance.
     */
    public LevelResultGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.bonusRewarder = app.getBonusRewarder();
        this.audioManager = app.getAudioManager();
        initComponents(); // Initialize UI components.
    }

    /**
     * Initializes and sets up the UI components for the level result panel.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 180));

        // Main Model Panel
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                RoundRectangle2D shape = new RoundRectangle2D.Double(0, 0, 500, 500, 20, 20);
                Area area = new Area(shape);
                g.setColor(AppColors.PRIMARY_COLOUR);
                ((Graphics2D) g).fill(area);
            }
        };
        mainPanel.setPreferredSize(new Dimension(500, 500));
        mainPanel.setOpaque(false);
        add(mainPanel);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button component to close the panel.
        btnClose = new ButtonUI(AppIcons.closeIcon);
        btnClose.setBorder(new EmptyBorder(15, 0, 0, 15));
        btnClose.addActionListener(this);
        headerPanel.add(btnClose, BorderLayout.EAST);

        // Body Panel
        bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setOpaque(false);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(0, 0, 0, 0);
        gbcMain.gridwidth = GridBagConstraints.REMAINDER;
        gbcMain.fill = GridBagConstraints.VERTICAL;
    }

    /**
     * Configures components and layout for a successfully completed level.
     *
     * @param level The level completed.
     * @param score The current score.
     */
    private void levelCompleteComponents(int level, int score) {
        gbcMain.insets = new Insets(0, 0, 0, 0);

        // Set up trophy icon.
        ImageScaler imageScaler = new ImageScaler();
        imageScaler.setSize(100, 100);
        ImageIcon trophyIcon = imageScaler.getScaledImage(AppImages.trophyImg);

        levelPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                trophyIcon.paintIcon(this, g, 0, 0);
            }
        };
        levelPanel.setLayout(null);
        levelPanel.setPreferredSize(new Dimension(100, 100));
        levelPanel.setOpaque(false);
        bodyPanel.add(levelPanel, gbcMain);

        // Label to display level.
        levelArea = new JLabel(String.valueOf(level), SwingConstants.CENTER);
        levelArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 20));
        levelArea.setBounds(0, 15, 100, 40);
        levelArea.setForeground(AppColors.NEUTRAL_COLOUR);
        levelPanel.add(levelArea);

        // Label to display "Level Complete!" title.
        title = new JLabel("Level Complete!");
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 28));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        bodyPanel.add(title, gbcMain);

        // Stars Panel
        starsPanel = new JPanel();
        starsPanel.setOpaque(false);
        bodyPanel.add(starsPanel, gbcMain);

        // Label to indicate score.
        JLabel urScore = new JLabel("Your Score");
        urScore.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        urScore.setForeground(AppColors.SECONDARY_COLOUR);
        bodyPanel.add(urScore, gbcMain);

        // Label to display player's score.
        scoreArea = new JLabel(String.valueOf(score), SwingConstants.CENTER);
        scoreArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 30));
        scoreArea.setForeground(AppColors.NEUTRAL_COLOUR);
        bodyPanel.add(scoreArea, gbcMain);

        // Bonus Panel to hold all the button bonus.
        JPanel bonusPanel = new JPanel();
        bonusPanel.setOpaque(false);
        bodyPanel.add(bonusPanel, gbcMain);

        ButtonBonusUI btnChanceBonus = new ButtonBonusUI(AppImages.heartPlusImg);
        btnChanceBonus.setBonusCount(bonusRewarder.getExtraChance());
        btnChanceBonus.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        bonusPanel.add(btnChanceBonus);

        ButtonBonusUI btnHintBonus = new ButtonBonusUI(AppImages.hintImg);
        btnHintBonus.setBonusCount(bonusRewarder.getAnswerHints());
        btnHintBonus.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        bonusPanel.add(btnHintBonus);

        ButtonBonusUI btnSkipBonus = new ButtonBonusUI(AppImages.skipImg);
        btnSkipBonus.setBonusCount(bonusRewarder.getGameSkips());
        btnSkipBonus.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        bonusPanel.add(btnSkipBonus);

        ButtonBonusUI btnTimeBonus = new ButtonBonusUI(AppImages.extraTimerImg);
        btnTimeBonus.setBonusCount(bonusRewarder.getExtraTime());
        btnTimeBonus.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        bonusPanel.add(btnTimeBonus);

        gbcMain.insets = new Insets(20, 0, 20, 0);

        // Button component for next level.
        btnNextLevel = new ButtonUI("Next Level", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnNextLevel.addActionListener(this);
        bodyPanel.add(btnNextLevel, gbcMain);
    }

    /**
     * Configures components and layout for a failed attempt at a level.
     *
     * @param level The current level failed to attempt.
     * @param score The current score.
     */
    private void tryAgainComponents(int level, int score) {

        // Label to display "Try Again!" title.
        title = new JLabel("Try Again!");
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 28));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        bodyPanel.add(title, gbcMain);

        gbcMain.insets = new Insets(5, 0, 30, 0);

        // Set up heart icon.
        heartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                AppIcons.filledHeartIcon_96.paintIcon(this, g, 0, 0);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 28));
                g2d.drawString("-1", 38, 54);
            }
        };
        heartPanel.setLayout(new BorderLayout());
        heartPanel.setPreferredSize(new Dimension(96, 96));
        heartPanel.setOpaque(false);
        bodyPanel.add(heartPanel, gbcMain);

        gbcMain.insets = new Insets(0, 0, 20, 0);

        // Game Detail Panel to display level, stars and score.
        JPanel gameDetail = new JPanel(new GridBagLayout());
        gameDetail.setBorder(new EmptyBorder(0, 0, 0, 15));
        gameDetail.setOpaque(false);
        bodyPanel.add(gameDetail, gbcMain);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 0);
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Displays level icon.
        JLabel levelIconArea = new JLabel(AppIcons.filledBarIcon);
        gameDetail.add(levelIconArea, gbc);

        // Label to display level.
        levelArea = new JLabel("0");
        levelArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 16));
        levelArea.setForeground(AppColors.NEUTRAL_COLOUR);
        gameDetail.add(levelArea, gbc);

        gbc.insets = new Insets(0, 60, 0, 0);

        // Displays star icon.
        JLabel starIconArea = new JLabel(AppIcons.filledStarsIcon);
        gameDetail.add(starIconArea, gbc);

        gbc.insets = new Insets(0, 15, 0, 0);

        // Label to display stars.
        starsArea = new JLabel("0");
        starsArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 16));
        starsArea.setForeground(AppColors.NEUTRAL_COLOUR);
        gameDetail.add(starsArea, gbc);

        gbcMain.insets = new Insets(0, 0, 0, 0);

        // Label to indicate score.
        JLabel urScore = new JLabel("Your Score");
        urScore.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        urScore.setForeground(AppColors.SECONDARY_COLOUR);
        bodyPanel.add(urScore, gbcMain);

        gbcMain.insets = new Insets(0, 0, 30, 0);

        // Label to display score.
        scoreArea = new JLabel(String.valueOf(score), SwingConstants.CENTER);
        scoreArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 30));
        scoreArea.setForeground(AppColors.NEUTRAL_COLOUR);
        bodyPanel.add(scoreArea, gbcMain);

        btnTryAgain = new ButtonUI("Try Again", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnTryAgain.addActionListener(this);
        bodyPanel.add(btnTryAgain, gbcMain);
    }

    /**
     * Sets the level result GUI based on the game outcome and updates relavant
     *
     * @param condition        True if the level was successfully completed
     *                         else false.
     * @param remainingChances Number of remaining chances for the player.
     * @param level            The player's level.
     * @param score            The player's score.
     */
    public void setLevelResult(boolean condition, int remainingChances, int level, int score) {
        // Update the game over panel with the current game result.
        int stars = tomatoEngine.getTotalChances();
        app.gameOverPanel.setGameResult(level, score, stars);

        if (condition) {
            // If the level is successfully completed, display level complete components and
            // update stars panel.
            bodyPanel.removeAll();
            levelCompleteComponents(level, score);
            starsPanel.removeAll();

            int lostChances = 3 - remainingChances;
            for (int i = 0; i < remainingChances; i++) {
                JLabel label = new JLabel(AppIcons.filledStarIcon);
                starsPanel.add(label);
            }

            for (int i = 0; i < lostChances; i++) {
                JLabel label = new JLabel(AppIcons.strokeStarIcon);
                starsPanel.add(label);
            }

        } else {
            // If the level is not successfully completed, display try again components.
            bodyPanel.removeAll();
            tryAgainComponents(level, score);
        }
    }

    /**
     * Handles action events triggered by buttons in the LevelResultGUI.
     *
     * @param e The ActionEvent generated by the player's interaction with the
     *          components.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnClose)) {
            // Handles the close button action.
            audioManager.playButtonSoundEffects();

            // Adjust application size, location and shape.
            app.setSize(500, 500);
            app.setLocationRelativeTo(null);
            app.setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

            // Navigate to the game over panel.
            app.getPanel(AppStrings.GAME_OVER_PANEL);

        } else if (e.getSource().equals(btnTryAgain)) {
            // Handles the try again button action.
            audioManager.playButtonSoundEffects();

            int remainingChances = tomatoEngine.getRemainingChances();
            if (remainingChances > 0) {
                // Decrease the remaining chances and update the UI.
                remainingChances--;
                tomatoEngine.setRemainingChances(remainingChances);
                app.gamePanel.setChances();
            }

            // Navigate to the game panel.
            app.getPanel(AppStrings.GAME_PANEL);

            // Starts the timer.
            app.gamePanel.startTimer(System.currentTimeMillis());

        } else if (e.getSource().equals(btnNextLevel)) {
            // Handles the next level button action.
            audioManager.playButtonSoundEffects();

            // Reset the remanining chances for the next level.
            tomatoEngine.setRemainingChances(3);

            // Update bonus information based on player progression.
            bonusRewarder.playerProgressedToLevel(tomatoEngine.getLevel());

            // Set the bonuses.
            app.gamePanel.setBonuses(
                    bonusRewarder.getExtraChance(),
                    bonusRewarder.getGameSkips(),
                    bonusRewarder.getAnswerHints(),
                    bonusRewarder.getExtraTime());

            // Update the UI and start next game.
            app.gamePanel.setChances();
            app.gamePanel.setNextGame();
            app.gamePanel.resetComponents();
            app.gamePanel.startTimer(System.currentTimeMillis());

            // Navigate to the game panel.
            app.getPanel(AppStrings.GAME_PANEL);
        }
    }
}
