package com.brookmanholmes.billiards.inning.helpers;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
class RotationTurnEndHelper extends TurnEndHelper {
    // TODO: 10/30/2015 deal with things like dead balls affecting allowing showing of safety etc.
    @Override
    boolean showWin() {
        return nextInning.getGameBallMade()
                || nextInning.getGameBallMadeOnBreak();
    }

    @Override
    boolean showLoss() {
        return game.currentPlayerConsecutiveFouls >= 2;
    }

    @Override
    boolean checkScratch() {
        return super.checkScratch() || nextInning.getDeadBalls() > 0;
    }

    @Override
    boolean showSafety() {
        return super.showSafety() && nextInning.getDeadBalls() == 0;
    }

    // // TODO: 1/29/2016 add in a test to make sure that push shot doesn't show when making the 9 on the break
    @Override
    boolean showPush() {
        return super.showPush() && !showWin();
    }
}
