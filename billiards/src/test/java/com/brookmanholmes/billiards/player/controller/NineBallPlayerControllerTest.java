package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.turn.TurnBuilder;

/**
 * Created by Brookman Holmes on 1/30/2016.
 */
public class NineBallPlayerControllerTest extends AbstractNineBallPlayerControllerTest<NineBallPlayer> {
    @Override public void setUp() {
        game = Game.newGame(GameType.BCA_NINE_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        playerController = new NineBallController("", "", 0, 0);
        actualPlayer = new NineBallPlayer("");
        expectedPlayer = new NineBallPlayer("");
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Override NineBallPlayer getBlankPlayer() {
        return new NineBallPlayer("");
    }
}
