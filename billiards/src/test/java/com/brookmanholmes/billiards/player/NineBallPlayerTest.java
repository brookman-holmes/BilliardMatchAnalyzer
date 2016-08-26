package com.brookmanholmes.billiards.player;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class NineBallPlayerTest extends AbstractPlayerTest {
    @Override public void setUp() {
        expected = new NineBallPlayer(testName);
        actual = new NineBallPlayer(testName);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForNineBall() {
        NineBallPlayer player = new NineBallPlayer(testName);

        player.addEarlyWin();
        player.addWinOnBreak();

        actual.addPlayerStats(player);

        ((NineBallPlayer) expected).addEarlyWin();
        ((NineBallPlayer) expected).addWinOnBreak();

        assertThat(actual, is(expected));
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
