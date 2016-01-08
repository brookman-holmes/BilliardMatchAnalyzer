package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;

import java.util.List;

/**
 * Created by Brookman Holmes on 11/4/2015.
 */
public interface Turn {
    int getShootingBallsMade();

    int getDeadBalls();

    int getDeadBallsOnBreak();

    int getBreakBallsMade();

    boolean isScratch();

    List<Integer> getBallsToRemoveFromTable();

    TurnEnd getTurnEnd();

    TableStatus getTableStatus();
}
