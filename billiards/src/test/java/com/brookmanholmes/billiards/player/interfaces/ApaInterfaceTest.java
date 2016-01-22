package com.brookmanholmes.billiards.player.interfaces;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/22/2016.
 */
public abstract class ApaInterfaceTest {
    Apa actual;
    int rank = 7;

    @Before
    public abstract void setUp();


    @Test
    public void getRankReturns7() {
        assertThat(actual.getRank(), is(rank));
    }

    @Test
    public abstract void getMatchPointsReturns0();

    @Test
    public abstract void getMatchPointsReturns1();

    @Test
    public abstract void getMatchPointsReturns2();

    @Test
    public abstract void getMatchPointsReturns3();
}
