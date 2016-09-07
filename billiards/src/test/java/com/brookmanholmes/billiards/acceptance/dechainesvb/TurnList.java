package com.brookmanholmes.billiards.acceptance.dechainesvb;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

/**
 * Created by helios on 9/7/2016.
 * 2015 Tiger challenge, SVB vs Mike Dechaine
 */

public class TurnList {
    private static TurnBuilder turn() {
        return new TurnBuilder(GameType.BCA_TEN_BALL);
    }

    /*
    Game 1
     */
    // shane breaks and plays a 2 way
    private static final ITurn turn1 = turn().breakBalls(7,8).safety();
    // mike kicks and puts the CB in a pocket
    private static final ITurn turn2 = turn().offTable(7,8).fouled().safetyMiss();
    // shane runs out
    private static final ITurn turn3 = turn().offTable(7,8).madeBalls(1,2,3,4,5,6,9,10).win();

    /*
    Game 2
     */
    // Mike breaks and runs
    private static final ITurn turn4 = turn().breakBalls(4,5).madeBalls(1,2,3,6,8,9,10).win();

    /*
    Game 3
     */
    // Shane breaks and misses his first shot
    private static final ITurn turn5 = turn().breakBalls(1).miss();
    // Mike runs out
    private static final ITurn turn6 = turn().offTable(1).madeBalls(2,3,4,5,6,7,8,9,10).win();

    /*
    Game 4
     */
    // Mike breaks and runs
    private static final ITurn turn7 = turn().breakBalls(1,4).madeBalls(2,6,3,5,7,8,9,10).win();

    /*
    Game 5
     */
    // Shane breaks and runs
    private static final ITurn turn8 = turn().breakBalls(6).madeBalls(1,2,3,4,7,5,8,9,10).win();

    /*
    Game 6
     */
    // Mike breaks is left funky and gets caromed into the side pocket after making the ball
    private static final ITurn turn9 = turn().breakBalls(4,6).deadBalls(1).fouled().miss();
    // shane runs out
    private static final ITurn turn10 = turn().offTable(1,4,6).madeBalls(2,3,5,7,8,9,10).win();

    /*
    Game 7
     */
    // Shane breaks dry
    private static final ITurn turn11 = turn().breakMiss();
    // Mike gets funky on the 2 and tries to play safe
    private static final ITurn turn12 = turn().madeBalls(1).safetyMiss();
    // Shane runs out
    private static final ITurn turn13 = turn().offTable(1).madeBalls(2,3,4,5,6,7,8,9,10).win();

    /*
    Game 8
     */
    // Mike breaks and makes FIVE balls and then runs out
    private static final ITurn turn14 = turn().breakBalls(1,2,3,4,6).madeBalls(5,7,8,9,10).win();

    /*
    Game 9
     */
    // Shane breaks dry
    private static final ITurn turn15 = turn().breakMiss();
    // Mike misses his safe and accidentally makes the 3
    private static final ITurn turn16 = turn().madeBalls(1,2).deadBalls(3).safetyMiss();
    // shane gives the shot back
    private static final ITurn turn17 = turn().offTable(1,2,3).skipTurn();
    // Mike plays safe?
    private static final ITurn turn18 = turn().offTable(1,2,3).safetyMiss();
    // Shane fouls
    private static final ITurn turn19 = turn().offTable(1,2,3).deadBalls(4).fouled().miss();
    // Mike runs out
    private static final ITurn turn20 = turn().offTable(1,2,3,4).madeBalls(5,6,7,8,9,10).win();

    /*
    Game 10
     */
    // Mike breaks dry
    private static final ITurn turn21 = turn().breakMiss();
    // Shane is left hooked and jumps the 1 in and runs out
    private static final ITurn turn22 = turn().madeBalls(1,2,3,4,5,6,7,8,9,10).win();

    /*
    Game 11
     */
    // Shane breaks and runs
    private static final ITurn turn23 = turn().breakBalls(3).madeBalls(1,2,4,5,6,7,8,9,10).win();

    /*
    Game 12
     */
    // Mike breaks dry
    private static final ITurn turn24 = turn().breakMiss();
    // Shane plays a 2 way
    private static final ITurn turn25 = turn().safety();
    // Mike hits the ball but doesn't get safe
    private static final ITurn turn26 = turn().safetyMiss();
    // Shane runs out
    private static final ITurn turn27 = turn().madeBalls(1,2,3,4,5,6,7,8,9,10).win();

    /*
    Game 13
     */
    // Shane breaks has a tough shot and misses (might have played safe?)
    private static final ITurn turn28 = turn().breakBalls(8).miss();
    // Mike accidentally makes the 6
    private static final ITurn turn29 = turn().offTable(8).deadBalls(6).safetyMiss();
    // Shane gives the shot back
    private static final ITurn turn30 = turn().offTable(6,8).skipTurn();
    // Mike jumps and fouls
    private static final ITurn turn31 = turn().offTable(6,8).fouled().miss();
    // Shane runs out
    private static final ITurn turn32 = turn().offTable(6,8).madeBalls(1,2,3,4,5,7,9,10).win();

    /*
    Game 14
     */
    // Mike breaks and pushes
    private static final ITurn turn33 = turn().breakBalls(4).push();
    // Shane hangs the 1
    private static final ITurn turn34 = turn().miss();
    // stopped at 52 minutes in
    private static final ITurn turn35 = turn().miss();
    private static final ITurn turn36 = turn().miss();
    private static final ITurn turn37 = turn().miss();
    private static final ITurn turn38 = turn().miss();
    private static final ITurn turn39 = turn().miss();
    private static final ITurn turn40 = turn().miss();
    private static final ITurn turn41 = turn().miss();
    private static final ITurn turn42 = turn().miss();
}
