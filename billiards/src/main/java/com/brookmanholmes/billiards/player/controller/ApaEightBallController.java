package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.turn.ITurn;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for APA 8 ball
 */
class ApaEightBallController extends PlayerController<ApaEightBallPlayer> {
    ApaEightBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override
    public Pair<ApaEightBallPlayer> addTurn(GameStatus gameStatus, ITurn turn) {
        Pair<ApaEightBallPlayer> pair = super.addTurn(gameStatus, turn);

        if (turn.isSeriousFoul()) {
            if (gameStatus.turn == PlayerTurn.PLAYER)
                pair.getPlayer().addEarlyWin();
            if (gameStatus.turn == PlayerTurn.OPPONENT)
                pair.getOpponent().addEarlyWin();
        }

        return pair;
    }

    @Override
    void addBreakingStats(ApaEightBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.isGameBallMadeOnBreak()) {
            player.addWinOnBreak();
            player.addEarlyWin();
        }
    }

    @Override
    int getMaximumBallsMakeable() {
        return 8;
    }


    @Override
    public ApaEightBallPlayer newPlayer() {
        return new ApaEightBallPlayer(playerName, playerRank);
    }

    @Override
    public ApaEightBallPlayer newOpponent() {
        return new ApaEightBallPlayer(opponentName, opponentRank);
    }
}