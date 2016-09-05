package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.turn.ITableStatus;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
// TODO: 10/27/2015 eliminate making all of your color but have a dead ball and also legally make the eight and winning the game be an option
class ApaEightBallTurnEndHelper extends EightBallTurnEndHelper {
    ApaEightBallTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override boolean showWin() {
        return super.showWin() || tableStatus.getGameBallMadeOnBreak();
    }

    @Override boolean lostGame() {
        return super.lostGame() || tableStatus.getBallStatus(game.GAME_BALL) == BallStatus.DEAD_ON_BREAK;
    }

    @Override boolean checkFoul() {
        return super.checkFoul() || tableStatus.getDeadBalls() > 0;
    }

    @Override boolean showSafety() {
        return super.showSafety() && tableStatus.getDeadBalls() == 0;
    }
}
