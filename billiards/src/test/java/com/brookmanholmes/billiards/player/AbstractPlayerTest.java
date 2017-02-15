package com.brookmanholmes.billiards.player;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
@SuppressWarnings("unused")
public abstract class AbstractPlayerTest {
    final String testName = "";
    Player actual;
    Player expected;
    int rank;

    @Before
    public abstract void setUp();

    @Test
    public void getNameReturnsName() {
        assertThat(actual.getName(), is(""));
    }

    @Test
    public void getRankReturnsRank() {
        assertThat(actual.getRank(), is(rank));
    }

    @Test
    public void addSafetyAttemptAddsOneToTotal() {
        actual.addSafetyAttempt(false);

        expected.safetyAttempts++;

        assertThat(actual, is(expected));
    }

    @Test
    public void dateIsNullGeneratesNewDate() {
        Date date = null;
        assertThat(actual.date, is(date));
    }

    @Test
    public void dateEqualsSomeDate() {
        Date date = new Date();
        actual.setMatchDate(date);

        assertThat(actual.getMatchDate(), is(date));
    }

    @Test
    public void getTotalFoulsReturns0() {
        assertThat(actual.getTotalFouls(), is(0));
    }

    @Test
    public void getTotalFoulsReturnsValues() {
        actual.breakFouls = 1;
        actual.safetyFouls = 2;
        actual.shootingFouls = 3;
        assertThat(actual.getTotalFouls(), is(6));
    }

    @Test
    public void addSafetyAttemptWithFoulAddsOneToTotalAndAddsAFoul() {
        actual.addSafetyAttempt(true);

        expected.safetyAttempts++;
        expected.safetyFouls++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addShootingBallsMadeAdds8BallsMade() {
        actual.addShootingBallsMade(8, false);

        expected.shootingBallsMade += 8;
        expected.shootingTurns++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addShootingBallsMadeAdds8BallsMadeWithFoul() {
        actual.addShootingBallsMade(8, true);

        expected.shootingBallsMade += 8;
        expected.shootingFouls++;
        expected.shootingTurns++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addShootingMissAddsOnlyOneMiss() {
        actual.addShootingMiss();

        expected.shootingMisses++;

        assertThat(actual, is(expected));
    }

    @Test
    public void getShootingAttemptsReturns9() {
        actual.addShootingBallsMade(8, false);
        actual.addShootingMiss();

        assertThat(actual.getShootingAttempts(), is(9));
    }

    @Test
    public void addShootingTurnAddsOnlyOneTurn() {
        actual.addShootingBallsMade(0, false);

        expected.shootingTurns++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addBreakShotWithoutAnyBalls() {
        actual.addBreakShot(0, false, false);

        expected.breakAttempts++;

        assertThat(actual, is(expected));

        // now add in a break foul
        actual.addBreakShot(0, false, true);

        expected.breakAttempts++;
        expected.breakFouls++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addBreakShotWithBallsMadeButNoContinuation() {
        actual.addBreakShot(2, false, false);

        expected.breakAttempts++;
        expected.breakSuccesses++;
        expected.breakBallsMade += 2;

        assertThat(actual, is(expected));
    }

    @Test
    public void addBreakShotWithBallsMadeWithContinuation() {
        actual.addBreakShot(2, true, false);

        expected.breakAttempts++;
        expected.breakSuccesses++;
        expected.breakContinuations++;
        expected.breakBallsMade += 2;

        assertThat(actual, is(expected));
    }

    @Test
    public void addGameWonAddsOneToTotalAndWins() {
        actual.addGameWon();

        expected.gameTotal++;
        expected.gameWins++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addGameLostAddsOnToTotal() {
        actual.addGameLost();

        expected.gameTotal++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addSafetyWithNoPreviousSafeAddsSuccessfulSafety() {
        actual.addSafety(false, 0);

        expected.safetyAttempts++;
        expected.safetySuccesses++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addSafetyWithPreviousSafeAddsSuccessfulSafetyAndReturn() {
        actual.addSafety(true, 0);

        expected.safetyAttempts++;
        expected.safetySuccesses++;
        expected.safetyReturns++;

        assertThat(actual, is(expected));
    }

    @Test
    public void checkGetWinPctTextIsCorrect() {
        assertThat(actual.getWinPct(), is(0d));

        actual.addGameWon();

        assertThat(actual.getWinPct(), is(1d));

        actual.addGameLost();

        assertThat(actual.getWinPct(), is(1d / 2d));
    }

    @Test
    public void checkGetAvgBallsTurnIsCorrect() {
        assertThat(actual.getAvgBallsTurn(), is(0d));

        actual.addShootingBallsMade(5, false);

        assertThat(actual.getAvgBallsTurn(), is(5d));
    }

    @Test
    public void checkGetAvgBreakBallsIsCorrect() {
        assertThat(actual.getAvgBallsBreak(), is(0d));

        actual.addBreakShot(5, false, true);

        assertThat(actual.getAvgBallsBreak(), is(0d));

        actual.addBreakShot(5, false, false);

        assertThat(actual.getAvgBallsBreak(), is(2.5d));
    }

    @Test
    public void checkGetSafetyPctTextIsCorrect() {
        assertThat(actual.getSafetyPct(), is(0d));

        actual.addSafety(false, 0);

        assertThat(actual.getSafetyPct(), is(1d));

        actual.addSafetyAttempt(false);

        assertThat(actual.getSafetyPct(), is(1d / 2d));
    }

    @Test
    public void checkGetBreakPctTextIsCorrect() {
        assertThat(actual.getBreakPct(), is(0d));

        actual.addBreakShot(1, false, false);

        assertThat(actual.getBreakPct(), is(1d));

        actual.addBreakShot(0, false, false);

        assertThat(actual.getBreakPct(), is(1d / 2d));
    }

    @Test
    public void checkGetShootingPctTextIsCorrect() {
        assertThat(actual.getShootingPct(), is(0d));

        actual.addShootingBallsMade(4, false);

        assertThat(actual.getShootingPct(), is(1d));

        actual.addShootingMiss();

        assertThat(actual.getShootingPct(), is(4d / 5d));
    }

    @Test
    public void getTotalShotsAttemptedReturns0() {
        assertThat(actual.getShotAttemptsOfAllTypes(), is(0));
    }

    @Test
    public void getTotalShotsAttemptedReturns10() {
        actual.addBreakShot(3, false, false);
        actual.addShootingBallsMade(8, false);
        actual.addSafetyAttempt(true);

        assertThat(actual.getShotAttemptsOfAllTypes(), is(10));


        assertThat(actual.getShotsSucceededOfAllTypes(), is(9));
    }

    @Test
    public void getAggressivenessRatingIsCorrect() {
        assertThat(actual.getAggressivenessRating(), is(0d));

        actual.addShootingBallsMade(8, false);
        actual.addBreakShot(2, true, false);

        assertThat(actual.getAggressivenessRating(), is(1d));

        actual.addSafety(false, 0);
        actual.addSafetyAttempt(true);

        assertThat(actual.getAggressivenessRating(), is(4d / 5d));
    }

    @Test
    public void getTrueShootingPctRatingIsCorrect() {
        assertThat(actual.getTrueShootingPct(), is(0d));

        actual.addShootingBallsMade(8, false);
        actual.addBreakShot(2, true, false);
        actual.addBreakShot(0, false, true);
        actual.addShootingMiss();
        actual.addSafety(false, 0);
        actual.addSafetyAttempt(true);

        assertThat(actual.getTrueShootingPct(), is(10d / 13d));
    }

}
