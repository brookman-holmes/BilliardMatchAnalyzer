package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.ApaEightBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class ApaEightBallController extends PlayerController<ApaEightBallPlayer> {
    private int playerRank;
    private int opponentRank;

    ApaEightBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName);

        this.playerRank = playerRank;
        this.opponentRank = opponentRank;
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