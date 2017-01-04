package com.brookmanholmes.billiards.player;

/**
 * Interface for keeping track of APA stats
 * Created by Brookman Holmes on 1/12/2016.
 */
public interface IApa extends IWinsOnBreak {
    /**
     * Returns the minimum number of match points earned based on the player's score,
     * their opponent's score and their opponent's rank
     * @param opponentScore The current score for the opponent
     * @return An integer that is the minimum number of match points scored
     */
    int getMatchPoints(int opponentScore);

    /**
     * The current number of points/games that the player has
     * @return The current number of points/games that the player has
     */
    int getPoints();

    /**
     * Get the points needed to win the match
     * @return The number of points/games needed for this player to win their match
     */
    int getPointsNeeded();
}
