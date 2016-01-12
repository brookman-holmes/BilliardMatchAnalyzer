package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.NineBallPlayer;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
class NineBallController extends PlayerController<NineBallPlayer> {
    public NineBallController(String playerName, String opponentName) {
        super();
        player1 = new NineBallPlayer(playerName);
        player2 = new NineBallPlayer(opponentName);
    }
}
