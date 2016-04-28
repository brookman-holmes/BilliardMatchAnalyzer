package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.TableUtils;

/**
 * Created by Brookman Holmes on 4/27/2016.
 */
class GhostTurnEndHelper extends TurnEndHelper {
    @Override boolean showWin() {
        if (game.gameType != GameType.BCA_EIGHT_BALL)
            return nextInning.getShootingBallsMade() + nextInning.getBreakBallsMade() >= game.MAX_BALLS && nextInning.isGameBallMade();
        else {
            return (TableUtils.getSolidsRemaining(nextInning.getBallStatuses()) == 0 || TableUtils.getStripesRemaining(nextInning.getBallStatuses()) == 0) && nextInning.isGameBallMade();
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
