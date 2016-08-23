package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.AmericanRotationPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class AmericanRotationController extends PlayerController<AmericanRotationPlayer> {
    AmericanRotationController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override int getMaximumBallsMakeable() {
        return 15;
    }

    @Override public AmericanRotationPlayer newPlayer() {
        return new AmericanRotationPlayer(playerName, playerRank);
    }

    @Override public AmericanRotationPlayer newOpponent() {
        return new AmericanRotationPlayer(opponentName, opponentRank);
    }
}
