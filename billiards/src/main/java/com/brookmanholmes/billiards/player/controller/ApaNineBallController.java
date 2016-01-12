package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.ApaNineBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ApaNineBallController extends PlayerController<ApaNineBallPlayer> {
    public ApaNineBallController(String playerName, String opponentName) {
        super();
        player1 = new ApaNineBallPlayer(playerName, 0);
        player2 = new ApaNineBallPlayer(opponentName, 0);
    }
}
