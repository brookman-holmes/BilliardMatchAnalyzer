package com.brookmanholmes.billiards.player.controller;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for American Rotation
 */
class AmericanRotationController extends PlayerController {
    public AmericanRotationController(String playerId, String opponentId, String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
    }

    @Override
    int getMaximumBallsMakeable() {
        return 15;
    }
}
