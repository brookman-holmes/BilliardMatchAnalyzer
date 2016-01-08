package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Brookman Holmes on 11/6/2015.
 */
public class NineBallGameTest extends AbstractRotationGameTest {
    @Override
    public void setUp() {
        game = new NineBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Override
    List<Integer> populateList() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Override
    Game createNewGame() {
        return new NineBallGame(PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Override
    GameType thisGamesGameType() {
        return GameType.BCA_NINE_BALL;
    }

    @Test(expected = InvalidGameTypeException.class)
    public void testWrongGameType() {
        game = new NineBallGame(GameType.APA_EIGHT_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
    }
}
