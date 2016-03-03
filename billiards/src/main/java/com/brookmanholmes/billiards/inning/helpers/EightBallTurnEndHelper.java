package com.brookmanholmes.billiards.inning.helpers;

import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.inning.TableUtils;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
class EightBallTurnEndHelper extends TurnEndHelper {
    @Override
    boolean showWin() {
        return !currentPlayerBallsRemaining() && nextInning.getGameBallMade();
    }

    @Override
    boolean showLoss() {
        return nextInning.getGameBallMadeIllegally()
                || (currentPlayerBallsRemaining() && nextInning.getGameBallMade());
    }

    @Override
    boolean showSafety() {
        return super.showSafety() && !showLoss();
    }

    @Override
    boolean showSafetyMiss() {
        return super.showSafetyMiss() && !showLoss();
    }

    @Override
    boolean showMiss() {
        return super.showMiss() && !showLoss();
    }

    @Override
    boolean showPush() {
        return false;
    }

    @Override
    boolean showTurnSkip() {
        return false;
    }

    boolean currentPlayerBallsRemaining() {
        if (game.currentPlayerColor == PlayerColor.SOLIDS) {
            return TableUtils.getSolidsRemaining(nextInning.getBallStatuses()) > 0;
        } else if (game.currentPlayerColor == PlayerColor.STRIPES) {
            return TableUtils.getStripesRemaining(nextInning.getBallStatuses()) > 0;
        } else {
            return TableUtils.getSolidsRemaining(nextInning.getBallStatuses()) > 0 && TableUtils.getStripesRemaining(nextInning.getBallStatuses()) > 0;
        }
    }
}