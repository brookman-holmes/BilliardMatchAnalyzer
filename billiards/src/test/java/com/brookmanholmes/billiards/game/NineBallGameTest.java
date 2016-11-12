package com.brookmanholmes.billiards.game;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/6/2015.
 */
public class NineBallGameTest extends AbstractRotationGameTest {
    @Override public void setUp() {
        game = new NineBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Override List<Integer> populateList() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Override Game createNewGame() {
        return new NineBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Override GameType thisGamesGameType() {
        return GameType.BCA_NINE_BALL;
    }
}
