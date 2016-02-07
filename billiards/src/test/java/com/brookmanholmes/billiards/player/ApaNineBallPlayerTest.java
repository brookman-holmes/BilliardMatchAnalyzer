package com.brookmanholmes.billiards.player;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ApaNineBallPlayerTest extends AbstractPlayerTest<ApaNineBallPlayer> {
    @Override
    public void setUp() {
        expected = new ApaNineBallPlayer(testName, 5);
        actual = new ApaNineBallPlayer(testName, 5);
    }

    @Test
    public void makeSurePlayerReceivesPointsForMakingBalls() {
        actual.addShootingBallsMade(5, false);


        assertThat(actual.getPoints(), is(5));
    }

    @Test
    public void playerReceivesExtraPointForWin() {
        actual.addGameWon();

        assertThat(actual.getPoints(), is(1));
    }

    @Test
    public void playerReceivesPointForBreak() {
        actual.addBreakShot(3, false, false);

        assertThat(actual.getPoints(), is(3));
    }
}
