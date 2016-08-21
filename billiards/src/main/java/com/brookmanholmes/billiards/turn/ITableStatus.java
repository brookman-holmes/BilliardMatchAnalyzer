package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;

import java.util.List;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public interface ITableStatus {
    int getShootingBallsMade();

    int getDeadBalls();

    int getDeadBallsOnBreak();

    int getBreakBallsMade();

    boolean getGameBallMadeOnBreak();

    boolean isGameBallMade();

    int getBallsRemaining();

    BallStatus getBallStatus(int ball);

    void setBallTo(BallStatus status, int... i);

    boolean getGameBallMadeIllegally();

    List<BallStatus> getBallStatuses();

    List<Integer> getBallsToRemoveFromTable();

    int size();

    GameType getGameType();
}
