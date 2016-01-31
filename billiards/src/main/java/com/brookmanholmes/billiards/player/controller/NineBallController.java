package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.NineBallPlayer;

/**
 * Created by Brookman Holmes on 10/28/2015.
 */
class NineBallController extends PlayerController<NineBallPlayer> {
    NineBallController(String playerName, String opponentName) {
        super();
        player1 = new NineBallPlayer(playerName);
        player2 = new NineBallPlayer(opponentName);
    }

    @Override
    void addBreakingStats(NineBallPlayer player) {
        super.addBreakingStats(player);

        ControllerHelperMethods.addWinOnBreak(player);
    }
}
