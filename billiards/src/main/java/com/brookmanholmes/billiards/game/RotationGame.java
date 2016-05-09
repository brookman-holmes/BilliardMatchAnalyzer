package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 10/25/2015.
 */
abstract class RotationGame extends Game {
    RotationGame(GameType gameType, PlayerTurn playerTurn, BreakType breakType, int maxBalls, int gameBall) {
        super(gameType, playerTurn, breakType, maxBalls, gameBall);
        allowPush = true;
    }

    @Override void startNewGame(com.brookmanholmes.billiards.turn.Turn turn) {
        super.startNewGame(turn);
        allowPush = true;
    }

    @Override boolean setAllowPlayerToBreakAgain(com.brookmanholmes.billiards.turn.Turn turn) {
        return false;
    }

    @Override PlayerColor setPlayerColor(com.brookmanholmes.billiards.turn.Turn turn) {
        return PlayerColor.OPEN;
    }

    @Override boolean setAllowTurnSkip(com.brookmanholmes.billiards.turn.Turn turn) {
        return turn.getTurnEnd() == TurnEnd.PUSH_SHOT;
    }

    @Override boolean setAllowPush(com.brookmanholmes.billiards.turn.Turn turn) {
        return (turn.getBreakBallsMade() == 0 && !turn.isScratch() && turn.getTurnEnd() == TurnEnd.BREAK_MISS);
    }

    @Override public int[] getGhostBallsToWinGame() {
        int[] ballsToWin = new int[ballsOnTable.size()];

        for (int i = 0; i < ballsOnTable.size(); i++) {
            ballsToWin[i] = ballsOnTable.get(i);
        }

        return ballsToWin;
    }
}
