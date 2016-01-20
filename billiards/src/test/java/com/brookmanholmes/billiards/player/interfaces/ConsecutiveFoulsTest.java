package com.brookmanholmes.billiards.player.interfaces;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ConsecutiveFoulsTest {
    ConsecutiveFouls consecutiveFouls;

    @Before
    public void setUp() {
        consecutiveFouls = new ConsecutiveFoulsImp();
    }

    @Test
    public void addFoul() {
        consecutiveFouls.addFoul();

        assertThat(consecutiveFouls.getFouls(), is(1));
    }

    @Test
    public void objectIsPreparedCorrectly() {
        assertThat(consecutiveFouls.getFouls(), is(0));
    }

    @Test
    public void removeFoulsRemovesAllFouls() {
        consecutiveFouls.addFoul();
        consecutiveFouls.addFoul();
        consecutiveFouls.removeFouls();

        assertThat(consecutiveFouls.getFouls(), is(0));
    }
}
