package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.turn.TableUtils;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
class EightBallTurnEndHelper extends TurnEndHelper {
    @Override boolean showWin() {
        return !currentPlayerBallsRemaining() && nextInning.isGameBallMade();
    }

    @Override boolean lostGame() {
        return (nextInning.getGameBallMadeIllegally() || nextInning.getBallStatus(8) == BallStatus.GAME_BALL_DEAD_ON_BREAK)
                || (currentPlayerBallsRemaining() && nextInning.isGameBallMade());
    }

    @Override boolean checkFoul() {
        return super.checkFoul() || lostGame();
    }

    @Override boolean showSafety() {
        return super.showSafety() && !lostGame();
    }

    @Override boolean showPush() {
        return false;
    }

    @Override boolean showTurnSkip() {
        return false;
    }

    private boolean currentPlayerBallsRemaining() {
        if (game.currentPlayerColor == PlayerColor.SOLIDS) {
            return TableUtils.getSolidsRemaining(nextInning.getBallStatuses()) > 0;
        } else if (game.currentPlayerColor == PlayerColor.STRIPES) {
            return TableUtils.getStripesRemaining(nextInning.getBallStatuses()) > 0;
        } else {
            return TableUtils.getSolidsRemaining(nextInning.getBallStatuses()) > 0 && TableUtils.getStripesRemaining(nextInning.getBallStatuses()) > 0;
        }
    }
}