package com.brookmanholmes.billiards.player.interfaces;

import com.brookmanholmes.billiards.player.ApaNineBallPlayer;

import org.junit.Before;

/**
 * Created by Brookman Holmes on 1/22/2016.
 */
public class ApaNineBallPlayerBreakWinsTest extends BreakWinsTest {
    @Before
    @Override
    public void initialize() {
        winsOnBreak = new ApaNineBallPlayer("", 0);
    }
}
