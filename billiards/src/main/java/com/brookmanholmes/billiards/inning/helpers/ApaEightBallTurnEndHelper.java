package com.brookmanholmes.billiards.inning.helpers;

import com.brookmanholmes.billiards.game.util.BallStatus;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
// TODO: 10/27/2015 eliminate making all of your color but have a dead ball and also legally make the eight and winning the game be an option
class ApaEightBallTurnEndHelper extends EightBallTurnEndHelper {
    @Override
    boolean showWin() {
        return super.showWin() || nextInning.getGameBallMadeOnBreak();
    }

    @Override
    boolean lostGame() {
        return super.lostGame() || nextInning.getBallStatus(game.GAME_BALL) == BallStatus.DEAD_ON_BREAK;
    }

    @Override
    boolean checkFoul() {
        return super.checkFoul() || nextInning.getDeadBalls() > 0;
    }

    @Override
    boolean showSafety() {
        return super.showSafety() && nextInning.getDeadBalls() == 0;
    }
}
