package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class EightBallPlayerTest extends AbstractPlayerTest {
    @Override public void setUp() {
        rank = 0;
        expected = new EightBallPlayer(testName);
        actual = new EightBallPlayer(testName);
    }
}
