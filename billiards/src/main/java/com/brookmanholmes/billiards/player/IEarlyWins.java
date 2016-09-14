package com.brookmanholmes.billiards.player;

/**
 * Interface for keeping track of early wins by a player
 * Created by Brookman Holmes on 1/12/2016.
 */
public interface IEarlyWins {
    /**
     * Adds an early win to the player
     */
    void addEarlyWin();

    /**
     * Retrieves the number of wins for the player
     * @return The number of games won early by the player
     */
    int getEarlyWins();

    /**
     * Adds a specified number of wins to the player
     * @param wins The number of wins to add, must be greater than or equal to 0
     */
    void addEarlyWins(int wins);
}
