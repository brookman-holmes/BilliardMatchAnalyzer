package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.TableUtils;

/**
 * Created by Brookman Holmes on 10/24/2016.
 */

class GhostEightBallTurnEndHelper extends GhostTurnEndHelper {
    GhostEightBallTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override
    boolean showWin() {
        return (TableUtils.getSolidsRemaining(tableStatus.getBallStatuses()) == 0 ||
                TableUtils.getStripesRemaining(tableStatus.getBallStatuses()) == 0)
                && tableStatus.isGameBallMade();
    }

    @Override
    boolean seriousFoul() {
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
