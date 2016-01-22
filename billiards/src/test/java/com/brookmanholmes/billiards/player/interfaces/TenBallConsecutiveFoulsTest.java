package com.brookmanholmes.billiards.player.interfaces;

import com.brookmanholmes.billiards.player.TenBallPlayer;

import org.junit.Before;

/**
 * Created by Brookman Holmes on 1/22/2016.
 */
public class TenBallConsecutiveFoulsTest extends ConsecutiveFoulsTest {
    @Before
    @Override
    public void setUp() {
        consecutiveFouls = new TenBallPlayer("");
    }
}
