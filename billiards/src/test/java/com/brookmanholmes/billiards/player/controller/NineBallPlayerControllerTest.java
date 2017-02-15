package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.TurnBuilder;

/**
 * Created by Brookman Holmes on 1/30/2016.
 */
public class NineBallPlayerControllerTest extends AbstractNineBallPlayerControllerTest {
    @Override
    public void setUp() {
        game = Game.newGame(GameType.BCA_NINE_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        playerController = new NineBallController("", "", 0, 0);
        actualPlayer = new Player("", GameType.BCA_NINE_BALL);
        expectedPlayer = new Player("", GameType.BCA_NINE_BALL);
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Override
    Player getBlankPlayer() {
        return new Player("", GameType.BCA_NINE_BALL);
    }
}
