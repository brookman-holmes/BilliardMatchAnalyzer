package com.brookmanholmes.billiards.player;

/**
 * Interface for keeping track of wins on the break and early wins
 * Created by Brookman Holmes on 1/12/2016.
 */
public interface IWinsOnBreak extends IEarlyWins {
    /**
     * Add a win on the break to the player
     */
    void addWinOnBreak();

    /**
     * Retrieve the number of wins on the break for the player
     * @return The number of wins from making the 8/9 on the break
     */
    int getWinsOnBreak();

    /**
     * Adds multiple wins on the break to the player
     * @param wins The number of wins to add, must be greater than or equal to 0
     */
    void addWinsOnBreak(int wins);
}
