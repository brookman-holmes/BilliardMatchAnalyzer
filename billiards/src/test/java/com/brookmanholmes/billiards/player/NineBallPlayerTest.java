package com.brookmanholmes.billiards.player;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class NineBallPlayerTest extends AbstractPlayerTest<NineBallPlayer> {
    @Override public void setUp() {
        rank = 0;
        expected = new NineBallPlayer(testName);
        actual = new NineBallPlayer(testName);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForNineBall() {
        NineBallPlayer player = new NineBallPlayer(testName);

        player.addEarlyWin();
        player.addWinOnBreak();

        actual.addPlayerStats(player);

        expected.addEarlyWin();
        expected.addWinOnBreak();

        assertThat(actual, is(expected));
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
}
