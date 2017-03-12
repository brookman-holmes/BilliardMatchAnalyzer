package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 3/10/2017.
 */

public class GhostPlayerControllerTest {
    PlayerController controller;
    Game game;
    GameType gameType = GameType.BCA_GHOST_EIGHT_BALL;
    String testName = "test1";
    String testName2 = "test2";

    TurnBuilder turn() {
        return new TurnBuilder(game.getGameType());
    }


    @Before
    public void setup() {
        game = Game.newGame(gameType, PlayerTurn.PLAYER, BreakType.PLAYER, 3);
        controller = PlayerController.createController(game, testName, testName2, testName, testName2, 4, 4);
    }

    @Test
    public void isGameOverReturnsTrueAfter3Attempts() {
        controller.addTurn(game.getGameStatus(), turn().breakMiss());
        game.addTurn(turn().breakMiss());
        assertThat(controller.isGameOver(), is(false));

        controller.addTurn(game.getGameStatus(), turn().miss());
        game.addTurn(turn().miss());
        assertThat(controller.isGameOver(), is(false));

        controller.addTurn(game.getGameStatus(), turn().miss());
        game.addTurn(turn().miss());
        assertThat(controller.isGameOver(), is(true));
    }
}
