package com.brookmanholmes.billiards.player;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public abstract class AbstractPlayerTest<T extends AbstractPlayer> {
    T actual;
    T expected;
    String testName = "";

    @Before
    public abstract void setUp();

    @Test
    public void getNameReturnsName() {
        assertThat(actual.getName(), is(""));
    }

    @Test
    public void addSafetyAttemptAddsOneToTotal() {
        actual.addSafetyAttempt(false);

        expected.safetyAttempts++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addSafetyAttemptWithScratchAddsOneToTotalAndAddsAScratch() {
        actual.addSafetyAttempt(true);

        expected.safetyAttempts++;
        expected.safetyScratches++;

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
    public void addShootingBallsMadeAdds8BallsMadeWithScratch() {
        actual.addShootingBallsMade(8, true);

        expected.shootingBallsMade += 8;
        expected.shootingScratches++;
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
        expected.breakScratches++;

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
        actual.addSafety(false);

        expected.safetyAttempts++;
        expected.safetySuccesses++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addSafetyWithPreviousSafeAddsSuccessfulSafetyAndReturn() {
        actual.addSafety(true);

        expected.safetyAttempts++;
        expected.safetySuccesses++;
        expected.safetyReturns++;

        assertThat(actual, is(expected));
    }

    @Test
    public void checkGetWinPctTextIsCorrect() {
        assertThat(actual.getWinPct(), is(".000"));

        actual.addGameWon();

        assertThat(actual.getWinPct(), is("1.000"));

        actual.addGameLost();

        assertThat(actual.getWinPct(), is(".500"));
    }

    @Test
    public void checkGetAvgBallsTurnIsCorrect() {
        assertThat(actual.getAvgBallsTurn(), is("0"));

        actual.addShootingBallsMade(5, false);

        assertThat(actual.getAvgBallsTurn(), is("5"));
    }

    @Test
    public void checkGetAvgBreakBallsIsCorrect() {
        assertThat(actual.getAvgBallsBreak(), is("0"));

        actual.addBreakShot(5, false, true);

        assertThat(actual.getAvgBallsBreak(), is("0"));

        actual.addBreakShot(5, false, false);

        assertThat(actual.getAvgBallsBreak(), is("2.5"));
    }

    @Test
    public void checkGetSafetyPctTextIsCorrect() {
        assertThat(actual.getSafetyPct(), is(".000"));

        actual.addSafety(false);

        assertThat(actual.getSafetyPct(), is("1.000"));

        actual.addSafetyAttempt(false);

        assertThat(actual.getSafetyPct(), is(".500"));
    }

    @Test
    public void checkGetShootingPctTextIsCorrect() {
        assertThat(actual.getShootingPct(), is(".000"));

        actual.addShootingBallsMade(4, false);

        assertThat(actual.getShootingPct(), is("1.000"));

        actual.addShootingMiss();

        assertThat(actual.getShootingPct(), is(".800"));
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
        assertThat(actual.getAggressivenessRating(), is(".000"));

        actual.addShootingBallsMade(8, false);
        actual.addBreakShot(2, true, false);

        assertThat(actual.getAggressivenessRating(), is("1.000"));

        actual.addSafety(false);
        actual.addSafetyAttempt(true);

        assertThat(actual.getAggressivenessRating(), is(".800"));
    }

    @Test
    public void getTrueShootingPctRatingIsCorrect() {
        assertThat(actual.getTrueShootingPct(), is(".000"));

        actual.addShootingBallsMade(8, false);
        actual.addBreakShot(2, true, false);
        actual.addBreakShot(0, false, true);
        actual.addShootingMiss();
        actual.addSafety(false);
        actual.addSafetyAttempt(true);

        assertThat(actual.getTrueShootingPct(), is(".769"));
    }

}
