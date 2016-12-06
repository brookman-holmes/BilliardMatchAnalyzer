package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;

import java.util.List;

/**
 * Created by Brookman Holmes on 12/3/2016.
 */

public class StraightPoolTableStatus implements ITableStatus {
    @Override
    public int getShootingBallsMade() {
        return 0;
    }

    @Override
    public int getDeadBalls() {
        return 0;
    }

    @Override
    public int getDeadBallsOnBreak() {
        return 0;
    }

    @Override
    public int getBreakBallsMade() {
        return 0;
    }

    @Override
    public boolean isGameBallMadeOnBreak() {
        return false;
    }

    @Override
    public boolean isGameBallMade() {
        return false;
    }

    @Override
    public int getBallsRemaining() {
        return 0;
    }

    @Override
    public BallStatus getBallStatus(int ball) throws InvalidBallException {
        return null;
    }

    @Override
    public void setBallTo(BallStatus status, int... balls) {

    }

    @Override
    public boolean isGameBallMadeIllegally() {
        return false;
    }

    @Override
    public List<BallStatus> getBallStatuses() {
        return null;
    }

    @Override
    public List<Integer> getBallsToRemoveFromTable() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public GameType getGameType() {
        return null;
    }

    @Override
    public int getGameBall() {
        return 0;
    }
}
