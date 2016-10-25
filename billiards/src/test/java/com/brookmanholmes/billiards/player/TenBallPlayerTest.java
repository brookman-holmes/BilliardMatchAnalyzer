package com.brookmanholmes.billiards.player;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class TenBallPlayerTest extends AbstractPlayerTest<TenBallPlayer> {
    @Override public void setUp() {
        rank = 0;
        expected = new TenBallPlayer(testName);
        actual = new TenBallPlayer(testName);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForTenBall() {
        TenBallPlayer player = new TenBallPlayer(testName);

        player.addEarlyWin();

        actual.addPlayerStats(player);

        expected.addEarlyWin();

        assertThat(actual, is(expected));
    }

    @Test
    public void addEarlyWinsAdds10Wins() {
        actual.addEarlyWins(10);

        assertThat(actual.getEarlyWins(), is(10));
    }
}
