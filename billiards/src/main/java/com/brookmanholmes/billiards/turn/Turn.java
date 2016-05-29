package com.brookmanholmes.billiards.turn;

import java.util.List;

/**
 * Created by Brookman Holmes on 11/4/2015.
 */
public interface Turn extends ITableStatus {
    boolean isFoul();

    boolean isGameLost();

    List<Integer> getBallsToRemoveFromTable();

    TurnEnd getTurnEnd();
}
