package com.aqeel.tomatoguess.game;

/**
 * Represents a player in the TomatoGuess Application.
 * Implements the PlayerProvider interface for accessing and updating player
 * information.
 *
 * @author Aqeel Jabir
 */
public class Player implements IPlayer {

    // Name of the player
    private String playerName;

    // Email address of the player.
    private String playerEmail;

    // Country of the player.
    private String country;

    /**
     * Gets the name of the player.
     *
     * @return name of the player.
     */
    @Override
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the name of the player.
     *
     * @param playerName New name of the player to be set.
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Updates the name of the player.
     *
     * @param newPlayerName New name of the player to be updated.
     */
    @Override
    public void updatePlayerName(String newPlayerName) {
        this.playerName = newPlayerName;
    }

    /**
     * Gets the email address of the player.
     *
     * @return the email of the player.
     */
    @Override
    public String getPlayerEmail() {
        return playerEmail;
    }

    /**
     * Sets the email address of the player
     *
     * @param playerEmail New email of the player to be set.
     */
    @Override
    public void setPlayerEmail(String playerEmail) {
        this.playerEmail = playerEmail;
    }

    /**
     * Updates the email address of the player.
     *
     * @param newPlayerEmail New email of the player to be updated.
     */
    @Override
    public void updatePlayerEmail(String newPlayerEmail) {
        this.playerEmail = newPlayerEmail;
    }

    /**
     * Gets the country associated with the player.
     *
     * @return the country associated with the player.
     */
    @Override
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country associated with the player.
     *
     * @param country associated with the player.
     */
    @Override
    public void setCountry(String country) {
        this.country = country;
    }
}
