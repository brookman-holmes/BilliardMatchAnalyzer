package com.brookmanholmes.billiards.player.interfaces;

import com.brookmanholmes.billiards.player.ApaNineBallPlayer;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/22/2016.
 */
public class ApaNineBallPlayerInterfaceTest extends ApaInterfaceTest {
    @Override public void setUp() {
        actual = new ApaNineBallPlayer("", rank);
    }

    private void playerHasNotYetWonHelper(int startPoints, int endPoints, int expectedMatchPoints) {
        getPlayer().addPoints(startPoints);
        assertThat(actual.getMatchPoints(0, 4), is(expectedMatchPoints));

        getPlayer().addPoints(endPoints - startPoints);
        assertThat(actual.getMatchPoints(0, 4), is(expectedMatchPoints));
    }

    private void playerWonHelper(int expectedMatchPoints, int minOppScore, int maxOppScore) {
        getPlayer().addPoints(55);
        assertThat(actual.getMatchPoints(minOppScore, 4), is(expectedMatchPoints));
        assertThat(actual.getMatchPoints(maxOppScore, 4), is(expectedMatchPoints));
    }

    @Override public void getMatchPointsReturns0() {
        playerHasNotYetWonHelper(0, 10, 0);
    }

    @Override public void getMatchPointsReturns1() {
        playerHasNotYetWonHelper(11, 15, 1);
    }

    @Override public void getMatchPointsReturns2() {
        playerHasNotYetWonHelper(16, 21, 2);
    }

    @Override public void getMatchPointsReturns3() {
        playerHasNotYetWonHelper(22, 26, 3);
    }


    @Test
    public void getMatchPointsReturns4() {
        playerHasNotYetWonHelper(27, 32, 4);
    }

    @Test
    public void getMatchPointsReturns5() {
        playerHasNotYetWonHelper(33, 37, 5);
    }

    @Test
    public void getMatchPointsReturns6() {
        playerHasNotYetWonHelper(38, 43, 6);
    }

    @Test
    public void getMatchPointsReturns7() {
        playerHasNotYetWonHelper(44, 49, 7);
    }

    @Test
    public void getMatchPointsReturns8() {
        playerHasNotYetWonHelper(50, 54, 8);
    }

    @Test
    public void getMatchPointsReturns12() {
        playerWonHelper(12, 28, 30);
    }

    @Test
    public void getMatchPointsReturns13() {
        playerWonHelper(13, 25, 27);
    }

    @Test
    public void getMatchPointsReturns14() {
        playerWonHelper(14, 22, 24);
    }

    @Test
    public void getMatchPointsReturns15() {
        playerWonHelper(15, 19, 21);
    }

    @Test
    public void getMatchPointsReturns16() {
        playerWonHelper(16, 15, 18);
    }

    @Test
    public void getMatchPointsReturns17() {
        playerWonHelper(17, 12, 14);
    }

    @Test
    public void getMatchPointsReturns18() {
        playerWonHelper(18, 9, 11);
    }

    @Test
    public void getMatchPointsReturns19() {
        playerWonHelper(19, 6, 8);
    }

    @Test
    public void getMatchPointsReturns20() {
        playerWonHelper(20, 0, 5);
    }

    private ApaNineBallPlayer getPlayer() {
        return (ApaNineBallPlayer) actual;
    }
}
