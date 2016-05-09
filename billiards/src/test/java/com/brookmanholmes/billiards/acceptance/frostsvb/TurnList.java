package com.brookmanholmes.billiards.acceptance.frostsvb;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

/**
 * Created by Brookman Holmes on 11/10/2015.
 */
public class TurnList {
    /**
     * Game 1 (0-0)
     */
    // Frost breaks and scratches
    static Turn turn1 = turn().deadOnBreak(1, 3).scratch().breakMiss();
    // Shane runs outs stripes
    static Turn turn2 = turn().madeBalls(9, 11, 10, 12, 13, 14, 15, 8).win();
    /**
     * Game 2 (1-0) Shane
     */
    // Shane breaks and doesn't make anything
    static Turn turn3 = turn().breakMiss();
    // Frost runs out
    static Turn turn4 = turn().madeBalls(13, 15, 10, 9, 14, 11, 8).win();
    /**
     * Game 2 (1-1)
     */
    // Frost breaks
    static Turn turn5 = turn().madeBalls(11, 12, 13, 14, 15, 10, 9, 8).breakBalls(6).win();
    /**
     * Game 3 (1-2)
     */
    // Shane breaks and runs
    static Turn turn6 = turn().madeBalls(9, 10, 11, 12, 13, 14, 8).breakBalls(5, 15).win();
    /**
     * Game 4 (2-2)
     */
    // Frost breaks dry
    static Turn turn7 = turn().breakMiss();
    // Shane runs out
    static Turn turn8 = turn().madeBalls(2, 6, 7, 4, 3, 1, 5, 8).win();
    /**
     * Game 5 (3-2)
     */
    // Shane breaks and runs
    static Turn turn9 = turn().breakBalls(5).madeBalls(2, 1, 4, 6, 7, 3, 8).win();
    /**
     * Game 6 (4-2)
     */
    // Frost breaks and runs
    static Turn turn10 = turn().breakBalls(9, 10).madeBalls(6, 1, 2, 3, 4, 5, 8).win();
    /**
     * Game 7 (4-3)
     */
    // Shane breaks and misses his first shot
    static Turn turn11 = turn().breakBalls(2, 9).miss();
    // Frost runs the table
    static Turn turn12 = turn().madeBalls(5, 4, 3, 1, 7, 6, 8).win();
    /**
     * Game 8 (4-4)
     */
    // Frost breaks and runs
    static Turn turn13 = turn().breakBalls(12).madeBalls(2, 6, 1, 4, 7, 5, 3, 8).win();
    /**
     * Game 9 (4-5)
     */
    // Shane breaks and runs
    static Turn turn14 = turn().breakBalls(3, 7, 13).madeBalls(9, 10, 11, 12, 15, 14, 8).win();
    /**
     * Game 10 (5-5)
     */
    // Frost scratches on the break
    static Turn turn15 = turn().deadOnBreak(7, 12).breakMiss();
    // Shane runs the table
    static Turn turn16 = turn().madeBalls(9, 10, 11, 13, 14, 15, 8).win();
    /**
     * Game 11 (6-5)
     */
    // Shane breaks
    static Turn turn17 = turn().miss();
    //
    static Turn turn18 = turn().miss();
    //
    static Turn turn19 = turn().miss();
    //
    static Turn turn20 = turn().miss();
    //
    static Turn turn21 = turn().miss();
    //
    static Turn turn22 = turn().miss();
    //
    static Turn turn23 = turn().miss();
    //
    static Turn turn24 = turn().miss();
    //
    static Turn turn25 = turn().miss();
    //
    static Turn turn26 = turn().miss();
    //
    static Turn turn27 = turn().miss();

    static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_EIGHT_BALL);
    }
}
