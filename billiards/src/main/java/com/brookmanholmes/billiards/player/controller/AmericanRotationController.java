package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.AmericanRotationPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class AmericanRotationController extends PlayerController<AmericanRotationPlayer> {
    AmericanRotationController(String playerName, String opponentName) {
        super();
        player1 = new AmericanRotationPlayer(playerName);
        player2 = new AmericanRotationPlayer(opponentName);
    }

    @Override
    int getMaximumBallsMakeable() {
        return 15;
    }
}
