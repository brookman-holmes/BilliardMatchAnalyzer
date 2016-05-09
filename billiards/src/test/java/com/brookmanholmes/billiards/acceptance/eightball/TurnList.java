package com.brookmanholmes.billiards.acceptance.eightball;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/16/2015.
 */
public class TurnList {
    /**
     * Game 1
     */
    // player break and run
    static Turn turn1 = turn().breakBalls(10, 6).madeBalls(9, 11, 12, 13, 14, 15, 8).win();
    /**
     * Game 2
     */
    // opponent claims stripes
    static Turn turn2 = turn().breakBalls(4).madeBalls(9).miss();
    // player makes all but the 8 ball
    static Turn turn3 = turn().offTable(4, 9).madeBalls(1, 2, 3, 5, 6).deadBalls(7).miss();
    // opponent makes the 8 early
    static Turn turn4 = turn().deadBalls(8).lose();
    /**
     * Game 3
     */
    // player breaks and makes the 8
    static Turn turn5 = turn().breakBalls(8).madeBalls(1, 2).miss();
    // opponent runs out
    static Turn turn6 = turn().offTable(1, 2).madeBalls(9, 10, 11, 12, 13, 14, 15, 8).win();
    /**
     * Game 4
     */
    // opponent breaks and scratches and makes the 8
    static Turn turn7 = turn().deadOnBreak(8).scratch().breakMiss();
    // player chooses to break
    static Turn turn8 = turn().currentPlayerBreaks();
    // player breaks and makes a few balls
    static Turn turn9 = turn().breakBalls(1, 7).madeBalls(9, 10, 11).miss();
    // opponent runs out
    static Turn turn10 = turn().offTable(1, 7, 9, 10, 11).madeBalls(2, 3, 4, 5, 6, 8).win();
    /**
     * Game 5
     */
    // player breaks and makes the 8 ball MIGHT BE A BUG HERE WITH 8 BALL BEING MADE AGAIN WITH THE TESTS
    static Turn turn11 = turn().gameBallMadeOnBreakAndThenMade().madeBalls(9, 10, 11, 12, 13, 14, 15).win();
    /**
     * Game 6
     */
    // opponent breaks and plays safe
    static Turn turn12 = turn().breakBalls(10).madeBalls(1).safety();
    // player plays safe back
    static Turn turn13 = turn().safety();
    // opponent runs a the rest of the table
    static Turn turn14 = turn().offTable(1, 10).madeBalls(2, 3, 4, 5, 6, 7, 8).win();
    /**
     * Game 7
     */
    // player scratches and makes the 8 on the break
    static Turn turn15 = turn().scratch().deadOnBreak(8).breakMiss();
    // opponent lets player break
    static Turn turn16 = turn().opposingPlayerBreaks();
    // player breaks and makes a few balls, including the 8 and wants to rebreak
    static Turn turn17 = turn().breakBalls(1, 2, 8).currentPlayerBreaks();
    // player breaks and runs the table
    static Turn turn18 = turn().breakBalls(1).madeBalls(2, 3, 4, 5, 6, 7, 8).win();

    static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_EIGHT_BALL);
    }

    public static List<Turn> getTurns() {
        return Arrays.asList(
                turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8, turn9, turn10,
                turn11, turn12, turn13, turn14, turn15, turn16, turn17, turn18
        );
    }
}
