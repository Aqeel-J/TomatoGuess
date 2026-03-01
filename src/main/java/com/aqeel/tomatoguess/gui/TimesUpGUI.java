package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.game.AudioManager;
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
 * The TimesUpGUI class represents the GUI displayed when the game time is up.
 *
 * It provides information about the player's score, level, and stars earned.
 *
 * The class extends JPanel and implements ActionListener to handle button
 * interactions.
 *
 * @author Aqeel Jabir
 */
public class TimesUpGUI extends JPanel implements ActionListener {

    App app;
    TomatoEngine tomatoEngine;
    AudioManager audioManager;

    // UI Components.
    ButtonUI btnTryAgain, btnClose;
    JLabel scoreArea, levelArea, starsArea;

    /**
     * Constructs a new instance of TimesUpGUI class.
     *
     * @param app The main application instance.
     */
    public TimesUpGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.audioManager = app.getAudioManager();
        initComponents();
    }

    /**
     * Initializes and configures the GUI components for the "Time's Up" panel.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 180));

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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        btnClose = new ButtonUI(AppIcons.closeIcon);
        btnClose.setBorder(new EmptyBorder(15, 0, 0, 15));
        btnClose.addActionListener(this);
        headerPanel.add(btnClose, BorderLayout.EAST);

        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setOpaque(false);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(0, 0, 0, 0);
        gbcMain.gridwidth = GridBagConstraints.REMAINDER;
        gbcMain.fill = GridBagConstraints.VERTICAL;

        ImageScaler imageScaler = new ImageScaler();
        imageScaler.setSize(100, 100);
        ImageIcon timesUpIcon = imageScaler.getScaledImage(AppImages.timesUpImg);

        JLabel timesUpIconLabel = new JLabel(timesUpIcon);
        bodyPanel.add(timesUpIconLabel, gbcMain);

        gbcMain.insets = new Insets(0, 0, 20, 0);

        JLabel title = new JLabel("Time's Up!");
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 34));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        bodyPanel.add(title, gbcMain);

        gbcMain.insets = new Insets(0, 0, 0, 0);

        JLabel urScore = new JLabel("Your Score");
        urScore.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        urScore.setForeground(AppColors.SECONDARY_COLOUR);
        bodyPanel.add(urScore, gbcMain);

        gbcMain.insets = new Insets(0, 0, 20, 0);

        scoreArea = new JLabel("0", SwingConstants.CENTER);
        scoreArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 30));
        scoreArea.setForeground(AppColors.NEUTRAL_COLOUR);
        bodyPanel.add(scoreArea, gbcMain);

        JPanel gameDetail = new JPanel(new GridBagLayout());
        gameDetail.setBorder(new EmptyBorder(0, 0, 0, 15));
        gameDetail.setOpaque(false);
        bodyPanel.add(gameDetail, gbcMain);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 0);
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel levelIconArea = new JLabel(AppIcons.filledBarIcon);
        gameDetail.add(levelIconArea, gbc);

        levelArea = new JLabel("0");
        levelArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 16));
        levelArea.setForeground(AppColors.NEUTRAL_COLOUR);
        gameDetail.add(levelArea, gbc);

        gbc.insets = new Insets(0, 60, 0, 0);

        JLabel starIconArea = new JLabel(AppIcons.filledStarsIcon);
        gameDetail.add(starIconArea, gbc);

        gbc.insets = new Insets(0, 15, 0, 0);

        starsArea = new JLabel("0");
        starsArea.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 16));
        starsArea.setForeground(AppColors.NEUTRAL_COLOUR);
        gameDetail.add(starsArea, gbc);

        gbcMain.insets = new Insets(0, 0, 0, 0);

        btnTryAgain = new ButtonUI("Try Again", AppColors.ACCENT_COLOUR, AppDimen.BUTTON_SIZE);
        btnTryAgain.addActionListener(this);
        bodyPanel.add(btnTryAgain, gbcMain);
    }

    /**
     * Sets the results (level, score, and stars) to be displayed on the panel.
     *
     * @param level The player's level.
     * @param score The player's score.
     * @param stars The number of stars earned by the player.
     */
    public void setResults(int level, int score, int stars) {
        levelArea.setText(String.valueOf(level));
        scoreArea.setText(String.valueOf(score));
        starsArea.setText(String.valueOf(stars));
    }

    /**
     * Handles action events triggered by buttons in the "Time's Up" panel.
     *
     * @param e The ActionEvent representing the button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnClose)) {

            audioManager.playButtonSoundEffects();

            // Set the size, location, and shape of the application window
            app.setSize(500, 500);
            app.setLocationRelativeTo(null);
            app.setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

            // Retrieve game results
            int level = tomatoEngine.getLevel() - 1;
            int score = tomatoEngine.getScore();
            int stars = tomatoEngine.getTotalChances();

            // Set game results in the game-over panel and navigate to it
            app.gameOverPanel.setGameResult(level, score, stars);
            app.getPanel(AppStrings.GAME_OVER_PANEL);

        } else if (e.getSource().equals(btnTryAgain)) {
            audioManager.playButtonSoundEffects();

            // Handle game retry
            int remainingChances = tomatoEngine.getRemainingChances();
            if (remainingChances > 0) {
                // Decrement remaining chances and update the game panel
                remainingChances--;
                tomatoEngine.setRemainingChances(remainingChances);
                app.gamePanel.setChances();
            }

            // Reset the application window size, shape, and navigate to the game panel
            app.setExtendedState(JFrame.MAXIMIZED_BOTH);
            app.setShape(null);
            app.getPanel(AppStrings.GAME_PANEL);
            app.gamePanel.startTimer(System.currentTimeMillis());
        }
    }
}
