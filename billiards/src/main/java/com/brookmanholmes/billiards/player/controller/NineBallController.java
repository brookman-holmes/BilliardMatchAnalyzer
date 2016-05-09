package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.NineBallPlayer;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
class NineBallController extends PlayerController<NineBallPlayer> {
    NineBallController(String playerName, String opponentName) {
        super(playerName, opponentName);
    }

    @Override void addBreakingStats(NineBallPlayer player) {
        super.addBreakingStats(player);


        if (turn.getGameBallMadeOnBreak())
            player.addWinOnBreak();
    }

    @Override void addRunOutStats(NineBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            player.addEarlyWin();
    }

    @Override public NineBallPlayer newOpponent() {
        return new NineBallPlayer(opponentName);
    }

    @Override public NineBallPlayer newPlayer() {
        return new NineBallPlayer(playerName);
    }
}
