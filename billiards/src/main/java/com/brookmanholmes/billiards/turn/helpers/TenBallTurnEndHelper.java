package com.brookmanholmes.billiards.turn.helpers;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
class TenBallTurnEndHelper extends RotationTurnEndHelper {
    @Override boolean showWin() {
        return nextInning.getGameBallMade();
    }

    @Override boolean checkFoul() {
        return nextInning.getDeadBallsOnBreak() > 0;
    }
}
