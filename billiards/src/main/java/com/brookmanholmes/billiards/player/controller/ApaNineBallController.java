package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.ApaNineBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class ApaNineBallController extends PlayerController<ApaNineBallPlayer> {
    ApaNineBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super();
        player1 = new ApaNineBallPlayer(playerName, playerRank);
        player2 = new ApaNineBallPlayer(opponentName, opponentRank);
    }

    @Override
    void addBreakingStats(ApaNineBallPlayer player) {
        super.addBreakingStats(player);


        if (turn.getGameBallMadeOnBreak()) {
            ControllerHelperMethods.addWinOnBreak(player);
        }
    }
}
