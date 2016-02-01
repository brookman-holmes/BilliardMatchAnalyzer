package com.brookmanholmes.billiards.inning;

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

    boolean getGameBallMade();

    int getBallsRemaining();

    BallStatus getBallStatus(int ball);

    boolean getGameBallMadeIllegally();

    List<BallStatus> getBallStatuses();

    int size();

    GameType getGameType();
}
