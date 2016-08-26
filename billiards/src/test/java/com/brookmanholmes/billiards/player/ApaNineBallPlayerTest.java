package com.brookmanholmes.billiards.player;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ApaNineBallPlayerTest extends AbstractPlayerTest<ApaNineBallPlayer> {
    @Override public void setUp() {
        expected = new ApaNineBallPlayer(testName, 5);
        actual = new ApaNineBallPlayer(testName, 5);
    }

    @Test
    public void addWinOnBreakAdds10Wins() {
        // TODO: 8/26/2016 create this test
    }

    @Test
    public void addEarlyWinsAdds10Wins() {
        // TODO: 8/26/2016 create this test
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
        ApaNineBallPlayer player = new ApaNineBallPlayer(testName, 5);

        player.addEarlyWin();
        player.addWinOnBreak();

        actual.addPlayerStats(player);

        expected.addEarlyWin();
        expected.addWinOnBreak();

        assertThat(actual, is(expected));
    }

    @Override @Test
    public void addBreakShotWithBallsMadeButNoContinuation() {
        expected.points += 2;
        super.addBreakShotWithBallsMadeButNoContinuation();
    }

    @Override @Test
    public void addGameWonAddsOneToTotalAndWins() {
        expected.points += 1;
        super.addGameWonAddsOneToTotalAndWins();
    }

    @Test
    public void addBreakShotWithBallsMadeWithContinuation() {
        expected.points += 2;
        super.addBreakShotWithBallsMadeWithContinuation();
    }

    @Test
    public void addShootingBallsMadeAdds8BallsMade() {
        expected.points += 8;
        super.addShootingBallsMadeAdds8BallsMade();
    }

    @Override @Test
    public void addShootingBallsMadeAdds8BallsMadeWithScratch() {
        expected.points += 8;
        super.addShootingBallsMadeAdds8BallsMadeWithScratch();
    }


    @Test
    public void addDeadBallsEquals3() {
        ApaNineBallPlayer player = new ApaNineBallPlayer(testName, 5);

        player.addDeadBalls(3);

        assertThat(player.getDeadBalls(), is(3));
    }
}
