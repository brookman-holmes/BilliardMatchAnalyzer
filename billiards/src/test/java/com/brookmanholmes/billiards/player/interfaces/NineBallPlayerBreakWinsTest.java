package com.brookmanholmes.billiards.player.interfaces;

import com.brookmanholmes.billiards.player.NineBallPlayer;

import org.junit.Before;

/**
 * Created by Brookman Holmes on 1/22/2016.
 */
public class NineBallPlayerBreakWinsTest extends BreakWinsTest {
    @Before
    @Override
    public void initialize() {
        winsOnBreak = new NineBallPlayer("");
    }
}
