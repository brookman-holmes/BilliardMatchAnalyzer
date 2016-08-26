package com.brookmanholmes.billiards.player;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ApaEightBallPlayerTest extends AbstractPlayerTest {
    @Override public void setUp() {
        expected = new ApaEightBallPlayer(testName, 5);
        actual = new ApaEightBallPlayer(testName, 5);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForNineBall() {
        ApaEightBallPlayer player = new ApaEightBallPlayer(testName, 5);

        player.addEarlyWin();
        player.addWinOnBreak();

        actual.addPlayerStats(player);

        ((ApaEightBallPlayer) expected).addEarlyWin();
        ((ApaEightBallPlayer) expected).addWinOnBreak();

        assertThat(actual, is(expected));
    }

    @Test
    public void getMatchPointsTests() {
        // TODO: 8/26/2016 create this test + more to test function
    }

    @Test
    public void addWinOnBreakAdds10Wins() {
        // TODO: 8/26/2016 create this test
    }

    @Test
    public void addEarlyWinsAdds10Wins() {
        // TODO: 8/26/2016 create this test
    }
}
