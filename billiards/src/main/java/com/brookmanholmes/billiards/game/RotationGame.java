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

    @Override void startNewGame(Turn turn) {
        super.startNewGame(turn);
        allowPush = true;
    }

    @Override boolean setAllowPlayerToBreakAgain(Turn turn) {
        return false;
    }

    @Override PlayerColor setPlayerColor(Turn turn) {
        return PlayerColor.OPEN;
    }

    @Override boolean setAllowTurnSkip(Turn turn) {
        return turn.getTurnEnd() == TurnEnd.PUSH_SHOT;
    }

    @Override boolean setAllowPush(Turn turn) {
        return (turn.getBreakBallsMade() == 0 && !turn.isScratch() && turn.getTurnEnd() == TurnEnd.BREAK_MISS);
    }
}
