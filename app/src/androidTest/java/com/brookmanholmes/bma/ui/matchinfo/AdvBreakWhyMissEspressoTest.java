package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.turn.ITurn;

import java.util.Arrays;
import java.util.List;

/**
 * Created by helios on 9/9/2016.
 */

public class AdvBreakWhyMissEspressoTest extends AdvStatsEspressoTest {
    @Override
    protected List<ITurn> getTurns() {
        return Arrays.asList(
                break2, wonGame,
                break3, wonGame,
                break4, wonGame,
                break5, wonGame);
    }
}
