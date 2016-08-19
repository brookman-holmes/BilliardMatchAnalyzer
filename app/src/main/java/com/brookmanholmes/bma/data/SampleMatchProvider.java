package com.brookmanholmes.bma.data;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by helios on 8/19/2016.
 */
class SampleMatchProvider {
    public static List<Turn> getHohmannSvbTurns() {
        return HohmannSvbMatch.getTurns();
    }

    public static Match<?> getHohmannSvbMatch() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return new Match.Builder("Hohmann", "SVB")
                .setDate(cal.getTime())
                .setBreakType(BreakType.ALTERNATE)
                .setPlayerTurn(PlayerTurn.PLAYER)
                .build(GameType.BCA_TEN_BALL);
    }

    public static List<Turn> getShawRobertsTurns() {
        return ShawRobertsMatch.getTurns();
    }

    public static Match<?> getShawRobertsMatch() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JUNE);
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.DAY_OF_MONTH, 5);
        return new Match.Builder("Shaw", "Roberts")
                .setBreakType(BreakType.ALTERNATE)
                .setPlayerTurn(PlayerTurn.OPPONENT)
                .build(GameType.BCA_TEN_BALL);
    }

    private static class HohmannSvbMatch {
        /**
         * Game 1 (0-0)
         */
        // Hohmann breaks dry
        private static final Turn turn1 = turn().breakMiss();
        // SVB runs to the 9 and misses
        private static final Turn turn2 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8).miss();
        // Hohmann runs out
        private static final Turn turn3 = turn().madeBalls(9, 10).offTable(1, 2, 3, 4, 5, 6, 7, 8).win();
        /**
         * Game 2 (1-0) Hohmann
         */
        // Shane breaks dry
        private static final Turn turn4 = turn().breakMiss();
        // Hohmann makes a few balls and plays safe
        private static final Turn turn5 = turn().madeBalls(1, 2, 3, 4).safety();
        // Shane hits the ball but doesn't quite get safe
        private static final Turn turn6 = turn().offTable(1, 2, 3, 4).safetyMiss();
        // Hohmann makes the 8 and then plays safe
        private static final Turn turn7 = turn().offTable(1, 2, 3, 4).madeBalls(8).safety();
        // Shane scratches
        private static final Turn turn8 = turn().offTable(1, 2, 3, 4, 8).scratch().safetyMiss();
        // Hohmann runs out
        private static final Turn turn9 = turn().offTable(1, 2, 3, 4, 8).madeBalls(5, 6, 7, 9, 10).win();
        /**
         * Game 3 (2-0)
         */
        // Hohmann breaks and has to push
        private static final Turn turn10 = turn().breakBalls(3, 6, 9).push();
        // Shane doesn't play safe
        private static final Turn turn11 = turn().offTable(3, 6, 9).safetyMiss();
        // Hohmann runs out
        private static final Turn turn12 = turn().offTable(3, 6, 9).madeBalls(1, 2, 4, 5, 7, 8, 10).win();
        /**
         * Game 4 (3-0)
         */
        // Shane breaks dry
        private static final Turn turn13 = turn().breakMiss();
        // Hohmann pushes
        private static final Turn turn14 = turn().push();
        // Shane gives the shot back
        private static final Turn turn15 = turn().skipTurn();
        // Hohmann plays safe
        private static final Turn turn16 = turn().safety();
        // Shane plays safe back
        private static final Turn turn17 = turn().safety();
        // Hohmann kicks and doesn't get quite safe
        private static final Turn turn18 = turn().safetyMiss();
        // Shane runs out
        private static final Turn turn19 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).win();
        /**
         * Game 5 (3-1)
         */
        // Hohmann breaks makes a ball and misses
        private static final Turn turn20 = turn().breakBalls(3, 8, 9).madeBalls(1).miss();
        // Shane gets out of shape on the 5 and plays safe
        private static final Turn turn21 = turn().offTable(1, 3, 8, 9).madeBalls(2, 4).safety();
        // Hohmann misses the kick
        private static final Turn turn22 = turn().offTable(1, 2, 3, 4, 8, 9).miss();
        // Shane misses as well
        private static final Turn turn23 = turn().offTable(1, 2, 3, 4, 8, 9).miss();
        // Hohmann runs out
        private static final Turn turn24 = turn().offTable(1, 2, 3, 4, 8, 9).madeBalls(5, 6, 7, 10).win();
        /**
         * Game 6 (4-1)
         */
        // Shane breaks and misses the 3 ball
        private static final Turn turn25 = turn().breakBalls(1, 2).miss();
        // Hohmann comes to the table hooked and doesn't get his kick safe
        private static final Turn turn26 = turn().offTable(1, 2).safetyMiss();
        // Shane scratches on the 4... somehow
        private static final Turn turn27 = turn().offTable(1, 2).madeBalls(3).deadBalls(4).scratch().miss();
        // Hohmann runs out
        private static final Turn turn28 = turn().offTable(1, 2, 3, 4).madeBalls(5, 6, 7, 8, 9, 10).win();
        /**
         * Game 7 (5-1)
         */
        // Hohmann scratches on the break
        private static final Turn turn29 = turn().deadOnBreak(5).scratch().breakMiss();
        // Shane makes a few balls and then plays safe
        private static final Turn turn30 = turn().offTable(5).madeBalls(1, 2, 3).safety();
        // Hohmann jumps at the 4 and doesn't clear the blocker ball
        private static final Turn turn31 = turn().offTable(1, 2, 3, 5).scratch().miss();
        // Shane better fucking run out
        private static final Turn turn32 = turn().offTable(1, 2, 3, 5).madeBalls(4, 6, 7, 8, 9, 10).win();
        /**
         * Game 8 (5-2)
         */
        // Shane breaks and runs
        private static final Turn turn33 = turn().breakBalls(9).madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 10).win();
        /**
         * Game 9 (5-3)
         */
        // Hohmann breaks dry
        private static final Turn turn34 = turn().breakMiss();
        // Shane runs the table
        private static final Turn turn35 = turn().madeBalls(7, 1, 2, 3, 4, 5, 6, 8, 9, 10).win();
        /**
         * Game 10 (5-4)
         */
        // Shane breaks and gets an early 10
        private static final Turn turn36 = turn().breakBalls(7).madeBalls(1, 10).win();
        /**
         * Game 11 (5-5)
         */
        // Hohmann scratches on the break
        private static final Turn turn37 = turn().scratch().breakMiss();
        // Shane runs out to the 8 and misses
        private static final Turn turn38 = turn().madeBalls(1, 2, 5, 3, 4, 6, 7).miss();
        // Hohmann
        private static final Turn turn39 = turn().offTable(1, 2, 3, 4, 5, 6, 7).madeBalls(8, 9, 10).win();
        /**
         * Game 12 (6-5)
         */
        // Shane breaks and runs
        private static final Turn turn40 = turn().breakBalls(8, 9).madeBalls(1, 2, 3, 4, 5, 6, 7, 10).win();
        /**
         * Game 13 (6-6)
         */
        // Hohmann breaks dry
        private static final Turn turn41 = turn().breakMiss();
        // Shane plays safe
        private static final Turn turn42 = turn().safety();
        // Hohmann scratches, not sure if it was a safety attempt or going for it
        private static final Turn turn43 = turn().scratch().safetyMiss();
        // Shane plays like a crazy man and makes an awesome break out
        private static final Turn turn44 = turn().madeBalls(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).win();
        /**
         * Game 14 (6-7)
         */
        // Shane breaks and has to push
        private static final Turn turn45 = turn().breakBalls(3, 8).push();
        // Hohmann gives the shot back
        private static final Turn turn46 = turn().offTable(3, 8).skipTurn();
        // Shane gets the shot back and almost kicks safe
        private static final Turn turn47 = turn().offTable(3, 8).safetyMiss();
        // Hohmann gets a tough shot on the 1 and gets hook
        private static final Turn turn48 = turn().offTable(3, 8).miss();
        // Shane calls the 1, probably playing safe
        private static final Turn turn49 = turn().offTable(3, 8).safety();
        // Hohmann can barely see the 1 and plays a great safe
        private static final Turn turn50 = turn().offTable(3, 8).safety();
        // Shane plays a pretty decent safe back
        private static final Turn turn51 = turn().offTable(3, 8).safety();
        // Hohmann plays a good safe
        private static final Turn turn52 = turn().offTable(3, 8).safety();
        // Shane gets a bit lucky
        private static final Turn turn53 = turn().offTable(3, 8).safety();
        // Hohmann plays a safe
        private static final Turn turn54 = turn().offTable(3, 8).safety();
        // Shane intentionally fouls
        private static final Turn turn55 = turn().offTable(3, 8).scratch().safetyMiss();
        // Hohmann hooks him again
        private static final Turn turn56 = turn().offTable(3, 8).safety();
        // Shane kicks and hits it but can't get safe
        private static final Turn turn57 = turn().offTable(3, 8).safetyMiss();
        // Hohmann fouls on his pre-stroke??!?
        private static final Turn turn58 = turn().offTable(3, 8).madeBalls(1).scratch().miss();
        // Shane runs out
        private static final Turn turn59 = turn().offTable(3, 8, 1).madeBalls(2, 3, 4, 5, 6, 7, 9, 10).win();
        /**
         * Game 15 (6-8)
         */
        // Hohmann breaks and runs
        private static final Turn turn60 = turn().breakBalls(7, 2).madeBalls(1, 3, 4, 5, 6, 8, 9, 10).win();
        /**
         * Game 16 (7-8)
         */
        // Shane breaks and runs to win the match
        private static final Turn turn61 = turn().breakBalls(2).madeBalls(1, 3, 4, 5, 6, 7, 8, 9, 10).win();

        private static TurnBuilder turn() {
            return new TurnBuilder(GameType.BCA_TEN_BALL);
        }

        private static List<Turn> getTurns() {
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

        private static List<Turn> getTurns() {
            return Arrays.asList(
                    turn1, turn2, turn3, turn4, turn5, turn6, turn7, turn8, turn9,
                    turn10, turn11, turn12, turn13, turn14, turn15, turn16, turn17,
                    turn18, turn19, turn20, turn21, turn22, turn23, turn24, turn25,
                    turn26, turn27, turn28, turn29, turn30, turn31, turn32, turn33
            );
        }
    }
}
