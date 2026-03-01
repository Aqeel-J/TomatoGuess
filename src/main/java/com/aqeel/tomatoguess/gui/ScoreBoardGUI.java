package com.aqeel.tomatoguess.gui;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.components.ButtonUI;
import com.aqeel.tomatoguess.database.ScoreboardHandler;
import com.aqeel.tomatoguess.game.AudioManager;
import com.aqeel.tomatoguess.game.TomatoEngine;
import com.aqeel.tomatoguess.resources.AppColors;
import com.aqeel.tomatoguess.resources.AppDimen;
import com.aqeel.tomatoguess.resources.AppIcons;
import com.aqeel.tomatoguess.resources.AppStrings;
import com.aqeel.tomatoguess.utilities.TimeConvertor;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

/**
 * The ScoreboardGUI class represents the GUI for displaying game scores.
 * It includes tabs for global, local, and personal scores.
 * The scores are retrieved from the ScoreboardHandler and displayed in tables.
 *
 * @author Aqeel Jabir.
 */
public class ScoreBoardGUI extends JPanel implements ActionListener {

    // Update interval for refreshing scores
    private static final int UPDATE_INTERVAL = 1000;

    App app;
    TomatoEngine tomatoEngine;
    AudioManager audioManager;

    // UI Components.
    ButtonUI btnBack;
    JTabbedPane tabbedPane;
    Timer updateTimer;
    JPanel personalScoresPanel, globalScoresPanel, localScoresPanel;
    JPanel personalBodyPanel, globalBodyPanel, localBodyPanel;

    /**
     * Constructor for ScoreboardGUI class.
     *
     * @param app Reference to the main application.
     */
    public ScoreBoardGUI(App app) {
        this.app = app;
        this.tomatoEngine = app.getEngine();
        this.audioManager = app.getAudioManager();

        setLayout(new BorderLayout());
        setOpaque(false);

        updateTimer = new Timer(UPDATE_INTERVAL, this);
        updateTimer.start();

        // Label to display "Scoreboard" title.
        JLabel title = new JLabel("Scoreboard", SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        title.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 24));
        title.setForeground(AppColors.NEUTRAL_COLOUR);
        add(title, BorderLayout.NORTH);

        // Create a tabbed pane for global, local and personal scores.
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(0, 15, 0, 15));
        tabbedPane.setBackground(AppColors.TRANSPARENT_COLOUR);

        globalScoresPanel = new JPanel(new BorderLayout());
        localScoresPanel = new JPanel(new BorderLayout());
        personalScoresPanel = new JPanel(new BorderLayout());

        tabbedPane.addTab("<html><nobr>&nbsp;&nbsp;&nbsp;Global</nobr></html>", AppIcons.globeIcon, globalScoresPanel);
        tabbedPane.addTab("<html><nobr>&nbsp;&nbsp;&nbsp;Local</nobr></html>", AppIcons.flagIcon, localScoresPanel);
        tabbedPane.addTab("Personal", personalScoresPanel);
        add(tabbedPane, BorderLayout.CENTER);

        // Update and display scores.
        updateScores();

        JPanel hsBtnPanel = new JPanel(new FlowLayout());
        hsBtnPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        hsBtnPanel.setOpaque(false);
        add(hsBtnPanel, BorderLayout.SOUTH);

        btnBack = new ButtonUI("Back", AppColors.SECONDARY_COLOUR, AppDimen.BUTTON_SIZE);
        btnBack.addActionListener(this);
        hsBtnPanel.add(btnBack);
    }

    /**
     * Creates and returns a panel representing the title/header row of the
     * scoreboard table.
     *
     * @param nameTitle Whether to include the player name in the title.
     * @param columns   The number of columns in the grid layout.
     * @return A JPanel representing the title/header row of the scoreboard table.
     */
    public JPanel tableTitleComponents(boolean nameTitle, int columns) {
        // Create a panel with a grid layout and specified columns
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, columns));
        panel.setBorder(new EmptyBorder(5, 0, 5, 10));
        panel.setBackground(AppColors.SECONDARY_COLOUR);

        // Position Label
        JLabel rankLabel = new JLabel("Rank", SwingConstants.CENTER);
        rankLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        rankLabel.setForeground(AppColors.NEUTRAL_COLOUR);
        panel.add(rankLabel);

        // Player Name Label (if nameTitle is true)
        if (nameTitle) {
            JLabel nameLabel = new JLabel("Name", SwingConstants.CENTER);
            nameLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
            nameLabel.setForeground(AppColors.NEUTRAL_COLOUR);
            panel.add(nameLabel);
        }

        // Level Label
        JLabel levelLabel = new JLabel("Level", SwingConstants.CENTER);
        levelLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        levelLabel.setForeground(AppColors.NEUTRAL_COLOUR);
        panel.add(levelLabel);

        // High Score Label
        JLabel scoreLabel = new JLabel("Score", SwingConstants.CENTER);
        scoreLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        scoreLabel.setForeground(AppColors.NEUTRAL_COLOUR);
        panel.add(scoreLabel);

        // Star Count Display
        JLabel starsLabel = new JLabel(AppIcons.filledStarsIcon, SwingConstants.CENTER);
        starsLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        starsLabel.setForeground(AppColors.NEUTRAL_COLOUR);
        panel.add(starsLabel);

        // Timelapse Display
        JLabel timeLabel = new JLabel("Timelapse", SwingConstants.CENTER);
        timeLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        timeLabel.setForeground(AppColors.NEUTRAL_COLOUR);
        panel.add(timeLabel);

        return panel;
    }

    /**
     * Creates and returns a panel representing a single score entry in the
     * scoreboard.
     *
     * @param columns       The number of columns in the grid layout.
     * @param position      The position of the player in the scoreboard.
     * @param playerEmail   The email of the player.
     * @param playerName    The name of the player.
     * @param level         The level achieved by the player.
     * @param score         The score achieved by the player.
     * @param stars         The total stars collected by the player.
     * @param totalPlayTime The total playtime of the player.
     * @return A JPanel representing a single score entry.
     */
    public JPanel singleScoreComponents(
            int columns,
            int position,
            String playerEmail,
            String playerName,
            String level,
            String score,
            String stars,
            String totalPlayTime) {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, columns));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setOpaque(false);

        // Highlight the row if it corresponds to the current player's email
        if (playerEmail.equals(tomatoEngine.getPlayerEmail())) {
            panel.setOpaque(true);
            panel.setBackground(AppColors.LIGHT_ACCENT_COLOUR);
            panel.setBorder(new LineBorder(AppColors.ACCENT_COLOUR, 1));
        }

        // Position Label
        JLabel positionLabel = new JLabel(String.valueOf(position), SwingConstants.CENTER);
        if (position == 1) {
            positionLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 13));
        } else {
            positionLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        }
        panel.add(positionLabel);

        // Player Name Label
        if (playerName != null) {
            JLabel playerNameLabel = new JLabel(playerName, SwingConstants.LEFT);
            if (position == 1) {
                playerNameLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 13));
            } else {
                playerNameLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
            }
            panel.add(playerNameLabel);
        }

        // Level Label
        JLabel levelLabel = new JLabel(level, SwingConstants.CENTER);
        if (position == 1) {
            levelLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 13));
        } else {
            levelLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        }
        panel.add(levelLabel);

        // High Score Label
        JLabel highScorLabel = new JLabel(score, SwingConstants.CENTER);
        if (position == 1) {
            highScorLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 13));
        } else {
            highScorLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        }
        panel.add(highScorLabel);

        // Star Count Display
        JLabel totalStarsLabel = new JLabel(stars, SwingConstants.CENTER);
        if (position == 1) {
            totalStarsLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 13));
        } else {
            totalStarsLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        }
        panel.add(totalStarsLabel);

        // Display Total Play Time
        JLabel totalPlayTimeLabel = new JLabel(totalPlayTime, SwingConstants.CENTER);
        if (position == 1) {
            totalPlayTimeLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.BOLD, 13));
        } else {
            totalPlayTimeLabel.setFont(new Font(AppStrings.APP_FONT_FAMILY, Font.PLAIN, 12));
        }
        panel.add(totalPlayTimeLabel);

        return panel;
    }

    /**
     * Displays and updates the global scores in the "Global" tab of the scoreboard.
     * Retrieves global scores from the database and populates the display.
     */
    public void displayGlobalScores() {
        // Clear existing components in the globalScoresPanel
        globalScoresPanel.removeAll();
        globalScoresPanel.setOpaque(false);

        // Add the title components to the "Global" tab
        globalScoresPanel.add(tableTitleComponents(true, 6), BorderLayout.NORTH);

        // Create a panel to hold the body of the "Global" tab
        globalBodyPanel = new JPanel();
        globalBodyPanel.setLayout(new BoxLayout(globalBodyPanel, BoxLayout.Y_AXIS));
        globalBodyPanel.setOpaque(false);

        // Create a panel to hold the body of the "Global" tab
        java.util.List<Document> resultList = ScoreboardHandler.getGlobalScores();

        // Create a panel to hold the body of the "Global" tab
        assert resultList != null;
        resultList.sort(new Comparator<Document>() {
            @Override
            public int compare(Document doc1, Document doc2) {
                int scoreComparison = Integer.compare(doc2.getInteger("score"), doc1.getInteger("score"));
                if (scoreComparison != 0) {
                    return scoreComparison;
                }

                int totalStarsComparison = Integer.compare(doc2.getInteger("totalStars"),
                        doc1.getInteger("totalStars"));
                if (totalStarsComparison != 0) {
                    return totalStarsComparison;
                }

                return Long.compare(doc1.getLong("totalPlayTime"), doc2.getLong("totalPlayTime"));
            }
        });

        // Initialize the position counter
        int position = 1;

        // Iterate over the sorted global scores and add them to the body panel
        for (Document document : resultList) {

            // Extract relevant data from the document
            String playerEmail = document.getString("email");
            String playerName = document.getString("playerName");
            String level = document.getInteger("level").toString();
            String maxScore = document.getInteger("score").toString();
            String maxStars = document.getInteger("totalStars").toString();
            String totalTime = TimeConvertor.getTime(document.getLong("totalPlayTime"));

            // Add a panel with the global score components to the body panel
            globalBodyPanel.add(
                    singleScoreComponents(6, position, playerEmail, playerName, level, maxScore, maxStars, totalTime));

            // Increment the position counter
            position++;
        }

        // Create a scroll pane for the global scores body panel
        JScrollPane hsScrollPane = new JScrollPane(globalBodyPanel);
        hsScrollPane.getViewport().setOpaque(false);
        hsScrollPane.setOpaque(false);
        hsScrollPane.setViewportBorder(null);
        hsScrollPane.setBorder(null);
        globalScoresPanel.add(hsScrollPane, BorderLayout.CENTER);

        // Revalidate and repaint the "Global" tab to reflect the changes
        globalScoresPanel.revalidate();
        globalScoresPanel.repaint();
    }

    /**
     * Displays and updates the local scores in the "Local" tab of the scoreboard.
     *
     * Retrieves local scores from the database based on the current country and
     * populates the display.
     */
    public void displayLocalScores() {
        // Clear existing components in the localScoresPanel
        localScoresPanel.removeAll();
        localScoresPanel.setOpaque(false);

        // Add the title components to the "Local" tab
        localScoresPanel.add(tableTitleComponents(true, 6), BorderLayout.NORTH);

        localBodyPanel = new JPanel();
        localBodyPanel.setLayout(new BoxLayout(localBodyPanel, BoxLayout.Y_AXIS));
        localBodyPanel.setOpaque(false);

        // Retrieve local scores from the database based on the current country
        java.util.List<Document> resultList = ScoreboardHandler.getLocalScores(tomatoEngine.getCountry());

        // Sort the local scores based on score, total stars, and total playtime
        assert resultList != null;
        resultList.sort((doc1, doc2) -> {
            int scoreComparison = Integer.compare(doc2.getInteger("score"), doc1.getInteger("score"));
            if (scoreComparison != 0) {
                return scoreComparison;
            }

            int totalStarsComparison = Integer.compare(doc2.getInteger("totalStars"),
                    doc1.getInteger("totalStars"));
            if (totalStarsComparison != 0) {
                return totalStarsComparison;
            }

            return Long.compare(doc1.getLong("totalPlayTime"), doc2.getLong("totalPlayTime"));
        });

        // Initialize the position counter
        int position = 1;

        // Iterate over the sorted local scores and add them to the body panel
        for (Document document : resultList) {

            // Extract relevant data from the document
            String playerEmail = document.getString("email");
            String playerName = document.getString("playerName");
            String level = document.getInteger("level").toString();
            String maxScore = document.getInteger("score").toString();
            String maxStars = document.getInteger("totalStars").toString();
            String totalTime = TimeConvertor.getTime(document.getLong("totalPlayTime"));

            // Add a panel with the local score components to the body panel
            localBodyPanel.add(
                    singleScoreComponents(6, position, playerEmail, playerName, level, maxScore, maxStars, totalTime));

            // Increment the position counter
            position++;
        }

        // Create a scroll pane for the local scores body panel
        JScrollPane hsScrollPane = new JScrollPane(localBodyPanel);
        hsScrollPane.getViewport().setOpaque(false);
        hsScrollPane.setOpaque(false);
        hsScrollPane.setViewportBorder(null);
        hsScrollPane.setBorder(null);
        localScoresPanel.add(hsScrollPane, BorderLayout.CENTER);

        // Revalidate and repaint the "Local" tab to reflect the changes
        localScoresPanel.revalidate();
        localScoresPanel.repaint();
    }

    /**
     * Displays and updates the personal scores in the "Personal" tab of the
     * scoreboard.
     *
     * Retrieves personal scores from the database and populates the display.
     */
    public void displayPersonalScores() {
        // Add the title components to the "Personal" tab
        personalScoresPanel.removeAll();
        personalScoresPanel.setOpaque(false);

        // Add the title components to the "Personal" tab
        personalScoresPanel.add(tableTitleComponents(false, 5), BorderLayout.NORTH);

        // Create a panel to hold the body of the "Personal" tab
        personalBodyPanel = new JPanel();
        personalBodyPanel.setLayout(new BoxLayout(personalBodyPanel, BoxLayout.Y_AXIS));
        personalBodyPanel.setOpaque(false);

        // Retrieve personal scores from the database using a cursor
        MongoCursor<Document> cursor = ScoreboardHandler.getPersonalScores(tomatoEngine.getPlayerEmail());

        // Initialize the position counter.
        int position = 1;

        // Iterate over the cursor results (personal scores)
        while (cursor.hasNext()) {
            Document document = cursor.next();

            // Extract relevant data from the document
            String level = document.getInteger("level").toString();
            String totalPlaytime = TimeConvertor.getTime(document.getLong("totalPlayTime"));
            int score = document.getInteger("score");
            int stars = document.getInteger("totalStars");

            // Add a panel with the individual score components to the body panel
            personalBodyPanel.add(singleScoreComponents(5, position, "", null, level,
                    String.valueOf(score), String.valueOf(stars), totalPlaytime));

            // Increment the position counter
            position++;
        }

        // Close the cursor to release resources
        cursor.close();

        // Create a scroll pane for the personal scores body panel
        JScrollPane hsScrollPane = new JScrollPane(personalBodyPanel);
        hsScrollPane.getViewport().setOpaque(false);
        hsScrollPane.setOpaque(false);
        hsScrollPane.setViewportBorder(null);
        hsScrollPane.setBorder(null);
        personalScoresPanel.add(hsScrollPane, BorderLayout.CENTER);

        // Revalidate and repaint the "Personal" tab to reflect the changes
        personalScoresPanel.revalidate();
        personalScoresPanel.repaint();
    }

    /**
     * Updates the displayed scores in the scoreboard.
     *
     * Invokes methods to refresh scores in the "Global," "Local," and "Personal"
     * tabs.
     */
    public void updateScores() {
        displayGlobalScores();
        displayLocalScores();
        displayPersonalScores();
    }

    /**
     * Sets the region information in the title of the "Local" tab.
     *
     * Retrieves the country/region information from the TomatoEngine and updates
     * the tab title accordingly.
     */
    public void setRegion() {
        String country = tomatoEngine.getCountry();
        tabbedPane.setTitleAt(1, "<html><nobr>&nbsp;&nbsp;&nbsp;Local : " + country + "</nobr></html>");
    }

    /**
     * Action performed method to handle button clicks and timer events.
     *
     * @param e ActionEvent representing the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(updateTimer)) {
            updateScores();
        } else if (e.getSource().equals(btnBack)) {
            audioManager.playButtonSoundEffects();
            app.getPanel(AppStrings.MAIN_MENU_PANEL);
        }
    }
}
