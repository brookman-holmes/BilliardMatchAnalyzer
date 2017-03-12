package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Abstract subclass of {@link com.brookmanholmes.billiards.game.Game} that keeps track of the status of a
 * game of rotation (9, 10 ball)
 * <p></p>Created by Brookman Holmes on 10/25/2015.
 */
abstract class RotationGame extends Game {
    RotationGame(GameType gameType, PlayerTurn playerTurn, BreakType breakType) {
        super(gameType, playerTurn, breakType);
        allowPush = true;
    }

    RotationGame(GameType gameType, PlayerTurn turn, BreakType breakType, int maxAttemptsPerGame) {
        super(gameType, turn, breakType, maxAttemptsPerGame);
    }

    @Override
    void startNewGame(ITurn turn) {
        super.startNewGame(turn);
        allowPush = true;
    }

    @Override
    boolean setAllowPlayerToBreakAgain(ITurn turn) {
        return false;
    }

    @Override
    PlayerColor setPlayerColor(ITurn turn) {
        return PlayerColor.OPEN;
    }

    @Override
    boolean setAllowTurnSkip(ITurn turn) {
        return turn.getTurnEnd() == TurnEnd.PUSH_SHOT;
    }

    @Override
    boolean setAllowPush(ITurn turn) {
        return (turn.getBreakBallsMade() == 0 && !turn.isFoul() && turn.getTurnEnd() == TurnEnd.BREAK_MISS);
    }

    @Override
    public int[] getGhostBallsToWinGame() {
        int[] ballsToWin = new int[ballsOnTable.size()];

        for (int i = 0; i < ballsOnTable.size(); i++) {
            ballsToWin[i] = ballsOnTable.get(i);
        }

        return ballsToWin;
    }
}
