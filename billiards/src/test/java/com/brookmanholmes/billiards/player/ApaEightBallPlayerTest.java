package com.brookmanholmes.billiards.player;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ApaEightBallPlayerTest extends AbstractPlayerTest<ApaEightBallPlayer> {
    @Override public void setUp() {
        rank = 5;
        expected = new ApaEightBallPlayer(testName, rank);
        actual = new ApaEightBallPlayer(testName, rank);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForNineBall() {
        ApaEightBallPlayer player = new ApaEightBallPlayer(testName, rank);

        player.addEarlyWin();
        player.addWinOnBreak();

        actual.addPlayerStats(player);

        expected.addEarlyWin();
        expected.addWinOnBreak();

        assertThat(actual, is(expected));
    }

    @Test
    public void getMatchPointsReturns0() {
        assertThat(actual.getMatchPoints(0, 2), is(0));
    }

    @Test
    public void getMatchPointsReturns1() {
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();

        assertThat(actual.getMatchPoints(0, 2), is(1));
    }

    @Test
    public void getMatchPointsReturns2() {
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();

        assertThat(actual.getMatchPoints(1, 2), is(2));
    }

    @Test
    public void getMatchPointsReturns3() {
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();

        assertThat(actual.getMatchPoints(0, 2), is(3));
    }

    @Test
    public void addWinOnBreakAdds10Wins() {
        actual.addWinsOnBreak(10);

        assertThat(actual.getWinsOnBreak(), is(10));
    }

    @Test
    public void addEarlyWinsAdds10Wins() {
        actual.addEarlyWins(10);

        assertThat(actual.getEarlyWins(), is(10));
    }

    @Test
    public void getPointsReturnsGameWins() {
        actual.addGameWon();
        actual.addGameWon();

        assertThat(actual.getPoints(), is(actual.getWins()));
    }


}
