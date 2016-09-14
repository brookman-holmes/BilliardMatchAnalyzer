package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for BCA 10 ball
 */
class TenBallController extends PlayerController<TenBallPlayer> {
    TenBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override void addRunOutStats(TenBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            player.addEarlyWin();
    }

    @Override public TenBallPlayer newPlayer() {
        return new TenBallPlayer(playerName, playerRank);
    }

    @Override public TenBallPlayer newOpponent() {
        return new TenBallPlayer(opponentName, opponentRank);
    }
}
