package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.player.EightBallPlayer;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
class EightBallController extends PlayerController<EightBallPlayer> {
    EightBallController(Game game, String playerName, String opponentName) {
        super(game);
        player1 = new EightBallPlayer(playerName);
        player2 = new EightBallPlayer(opponentName);
    }
}
