package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.StraightPoolPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for straight pool
 */
class StraightPoolController extends PlayerController<StraightPoolPlayer> {
    StraightPoolController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override public StraightPoolPlayer newPlayer() {
        return new StraightPoolPlayer(playerName, playerRank);
    }

    @Override public StraightPoolPlayer newOpponent() {
        return new StraightPoolPlayer(opponentName, opponentRank);
    }
}
