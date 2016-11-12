package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.turn.ITurn;

import java.util.List;

/**
 * Created by helios on 9/9/2016.
 */

public class AdvBreakHowMissEspressoTest extends AdvStatsEspressoTest {
    @Override
    protected List<ITurn> getTurns() {
        return list(break0, wonGame,
                break1, wonGame);
    }
}
