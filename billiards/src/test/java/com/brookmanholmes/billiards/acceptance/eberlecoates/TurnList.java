package com.brookmanholmes.billiards.acceptance.eberlecoates;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TurnBuilder;

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
    static Turn turn1 = turn().breakBalls(6, 8).push();
    // Jeff misses his safety
    static Turn turn2 = turn().offTable(6, 8).safetyMiss();
    // Max runs out
    static Turn turn3 = turn().offTable(6, 8).madeBalls(1, 4, 2, 3, 5, 7, 9).win();
    /**
     * GAME 2 (2-1)
     */
    // Jeff makes the 4 on the break and misses his safety
    static Turn turn4 = turn().breakBalls(4).safetyMiss();
    // Max misses the 3 ball and leaves the cue ball right on it
    static Turn turn5 = turn().offTable(4).madeBalls(1, 2).miss();
    // Jeff runs out
    static Turn turn6 = turn().offTable(4, 1, 2).madeBalls(3, 5, 6, 7, 8, 9).win();
    /**
     * GAME 3 (3-1)
     */
    // Max breaks and runs
    static Turn turn7 = turn().breakBalls(3, 6, 8).madeBalls(1, 2, 4, 5, 7, 9).win();
    /**
     * GAME 4 (3-2)
     */
    // Jeff breaks and misses a shot/safety?
    static Turn turn8 = turn().breakBalls(1, 4).miss();
    // Max runs out
    static Turn turn9 = turn().offTable(1, 4).madeBalls(2, 3, 5, 6, 7, 8, 9).win();
    /**
     * Game 5 (3-3)
     */
    // Max breaks and misses
    static Turn turn10 = turn().breakBalls(8).miss();
    // Jeff misses
    static Turn turn11 = turn().offTable(8).miss();
    // Max makes a couple balls and plays a shit safe
    static Turn turn12 = turn().offTable(8).madeBalls(1, 2).safetyMiss();
    // Jeff plays a good safe
    static Turn turn13 = turn().offTable(1, 2, 8).safety();
    // Max misses a kick
    static Turn turn14 = turn().offTable(1, 2, 8).miss();
    // Jeff runs out
    static Turn turn15 = turn().offTable(1, 2, 8).madeBalls(3, 4, 5, 6, 7, 9).win();
    /**
     * Game 6 (4-3)
     */
    // Jeff broke missed and got safe
    static Turn turn16 = turn().breakBalls(5).miss();
    // Max plays a good safe?
    static Turn turn17 = turn().offTable(5).safetyMiss();
    // Jeff plays a good safe
    static Turn turn18 = turn().offTable(5).safety();
    // Max kicks in the 1 and runs out
    static Turn turn19 = turn().offTable(5).madeBalls(1, 2, 3, 4, 6, 7, 8, 9).win();
    /**
     * GAME 7 (4-4)
     */
    // Max breaks hangs the 9 and scratches
    static Turn turn20 = turn().breakBalls(2, 8, 6).madeBalls(1, 3, 4, 5, 7).scratch().miss();
    // Jeff wins
    static Turn turn21 = turn().offTable(2, 8, 6, 1, 3, 4, 5, 7).madeBalls(9).win();
    /**
     * GAME 8 (5-4)
     */
    // Jeff breaks and plays safe
    static Turn turn22 = turn().breakBalls(3, 5).safety();
    // Max misses his safe
    static Turn turn23 = turn().offTable(3, 5).safetyMiss();
    // Jeff misses the 1
    static Turn turn24 = turn().offTable(3, 5).miss();
    // Max misses the 1 and scratches
    static Turn turn25 = turn().offTable(3, 5).scratch().miss();
    // Jeff makes a couple balls and plays safe
    static Turn turn26 = turn().madeBalls(1, 2).offTable(3, 5).safety();
    // Max misses
    static Turn turn27 = turn().offTable(1, 2, 3, 5).scratch().miss();
    // Jeff runs out
    static Turn turn28 = turn().offTable(1, 2, 3, 5).madeBalls(4, 6, 7, 8, 9).win();
    /**
     * GAME 9 (6-4)
     */
    // Max breaks and misses the 1 ball
    static Turn turn29 = turn().breakBalls(5, 8).miss();
    // Jeff plays safe at some point
    static Turn turn30 = turn().offTable(5, 8).madeBalls(1, 2, 3, 6).safety();
    // Max plays like an idiot
    static Turn turn31 = turn().offTable(5, 8, 1, 2, 3, 6).miss();
    // Jeff runs out
    static Turn turn32 = turn().offTable(5, 8, 1, 2, 3, 6).madeBalls(4, 7, 9).win();
    /**
     * GAME 10 (7-4)
     */
    // Jeff breaks terribly and has to play safe again
    static Turn turn33 = turn().breakBalls(3, 7).safetyMiss();
    // Max hangs the 1
    static Turn turn34 = turn().offTable(3, 7).miss();
    // Jeff kicks the 1 in and then plays safe on the 4
    static Turn turn35 = turn().offTable(3, 7).madeBalls(1, 2).safety();
    // Max hangs the 4 on a kick
    static Turn turn36 = turn().offTable(3, 7, 1, 2).miss();
    // Jeff
    static Turn turn37 = turn().offTable(3, 7, 1, 2).madeBalls(4, 5, 6, 8, 9).win();
    /**
     * GAME 11 (8-4)
     */
    // Max breaks and has to play safe
    static Turn turn38 = turn().breakBalls(4).safety();
    // Jeff kicks and gets safe
    static Turn turn39 = turn().offTable(4).safety();
    // Max played safe again
    static Turn turn40 = turn().offTable(4).safety();
    // Jeff missed
    static Turn turn41 = turn().offTable(4).safetyMiss();
    // Max ran out
    static Turn turn42 = turn().offTable(4).madeBalls(1, 2, 3, 5, 6, 7, 8, 9).win();
    /**
     * GAME 12 (8-5)
     */
    // Jeff breaks misses the 6?
    static Turn turn43 = turn().breakBalls(5).madeBalls(1, 2, 8, 3, 4).miss();
    // Max runs out
    static Turn turn44 = turn().offTable(5, 1, 2, 8, 3, 4).madeBalls(6, 7, 9).win();
    /**
     * GAME 13 (8-6)
     */
    // Max breaks and misses the 1
    static Turn turn45 = turn().breakBalls(8).miss();
    // Jeff misses the 3
    static Turn turn46 = turn().offTable(8).madeBalls(1, 2).miss();
    // Max
    static Turn turn47 = turn().offTable(8, 1, 2).madeBalls(3, 4, 5, 6, 7, 9).win();
    /**
     * GAME 14 (8-7)
     */
    // Jeff breaks jumps and misses the 2
    static Turn turn48 = turn().breakBalls(5, 6, 4).madeBalls(1).miss();
    // Max plays safe
    static Turn turn49 = turn().offTable(5, 6, 1, 4).safety();
    // Jeff kicks and doesn't get safe
    static Turn turn50 = turn().safetyMiss();
    // Max hangs the 2
    static Turn turn51 = turn().offTable(5, 6, 1, 4).miss();
    // Jeff misses the 3
    static Turn turn52 = turn().madeBalls(2).offTable(5, 6, 1, 4).miss();
    // Max runs out
    static Turn turn53 = turn().offTable(5, 6, 1, 2, 4).madeBalls(3, 7, 8, 9).win();
    /**
     * GAME 15 (8-8)
     */
    // Max breaks and almost scratches
    static Turn turn54 = turn().breakBalls(2).safetyMiss();
    // Jeff scratches
    static Turn turn55 = turn().offTable(2).scratch().miss();
    // Max runs out and wins the set!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    static Turn turn56 = turn().offTable(2).madeBalls(1, 3, 4, 5, 6, 7, 8, 9).win();

    static TurnBuilder turn() {
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
