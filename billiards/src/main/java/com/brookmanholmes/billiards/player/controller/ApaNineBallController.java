package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.turn.ITurn;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for APA 9 ball
 */
class ApaNineBallController extends PlayerController<ApaNineBallPlayer> {
    ApaNineBallController(String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerName, opponentName, playerRank, opponentRank);
    }

    @Override void addBreakingStats(ApaNineBallPlayer player) {
        super.addBreakingStats(player);

        if (turn.isGameBallMadeOnBreak())
            player.addWinOnBreak();
    }

    @Override public Pair<ApaNineBallPlayer> addTurn(GameStatus gameStatus, ITurn turn) {
        Pair<ApaNineBallPlayer> players = super.addTurn(gameStatus, turn);

        // keep track of dead balls this turn
        players.getPlayer().addDeadBalls(turn.getDeadBalls() + turn.getDeadBallsOnBreak());
        players.getOpponent().addDeadBalls(turn.getDeadBalls() + turn.getDeadBallsOnBreak());

        return players;
    }

    @Override void addRunOutStats(ApaNineBallPlayer player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            player.addEarlyWin();
    }

    @Override void addGamesToPlayers(ApaNineBallPlayer player1, ApaNineBallPlayer player2) {
        super.addGamesToPlayers(player1, player2);

        // keep track of balls that are remaining in the game after a win
        player1.addDeadBalls(turn.getBallsRemaining());
        player2.addDeadBalls(turn.getBallsRemaining());
    }

    @Override public ApaNineBallPlayer newPlayer() {
        return new ApaNineBallPlayer(playerName, playerRank);
    }

    @Override public ApaNineBallPlayer newOpponent() {
        return new ApaNineBallPlayer(opponentName, opponentRank);
    }
}
