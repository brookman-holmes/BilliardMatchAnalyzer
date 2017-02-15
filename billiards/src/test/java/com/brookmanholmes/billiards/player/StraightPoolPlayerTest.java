package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.GameType;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class StraightPoolPlayerTest {
    Player player;
    String name = "name";
    int rank = 100;

    @Before
    public void setup() {
        player = new Player(name, GameType.STRAIGHT_POOL, rank);
    }

    @Test
    public void seriousFoulGetsAddedWhenAddingPlayerData() {
        Player player2 = new Player(name, GameType.STRAIGHT_POOL, rank);
        player2.addSeriousFoul();

        player.addPlayerStats(player2);

        assertThat(player.getSeriousFouls(), is(1));
    }
}
