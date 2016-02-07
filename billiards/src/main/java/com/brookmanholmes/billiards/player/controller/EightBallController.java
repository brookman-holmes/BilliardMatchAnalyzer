package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.EightBallPlayer;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
class EightBallController extends PlayerController<EightBallPlayer> {
    EightBallController(String playerName, String opponentName) {
        super(playerName, opponentName);
    }

    @Override
    int getMaximumBallsMakeable() {
        return 8;
    }

    @Override
    public EightBallPlayer newPlayer() {
        return new EightBallPlayer(playerName);
    }

    @Override
    public EightBallPlayer newOpponent() {
        return new EightBallPlayer(opponentName);
    }
}
