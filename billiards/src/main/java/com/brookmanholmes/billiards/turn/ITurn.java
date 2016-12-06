package com.brookmanholmes.billiards.turn;

/**
 * Created by Brookman Holmes on 11/4/2015.
 */
public interface ITurn extends ITableStatus {
    /**
     * Returns whether or not the current turn resulted in a foul
     * @return true for a foul occurred, false otherwise
     */
    boolean isFoul();

    /**
     * Returns whether or not the current turn resulted in the game being lost (from something like
     * a three fouls rule or illegally pocketing the 8 ball)
     * @return true if the game was lost, false otherwise
     */
    boolean isSeriousFoul();

    /**
     * Returns what type of ending the turn was
     * @return an enum from {@link com.brookmanholmes.billiards.turn.TurnEnd}
     */
    TurnEnd getTurnEnd();

    /**
     * Returns the attached advanced stats for this turn
     * @return The attached {@link com.brookmanholmes.billiards.turn.AdvStats} or null if there is no
     * advanced stats for this turn
     */
    AdvStats getAdvStats();
}
