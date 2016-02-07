package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.ApaNineBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class ApaNineBallController extends PlayerController<ApaNineBallPlayer> {
    int playerRank, opponentRank;
    ApaNineBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName);

        this.playerRank = playerRank;
        this.opponentRank = opponentRank;
    }

    @Override
    void addBreakingStats(ApaNineBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.getGameBallMadeOnBreak()) {
            ControllerHelperMethods.addWinOnBreak(player);
        }
    }

    @Override
    void addRunOutStats(ApaNineBallPlayer player) {
        super.addRunOutStats(player);

        ControllerHelperMethods.addEarlyWin(player);
    }

    @Override
    public ApaNineBallPlayer newPlayer() {
        return new ApaNineBallPlayer(playerName, playerRank);
    }

    @Override
    public ApaNineBallPlayer newOpponent() {
        return new ApaNineBallPlayer(opponentName, opponentRank);
    }
}
