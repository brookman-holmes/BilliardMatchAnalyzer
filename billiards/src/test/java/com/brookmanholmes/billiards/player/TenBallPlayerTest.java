package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class TenBallPlayerTest extends AbstractPlayerTest {
    @Override
    public void setUp() {
        expected = new TenBallPlayer(testName);
        actual = new TenBallPlayer(testName);
    }
}
