package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class ApaNineBallController extends PlayerController<ApaNineBallPlayer> {
    ApaNineBallController(Game game, String playerName, String opponentName, int playerRank, int opponentRank) {
        super(game);
        player1 = new ApaNineBallPlayer(playerName, playerRank);
        player2 = new ApaNineBallPlayer(opponentName, opponentRank);
    }
}
