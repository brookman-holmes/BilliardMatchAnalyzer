package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class TenBallController extends PlayerController<TenBallPlayer> {
    TenBallController(String playerName, String opponentName) {
        super();
        player1 = new TenBallPlayer(playerName);
        player2 = new TenBallPlayer(opponentName);
    }
}
