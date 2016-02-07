package com.brookmanholmes.billiards.player.interfaces;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class BreakWinsTest {
    WinsOnBreak winsOnBreak;

    @Before
    public void initialize() {
        winsOnBreak = new WinsOnBreakImp();
    }

    @Test
    public void objectIsPreparedCorrectly() {
        assertThat(winsOnBreak.getWinsOnBreak(), is(0));
        assertThat(winsOnBreak.getEarlyWins(), is(0));
    }

    @Test
    public void addWinOnBreak() {
        winsOnBreak.addWinOnBreak();

        assertThat(winsOnBreak.getWinsOnBreak(), is(1));
    }

    @Test
    public void testAddingEarlyWins() {
        winsOnBreak.addEarlyWin();

        assertThat(winsOnBreak.getEarlyWins(), is(1));
    }

    @Test
    public void testAddingMultipleEarlyWins() {
        winsOnBreak.addEarlyWins(3);

        assertThat(winsOnBreak.getEarlyWins(), is(3));
    }

    @Test
    public void testAddingMultipleBreakWins() {
        winsOnBreak.addWinsOnBreak(3);

        assertThat(winsOnBreak.getWinsOnBreak(), is(3));
    }
}
