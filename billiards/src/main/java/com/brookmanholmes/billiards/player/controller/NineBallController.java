package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.NineBallPlayer;

/**
 * Created by Brookman Holmes on 10/28/2015.
 * A controller for adding up player stats for BCA 9 ball
 */
class NineBallController extends PlayerController<NineBallPlayer> {
    NineBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override
    void addBreakingStats(NineBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.isGameBallMadeOnBreak())
            player.addWinOnBreak();
    }

    @Override
    void addRunOutStats(NineBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            player.addEarlyWin();
    }

    @Override
    public NineBallPlayer newPlayer() {
        return new NineBallPlayer(playerName, playerRank);
    }

    @Override
    public NineBallPlayer newOpponent() {
        return new NineBallPlayer(opponentName, opponentRank);
    }
}
