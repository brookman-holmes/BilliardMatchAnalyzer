package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.turn.ITableStatus;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
class RotationTurnEndHelper extends TurnEndHelper {
    RotationTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override boolean showWin() {
        return tableStatus.isGameBallMade()
                || tableStatus.getGameBallMadeOnBreak();
    }

    @Override boolean lostGame() {
        return game.currentPlayerConsecutiveFouls >= 2 && tableStatus.getShootingBallsMade() == 0;
    }

    @Override boolean checkFoul() {
        return super.checkFoul() || tableStatus.getDeadBalls() > 0;
    }

    @Override boolean showSafety() {
        return super.showSafety() && tableStatus.getDeadBalls() == 0;
    }

    // // TODO: 1/29/2016 add in a test to make sure that push shot doesn't show when making the 9 on the break
    // TODO: 3/9/2016 add in test to make sure you don't show push shot when the player fouls
    @Override boolean showPush() {
        return super.showPush() && !showWin() && !checkFoul() && game.gameType != GameType.APA_NINE_BALL;
    }
}
