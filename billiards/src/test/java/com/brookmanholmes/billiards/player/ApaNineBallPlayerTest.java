package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.GameType;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ApaNineBallPlayerTest extends AbstractPlayerTest {
    @Override public void setUp() {
        rank = 5;
        expected = new Player(testName, testName, GameType.APA_NINE_BALL, rank, rank);
        actual = new Player(testName, testName, GameType.APA_NINE_BALL, rank, rank);
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

    @Test
    public void addPlayerStatsWorksCorrectlyForNineBall() {
        Player player = new Player(testName, testName, GameType.APA_NINE_BALL, rank, rank);

        player.addEarlyWin();
        player.addWinOnBreak();

        actual.addPlayerStats(player);

        expected.addEarlyWin();
        expected.addWinOnBreak();

        assertThat(actual, is(expected));
    }

    @Test
    public void addDeadBallsEquals3() {
        Player player = new Player(testName, testName, GameType.APA_NINE_BALL, rank, rank);

        player.addDeadBalls(3);

        assertThat(player.getDeadBalls(), is(3));
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
    public void getMatchPointsReturns20() {
        actual.addPoints(700);
        assertThat(actual.getMatchPoints(0), is(20));
    }

    @Test
    public void getMatchPointsReturns0() {
        assertThat(actual.getMatchPoints(0), is(0));
    }

    @Test
    public void getPointsNeededReturnsRank5Value() {
        assertThat(actual.getPointsNeeded(), is(Players.apa9BallRaceTo(rank)));
    }
}
