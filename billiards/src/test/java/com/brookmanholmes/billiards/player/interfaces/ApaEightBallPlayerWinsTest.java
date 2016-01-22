package com.brookmanholmes.billiards.player.interfaces;

import com.brookmanholmes.billiards.player.ApaEightBallPlayer;

import org.junit.Before;

/**
 * Created by Brookman Holmes on 1/22/2016.
 */
public class ApaEightBallPlayerWinsTest extends BreakWinsTest {
    @Before
    @Override
    public void initialize() {
        winsOnBreak = new ApaEightBallPlayer("", 0);
    }
}
