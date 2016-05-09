package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 10/27/2015.
 */
class TenBallGame extends RotationGame {
    final static int GAME_BALL = 10;
    final static int MAX_BALLS = 10;

    TenBallGame(PlayerTurn playerTurn, BreakType breakType) {
        super(GameType.BCA_TEN_BALL, playerTurn, breakType, MAX_BALLS, GAME_BALL);
    }

    @Override boolean setAllowTurnSkip(com.brookmanholmes.billiards.turn.Turn turn) {
        return super.setAllowTurnSkip(turn) ||
                (turn.getTurnEnd() == TurnEnd.MISS || turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) &&
                        turn.getDeadBalls() > 0 && !turn.isScratch();
    }
}
