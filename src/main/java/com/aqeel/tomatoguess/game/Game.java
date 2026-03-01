package com.aqeel.tomatoguess.game;

import java.awt.image.BufferedImage;

/**
 * Represents a game in the TomatoGuess application.
 * Each game is associated with an image and a solution.
 *
 * @author Aqeel Jabir
 */
public class Game {

    BufferedImage image = null;
    int solution = -1;

    /**
     * Constructs a Game with the specified image and solution.
     *
     * @param image    representing the game image.
     * @param solution associated with the game.
     */
    public Game(BufferedImage image, int solution) {
        super();
        this.image = image;
        this.solution = solution;
    }

    /**
     * Gets the BufferedImage representing the game image.
     *
     * @return the BufferedImage representing the game image.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Gets the solution associated with the game.
     *
     * @return the solution associated with the game.
     */
    public int getSolution() {
        return solution;
    }
}
