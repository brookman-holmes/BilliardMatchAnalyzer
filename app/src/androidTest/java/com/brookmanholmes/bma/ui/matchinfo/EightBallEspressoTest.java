package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.data.SampleMatchProvider;

import java.util.List;

/**
 * Created by Brookman Holmes on 9/7/2016.
 */
public class EightBallEspressoTest extends BaseMatchTest {
    @Override
    protected Match getMatch() {
        return SampleMatchProvider.getTestEightBallMatch();
    }

    @Override
    protected List<ITurn> getTurns() {
        return SampleMatchProvider.getTestEightBallTurns();
    }
}
