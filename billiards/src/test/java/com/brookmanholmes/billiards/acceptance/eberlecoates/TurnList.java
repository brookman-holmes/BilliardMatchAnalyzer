package com.brookmanholmes.billiards.acceptance.eberlecoates;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.ITurn;
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
    private static final ITurn turn1 = turn().breakBalls(6, 8).push();
    // Jeff misses his safety
    private static final ITurn turn2 = turn().offTable(6, 8).safetyMiss();
    // Max runs out
    private static final ITurn turn3 = turn().offTable(6, 8).madeBalls(1, 4, 2, 3, 5, 7, 9).win();
    /**
     * GAME 2 (2-1)
     */
    // Jeff makes the 4 on the break and misses his safety
    private static final ITurn turn4 = turn().breakBalls(4).safetyMiss();
    // Max misses the 3 ball and leaves the cue ball right on it
    private static final ITurn turn5 = turn().offTable(4).madeBalls(1, 2).miss();
    // Jeff runs out
    private static final ITurn turn6 = turn().offTable(4, 1, 2).madeBalls(3, 5, 6, 7, 8, 9).win();
    /**
     * GAME 3 (3-1)
     */
    // Max breaks and runs
    private static final ITurn turn7 = turn().breakBalls(3, 6, 8).madeBalls(1, 2, 4, 5, 7, 9).win();
    /**
     * GAME 4 (3-2)
     */
    // Jeff breaks and misses a shot/safety?
    private static final ITurn turn8 = turn().breakBalls(1, 4).miss();
    // Max runs out
    private static final ITurn turn9 = turn().offTable(1, 4).madeBalls(2, 3, 5, 6, 7, 8, 9).win();
    /**
     * Game 5 (3-3)
     */
    // Max breaks and misses
    private static final ITurn turn10 = turn().breakBalls(8).miss();
    // Jeff misses
    private static final ITurn turn11 = turn().offTable(8).miss();
    // Max makes a couple balls and plays a shit safe
    private static final ITurn turn12 = turn().offTable(8).madeBalls(1, 2).safetyMiss();
    // Jeff plays a good safe
    private static final ITurn turn13 = turn().offTable(1, 2, 8).safety();
    // Max misses a kick
    private static final ITurn turn14 = turn().offTable(1, 2, 8).miss();
    // Jeff runs out
    private static final ITurn turn15 = turn().offTable(1, 2, 8).madeBalls(3, 4, 5, 6, 7, 9).win();
    /**
     * Game 6 (4-3)
     */
    // Jeff broke missed and got safe
    private static final ITurn turn16 = turn().breakBalls(5).miss();
    // Max plays a good safe?
    private static final ITurn turn17 = turn().offTable(5).safetyMiss();
    // Jeff plays a good safe
    private static final ITurn turn18 = turn().offTable(5).safety();
    // Max kicks in the 1 and runs out
    private static final ITurn turn19 = turn().offTable(5).madeBalls(1, 2, 3, 4, 6, 7, 8, 9).win();
    /**
     * GAME 7 (4-4)
     */
    // Max breaks hangs the 9 and scratches
    private static final ITurn turn20 = turn().breakBalls(2, 8, 6).madeBalls(1, 3, 4, 5, 7).fouled().miss();
    // Jeff wins
    private static final ITurn turn21 = turn().offTable(2, 8, 6, 1, 3, 4, 5, 7).madeBalls(9).win();
    /**
     * GAME 8 (5-4)
     */
    // Jeff breaks and plays safe
    private static final ITurn turn22 = turn().breakBalls(3, 5).safety();
    // Max misses his safe
    private static final ITurn turn23 = turn().offTable(3, 5).safetyMiss();
    // Jeff misses the 1
    private static final ITurn turn24 = turn().offTable(3, 5).miss();
    // Max misses the 1 and scratches
    private static final ITurn turn25 = turn().offTable(3, 5).fouled().miss();
    // Jeff makes a couple balls and plays safe
    private static final ITurn turn26 = turn().madeBalls(1, 2).offTable(3, 5).safety();
    // Max misses
    private static final ITurn turn27 = turn().offTable(1, 2, 3, 5).fouled().miss();
    // Jeff runs out
    private static final ITurn turn28 = turn().offTable(1, 2, 3, 5).madeBalls(4, 6, 7, 8, 9).win();
    /**
     * GAME 9 (6-4)
     */
    // Max breaks and misses the 1 ball
    private static final ITurn turn29 = turn().breakBalls(5, 8).miss();
    // Jeff plays safe at some point
    private static final ITurn turn30 = turn().offTable(5, 8).madeBalls(1, 2, 3, 6).safety();
    // Max plays like an idiot
    private static final ITurn turn31 = turn().offTable(5, 8, 1, 2, 3, 6).miss();
    // Jeff runs out
    private static final ITurn turn32 = turn().offTable(5, 8, 1, 2, 3, 6).madeBalls(4, 7, 9).win();
    /**
     * GAME 10 (7-4)
     */
    // Jeff breaks terribly and has to play safe again
    private static final ITurn turn33 = turn().breakBalls(3, 7).safetyMiss();
    // Max hangs the 1
    private static final ITurn turn34 = turn().offTable(3, 7).miss();
    // Jeff kicks the 1 in and then plays safe on the 4
    private static final ITurn turn35 = turn().offTable(3, 7).madeBalls(1, 2).safety();
    // Max hangs the 4 on a kick
    private static final ITurn turn36 = turn().offTable(3, 7, 1, 2).miss();
    // Jeff
    private static final ITurn turn37 = turn().offTable(3, 7, 1, 2).madeBalls(4, 5, 6, 8, 9).win();
    /**
     * GAME 11 (8-4)
     */
    // Max breaks and has to play safe
    private static final ITurn turn38 = turn().breakBalls(4).safety();
    // Jeff kicks and gets safe
    private static final ITurn turn39 = turn().offTable(4).safety();
    // Max played safe again
    private static final ITurn turn40 = turn().offTable(4).safety();
    // Jeff missed
    private static final ITurn turn41 = turn().offTable(4).safetyMiss();
    // Max ran out
    private static final ITurn turn42 = turn().offTable(4).madeBalls(1, 2, 3, 5, 6, 7, 8, 9).win();
    /**
     * GAME 12 (8-5)
     */
    // Jeff breaks misses the 6?
    private static final ITurn turn43 = turn().breakBalls(5).madeBalls(1, 2, 8, 3, 4).miss();
    // Max runs out
    private static final ITurn turn44 = turn().offTable(5, 1, 2, 8, 3, 4).madeBalls(6, 7, 9).win();
    /**
     * GAME 13 (8-6)
     */
    // Max breaks and misses the 1
    private static final ITurn turn45 = turn().breakBalls(8).miss();
    // Jeff misses the 3
    private static final ITurn turn46 = turn().offTable(8).madeBalls(1, 2).miss();
    // Max
    private static final ITurn turn47 = turn().offTable(8, 1, 2).madeBalls(3, 4, 5, 6, 7, 9).win();
    /**
     * GAME 14 (8-7)
     */
    // Jeff breaks jumps and misses the 2
    private static final ITurn turn48 = turn().breakBalls(5, 6, 4).madeBalls(1).miss();
    // Max plays safe
    private static final ITurn turn49 = turn().offTable(5, 6, 1, 4).safety();
    // Jeff kicks and doesn't get safe
    private static final ITurn turn50 = turn().safetyMiss();
    // Max hangs the 2
    private static final ITurn turn51 = turn().offTable(5, 6, 1, 4).miss();
    // Jeff misses the 3
    private static final ITurn turn52 = turn().madeBalls(2).offTable(5, 6, 1, 4).miss();
    // Max runs out
    private static final ITurn turn53 = turn().offTable(5, 6, 1, 2, 4).madeBalls(3, 7, 8, 9).win();
    /**
     * GAME 15 (8-8)
     */
    // Max breaks and almost scratches
    private static final ITurn turn54 = turn().breakBalls(2).safetyMiss();
    // Jeff scratches
    private static final ITurn turn55 = turn().offTable(2).fouled().miss();
    // Max runs out and wins the set!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private static final ITurn turn56 = turn().offTable(2).madeBalls(1, 3, 4, 5, 6, 7, 8, 9).win();

    private static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_NINE_BALL);
    }

    public static List<ITurn> getTurns() {
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
