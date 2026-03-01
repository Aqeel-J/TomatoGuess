package com.aqeel.tomatoguess.game;

/**
 * An interface defining methods to access and update player information.
 * Classes implementing this interface should provide functionality for
 * retrieving and modifying player details such as name, email and country.
 *
 * @author Aqeel Jabir
 */
public interface IPlayer {

    /**
     * Gets the name of the player.
     *
     * @return the name of the player.
     */
    public String getPlayerName();

    /**
     * Sets the name of the player.
     *
     * @param playerName New name of the player to be set.
     */
    public void setPlayerName(String playerName);

    /**
     * Updates the name of the player.
     *
     * @param newPlayerName New name of the player to be updated.
     */
    public void updatePlayerName(String newPlayerName);

    /**
     * Gets the email address of the player.
     *
     * @return the emaill address of the player.
     */
    public String getPlayerEmail();

    /**
     * Sets the email address of the player.
     *
     * @param playerEmail New email address of the player to be set.
     */
    public void setPlayerEmail(String playerEmail);

    /**
     * Updates the email address of the player.
     *
     * @param newPlayerEmail New email address of the player to be updated.
     */
    public void updatePlayerEmail(String newPlayerEmail);

    /**
     * Gets the country associated with the player.
     *
     * @return the country assocated with the player.
     */
    public String getCountry();

    /**
     * Sets the country associated with the player.
     *
     * @param country associated with the player.
     */
    public void setCountry(String country);
}
