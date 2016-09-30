package com.brookmanholmes.bma.ui.matchinfo;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.data.SampleMatchProvider;

import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by Brookman Holmes on 9/7/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HohmannSvbEspressoTest extends BaseMatchTest {
    @Override
    protected Match getMatch() {
        return SampleMatchProvider.getHohmannSvbMatch();
    }

    @Override
    protected List<ITurn> getTurns() {
        return SampleMatchProvider.getHohmannSvbTurns();
    }
}
