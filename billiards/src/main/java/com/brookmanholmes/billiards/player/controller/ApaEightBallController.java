package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.ApaEightBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class ApaEightBallController extends PlayerController<ApaEightBallPlayer> {
    ApaEightBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super();
        player1 = new ApaEightBallPlayer(playerName, playerRank);
        player2 = new ApaEightBallPlayer(opponentName, opponentRank);
    }

    @Override
    void addBreakingStats(ApaEightBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.getGameBallMadeOnBreak())
            ControllerHelperMethods.addWinOnBreak(player);
    }

    @Override
    int getMaximumBallsMakeable() {
        return 8;
    }
}