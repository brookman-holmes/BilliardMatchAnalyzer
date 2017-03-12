package com.brookmanholmes.bma.ui.stats;

import com.brookmanholmes.billiards.turn.ITurn;

import java.util.Collection;

/**
 * Created by Brookman Holmes on 3/5/2017.
 */

public interface TurnListener {
    void setAdvStats(Collection<ITurn> turns);
}
