package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.ApaEightBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for APA 8 ball
 */
class ApaEightBallController extends PlayerController<ApaEightBallPlayer> {
    ApaEightBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override void addBreakingStats(ApaEightBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.getGameBallMadeOnBreak())
            player.addWinOnBreak();
    }

    @Override void addRunOutStats(ApaEightBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            player.addEarlyWin();
    }

    @Override int getMaximumBallsMakeable() {
        return 8;
    }


    @Override public ApaEightBallPlayer newPlayer() {
        return new ApaEightBallPlayer(playerName, playerRank);
    }

    @Override public ApaEightBallPlayer newOpponent() {
        return new ApaEightBallPlayer(opponentName, opponentRank);
    }
}