package com.brookmanholmes.billiards.acceptance.frostsvb;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

/**
 * Created by Brookman Holmes on 11/10/2015.
 */
class TurnList {
    /**
     * Game 1 (0-0)
     */
    // Frost breaks and scratches
    static ITurn turn1 = turn().deadOnBreak(1, 3).fouled().breakMiss();
    // Shane runs outs stripes
    static ITurn turn2 = turn().madeBalls(9, 11, 10, 12, 13, 14, 15, 8).win();
    /**
     * Game 2 (1-0) Shane
     */
    // Shane breaks and doesn't make anything
    static ITurn turn3 = turn().breakMiss();
    // Frost runs out
    static ITurn turn4 = turn().madeBalls(13, 15, 10, 9, 14, 11, 8).win();
    /**
     * Game 2 (1-1)
     */
    // Frost breaks
    static ITurn turn5 = turn().madeBalls(11, 12, 13, 14, 15, 10, 9, 8).breakBalls(6).win();
    /**
     * Game 3 (1-2)
     */
    // Shane breaks and runs
    static ITurn turn6 = turn().madeBalls(9, 10, 11, 12, 13, 14, 8).breakBalls(5, 15).win();
    /**
     * Game 4 (2-2)
     */
    // Frost breaks dry
    static ITurn turn7 = turn().breakMiss();
    // Shane runs out
    static ITurn turn8 = turn().madeBalls(2, 6, 7, 4, 3, 1, 5, 8).win();
    /**
     * Game 5 (3-2)
     */
    // Shane breaks and runs
    static ITurn turn9 = turn().breakBalls(5).madeBalls(2, 1, 4, 6, 7, 3, 8).win();
    /**
     * Game 6 (4-2)
     */
    // Frost breaks and runs
    static ITurn turn10 = turn().breakBalls(9, 10).madeBalls(6, 1, 2, 3, 4, 5, 8).win();
    /**
     * Game 7 (4-3)
     */
    // Shane breaks and misses his first shot
    static ITurn turn11 = turn().breakBalls(2, 9).miss();
    // Frost runs the table
    static ITurn turn12 = turn().madeBalls(5, 4, 3, 1, 7, 6, 8).win();
    /**
     * Game 8 (4-4)
     */
    // Frost breaks and runs
    static ITurn turn13 = turn().breakBalls(12).madeBalls(2, 6, 1, 4, 7, 5, 3, 8).win();
    /**
     * Game 9 (4-5)
     */
    // Shane breaks and runs
    static ITurn turn14 = turn().breakBalls(3, 7, 13).madeBalls(9, 10, 11, 12, 15, 14, 8).win();
    /**
     * Game 10 (5-5)
     */
    // Frost scratches on the break
    static ITurn turn15 = turn().deadOnBreak(7, 12).breakMiss();
    // Shane runs the table
    static ITurn turn16 = turn().madeBalls(9, 10, 11, 13, 14, 15, 8).win();
    /**
     * Game 11 (6-5)
     */
    // Shane breaks
    static ITurn turn17 = turn().miss();
    //
    static ITurn turn18 = turn().miss();
    //
    static ITurn turn19 = turn().miss();
    //
    static ITurn turn20 = turn().miss();
    //
    static ITurn turn21 = turn().miss();
    //
    static ITurn turn22 = turn().miss();
    //
    static ITurn turn23 = turn().miss();
    //
    static ITurn turn24 = turn().miss();
    //
    static ITurn turn25 = turn().miss();
    //
    static ITurn turn26 = turn().miss();
    //
    static ITurn turn27 = turn().miss();

    private static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_EIGHT_BALL);
    }
}
