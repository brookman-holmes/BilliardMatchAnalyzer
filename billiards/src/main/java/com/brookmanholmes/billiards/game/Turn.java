package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.inning.TableStatusInterface;
import com.brookmanholmes.billiards.inning.TurnEnd;

import java.util.List;

/**
 * Created by Brookman Holmes on 11/4/2015.
 */
public interface Turn extends TableStatusInterface {
    boolean isScratch();

    boolean isGameLost();

    List<Integer> getBallsToRemoveFromTable();

    TurnEnd getTurnEnd();
}
