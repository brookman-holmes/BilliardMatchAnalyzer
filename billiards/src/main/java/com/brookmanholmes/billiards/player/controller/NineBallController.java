package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.player.NineBallPlayer;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
class NineBallController extends PlayerController<NineBallPlayer> {
    public NineBallController(Game game, String playerName, String opponentName) {
        super(game);
        player1 = new NineBallPlayer(playerName);
        player2 = new NineBallPlayer(opponentName);
    }
}
