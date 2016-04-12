package com.brookmanholmes.billiards.player.interfaces;

import com.brookmanholmes.billiards.player.TenBallPlayer;

import org.junit.Before;

/**
 * Created by Brookman Holmes on 1/22/2016.
 */
public class TenBallPlayerEarlyWinsTest extends EarlyWinsTest {
    @Override @Before
    public void setUp() {
        earlyWins = new TenBallPlayer("");
    }
}
