package com.brookmanholmes.billiards.player;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public abstract class AbstractPlayerTest {
    AbstractPlayer actual;
    AbstractPlayer expected;
    String testName = "";

    @Before
    public abstract void setUp();

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

        assertThat(actual, is(expected));
    }

    @Test
    public void addShootingBallsMadeAdds8BallsMadeWithScratch() {
        actual.addShootingBallsMade(8, true);

        expected.shootingBallsMade += 8;
        expected.shootingScratches++;

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
        actual.addShootingTurn();

        expected.shootingTurns++;

        assertThat(actual, is(expected));
    }

    @Test
    public void addBreakShotWithoutAnyBalls() {
        actual.addBreakShot(0, false, false);

        expected.breakAttempts++;

        assertThat(actual, is(expected));

        // now add in a break scratch
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

        // now add in a break with continuation
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
}
