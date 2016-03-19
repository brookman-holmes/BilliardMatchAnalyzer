package com.brookmanholmes.billiards.acceptance.hohmannsvb;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.GameType;
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
    static Turn turn1 = turn().breakMiss();
    // SVB runs to the 9 and misses
    static Turn turn2 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8).miss();
    // Hohmann runs out
    static Turn turn3 = turn().madeBalls(9, 10).offTable(1, 2, 3, 4, 5, 6, 7, 8).win();
    /**
     * Game 2 (1-0) Hohmann
     */
    // Shane breaks dry
    static Turn turn4 = turn().breakMiss();
    // Hohmann makes a few balls and plays safe
    static Turn turn5 = turn().madeBalls(1, 2, 3, 4).safety();
    // Shane hits the ball but doesn't quite get safe
    static Turn turn6 = turn().offTable(1, 2, 3, 4).safetyMiss();
    // Hohmann makes the 8 and then plays safe
    static Turn turn7 = turn().offTable(1, 2, 3, 4).madeBalls(8).safety();
    // Shane scratches
    static Turn turn8 = turn().offTable(1, 2, 3, 4, 8).scratch().safetyMiss();
    // Hohmann runs out
    static Turn turn9 = turn().offTable(1, 2, 3, 4, 8).madeBalls(5, 6, 7, 9, 10).win();
    /**
     * Game 3 (2-0)
     */
    // Hohmann breaks and has to push
    static Turn turn10 = turn().breakBalls(3, 6, 9).push();
    // Shane doesn't play safe
    static Turn turn11 = turn().offTable(3, 6, 9).safetyMiss();
    // Hohmann runs out
    static Turn turn12 = turn().offTable(3, 6, 9).madeBalls(1, 2, 4, 5, 7, 8, 10).win();
    /**
     * Game 4 (3-0)
     */
    // Shane breaks dry
    static Turn turn13 = turn().breakMiss();
    // Hohmann pushes
    static Turn turn14 = turn().push();
    // Shane gives the shot back
    static Turn turn15 = turn().skipTurn();
    // Hohmann plays safe
    static Turn turn16 = turn().safety();
    // Shane plays safe back
    static Turn turn17 = turn().safety();
    // Hohmann kicks and doesn't get quite safe
    static Turn turn18 = turn().safetyMiss();
    // Shane runs out
    static Turn turn19 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).win();
    /**
     * Game 5 (3-1)
     */
    // Hohmann breaks makes a ball and misses
    static Turn turn20 = turn().breakBalls(3, 8, 9).madeBalls(1).miss();
    // Shane gets out of shape on the 5 and plays safe
    static Turn turn21 = turn().offTable(1, 3, 8, 9).madeBalls(2, 4).safety();
    // Hohmann misses the kick
    static Turn turn22 = turn().offTable(1, 2, 3, 4, 8, 9).miss();
    // Shane misses as well
    static Turn turn23 = turn().offTable(1, 2, 3, 4, 8, 9).miss();
    // Hohmann runs out
    static Turn turn24 = turn().offTable(1, 2, 3, 4, 8, 9).madeBalls(5, 6, 7, 10).win();
    /**
     * Game 6 (4-1)
     */
    // Shane breaks and misses the 3 ball
    static Turn turn25 = turn().breakBalls(1, 2).miss();
    // Hohmann comes to the table hooked and doesn't get his kick safe
    static Turn turn26 = turn().offTable(1, 2).safetyMiss();
    // Shane scratches on the 4... somehow
    static Turn turn27 = turn().offTable(1, 2).madeBalls(3).deadBalls(4).scratch().miss();
    // Hohmann runs out
    static Turn turn28 = turn().offTable(1, 2, 3, 4).madeBalls(5, 6, 7, 8, 9, 10).win();
    /**
     * Game 7 (5-1)
     */
    // Hohmann scratches on the break
    static Turn turn29 = turn().deadOnBreak(5).scratch().breakMiss();
    // Shane makes a few balls and then plays safe
    static Turn turn30 = turn().offTable(5).madeBalls(1, 2, 3).safety();
    // Hohmann jumps at the 4 and doesn't clear the blocker ball
    static Turn turn31 = turn().offTable(1, 2, 3, 5).scratch().miss();
    // Shane better fucking run out
    static Turn turn32 = turn().offTable(1, 2, 3, 5).madeBalls(4, 6, 7, 8, 9, 10).win();
    /**
     * Game 8 (5-2)
     */
    // Shane breaks and runs
    static Turn turn33 = turn().breakBalls(9).madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 10).win();
    /**
     * Game 9 (5-3)
     */
    // Hohmann breaks dry
    static Turn turn34 = turn().breakMiss();
    // Shane runs the table
    static Turn turn35 = turn().madeBalls(7, 1, 2, 3, 4, 5, 6, 8, 9, 10).win();
    /**
     * Game 10 (5-4)
     */
    // Shane breaks and gets an early 10
    static Turn turn36 = turn().breakBalls(7).madeBalls(1, 10).win();
    /**
     * Game 11 (5-5)
     */
    // Hohmann scratches on the break
    static Turn turn37 = turn().scratch().breakMiss();
    // Shane runs out to the 8 and misses
    static Turn turn38 = turn().madeBalls(1, 2, 5, 3, 4, 6, 7).miss();
    // Hohmann
    static Turn turn39 = turn().offTable(1, 2, 3, 4, 5, 6, 7).madeBalls(8, 9, 10).win();
    /**
     * Game 12 (6-5)
     */
    // Shane breaks and runs
    static Turn turn40 = turn().breakBalls(8, 9).madeBalls(1, 2, 3, 4, 5, 6, 7, 10).win();
    /**
     * Game 13 (6-6)
     */
    // Hohmann breaks dry
    static Turn turn41 = turn().breakMiss();
    // Shane plays safe
    static Turn turn42 = turn().safety();
    // Hohmann scratches, not sure if it was a safety attempt or going for it
    static Turn turn43 = turn().scratch().safetyMiss();
    // Shane plays like a crazy man and makes an awesome break out
    static Turn turn44 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).win();
    /**
     * Game 14 (6-7)
     */
    // Shane breaks and has to push
    static Turn turn45 = turn().breakBalls(3, 8).push();
    // Hohmann gives the shot back
    static Turn turn46 = turn().offTable(3, 8).skipTurn();
    // Shane gets the shot back and almost kicks safe
    static Turn turn47 = turn().offTable(3, 8).safetyMiss();
    // Hohmann gets a tough shot on the 1 and gets hook
    static Turn turn48 = turn().offTable(3, 8).miss();
    // Shane calls the 1, probably playing safe
    static Turn turn49 = turn().offTable(3, 8).safety();
    // Hohmann can barely see the 1 and plays a great safe
    static Turn turn50 = turn().offTable(3, 8).safety();
    // Shane plays a pretty decent safe back
    static Turn turn51 = turn().offTable(3, 8).safety();
    // Hohmann plays a good safe
    static Turn turn52 = turn().offTable(3, 8).safety();
    // Shane gets a bit lucky
    static Turn turn53 = turn().offTable(3, 8).safety();
    // Hohmann plays a safe
    static Turn turn54 = turn().offTable(3, 8).safety();
    // Shane intentionally fouls
    static Turn turn55 = turn().offTable(3, 8).scratch().safetyMiss();
    // Hohmann hooks him again
    static Turn turn56 = turn().offTable(3, 8).safety();
    // Shane kicks and hits it but can't get safe
    static Turn turn57 = turn().offTable(3, 8).safetyMiss();
    // Hohmann fouls on his pre-stroke??!?
    static Turn turn58 = turn().offTable(3, 8).madeBalls(1).scratch().miss();
    // Shane runs out
    static Turn turn59 = turn().offTable(3, 8, 1).madeBalls(2, 3, 4, 5, 6, 7, 9, 10).win();
    /**
     * Game 15 (6-8)
     */
    // Hohmann breaks and runs
    static Turn turn60 = turn().breakBalls(7, 2).madeBalls(1, 3, 4, 5, 6, 8, 9, 10).win();
    /**
     * Game 16 (7-8)
     */
    // Shane breaks and runs to win the match
    static Turn turn61 = turn().breakBalls(2).madeBalls(1, 3, 4, 5, 6, 7, 8, 9, 10).win();

    static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_TEN_BALL);
    }

    public static List<Turn> getTurns() {
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
