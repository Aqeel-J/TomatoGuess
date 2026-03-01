package com.aqeel.tomatoguess.game;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * The main game engine for managing player progress, game state and
 * interactions.
 * This class controls the game's logic, such as starting a new game, updating
 * player details, handling game rounds and checking solutions.
 *
 * <p>
 * The engine integrates with a {@link IPlayer} to retrieve and update
 * player information.
 * </p>
 *
 * <p>
 * The game progress through levels and the engine manages the player's score,
 * remaning chances, total chances played and total play time.
 * </p>
 *
 * <p>
 * It uses a {@link TomatoServer} to fetch random games and applies a visual
 * effect to the game images before presenting them to the player.
 * </p>
 *
 * @author Aqeel Jabir
 *
 */
public class TomatoEngine {

    private static TomatoEngine instance = null;

    /**
     * Gets the instance of TomatoEngine.
     * Uses a singleton pattern to ensure a single instane of the engine is created.
     *
     * @param player provider for managing player details.
     * @return instance of TomatoEngine.
     */
    public static TomatoEngine getInstance(IPlayer player) {
        if (instance == null) {
            instance = new TomatoEngine(player);
        }
        return instance;
    }

    private final TomatoServer gameServer = new TomatoServer();
    private Game current = null;

    private final IPlayer player;
    private int score;
    private int level;
    private int remainingChances;
    private int totalChances;
    private long totalPlayTime;

    /**
     * Constructs a TomatoEngine instance.
     *
     * @param player Player provider for maanging player details.
     */
    private TomatoEngine(IPlayer player) {
        this.player = player;
    }

    /**
     * Gets the name of the player.
     *
     * @return the name of the player.
     */
    public String getPlayerName() {
        return player.getPlayerName();
    }

    /**
     * Sets the name of the player.
     *
     * @param playerName to be set.
     */
    public void setPlayerName(String playerName) {
        this.player.setPlayerName(playerName);
    }

    /**
     * Updates the name of the player.
     *
     * @param newPlayerName to be updated.
     */
    public void updatePlayerName(String newPlayerName) {
        this.player.updatePlayerName(newPlayerName);
    }

    /**
     * Gets the email address of the player.
     *
     * @return the email address of the player.
     */
    public String getPlayerEmail() {
        return player.getPlayerEmail();
    }

    /**
     * Sets the email address of the player.
     *
     * @param playerEmail to be set.
     */
    public void setPlayerEmail(String playerEmail) {
        this.player.setPlayerEmail(playerEmail);
    }

    /**
     * Updates the email address of the player.
     *
     * @param newPlayerEmail to be updated.
     */
    public void updatePlayerEmail(String newPlayerEmail) {
        this.player.updatePlayerEmail(newPlayerEmail);
    }

    /**
     * Gets the country associated with the player.
     *
     * @return the country associated with the player.
     */
    public String getCountry() {
        return player.getCountry();
    }

    /**
     * Sets the country associated with the player.
     *
     * @param country associated with the player.
     */
    public void setCountry(String country) {
        this.player.setCountry(country);
    }

    /**
     * Starts a new game, resetting the player's score, level and chances.
     */
    public void startNewGame() {
        score = 0;
        level = 1;
        remainingChances = 3;
        totalChances = 0;
        totalPlayTime = 0;
    }

    /**
     * Sets the score of the game.
     *
     * @param score to be set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the current level of the game.
     *
     * @param level to be set.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Sets the remaining chances available for the player.
     *
     * @param remainingChances to be set.
     */
    public void setRemainingChances(int remainingChances) {
        this.remainingChances = remainingChances;
    }

    /**
     * Sets the total chances available for the player.
     *
     * @param totalChances to be set.
     */
    public void setTotalChances(int totalChances) {
        this.totalChances = totalChances;
    }

    /**
     * Adds te specified time in milliseconds to the total playtime.
     *
     * @param tMilliSeconds to be added to the total playtime.
     */
    public void addTotalPlayTime(long tMilliSeconds) {
        this.totalPlayTime += tMilliSeconds;
    }

    /**
     * Gets the remaning chances available for the player.
     *
     * @return the remaining chances.
     */
    public int getRemainingChances() {
        return remainingChances;
    }

    /**
     * Gets the total chances available for the player.
     *
     * @return the total chances.
     */
    public int getTotalChances() {
        return totalChances;
    }

    /**
     * Gets the total playtime in milliseconds.
     *
     * @return the total playtime in milliseconds.
     */
    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    /**
     * Updates the remaning chances by adding the specified extra chances.
     *
     * @param extraChance the number of chances to be added.
     */
    public void updateRemainingChances(int extraChance) {
        this.remainingChances += extraChance;
    }

    /**
     * Retrieves the next game from the game server.
     * Applies a visual effect to the game image by rounding its corners and returns
     * the modified image.
     *
     * @return modified image with rounded corners.
     */
    public BufferedImage nextGame() {

        // Fetch a random game from the server.
        current = gameServer.getRandomGame();

        // Get the dimensions of the current game image.
        int width = current.getImage().getWidth();
        int height = current.getImage().getHeight();

        // Creating a new BufferedImage to store the modified image.
        BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Creating a Graphics2D object to perform graphics operations on the image.
        Graphics2D g2 = roundedImage.createGraphics();

        // g2.setColor(new Color(0, 0, 0, 100));

        // Anti-aliasing for smoother graphics.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Creating a rounded rectangle shape to use as a clip for the image.
        RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, width, height, 15, 15);
        g2.setClip(roundRect);
        g2.drawImage(current.getImage(), 0, 0, null);
        g2.dispose();

        return roundedImage;
    }

    /**
     * Checks if the solution entered by the player is correct and updates the game
     * state accordingly.
     *
     * @param i soluition entered by the player.
     * @return true if the solution is correct else false.
     */
    public boolean checkSolution(int i) {
        if (i == current.getSolution()) {
            score += 5;
            level += 1;
            totalChances += remainingChances;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the solution of the game.
     *
     * @return the solution of the game.
     */
    public int getSolution() {
        return current.getSolution();
    }

    /**
     * Gets the current level of the game.
     *
     * @return the current level of the game.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the score of the game.
     *
     * @return the score of the game.
     */
    public int getScore() {
        return score;
    }
}
