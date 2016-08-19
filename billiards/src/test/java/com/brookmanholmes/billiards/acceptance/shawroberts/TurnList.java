package com.brookmanholmes.billiards.acceptance.shawroberts;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/9/2015.
 */
public class TurnList {
    /**
     * GAME 1
     */
    // roberts breaks and runs the first rack
    private static final Turn turn1 = turn().madeBalls(2, 3, 4, 5, 8, 9, 10).breakBalls(1, 6, 7).win();
    /**
     * GAME 2
     */
    // Shaw breaks dry
    private static final Turn turn2 = turn().breakMiss();
    // Roberts pushes and makes the 7 and 9
    private static final Turn turn3 = turn().deadBalls(7, 9).push();
    // Shaw plays a bad safe
    private static final Turn turn4 = turn().offTable(7, 9).safetyMiss();
    // Roberts fails to run out
    private static final Turn turn5 = turn().offTable(7, 9).madeBalls(1, 2, 3, 8).miss();
    // Shaw scratches on the 5
    private static final Turn turn6 = turn().offTable(1, 2, 3, 8, 7, 9).madeBalls(4).deadBalls(5).scratch().miss();
    // Roberts wins
    private static final Turn turn7 = turn().offTable(1, 2, 3, 8, 7, 9, 4, 5).madeBalls(6, 10).win();
    /**
     * GAME 3
     */
    // Roberts scratches on the break
    private static final Turn turn8 = turn().deadOnBreak(6).scratch().breakMiss();
    // Shaw runs out
    private static final Turn turn9 = turn().offTable(6).madeBalls(1, 2, 3, 4, 5, 7, 8, 9, 10).win();
    /**
     * GAME 4
     */
    // Shaw breaks and runs
    private static final Turn turn10 = turn().breakBalls(3, 7).madeBalls(1, 2, 4, 5, 6, 8, 9, 10).win();
    /**
     * GAME 5
     */
    // Roberts breaks and runs
    private static final Turn turn11 = turn().breakBalls(6, 9).madeBalls(1, 2, 3, 4, 5, 7, 8, 10).win();
    /**
     * GAME 6
     */
    // Shaw gets out of shape and plays safe
    private static final Turn turn12 = turn().breakBalls(2).madeBalls(1, 3).safety();
    // Roberts misses his safety attempt
    private static final Turn turn13 = turn().offTable(1, 2, 3).safetyMiss();
    // Shaw runs out
    private static final Turn turn14 = turn().offTable(1, 2, 3).madeBalls(4, 5, 6, 7, 8, 9, 10).win();
    /**
     * GAME 7
     */
    // Roberts breaks and pushes out
    private static final Turn turn15 = turn().breakBalls(1, 4, 6).push();
    // Shaw gives the shot back
    private static final Turn turn16 = turn().skipTurn();
    // Roberts misses
    private static final Turn turn17 = turn().scratch().offTable(1, 4, 6).miss();
    // Shaw locks him up
    private static final Turn turn18 = turn().offTable(1, 4, 6).safety();
    // Roberts misses his safety attempt
    private static final Turn turn19 = turn().deadBalls(2).offTable(1, 4, 6).scratch().safetyMiss();
    // Shaw runs out
    private static final Turn turn20 = turn().offTable(1, 4, 6, 2).madeBalls(3, 5, 7, 8, 9, 10).win();
    /**
     * GAME 8
     */
    // Shaw breaks gets out of shape and plays a two way
    private static final Turn turn21 = turn().breakBalls(2).madeBalls(1).safety();
    // Roberts misses his jump and makes the 10 by accident
    private static final Turn turn22 = turn().offTable(1, 2).deadBalls(10).miss();
    // Shaw misses his safety attempt
    private static final Turn turn23 = turn().offTable(1, 2).safetyMiss();
    // Roberts misses
    private static final Turn turn24 = turn().offTable(1, 2).miss();
    // Shaw has to kick at the 3 and then runs out
    private static final Turn turn25 = turn().offTable(1, 2).madeBalls(3, 4, 5, 6, 7, 8, 9, 10).win();
    /**
     * GAME 9
     */
    // Roberts break and misses
    private static final Turn turn26 = turn().breakBalls(3, 7).miss();
    // Shaw runs out
    private static final Turn turn27 = turn().offTable(3, 7).madeBalls(1, 2, 4, 5, 6, 8, 9, 10).win();
    /**
     * GAME 10
     */
    // Shaw breaks and hangs the 10 and doesn't make a ball
    private static final Turn turn28 = turn().breakMiss();
    // Shaw makes the 10
    private static final Turn turn29 = turn().madeBalls(10).win();
    /**
     * GAME 11
     */
    // Roberts breaks
    private static final Turn turn30 = turn().breakBalls(7).push();
    // Shaw gives the shot back
    private static final Turn turn31 = turn().offTable(7).skipTurn();
    // Roberts misses
    private static final Turn turn32 = turn().offTable(7).miss();
    // Shaw runs out
    private static final Turn turn33 = turn().offTable(7).madeBalls(1, 2, 3, 4, 5, 6, 8, 9, 10).win();

    private static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_TEN_BALL);
    }

    public static List<Turn> getTurns() {
        return Arrays.asList(
                turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8, turn9,
                turn10, turn11, turn12, turn13, turn14, turn15, turn16, turn17,
                turn18, turn19, turn20, turn21, turn22, turn23, turn24, turn25,
                turn26, turn27, turn28, turn29, turn30, turn31, turn32, turn33
        );
    }
}
