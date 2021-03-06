package com.brookmanholmes.billiards.acceptance.hohmannsvb;

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
     * Game 1 (0-0)
     */
    // Hohmann breaks dry
    private static final ITurn turn1 = turn().breakMiss();
    // SVB runs to the 9 and misses
    private static final ITurn turn2 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8).miss();
    // Hohmann runs out
    private static final ITurn turn3 = turn().madeBalls(9, 10).offTable(1, 2, 3, 4, 5, 6, 7, 8).win();
    /**
     * Game 2 (1-0) Hohmann
     */
    // Shane breaks dry
    private static final ITurn turn4 = turn().breakMiss();
    // Hohmann makes a few balls and plays safe
    private static final ITurn turn5 = turn().madeBalls(1, 2, 3, 4).safety();
    // Shane hits the ball but doesn't quite get safe
    private static final ITurn turn6 = turn().offTable(1, 2, 3, 4).safetyMiss();
    // Hohmann makes the 8 and then plays safe
    private static final ITurn turn7 = turn().offTable(1, 2, 3, 4).madeBalls(8).safety();
    // Shane scratches
    private static final ITurn turn8 = turn().offTable(1, 2, 3, 4, 8).fouled().safetyMiss();
    // Hohmann runs out
    private static final ITurn turn9 = turn().offTable(1, 2, 3, 4, 8).madeBalls(5, 6, 7, 9, 10).win();
    /**
     * Game 3 (2-0)
     */
    // Hohmann breaks and has to push
    private static final ITurn turn10 = turn().breakBalls(3, 6, 9).push();
    // Shane doesn't play safe
    private static final ITurn turn11 = turn().offTable(3, 6, 9).safetyMiss();
    // Hohmann runs out
    private static final ITurn turn12 = turn().offTable(3, 6, 9).madeBalls(1, 2, 4, 5, 7, 8, 10).win();
    /**
     * Game 4 (3-0)
     */
    // Shane breaks dry
    private static final ITurn turn13 = turn().breakMiss();
    // Hohmann pushes
    private static final ITurn turn14 = turn().push();
    // Shane gives the shot back
    private static final ITurn turn15 = turn().skipTurn();
    // Hohmann plays safe
    private static final ITurn turn16 = turn().safety();
    // Shane plays safe back
    private static final ITurn turn17 = turn().safety();
    // Hohmann kicks and doesn't get quite safe
    private static final ITurn turn18 = turn().safetyMiss();
    // Shane runs out
    private static final ITurn turn19 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).win();
    /**
     * Game 5 (3-1)
     */
    // Hohmann breaks makes a ball and misses
    private static final ITurn turn20 = turn().breakBalls(3, 8, 9).madeBalls(1).miss();
    // Shane gets out of shape on the 5 and plays safe
    private static final ITurn turn21 = turn().offTable(1, 3, 8, 9).madeBalls(2, 4).safety();
    // Hohmann misses the kick
    private static final ITurn turn22 = turn().offTable(1, 2, 3, 4, 8, 9).miss();
    // Shane misses as well
    private static final ITurn turn23 = turn().offTable(1, 2, 3, 4, 8, 9).miss();
    // Hohmann runs out
    private static final ITurn turn24 = turn().offTable(1, 2, 3, 4, 8, 9).madeBalls(5, 6, 7, 10).win();
    /**
     * Game 6 (4-1)
     */
    // Shane breaks and misses the 3 ball
    private static final ITurn turn25 = turn().breakBalls(1, 2).miss();
    // Hohmann comes to the table hooked and doesn't get his kick safe
    private static final ITurn turn26 = turn().offTable(1, 2).safetyMiss();
    // Shane scratches on the 4... somehow
    private static final ITurn turn27 = turn().offTable(1, 2).madeBalls(3).deadBalls(4).fouled().miss();
    // Hohmann runs out
    private static final ITurn turn28 = turn().offTable(1, 2, 3, 4).madeBalls(5, 6, 7, 8, 9, 10).win();
    /**
     * Game 7 (5-1)
     */
    // Hohmann scratches on the break
    private static final ITurn turn29 = turn().deadOnBreak(5).fouled().breakMiss();
    // Shane makes a few balls and then plays safe
    private static final ITurn turn30 = turn().offTable(5).madeBalls(1, 2, 3).safety();
    // Hohmann jumps at the 4 and doesn't clear the blocker ball
    private static final ITurn turn31 = turn().offTable(1, 2, 3, 5).fouled().miss();
    // Shane better fucking run out
    private static final ITurn turn32 = turn().offTable(1, 2, 3, 5).madeBalls(4, 6, 7, 8, 9, 10).win();
    /**
     * Game 8 (5-2)
     */
    // Shane breaks and runs
    private static final ITurn turn33 = turn().breakBalls(9).madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 10).win();
    /**
     * Game 9 (5-3)
     */
    // Hohmann breaks dry
    private static final ITurn turn34 = turn().breakMiss();
    // Shane runs the table
    private static final ITurn turn35 = turn().madeBalls(7, 1, 2, 3, 4, 5, 6, 8, 9, 10).win();
    /**
     * Game 10 (5-4)
     */
    // Shane breaks and gets an early 10
    private static final ITurn turn36 = turn().breakBalls(7).madeBalls(1, 10).win();
    /**
     * Game 11 (5-5)
     */
    // Hohmann scratches on the break
    private static final ITurn turn37 = turn().fouled().breakMiss();
    // Shane runs out to the 8 and misses
    private static final ITurn turn38 = turn().madeBalls(1, 2, 5, 3, 4, 6, 7).miss();
    // Hohmann
    private static final ITurn turn39 = turn().offTable(1, 2, 3, 4, 5, 6, 7).madeBalls(8, 9, 10).win();
    /**
     * Game 12 (6-5)
     */
    // Shane breaks and runs
    private static final ITurn turn40 = turn().breakBalls(8, 9).madeBalls(1, 2, 3, 4, 5, 6, 7, 10).win();
    /**
     * Game 13 (6-6)
     */
    // Hohmann breaks dry
    private static final ITurn turn41 = turn().breakMiss();
    // Shane plays safe
    private static final ITurn turn42 = turn().safety();
    // Hohmann scratches, not sure if it was a safety attempt or going for it
    private static final ITurn turn43 = turn().fouled().safetyMiss();
    // Shane plays like a crazy man and makes an awesome break out
    private static final ITurn turn44 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).win();
    /**
     * Game 14 (6-7)
     */
    // Shane breaks and has to push
    private static final ITurn turn45 = turn().breakBalls(3, 8).push();
    // Hohmann gives the shot back
    private static final ITurn turn46 = turn().offTable(3, 8).skipTurn();
    // Shane gets the shot back and almost kicks safe
    private static final ITurn turn47 = turn().offTable(3, 8).safetyMiss();
    // Hohmann gets a tough shot on the 1 and gets hook
    private static final ITurn turn48 = turn().offTable(3, 8).miss();
    // Shane calls the 1, probably playing safe
    private static final ITurn turn49 = turn().offTable(3, 8).safety();
    // Hohmann can barely see the 1 and plays a great safe
    private static final ITurn turn50 = turn().offTable(3, 8).safety();
    // Shane plays a pretty decent safe back
    private static final ITurn turn51 = turn().offTable(3, 8).safety();
    // Hohmann plays a good safe
    private static final ITurn turn52 = turn().offTable(3, 8).safety();
    // Shane gets a bit lucky
    private static final ITurn turn53 = turn().offTable(3, 8).safety();
    // Hohmann plays a safe
    private static final ITurn turn54 = turn().offTable(3, 8).safety();
    // Shane intentionally fouls
    private static final ITurn turn55 = turn().offTable(3, 8).fouled().safetyMiss();
    // Hohmann hooks him again
    private static final ITurn turn56 = turn().offTable(3, 8).safety();
    // Shane kicks and hits it but can't get safe
    private static final ITurn turn57 = turn().offTable(3, 8).safetyMiss();
    // Hohmann fouls on his pre-stroke??!?
    private static final ITurn turn58 = turn().offTable(3, 8).madeBalls(1).fouled().miss();
    // Shane runs out
    private static final ITurn turn59 = turn().offTable(3, 8, 1).madeBalls(2, 3, 4, 5, 6, 7, 9, 10).win();
    /**
     * Game 15 (6-8)
     */
    // Hohmann breaks and runs
    private static final ITurn turn60 = turn().breakBalls(7, 2).madeBalls(1, 3, 4, 5, 6, 8, 9, 10).win();
    /**
     * Game 16 (7-8)
     */
    // Shane breaks and runs to win the match
    private static final ITurn turn61 = turn().breakBalls(2).madeBalls(1, 3, 4, 5, 6, 7, 8, 9, 10).win();

    private static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_TEN_BALL);
    }

    public static List<ITurn> getTurns() {
        return Arrays.asList(
                turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8, turn9, turn10,
                turn11, turn12, turn13, turn14, turn15, turn16, turn17, turn18, turn19, turn20,
                turn21, turn22, turn23, turn24, turn25, turn26, turn27, turn28, turn29, turn30,
                turn31, turn32, turn33, turn34, turn35, turn36, turn37, turn38, turn39, turn40,
                turn41, turn42, turn43, turn44, turn45, turn46, turn47, turn48, turn49, turn50,
                turn51, turn52, turn53, turn54, turn55, turn56, turn57, turn58, turn59, turn60,
                turn61
        );
    }
}
