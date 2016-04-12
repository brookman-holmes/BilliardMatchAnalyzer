package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.ApaEightBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class ApaEightBallController extends PlayerController<ApaEightBallPlayer> {
    int playerRank, opponentRank;

    ApaEightBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName);

        this.playerRank = playerRank;
        this.opponentRank = opponentRank;
    }
    // TODO: 3/18/2016 add in turn information here

    @Override void addBreakingStats(ApaEightBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.getGameBallMadeOnBreak())
            ControllerHelperMethods.addWinOnBreak(player);
    }

    @Override void addRunOutStats(ApaEightBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            ControllerHelperMethods.addEarlyWin(player);
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