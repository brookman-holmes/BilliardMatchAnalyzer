package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.GameType;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class TenBallPlayerTest extends AbstractPlayerTest {
    @Override
    public void setUp() {
        rank = 0;
        expected = new Player(testName, GameType.BCA_TEN_BALL);
        actual = new Player(testName, GameType.BCA_TEN_BALL);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForTenBall() {
        Player player = new Player(testName, GameType.BCA_TEN_BALL);

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
