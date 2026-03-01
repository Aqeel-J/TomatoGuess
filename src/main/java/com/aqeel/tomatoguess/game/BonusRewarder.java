package com.aqeel.tomatoguess.game;


import java.util.Random;

/**
 * Manages the rewarding and usage of bonus items for player progression in the
 * game.
 * This class provides methods to reward players with random bonuses at certain
 * levels, use bonus item during the game, and retrieve the current counts of
 * each bonus item. Bonus items include extra chances, game skips, answer
 * hints and extra time.
 *
 * <p>
 * Bonus items are rewarded to the player when reaching multiple of the
 * specified rewarding level.
 * </p>
 *
 * <p>
 * The class follows the Singleton design pattern to ensure a single instane is
 * used throughout the game.
 * </p>
 *
 * @author Aqeel Jabir
 */
public class BonusRewarder {

    private static BonusRewarder instance = null;

    /**
     * Gets the singleton instance of BonusRewarder.
     *
     * @return singleton instance of BonusRewarder.
     */
    public static BonusRewarder getInstance() {
        if (instance == null) {
            instance = new BonusRewarder();
        }
        return instance;
    }

    private static final int REWARDING_LEVEL = 5;

    private int extraChance = 0;
    private int gameSkips = 0;
    private int answerHints = 0;
    private int extraTime = 0;

    // Generate random number for selecting random bonuses.
    private Random random = new Random();

    /**
     * Checks if the player should be rewarded with bonus items based on the current
     * game level.
     *
     * @param currentLevel of the player in the game.
     */
    public void playerProgressedToLevel(int currentLevel) {
        if (shouldRewardBonus(currentLevel)) {
            rewardRandomBonus();
        }
    }

    // Private helper method to check if player should be rewarded based on current
    // level.
    private boolean shouldRewardBonus(int currentLevel) {
        return currentLevel % REWARDING_LEVEL == 0;
    }

    // Private helper method to reward a random bonus item.
    private void rewardRandomBonus() {
        // Generates random number between 0 and 4.
        int randomNumber = random.nextInt(4);

        // Switch statement to determine the type of bonus based on the random number.
        switch (randomNumber) {
            case 0:
                extraChance++;
                break;
            case 1:
                gameSkips++;
                break;
            case 2:
                answerHints++;
                break;
            case 3:
                extraTime++;
                break;
        }
    }

    /**
     * Uses an extra chance bonus item.
     */
    public void useExtraChance() {
        System.out.println("Extra Chance Used!");
        extraChance--;
    }

    /**
     * Uses a game skips bonus item.
     */
    public void useGameSkips() {
        System.out.println("Game Skip Used!");
        gameSkips--;
    }

    /**
     * Uses an answer hints bonus item.
     */
    public void useAnswerHints() {
        System.out.println("Answer Hints Used!");
        answerHints--;
    }

    /**
     * Uses an extra time bonus item.
     */
    public void useExtraTime() {
        System.out.println("Extra Time Used!");
        extraTime--;
    }

    /**
     * Gets the current count of extra chance bonus items.
     *
     * @return current count of extra chance bonus items.
     */
    public int getExtraChance() {
        return extraChance;
    }

    /**
     * Gets the current count of game skips bonus items.
     *
     * @return current count of game skips bonus items.
     */
    public int getGameSkips() {
        return gameSkips;
    }

    /**
     * Gets the current count of answer hints bonus items.
     *
     * @return current count of answer hints bonus items.
     */
    public int getAnswerHints() {
        return answerHints;
    }

    /**
     * Gets the current count of extra time bonus items.
     *
     * @return current count of extra time bonus items.
     */
    public int getExtraTime() {
        return extraTime;
    }

    /**
     * Sets the count of extra chance bonus items.
     *
     * @param extraChance The new count of extra chance bonus items.
     */
    public void setExtraChance(int extraChance) {
        this.extraChance = extraChance;
    }

    /**
     * Sets the count of game skips bonus items.
     *
     * @param gameSkips The new count of game skips bonus items.
     */
    public void setGameSkips(int gameSkips) {
        this.gameSkips = gameSkips;
    }

    /**
     * Sets the count of answer hints bonus items.
     *
     * @param answerHints The new count of answer hints bonus items.
     */
    public void setAnswerHints(int answerHints) {
        this.answerHints = answerHints;
    }

    /**
     * Sets the count of extra time bonus items.
     *
     * @param extraTime The new count of extra time bonus items.
     */
    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }
}
