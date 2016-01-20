package com.brookmanholmes.billiards.player.interfaces;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class EarlyWinsTest {
    EarlyWins earlyWins;

    @Before
    public void setUp() {
        earlyWins = new EarlyWinsImp();
    }


    @Test
    public void addEarlyWin() {
        earlyWins.addEarlyWin();

        assertThat(earlyWins.getEarlyWins(), is(1));
    }

    @Test
    public void objectIsPreparedCorrectly() {
        assertThat(earlyWins.getEarlyWins(), is(0));
    }
}
