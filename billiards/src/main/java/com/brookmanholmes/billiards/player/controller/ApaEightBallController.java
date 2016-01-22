package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class ApaEightBallController extends PlayerController<ApaEightBallPlayer> {
    ApaEightBallController(Game game, String playerName, String opponentName, int playerRank, int opponentRank) {
        super(game);
        player1 = new ApaEightBallPlayer(playerName, playerRank);
        player2 = new ApaEightBallPlayer(opponentName, opponentRank);
    }
}
