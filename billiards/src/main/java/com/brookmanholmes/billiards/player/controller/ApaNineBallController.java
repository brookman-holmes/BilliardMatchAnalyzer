package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.player.Pair;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.ITurn;

/**
 * Created by Brookman Holmes on 1/12/2016.
 * A controller for adding up player stats for APA 9 ball
 */
class ApaNineBallController extends PlayerController {
    public ApaNineBallController(String playerId, String opponentId, String playerName, String opponentName, int playerRank, int opponentRank) {
        super(playerId, opponentId, playerName, opponentName, playerRank, opponentRank);
    }

    @Override
    void addBreakingStats(Player player) {
        super.addBreakingStats(player);

        if (turn.isGameBallMadeOnBreak())
            player.addWinOnBreak();
    }

    @Override
    public Pair<Player> addTurn(GameStatus gameStatus, ITurn turn) {
        Pair<Player> players = super.addTurn(gameStatus, turn);

        // keep track of dead balls this turn
        players.getPlayer().addDeadBalls(turn.getDeadBalls() + turn.getDeadBallsOnBreak());
        players.getOpponent().addDeadBalls(turn.getDeadBalls() + turn.getDeadBallsOnBreak());

        return players;
    }

    @Override
    void addRunOutStats(Player player) {
        super.addRunOutStats(player);

        if (turn.getBallsRemaining() > 0)
            player.addEarlyWin();
    }

    @Override
    void addGamesToPlayers(Player player1, Player player2) {
        super.addGamesToPlayers(player1, player2);

        // keep track of balls that are remaining in the game after a win
        player1.addDeadBalls(turn.getBallsRemaining());
        player2.addDeadBalls(turn.getBallsRemaining());
    }
}
