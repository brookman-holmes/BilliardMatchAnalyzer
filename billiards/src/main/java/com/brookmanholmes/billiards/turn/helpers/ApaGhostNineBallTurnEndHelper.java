package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.ITableStatus;

/**
 * Created by Brookman Holmes on 4/27/2016.
 */
class ApaGhostNineBallTurnEndHelper extends TurnEndHelper {
    ApaGhostNineBallTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override
    boolean showWin() {
        return tableStatus.isGameBallMade() || tableStatus.isGameBallMadeOnBreak();
    }

    @Override
    boolean lostGame() {
        return false;
    }

    @Override
    boolean showPush() {
        return false;
    }

    @Override
    boolean showTurnSkip() {
        return false;
    }

    @Override
    boolean showSafety() {
        return false;
    }

    @Override
    boolean showSafetyMiss() {
        return false;
    }

    @Override
    boolean showBreakMiss() {
        return false;
    }
}
