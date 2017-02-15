package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.GameType;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ApaEightBallPlayerTest extends AbstractPlayerTest {
    int opponentRank = 2;

    @Override
    public void setUp() {
        rank = 5;
        expected = new Player(testName, GameType.APA_EIGHT_BALL, rank, opponentRank);
        actual = new Player(testName, GameType.APA_EIGHT_BALL, rank, opponentRank);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForNineBall() {
        Player player = new Player(testName, GameType.APA_EIGHT_BALL, rank, opponentRank);

        player.addEarlyWin();
        player.addWinOnBreak();

        actual.addPlayerStats(player);

        expected.addEarlyWin();
        expected.addWinOnBreak();

        assertThat(actual, is(expected));
    }

    @Test
    public void getMatchPointsReturns0() {
        assertThat(actual.getMatchPoints(0), is(0));
    }

    @Test
    public void getMatchPointsReturns1() {
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();

        assertThat(actual.getMatchPoints(0), is(1));
    }

    @Test
    public void getMatchPointsReturns2() {
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();

        assertThat(actual.getMatchPoints(1), is(2));
    }

    @Test
    public void getMatchPointsReturns3() {
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();
        actual.addGameWon();

        assertThat(actual.getMatchPoints(0), is(3));
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
