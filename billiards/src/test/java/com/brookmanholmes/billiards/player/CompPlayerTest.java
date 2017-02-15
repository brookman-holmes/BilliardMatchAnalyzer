package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.GameType;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 8/30/2016.
 */
public class CompPlayerTest extends AbstractPlayerTest {
    @Override
    public void setUp() {
        rank = 0;
        expected = new Player(testName, GameType.ALL);
        actual = new Player(testName, GameType.ALL);
    }

    @Test
    public void addPlayerStatsWorksCorrectlyForNineBall() {
        Player player = new Player(testName, GameType.ALL);

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
