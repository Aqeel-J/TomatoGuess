package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonBonusUI;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.components.RoundPanelUI;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.BonusRewarder;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppIcons;
import com.aqeel.tomatoguess.resources.AppImages;
import com.aqeel.tomatoguess.resources.AppStrings;
import com.aqeel.tomatoguess.utilities.ImageScaler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * GameGUI extends JPanel and provides the player interface for the main
 * gameplay screen.
 *
 * It includes the game display, player information, bonus buttons and timer.
 *
 * The class manages the game mechanics, handles button clicks and transitions
 * between different game states.
 *
 * The timer is used to track the remaining time for each level and bonuses
 * provide additional gameplay features.
 *
 * The class employs Swing components and uses a custom designed UI for a
 * visually appealing and intuitive experience.
 *
 * It integrates audio feedback for a more engaging gameplay experience.
 *
 * @author Aqeel Jabir
 *
 */
public class GameGUI extends JPanel implements ActionListener {

    // Class Variables.
    App app; // Reference to the main application.
    TomatoEngine tomatoEngine;
    BonusRewarder bonusRewarder;
    AudioManager audioManager;

    // Bonus-related constants.
    private static final int EXTRA_CHANCE_LEVEL = 10; // Level 10
    private static final int GAME_SKIPS_LEVEL = 20; // Level 20
    private static final int ANSWER_HINTS_LEVEL = 40; // Level 40
    private static final int EXTRA_TIME_LEVEL = 70; // Level 70

    // Game-related variables.
    private long time = 30 * 1000;
    private java.util.Timer timer;
    private long elapsedTime;
    private BufferedImage currentGame;
    private boolean isBlinking = false;

    // Allocate bonuses per button click
    private static final int ALLOCATED_EXTRA_CHANCES = 1;
    private static final long ALLOCATED_EXTRA_TIME = 10 * 1000;

    // UI Components
    ButtonUI btnExit;
    ButtonBonusUI btnChanceBonus, btnSkipBonus, btnHintBonus, btnTimeBonus;
    JPanel heartsPanel, heartBonusPanel, questBtnPanel;
    JLabel levelArea, nameArea, scoreArea, timerArea, questArea;

    /**
     * Constrcutor for GameGUI class.
     *
     * @param app The main application instance.
     */
    public GameGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.bonusRewarder = app.getBonusRewarder();
        this.audioManager = app.getAudioManager();
        initComponents(); // Initialize UI components.
    }

    /**
     * Starts the timer for the game with the given start time.
     *
     * @param startTime of the timer.
     */
    public void startTimer(long startTime) {
        timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                elapsedTime = currentTime - startTime;

                if (elapsedTime <= time) {
                    // Calculate remaining time and update the timer UI.
                    long remainingTime = time - elapsedTime;
                    long seconds = (remainingTime / 1000) % 60;

                    SwingUtilities.invokeLater(() -> timerArea.setText(String.format("%02d", seconds)));

                } else {
                    // Game over conditions when time runs out.
                    int level = tomatoEngine.getLevel() - 1;
                    int score = tomatoEngine.getScore();
                    int stars = tomatoEngine.getTotalChances();
                    int remainingChances = tomatoEngine.getRemainingChances();

                    if (remainingChances > 0) {
                        // Handles times-up scenario.
                        app.timesUpPanel.setResults(level, score, stars);
                        app.getPanel(AppStrings.TIMES_UP_PANEL);
                    } else {
                        // Handles game oer scenario.
                        app.setSize(500, 500);
                        app.setLocationRelativeTo(null);
                        app.setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

                        app.gameOverPanel.setGameResult(level, score, stars);
                        app.getPanel(AppStrings.GAME_OVER_PANEL);
                    }
                    // Cancel the timer.
                    timer.cancel();
                }
            }
        }, 0, 1000); // Schedule the timer task to run every second.
    }

    /**
     * Sets the next game by updating the displayed game image.
     */
    public void setNextGame() {
        currentGame = tomatoEngine.nextGame();
        ImageIcon ii = new ImageIcon(currentGame);
        questArea.setIcon(ii);
    }

    /**
     * Sets the displayed heart icons based on the remaining chances.
     */
    public void setChances() {
        // Get the current remaining chances and calculate the lost chances.
        int remainingChances = tomatoEngine.getRemainingChances();
        int lostChances = 3 - remainingChances;

        // Remove all components from the hearts panel.
        heartsPanel.removeAll();

        // Display filled hearts for remaining chances.
        for (int i = 0; i < remainingChances; i++) {
            JLabel label = new JLabel(AppIcons.filledHeartIcon);
            heartsPanel.add(label);
        }

        // Display stroke hearts for lost chances.
        for (int i = 0; i < lostChances; i++) {
            JLabel label = new JLabel(AppIcons.strokeHeartIcon);
            heartsPanel.add(label);
        }

        // Repaint the panel to reflect the changes.
        heartsPanel.revalidate();
        heartsPanel.repaint();
    }

    /**
     * Resets the components to their initial state.
     */
    public void resetComponents() {
        time = 30 * 1000;
        nameArea.setText(tomatoEngine.getPlayerName());
        levelArea.setText(String.format("%02d", tomatoEngine.getLevel()));
        scoreArea.setText(String.valueOf(tomatoEngine.getScore()));
    }

    /**
     * Sets the bonuses for each type based on the provided values.
     *
     * @param extraChance Number of extra chances.
     * @param gameSkips   Number of game skips.
     * @param answerHints Number of answer hints.
     * @param extraTime   Number of extra time.
     */
    public void setBonuses(int extraChance, int gameSkips, int answerHints, int extraTime) {
        btnChanceBonus.setBonusCount(extraChance);
        btnSkipBonus.setBonusCount(gameSkips);
        btnHintBonus.setBonusCount(answerHints);
        btnTimeBonus.setBonusCount(extraTime);
    }

    /**
     * Initializes and sets up the UI components for the game.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        headerPanel.setOpaque(false);
        add(headerPanel, BorderLayout.NORTH);

        // Left Header Panel
        JPanel L_HeaderPanel = new JPanel();
        L_HeaderPanel.setOpaque(false);
        headerPanel.add(L_HeaderPanel, BorderLayout.WEST);

        // Level Panel
        JPanel levelPanel = new JPanel(new GridBagLayout());
        levelPanel.setOpaque(false);
        L_HeaderPanel.add(levelPanel);

        GridBagConstraints gbcLHeader = new GridBagConstraints();
        gbcLHeader.insets = new Insets(0, 20, 0, 20);
        gbcLHeader.gridwidth = GridBagConstraints.REMAINDER;
        gbcLHeader.fill = GridBagConstraints.HORIZONTAL;

        // Label indicating the current level.
        JLabel levelLabel = new JLabel("Level", SwingConstants.CENTER);
        levelLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        levelLabel.setForeground(AppColors.SECONDARY_COLOUR);
        levelPanel.add(levelLabel, gbcLHeader);

        // Label displaying the current level.
        levelArea = new JLabel("", SwingConstants.CENTER);
        // levelArea.setPreferredSize(new Dimension(80, 80));
        levelArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 28));
        levelArea.setForeground(AppColors.NEUTRAL_COLOUR);
        levelPanel.add(levelArea, gbcLHeader);

        // Panel for displaying name and score label.
        JPanel NS_Panel = new JPanel();
        NS_Panel.setLayout(new BoxLayout(NS_Panel, BoxLayout.Y_AXIS));
        NS_Panel.setOpaque(false);
        L_HeaderPanel.add(NS_Panel);

        // Label displaying player's name.
        nameArea = new JLabel("");
        nameArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 24));
        nameArea.setForeground(AppColors.NEUTRAL_COLOUR);
        NS_Panel.add(nameArea);

        // Label displaying current game score.
        scoreArea = new JLabel("");
        scoreArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 16));
        NS_Panel.add(scoreArea);

        // Center Header Panel
        JPanel C_HeaderPanel = new JPanel(new GridBagLayout());
        C_HeaderPanel.setOpaque(false);
        headerPanel.add(C_HeaderPanel, BorderLayout.CENTER);

        // Scales image to specified size.
        ImageScaler imageScaler = new ImageScaler();
        imageScaler.setSize(80, 60);
        ImageIcon appLogo = imageScaler.getScaledImage(AppImages.appLogo);

        // Game icon display using a label.
        JLabel gameIconDisplay = new JLabel(appLogo, SwingConstants.CENTER);
        C_HeaderPanel.add(gameIconDisplay);

        // Label to display app name.
        JLabel appName = new JLabel(AppStrings.APP_NAME);
        appName.setBorder(new EmptyBorder(0, 15, 0, 0));
        appName.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 24));
        appName.setForeground(AppColors.NEUTRAL_COLOUR);
        C_HeaderPanel.add(appName);

        // Right Header Panel
        JPanel R_HeaderPanel = new JPanel(new GridBagLayout());
        R_HeaderPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
        R_HeaderPanel.setOpaque(false);
        headerPanel.add(R_HeaderPanel, BorderLayout.EAST);

        // Hearts Panel
        heartsPanel = new JPanel();
        heartsPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
        heartsPanel.setOpaque(false);
        R_HeaderPanel.add(heartsPanel);

        // Round Panel to hold time label
        RoundPanelUI roundPanel = new RoundPanelUI(new Dimension(60, 60));
        roundPanel.setArc(60, 60);
        roundPanel.setBackground(AppColors.ACCENT_COLOUR);
        R_HeaderPanel.add(roundPanel);

        // Label to display timer.
        timerArea = new JLabel("00", SwingConstants.CENTER);
        timerArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 28));
        timerArea.setForeground(AppColors.NEUTRAL_COLOUR);
        roundPanel.add(timerArea);

        // Body Panel.
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setOpaque(false);
        add(bodyPanel, BorderLayout.CENTER);

        // Load the next game from the TomatoEngine and display it in the GUI.
        currentGame = tomatoEngine.nextGame();
        ImageIcon ii = new ImageIcon(currentGame);
        questArea = new JLabel(ii);
        questArea.setSize(330, 800);
        bodyPanel.add(questArea, BorderLayout.CENTER);

        // Right Body Panel.
        JPanel bodyRightPanel = new JPanel(new GridBagLayout());
        bodyRightPanel.setOpaque(false);
        bodyPanel.add(bodyRightPanel, BorderLayout.EAST);

        GridBagConstraints gbcRPanel = new GridBagConstraints();
        gbcRPanel.insets = new Insets(5, 30, 5, 30);
        gbcRPanel.gridwidth = GridBagConstraints.REMAINDER;
        gbcRPanel.fill = GridBagConstraints.HORIZONTAL;

        // Button bonus UI components.
        btnChanceBonus = new ButtonBonusUI(AppImages.heartPlusImg);
        btnChanceBonus.setToolTipText("Extra Life");
        btnChanceBonus.addActionListener(this);
        bodyRightPanel.add(btnChanceBonus, gbcRPanel);

        btnHintBonus = new ButtonBonusUI(AppImages.hintImg);
        btnHintBonus.setToolTipText("Hints");
        btnHintBonus.addActionListener(this);
        bodyRightPanel.add(btnHintBonus, gbcRPanel);

        btnSkipBonus = new ButtonBonusUI(AppImages.skipImg);
        btnSkipBonus.setToolTipText("Skips");
        btnSkipBonus.addActionListener(this);
        bodyRightPanel.add(btnSkipBonus, gbcRPanel);

        btnTimeBonus = new ButtonBonusUI(AppImages.extraTimerImg);
        btnTimeBonus.setToolTipText("Extra Time");
        btnTimeBonus.addActionListener(this);
        bodyRightPanel.add(btnTimeBonus, gbcRPanel);

        // Left Body Panel.
        JPanel bodyLeftPanel = new JPanel();
        bodyLeftPanel.setPreferredSize(new Dimension(120, 60));
        bodyLeftPanel.setOpaque(false);
        add(bodyLeftPanel, BorderLayout.WEST);

        // Footer Panel.
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        footerPanel.setOpaque(false);
        add(footerPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.VERTICAL;

        // Quest Button Panel
        questBtnPanel = new JPanel(new GridBagLayout());
        questBtnPanel.setBorder(new EmptyBorder(0, 150, 0, 0));
        questBtnPanel.setOpaque(false);
        footerPanel.add(questBtnPanel, BorderLayout.CENTER);

        // Add 10 quest buttons to the panel.
        for (int i = 0; i < 10; i++) {
            ButtonUI btnQuest = new ButtonUI(String.valueOf(i), AppColors.ACCENT_COLOUR, new Dimension(60, 30));
            btnQuest.addActionListener(this);
            questBtnPanel.add(btnQuest, gbc);
        }

        gbc.insets = new Insets(0, 80, 0, 25);

        // Right Footer Panel
        JPanel rightFooterPanel = new JPanel(new GridBagLayout());
        rightFooterPanel.setOpaque(false);
        footerPanel.add(rightFooterPanel, BorderLayout.EAST);

        // Button component to exit game.
        btnExit = new ButtonUI(AppIcons.exitIcon, AppColors.SECONDARY_COLOUR, new Dimension(40, 40));
        btnExit.addActionListener(this);
        rightFooterPanel.add(btnExit, gbc);
    }

    /**
     * Handles action events triggered by the components.
     * Performs actions, such as clicking on the answer, activating game bonuses and
     * exit the game.
     *
     * @param e The ActionEvent generated by the player's interaction with the
     *          components.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String message;
        if (e.getSource().equals(btnExit)) {
            // Handles the exit button action.
            audioManager.playButtonSoundEffects();

            // Show confirmation dialog before exiting.
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Warning",
                    JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                timer.cancel();

                // Get game information for the game over panel.
                int level = tomatoEngine.getLevel();
                int score = tomatoEngine.getScore();
                int stars = tomatoEngine.getTotalChances();
                app.gameOverPanel.setGameResult(level - 1, score, stars);

                // Set the game over panel and adjust the application shape.
                app.setSize(500, 500);
                app.setLocationRelativeTo(null);
                app.setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                app.getPanel(AppStrings.GAME_OVER_PANEL);
            }
        } else if (e.getSource() instanceof ButtonBonusUI bonusClicked) {
            // Handles bonus buttons actions.
            int currentLevel = tomatoEngine.getLevel();

            // Check the type of the bonus clicked.
            if (bonusClicked.getToolTipText().equals("Extra Life")) {
                // Handle Extra Life bonus.
                if (currentLevel >= EXTRA_CHANCE_LEVEL) {
                    System.out.println("Extra Life");

                    // Use extra life if available and update UI.
                    if (bonusRewarder.getExtraChance() > 0 && tomatoEngine.getRemainingChances() <= 3) {
                        bonusRewarder.useExtraChance();
                        tomatoEngine.updateRemainingChances(ALLOCATED_EXTRA_CHANCES);
                        btnChanceBonus.setBonusCount(bonusRewarder.getExtraChance());
                        setChances();
                    }
                }

            } else if (bonusClicked.getToolTipText().equals("Hints")) {
                // Handle Hints bonus.
                if (currentLevel >= ANSWER_HINTS_LEVEL) {
                    System.out.println("Hints");

                    // Use hints if available and trigger blinking effect.
                    Component button = questBtnPanel.getComponent(tomatoEngine.getSolution());
                    if (bonusRewarder.getAnswerHints() > 0) {
                        bonusRewarder.useAnswerHints();

                        if (!isBlinking) {
                            // Blink effect for the correct answer button.
                            java.util.Timer blinkTimer = new Timer();
                            isBlinking = true;
                            long startTime = System.currentTimeMillis();
                            blinkTimer.schedule(new TimerTask() {
                                private boolean originalColor = true;

                                @Override
                                public void run() {
                                    SwingUtilities.invokeLater(() -> {
                                        if (originalColor) {
                                            button.setBackground(Color.RED);
                                        } else {
                                            button.setBackground(AppColors.ACCENT_COLOUR);
                                        }
                                        originalColor = !originalColor;

                                        long currentTime = System.currentTimeMillis();
                                        if (currentTime - startTime >= 3000) {
                                            button.setBackground(AppColors.ACCENT_COLOUR);

                                            isBlinking = false;
                                            blinkTimer.cancel();
                                        }
                                    });
                                }
                            }, 0, 250);
                        }
                    }

                    btnHintBonus.setBonusCount(bonusRewarder.getAnswerHints());
                }

            } else if (bonusClicked.getToolTipText().equals("Skips")) {
                // Handle Skips bonus.
                if (currentLevel >= GAME_SKIPS_LEVEL) {
                    System.out.println("Skips");

                    // Use skips if available and update UI.
                    if (bonusRewarder.getGameSkips() > 0) {
                        bonusRewarder.useGameSkips();
                        btnSkipBonus.setBonusCount(bonusRewarder.getGameSkips());
                        setNextGame();
                    }
                }

            } else if (bonusClicked.getToolTipText().equals("Extra Time")) {
                // Handle Extra Time bonus.
                if (currentLevel >= EXTRA_TIME_LEVEL) {
                    System.out.println("Extra Time");

                    // Use extra time if available and update UI.
                    if (bonusRewarder.getExtraTime() > 0) {
                        bonusRewarder.useExtraTime();
                        time += ALLOCATED_EXTRA_TIME;
                        btnTimeBonus.setBonusCount(bonusRewarder.getExtraTime());
                    }
                }
            }

        } else {
            // Handle button click for game level answers.
            audioManager.playButtonSoundEffects();

            // Check if the selected solution is correct.
            int solution = Integer.parseInt(e.getActionCommand());
            boolean correct = tomatoEngine.checkSolution(solution);

            // Retrieve game-related information from the engine.
            int score = tomatoEngine.getScore();
            int level = tomatoEngine.getLevel();
            int stars = tomatoEngine.getTotalChances();
            int remainingChances = tomatoEngine.getRemainingChances();

            // Set the level result panel based on correctness.
            app.levelResultPanel.setLevelResult(correct, remainingChances, level - 1, score);
            timer.cancel();

            if (correct) {
                tomatoEngine.addTotalPlayTime(elapsedTime);

                // Update level and score UI components.
                levelArea.setText(String.format("%02d", level));
                scoreArea.setText(String.valueOf(score));

                // Display level result panel.
                app.getPanel(AppStrings.LEVEL_RESULT_PANEL);

                message = "Correct Answer!";

            } else {

                // Check if there are remaining chances for the player.
                if (remainingChances > 0) {
                    app.getPanel(AppStrings.LEVEL_RESULT_PANEL);
                } else {
                    // Set the game over panel if no chances left.
                    app.gameOverPanel.setGameResult(level - 1, score, stars);

                    // Adjust application size, location and shape.
                    app.setSize(500, 500);
                    app.setLocationRelativeTo(null);
                    app.setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

                    // Display level result panel.
                    app.getPanel(AppStrings.GAME_OVER_PANEL);
                }

                message = "Incorrect Answer!";
            }
            System.out.println(message);
        }
    }
}
