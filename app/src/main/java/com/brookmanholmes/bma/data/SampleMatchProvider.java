package com.brookmanholmes.bma.data;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by helios on 8/19/2016.
 */
public class SampleMatchProvider {
    public static List<ITurn> getHohmannSvbTurns() {
        return HohmannSvbMatch.getTurns();
    }

    public static Match getHohmannSvbMatch() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return new Match.Builder("Hohmann", "SVB")
                .setDate(cal.getTime())
                .setBreakType(BreakType.ALTERNATE)
                .setPlayerTurn(PlayerTurn.PLAYER)
                .setDate(cal.getTime())
                .setPlayerRanks(9, 9)
                .build(GameType.BCA_TEN_BALL);
    }

    public static List<ITurn> getShawRobertsTurns() {
        return ShawRobertsMatch.getTurns();
    }

    public static Match getShawRobertsMatch() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JUNE);
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.DAY_OF_MONTH, 5);
        return new Match.Builder("Shaw", "Roberts")
                .setBreakType(BreakType.ALTERNATE)
                .setPlayerTurn(PlayerTurn.OPPONENT)
                .setDate(cal.getTime())
                .setPlayerRanks(7, 7)
                .build(GameType.BCA_TEN_BALL);
    }

    public static Match getTestEightBallMatch() {
        return new Match.Builder("Test 1", "Test 2")
                .setPlayerTurn(PlayerTurn.PLAYER)
                .setDate(new Date())
                .build(GameType.BCA_EIGHT_BALL);
    }

    public static List<ITurn> getTestEightBallTurns() {
        return EightBallTestMatch.getTurns();
    }

    public static Match getEberleCoatesMatch() {
        return new Match.Builder("Max", "Jeff")
                .setPlayerTurn(PlayerTurn.PLAYER)
                .setBreakType(BreakType.ALTERNATE)
                .setPlayerRanks(9, 7)
                .build(GameType.BCA_NINE_BALL);
    }

    public static List<ITurn> getEberleCoatesTurns() {
        return EberleCoatesMatch.getTurns();
    }

    private static class HohmannSvbMatch {
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

        private static List<ITurn> getTurns() {
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

    private static class ShawRobertsMatch {
        /**
         * GAME 1
         */
        // roberts breaks and runs the first rack
        private static final ITurn turn1 = turn().madeBalls(2, 3, 4, 5, 8, 9, 10).breakBalls(1, 6, 7).win();
        /**
         * GAME 2
         */
        // Shaw breaks dry
        private static final ITurn turn2 = turn().breakMiss();
        // Roberts pushes and makes the 7 and 9
        private static final ITurn turn3 = turn().deadBalls(7, 9).push();
        // Shaw plays a bad safe
        private static final ITurn turn4 = turn().offTable(7, 9).safetyMiss();
        // Roberts fails to run out
        private static final ITurn turn5 = turn().offTable(7, 9).madeBalls(1, 2, 3, 8).miss();
        // Shaw scratches on the 5
        private static final ITurn turn6 = turn().offTable(1, 2, 3, 8, 7, 9).madeBalls(4).deadBalls(5).fouled().miss();
        // Roberts wins
        private static final ITurn turn7 = turn().offTable(1, 2, 3, 8, 7, 9, 4, 5).madeBalls(6, 10).win();
        /**
         * GAME 3
         */
        // Roberts scratches on the break
        private static final ITurn turn8 = turn().deadOnBreak(6).fouled().breakMiss();
        // Shaw runs out
        private static final ITurn turn9 = turn().offTable(6).madeBalls(1, 2, 3, 4, 5, 7, 8, 9, 10).win();
        /**
         * GAME 4
         */
        // Shaw breaks and runs
        private static final ITurn turn10 = turn().breakBalls(3, 7).madeBalls(1, 2, 4, 5, 6, 8, 9, 10).win();
        /**
         * GAME 5
         */
        // Roberts breaks and runs
        private static final ITurn turn11 = turn().breakBalls(6, 9).madeBalls(1, 2, 3, 4, 5, 7, 8, 10).win();
        /**
         * GAME 6
         */
        // Shaw gets out of shape and plays safe
        private static final ITurn turn12 = turn().breakBalls(2).madeBalls(1, 3).safety();
        // Roberts misses his safety attempt
        private static final ITurn turn13 = turn().offTable(1, 2, 3).safetyMiss();
        // Shaw runs out
        private static final ITurn turn14 = turn().offTable(1, 2, 3).madeBalls(4, 5, 6, 7, 8, 9, 10).win();
        /**
         * GAME 7
         */
        // Roberts breaks and pushes out
        private static final ITurn turn15 = turn().breakBalls(1, 4, 6).push();
        // Shaw gives the shot back
        private static final ITurn turn16 = turn().skipTurn();
        // Roberts misses
        private static final ITurn turn17 = turn().fouled().offTable(1, 4, 6).miss();
        // Shaw locks him up
        private static final ITurn turn18 = turn().offTable(1, 4, 6).safety();
        // Roberts misses his safety attempt
        private static final ITurn turn19 = turn().deadBalls(2).offTable(1, 4, 6).fouled().safetyMiss();
        // Shaw runs out
        private static final ITurn turn20 = turn().offTable(1, 4, 6, 2).madeBalls(3, 5, 7, 8, 9, 10).win();
        /**
         * GAME 8
         */
        // Shaw breaks gets out of shape and plays a two way
        private static final ITurn turn21 = turn().breakBalls(2).madeBalls(1).safety();
        // Roberts misses his jump and makes the 10 by accident
        private static final ITurn turn22 = turn().offTable(1, 2).deadBalls(10).miss();
        // Shaw misses his safety attempt
        private static final ITurn turn23 = turn().offTable(1, 2).safetyMiss();
        // Roberts misses
        private static final ITurn turn24 = turn().offTable(1, 2).miss();
        // Shaw has to kick at the 3 and then runs out
        private static final ITurn turn25 = turn().offTable(1, 2).madeBalls(3, 4, 5, 6, 7, 8, 9, 10).win();
        /**
         * GAME 9
         */
        // Roberts break and misses
        private static final ITurn turn26 = turn().breakBalls(3, 7).miss();
        // Shaw runs out
        private static final ITurn turn27 = turn().offTable(3, 7).madeBalls(1, 2, 4, 5, 6, 8, 9, 10).win();
        /**
         * GAME 10
         */
        // Shaw breaks and hangs the 10 and doesn't make a ball
        private static final ITurn turn28 = turn().breakMiss();
        // Shaw makes the 10
        private static final ITurn turn29 = turn().madeBalls(10).win();
        /**
         * GAME 11
         */
        // Roberts breaks
        private static final ITurn turn30 = turn().breakBalls(7).push();
        // Shaw gives the shot back
        private static final ITurn turn31 = turn().offTable(7).skipTurn();
        // Roberts misses
        private static final ITurn turn32 = turn().offTable(7).miss();
        // Shaw runs out
        private static final ITurn turn33 = turn().offTable(7).madeBalls(1, 2, 3, 4, 5, 6, 8, 9, 10).win();

        private static TurnBuilder turn() {
            return new TurnBuilder(GameType.BCA_TEN_BALL);
        }

        private static List<ITurn> getTurns() {
            return Arrays.asList(
                    turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8, turn9,
                    turn10, turn11, turn12, turn13, turn14, turn15, turn16, turn17,
                    turn18, turn19, turn20, turn21, turn22, turn23, turn24, turn25,
                    turn26, turn27, turn28, turn29, turn30, turn31, turn32, turn33
            );
        }
    }

    private static class EightBallTestMatch {
        /**
         * Game 1
         */
        // player break and run
        private static final ITurn turn1 = turn().breakBalls(10, 6).madeBalls(9, 11, 12, 13, 14, 15, 8).win();
        /**
         * Game 2
         */
        // opponent claims stripes
        private static final ITurn turn2 = turn().breakBalls(4).madeBalls(9).miss();
        // player makes all but the 8 ball
        private static final ITurn turn3 = turn().offTable(4, 9).madeBalls(1, 2, 3, 5, 6).deadBalls(7).miss();
        // opponent makes the 8 early
        private static final ITurn turn4 = turn().deadBalls(8).seriousFoul().miss();
        /**
         * Game 3
         */
        // player breaks and makes the 8
        private static final ITurn turn5 = turn().breakBalls(8).madeBalls(1, 2).miss();
        // opponent runs out
        private static final ITurn turn6 = turn().offTable(1, 2).madeBalls(9, 10, 11, 12, 13, 14, 15, 8).win();
        /**
         * Game 4
         */
        // opponent breaks and scratches and makes the 8
        private static final ITurn turn7 = turn().deadOnBreak(8).fouled().breakMiss();
        // player chooses to break
        private static final ITurn turn8 = turn().currentPlayerBreaks();
        // player breaks and makes a few balls
        private static final ITurn turn9 = turn().breakBalls(1, 7).madeBalls(9, 10, 11).miss();
        // opponent runs out
        private static final ITurn turn10 = turn().offTable(1, 7, 9, 10, 11).madeBalls(2, 3, 4, 5, 6, 8).win();
        /**
         * Game 5
         */
        // player breaks and makes the 8 ball MIGHT BE A BUG HERE WITH 8 BALL BEING MADE AGAIN WITH THE TESTS
        private static final ITurn turn11 = turn().gameBallMadeOnBreakAndThenMade().madeBalls(9, 10, 11, 12, 13, 14, 15).win();
        /**
         * Game 6
         */
        // opponent breaks and plays safe
        private static final ITurn turn12 = turn().breakBalls(10).madeBalls(1).safety();
        // player plays safe back
        private static final ITurn turn13 = turn().safety();
        // opponent runs a the rest of the table
        private static final ITurn turn14 = turn().offTable(1, 10).madeBalls(2, 3, 4, 5, 6, 7, 8).win();
        /**
         * Game 7
         */
        // player scratches and makes the 8 on the break
        private static final ITurn turn15 = turn().fouled().deadOnBreak(8).breakMiss();
        // opponent lets player break
        private static final ITurn turn16 = turn().opposingPlayerBreaks();
        // player breaks and makes a few balls, including the 8 and wants to rebreak
        private static final ITurn turn17 = turn().breakBalls(1, 2, 8).currentPlayerBreaks();
        // player breaks and runs the table
        private static final ITurn turn18 = turn().breakBalls(1).madeBalls(2, 3, 4, 5, 6, 7, 8).win();

        private static TurnBuilder turn() {
            return new TurnBuilder(GameType.BCA_EIGHT_BALL);
        }

        public static List<ITurn> getTurns() {
            return Arrays.asList(
                    turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8, turn9, turn10,
                    turn11, turn12, turn13, turn14, turn15, turn16, turn17, turn18
            );
        }
    }

    private static class EberleCoatesMatch {
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
}
