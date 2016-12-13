package com.brookmanholmes.billiards.player;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class StraightPoolPlayerTest {
    StraightPoolPlayer player;
    String name = "name";
    int rank = 100;

    @Before
    public void setup() {
        player = new StraightPoolPlayer(name, rank);
    }

    @Test
    public void seriousFoulGetsAddedWhenAddingPlayerData() {
        StraightPoolPlayer player2 = new StraightPoolPlayer(name, rank);
        player2.addSeriousFoul();

        player.addPlayerStats(player2);

        assertThat(player.getSeriousFouls(), is(1));
    }
}
