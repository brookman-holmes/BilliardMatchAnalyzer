package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.ITableStatus;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
class ApaEightBallTurnEndHelper extends EightBallTurnEndHelper {
    ApaEightBallTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override
    boolean showWin() {
        return super.showWin() || tableStatus.isGameBallMadeOnBreak();
    }

    @Override
    boolean lostGame() {
        return super.lostGame() || tableStatus.isGameBallMadeIllegally();
    }

    @Override
    boolean showSafety() {
        return super.showSafety() && tableStatus.getDeadBalls() == 0;
    }
}
