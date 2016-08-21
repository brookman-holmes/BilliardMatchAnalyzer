package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.TableUtils;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
class EightBallTurnEndHelper extends TurnEndHelper {
    EightBallTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override boolean showWin() {
        return !currentPlayerBallsRemaining() && tableStatus.isGameBallMade();
    }

    @Override boolean lostGame() {
        return (tableStatus.getGameBallMadeIllegally() || tableStatus.getBallStatus(8) == BallStatus.GAME_BALL_DEAD_ON_BREAK)
                || (currentPlayerBallsRemaining() && tableStatus.isGameBallMade());
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
            return TableUtils.getSolidsRemaining(tableStatus.getBallStatuses()) > 0;
        } else if (game.currentPlayerColor == PlayerColor.STRIPES) {
            return TableUtils.getStripesRemaining(tableStatus.getBallStatuses()) > 0;
        } else {
            return TableUtils.getSolidsRemaining(tableStatus.getBallStatuses()) > 0 && TableUtils.getStripesRemaining(tableStatus.getBallStatuses()) > 0;
        }
    }
}