package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.TableUtils;

/**
 * Created by Brookman Holmes on 4/27/2016.
 */
class GhostTurnEndHelper extends TurnEndHelper {
    GhostTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override boolean showWin() {
        if (game.gameType != GameType.BCA_EIGHT_BALL)
            return tableStatus.getShootingBallsMade() + tableStatus.getBreakBallsMade() >= game.MAX_BALLS && tableStatus.isGameBallMade();
        else {
            return (TableUtils.getSolidsRemaining(tableStatus.getBallStatuses()) == 0 || TableUtils.getStripesRemaining(tableStatus.getBallStatuses()) == 0) && tableStatus.isGameBallMade();
        }
    }

    @Override boolean lostGame() {
        return false;
    }

    @Override boolean showPush() {
        return false;
    }

    @Override boolean showTurnSkip() {
        return false;
    }

    @Override boolean showSafety() {
        return false;
    }

    @Override boolean showSafetyMiss() {
        return false;
    }

    @Override boolean showBreakMiss() {
        return false;
    }
}
