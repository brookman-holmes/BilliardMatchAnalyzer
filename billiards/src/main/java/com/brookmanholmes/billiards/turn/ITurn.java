package com.brookmanholmes.billiards.turn;

import java.util.List;

/**
 * Created by Brookman Holmes on 11/4/2015.
 */
public interface ITurn extends ITableStatus {
    boolean isFoul();

    boolean isGameLost();

    TurnEnd getTurnEnd();

    AdvStats getAdvStats();
}
