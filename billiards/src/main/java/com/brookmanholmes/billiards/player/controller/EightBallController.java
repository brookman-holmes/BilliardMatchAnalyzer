package com.brookmanholmes.billiards.player.controller;


/**
 * Created by Brookman Holmes on 10/28/2015.
 * A controller for adding up player stats for BCA 8 ball
 */
class EightBallController extends PlayerController {
    EightBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override
    int getMaximumBallsMakeable() {
        return 8;
    }
}
