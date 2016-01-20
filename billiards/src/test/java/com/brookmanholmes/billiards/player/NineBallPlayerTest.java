package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class NineBallPlayerTest extends AbstractPlayerTest {
    @Override
    public void setUp() {
        expected = new NineBallPlayer(testName);
        actual = new NineBallPlayer(testName);
    }
}
