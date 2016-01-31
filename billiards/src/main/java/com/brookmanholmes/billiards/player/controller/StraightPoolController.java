package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.StraightPoolPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class StraightPoolController extends PlayerController<StraightPoolPlayer> {
    StraightPoolController(String playerName, String opponentName) {
        super();
        player1 = new StraightPoolPlayer(playerName);
        player2 = new StraightPoolPlayer(opponentName);
    }
}
