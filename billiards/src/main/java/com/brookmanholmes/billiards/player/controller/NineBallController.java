package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.turn.ITurn;

/**
 * Created by Brookman Holmes on 10/28/2015.
 * A controller for adding up player stats for BCA 9 ball
 */
class NineBallController extends PlayerController<NineBallPlayer> {
    NineBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override
    public Pair<NineBallPlayer> addTurn(GameStatus gameStatus, ITurn turn) {
        Pair<NineBallPlayer> pair = super.addTurn(gameStatus, turn);

        if (turn.isSeriousFoul()) {
            if (gameStatus.turn == PlayerTurn.PLAYER)
                pair.getPlayer().addEarlyWin();
            if (gameStatus.turn == PlayerTurn.OPPONENT)
                pair.getOpponent().addEarlyWin();
        }

        return pair;
    }

    @Override
    void addBreakingStats(NineBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.isGameBallMadeOnBreak())
            player.addWinOnBreak();
    }

    @Override
    void addRunOutStats(NineBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            player.addEarlyWin();
    }

    @Override
    public NineBallPlayer newPlayer() {
        return new NineBallPlayer(playerName, playerRank);
    }

    @Override
    public NineBallPlayer newOpponent() {
        return new NineBallPlayer(opponentName, opponentRank);
    }
}
