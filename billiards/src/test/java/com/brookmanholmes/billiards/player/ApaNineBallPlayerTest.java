package com.brookmanholmes.billiards.player;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class ApaNineBallPlayerTest extends AbstractPlayerTest {
    @Override
    public void setUp() {
        expected = new ApaNineBallPlayer(testName, 5);
        actual = new ApaNineBallPlayer(testName, 5);
    }
}