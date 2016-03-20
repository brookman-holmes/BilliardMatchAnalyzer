package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.TurnBuilder;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public class ApaNineBallPlayerControllerTest extends AbstractNineBallPlayerControllerTest<ApaNineBallPlayer> {
    int playerRank = 4;
    int opponentRank = 4;

    @Override
    public void setUp() {
        game = Game.newGame(GameType.BCA_NINE_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        playerController = new ApaNineBallController("", "", playerRank, opponentRank);
        actualPlayer = new ApaNineBallPlayer("", playerRank);
        expectedPlayer = new ApaNineBallPlayer("", playerRank);
        turnBuilder = new TurnBuilder(game.getGameType());
    }

    @Override
    ApaNineBallPlayer getBlankPlayer() {
        return new ApaNineBallPlayer("", playerRank);
    }
}
