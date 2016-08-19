package com.brookmanholmes.billiards.acceptance.eberlecoates;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/10/2015.
 */
public class TurnList {
    /**
     * GAME 1 (2-0) Jeff
     */
    // Max breaks and pushes
    private static final Turn turn1 = turn().breakBalls(6, 8).push();
    // Jeff misses his safety
    private static final Turn turn2 = turn().offTable(6, 8).safetyMiss();
    // Max runs out
    private static final Turn turn3 = turn().offTable(6, 8).madeBalls(1, 4, 2, 3, 5, 7, 9).win();
    /**
     * GAME 2 (2-1)
     */
    // Jeff makes the 4 on the break and misses his safety
    private static final Turn turn4 = turn().breakBalls(4).safetyMiss();
    // Max misses the 3 ball and leaves the cue ball right on it
    private static final Turn turn5 = turn().offTable(4).madeBalls(1, 2).miss();
    // Jeff runs out
    private static final Turn turn6 = turn().offTable(4, 1, 2).madeBalls(3, 5, 6, 7, 8, 9).win();
    /**
     * GAME 3 (3-1)
     */
    // Max breaks and runs
    private static final Turn turn7 = turn().breakBalls(3, 6, 8).madeBalls(1, 2, 4, 5, 7, 9).win();
    /**
     * GAME 4 (3-2)
     */
    // Jeff breaks and misses a shot/safety?
    private static final Turn turn8 = turn().breakBalls(1, 4).miss();
    // Max runs out
    private static final Turn turn9 = turn().offTable(1, 4).madeBalls(2, 3, 5, 6, 7, 8, 9).win();
    /**
     * Game 5 (3-3)
     */
    // Max breaks and misses
    private static final Turn turn10 = turn().breakBalls(8).miss();
    // Jeff misses
    private static final Turn turn11 = turn().offTable(8).miss();
    // Max makes a couple balls and plays a shit safe
    private static final Turn turn12 = turn().offTable(8).madeBalls(1, 2).safetyMiss();
    // Jeff plays a good safe
    private static final Turn turn13 = turn().offTable(1, 2, 8).safety();
    // Max misses a kick
    private static final Turn turn14 = turn().offTable(1, 2, 8).miss();
    // Jeff runs out
    private static final Turn turn15 = turn().offTable(1, 2, 8).madeBalls(3, 4, 5, 6, 7, 9).win();
    /**
     * Game 6 (4-3)
     */
    // Jeff broke missed and got safe
    private static final Turn turn16 = turn().breakBalls(5).miss();
    // Max plays a good safe?
    private static final Turn turn17 = turn().offTable(5).safetyMiss();
    // Jeff plays a good safe
    private static final Turn turn18 = turn().offTable(5).safety();
    // Max kicks in the 1 and runs out
    private static final Turn turn19 = turn().offTable(5).madeBalls(1, 2, 3, 4, 6, 7, 8, 9).win();
    /**
     * GAME 7 (4-4)
     */
    // Max breaks hangs the 9 and scratches
    private static final Turn turn20 = turn().breakBalls(2, 8, 6).madeBalls(1, 3, 4, 5, 7).scratch().miss();
    // Jeff wins
    private static final Turn turn21 = turn().offTable(2, 8, 6, 1, 3, 4, 5, 7).madeBalls(9).win();
    /**
     * GAME 8 (5-4)
     */
    // Jeff breaks and plays safe
    private static final Turn turn22 = turn().breakBalls(3, 5).safety();
    // Max misses his safe
    private static final Turn turn23 = turn().offTable(3, 5).safetyMiss();
    // Jeff misses the 1
    private static final Turn turn24 = turn().offTable(3, 5).miss();
    // Max misses the 1 and scratches
    private static final Turn turn25 = turn().offTable(3, 5).scratch().miss();
    // Jeff makes a couple balls and plays safe
    private static final Turn turn26 = turn().madeBalls(1, 2).offTable(3, 5).safety();
    // Max misses
    private static final Turn turn27 = turn().offTable(1, 2, 3, 5).scratch().miss();
    // Jeff runs out
    private static final Turn turn28 = turn().offTable(1, 2, 3, 5).madeBalls(4, 6, 7, 8, 9).win();
    /**
     * GAME 9 (6-4)
     */
    // Max breaks and misses the 1 ball
    private static final Turn turn29 = turn().breakBalls(5, 8).miss();
    // Jeff plays safe at some point
    private static final Turn turn30 = turn().offTable(5, 8).madeBalls(1, 2, 3, 6).safety();
    // Max plays like an idiot
    private static final Turn turn31 = turn().offTable(5, 8, 1, 2, 3, 6).miss();
    // Jeff runs out
    private static final Turn turn32 = turn().offTable(5, 8, 1, 2, 3, 6).madeBalls(4, 7, 9).win();
    /**
     * GAME 10 (7-4)
     */
    // Jeff breaks terribly and has to play safe again
    private static final Turn turn33 = turn().breakBalls(3, 7).safetyMiss();
    // Max hangs the 1
    private static final Turn turn34 = turn().offTable(3, 7).miss();
    // Jeff kicks the 1 in and then plays safe on the 4
    private static final Turn turn35 = turn().offTable(3, 7).madeBalls(1, 2).safety();
    // Max hangs the 4 on a kick
    private static final Turn turn36 = turn().offTable(3, 7, 1, 2).miss();
    // Jeff
    private static final Turn turn37 = turn().offTable(3, 7, 1, 2).madeBalls(4, 5, 6, 8, 9).win();
    /**
     * GAME 11 (8-4)
     */
    // Max breaks and has to play safe
    private static final Turn turn38 = turn().breakBalls(4).safety();
    // Jeff kicks and gets safe
    private static final Turn turn39 = turn().offTable(4).safety();
    // Max played safe again
    private static final Turn turn40 = turn().offTable(4).safety();
    // Jeff missed
    private static final Turn turn41 = turn().offTable(4).safetyMiss();
    // Max ran out
    private static final Turn turn42 = turn().offTable(4).madeBalls(1, 2, 3, 5, 6, 7, 8, 9).win();
    /**
     * GAME 12 (8-5)
     */
    // Jeff breaks misses the 6?
    private static final Turn turn43 = turn().breakBalls(5).madeBalls(1, 2, 8, 3, 4).miss();
    // Max runs out
    private static final Turn turn44 = turn().offTable(5, 1, 2, 8, 3, 4).madeBalls(6, 7, 9).win();
    /**
     * GAME 13 (8-6)
     */
    // Max breaks and misses the 1
    private static final Turn turn45 = turn().breakBalls(8).miss();
    // Jeff misses the 3
    private static final Turn turn46 = turn().offTable(8).madeBalls(1, 2).miss();
    // Max
    private static final Turn turn47 = turn().offTable(8, 1, 2).madeBalls(3, 4, 5, 6, 7, 9).win();
    /**
     * GAME 14 (8-7)
     */
    // Jeff breaks jumps and misses the 2
    private static final Turn turn48 = turn().breakBalls(5, 6, 4).madeBalls(1).miss();
    // Max plays safe
    private static final Turn turn49 = turn().offTable(5, 6, 1, 4).safety();
    // Jeff kicks and doesn't get safe
    private static final Turn turn50 = turn().safetyMiss();
    // Max hangs the 2
    private static final Turn turn51 = turn().offTable(5, 6, 1, 4).miss();
    // Jeff misses the 3
    private static final Turn turn52 = turn().madeBalls(2).offTable(5, 6, 1, 4).miss();
    // Max runs out
    private static final Turn turn53 = turn().offTable(5, 6, 1, 2, 4).madeBalls(3, 7, 8, 9).win();
    /**
     * GAME 15 (8-8)
     */
    // Max breaks and almost scratches
    private static final Turn turn54 = turn().breakBalls(2).safetyMiss();
    // Jeff scratches
    private static final Turn turn55 = turn().offTable(2).scratch().miss();
    // Max runs out and wins the set!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private static final Turn turn56 = turn().offTable(2).madeBalls(1, 3, 4, 5, 6, 7, 8, 9).win();

    private static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_NINE_BALL);
    }

    public static List<Turn> getTurns() {
        return Arrays.asList(
                turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8, turn9, turn10,
                turn11, turn12, turn13, turn14, turn15, turn16, turn17, turn18, turn19, turn20,
                turn21, turn22, turn23, turn24, turn25, turn26, turn27, turn28, turn29, turn30,
                turn31, turn32, turn33, turn34, turn35, turn36, turn37, turn38, turn39, turn40,
                turn41, turn42, turn43, turn44, turn45, turn46, turn47, turn48, turn49, turn50,
                turn51, turn52, turn53, turn54, turn55, turn56
        );
    }
}
