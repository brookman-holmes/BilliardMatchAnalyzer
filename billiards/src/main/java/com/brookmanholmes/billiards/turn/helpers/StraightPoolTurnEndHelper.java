package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.ITableStatus;

/**
 * Created by Brookman Holmes on 12/1/2016.
 */

class StraightPoolTurnEndHelper extends TurnEndHelper {
    StraightPoolTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override
    boolean showWin() {
        return false;
    }

    @Override
    boolean lostGame() {
        return false;
    }

    @Override
    boolean showTurnSkip() {
        return false;
    }

    @Override
    boolean showPush() {
        return false;
    }

    @Override
    boolean showSafety() {
        return true;
    }

    @Override
    boolean showSafetyMiss() {
        return true;
    }

    @Override
    boolean showMiss() {
        return true;
    }

    @Override
    boolean checkFoul() {
        return false;
    }

    @Override
    boolean showBreakMiss() {
        return false;
    }

    @Override
    boolean reallyLostGame() {
        return false;
    }
}
