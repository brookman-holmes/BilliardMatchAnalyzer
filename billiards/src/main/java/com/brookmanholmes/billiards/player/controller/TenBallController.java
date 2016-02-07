package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class TenBallController extends PlayerController<TenBallPlayer> {
    TenBallController(String playerName, String opponentName) {
        super(playerName, opponentName);
    }

    @Override
    void addRunOutStats(TenBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            ControllerHelperMethods.addEarlyWin(player);
    }

    @Override
    public TenBallPlayer newPlayer() {
        return new TenBallPlayer(playerName);
    }

    @Override
    public TenBallPlayer newOpponent() {
        return new TenBallPlayer(opponentName);
    }
}
