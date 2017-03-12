package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.GameType;

/**
 * Created by Brookman Holmes on 1/19/2016.
 */
public class EightBallPlayerTest extends AbstractPlayerTest {
    @Override
    public void setUp() {
        rank = 0;
        expected = new Player(testName, testName, GameType.BCA_EIGHT_BALL);
        actual = new Player(testName, testName, GameType.BCA_EIGHT_BALL);
    }
}
