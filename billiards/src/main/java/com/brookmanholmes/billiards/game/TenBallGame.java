package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Subclass of {@link com.brookmanholmes.billiards.game.RotationGame} that keeps track of the status
 * of a game of 10 ball (BCA rule set)
 * <p></p>Created by Brookman Holmes on 10/27/2015.
 */
class TenBallGame extends RotationGame {
    private final static int GAME_BALL = 10;
    private final static int MAX_BALLS = 10;

    TenBallGame(PlayerTurn playerTurn, BreakType breakType) {
        super(GameType.BCA_TEN_BALL, playerTurn, breakType, MAX_BALLS, GAME_BALL);
    }

    @Override boolean setAllowTurnSkip(ITurn turn) {
        return super.setAllowTurnSkip(turn) ||
                (turn.getTurnEnd() == TurnEnd.MISS || turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) &&
                        turn.getDeadBalls() > 0 && !turn.isFoul();
    }
}
