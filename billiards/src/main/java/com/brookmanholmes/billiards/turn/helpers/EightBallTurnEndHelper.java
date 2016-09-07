package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.PlayerColor;
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
        return tableStatus.getGameBallMadeIllegally() ||
                (currentPlayerBallsRemaining() && tableStatus.isGameBallMade());
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

    /**
     * Returns whether the current player has any balls remaining on the table
     * @return True if the player has balls remaining, false otherwise
     */
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