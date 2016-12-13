package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.TenBallPlayer;
import com.brookmanholmes.billiards.turn.ITurn;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for BCA 10 ball
 */
class TenBallController extends PlayerController<TenBallPlayer> {
    TenBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override
    public Pair<TenBallPlayer> addTurn(GameStatus gameStatus, ITurn turn) {
        Pair<TenBallPlayer> pair = super.addTurn(gameStatus, turn);

        if (turn.isSeriousFoul()) {
            if (gameStatus.turn == PlayerTurn.PLAYER)
                pair.getPlayer().addEarlyWin();
            if (gameStatus.turn == PlayerTurn.OPPONENT)
                pair.getOpponent().addEarlyWin();
        }

        return pair;
    }

    @Override
    void addRunOutStats(TenBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            player.addEarlyWin();
    }

    @Override
    public TenBallPlayer newPlayer() {
        return new TenBallPlayer(playerName, playerRank);
    }

    @Override
    public TenBallPlayer newOpponent() {
        return new TenBallPlayer(opponentName, opponentRank);
    }
}
