package com.brookmanholmes.billiards.player;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class TenBallPlayerTest extends AbstractPlayerTest {
    @Override public void setUp() {
        expected = new TenBallPlayer(testName);
        actual = new TenBallPlayer(testName);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForTenBall() {
        TenBallPlayer player = new TenBallPlayer(testName);

        player.addEarlyWin();

        actual.addPlayerStats(player);

        ((TenBallPlayer) expected).addEarlyWin();

        assertThat(actual, is(expected));
    }

    @Test
    public void addEarlyWinsAdds10Wins() {
        // TODO: 8/26/2016 create this test
    }
}
