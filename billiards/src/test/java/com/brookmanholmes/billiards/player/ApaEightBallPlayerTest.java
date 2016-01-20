package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ApaEightBallPlayerTest extends AbstractPlayerTest {
    @Override
    public void setUp() {
        expected = new ApaEightBallPlayer(testName, 5);
        actual = new ApaEightBallPlayer(testName, 5);
    }
}
