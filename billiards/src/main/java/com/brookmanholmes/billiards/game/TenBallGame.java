package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Subclass of {@link com.brookmanholmes.billiards.game.RotationGame} that keeps track of the status
 * of a game of 10 ball (BCA rule set)
 * <p></p>Created by Brookman Holmes on 10/27/2015.
 */
class TenBallGame extends RotationGame {
    TenBallGame(PlayerTurn playerTurn, BreakType breakType) {
        super(GameType.BCA_TEN_BALL, playerTurn, breakType);
    }

    TenBallGame(GameType gameType, PlayerTurn playerTurn, BreakType breakType) {
        super(gameType, playerTurn, breakType);
    }

    TenBallGame(GameType gameType, PlayerTurn turn, BreakType breakType, int maxAttemptsPerGame) {
        super(gameType, turn, breakType, maxAttemptsPerGame);
    }

    @Override boolean setAllowTurnSkip(ITurn turn) {
        return super.setAllowTurnSkip(turn) ||
                (turn.getTurnEnd() == TurnEnd.MISS || turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) &&
                        turn.getDeadBalls() > 0 && !turn.isFoul();
    }
}
