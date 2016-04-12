package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.Pair;

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

    @Override public Pair<ApaNineBallPlayer> updatePlayerStats(GameStatus gameStatus, Turn turn) {
        Pair<ApaNineBallPlayer> pair = super.updatePlayerStats(gameStatus, turn);

        // TODO: 3/18/2016 add in turn information here
        return pair;
    }

    @Override void addBreakingStats(ApaNineBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.getGameBallMadeOnBreak()) {
            ControllerHelperMethods.addWinOnBreak(player);
        }
    }

    @Override void addRunOutStats(ApaNineBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            ControllerHelperMethods.addEarlyWin(player);
    }

    @Override public ApaNineBallPlayer newPlayer() {
        return new ApaNineBallPlayer(playerName, playerRank);
    }

    @Override public ApaNineBallPlayer newOpponent() {
        return new ApaNineBallPlayer(opponentName, opponentRank);
    }
}
