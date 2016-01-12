package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.ApaEightBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ApaEightBallController extends PlayerController<ApaEightBallPlayer> {
    public ApaEightBallController(String playerName, String opponentName) {
        super();
        player1 = new ApaEightBallPlayer(playerName, 0);
        player2 = new ApaEightBallPlayer(opponentName, 0);
    }
}
