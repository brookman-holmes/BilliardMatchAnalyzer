package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;

import org.junit.Test;

import java.util.List;

import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICK_LONG;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICK_SHORT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.MISCUE;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.THICK;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.THIN;
import static com.brookmanholmes.billiards.turn.AdvStats.SubType.FULL_HOOK;
import static com.brookmanholmes.billiards.turn.AdvStats.SubType.LONG_T;
import static com.brookmanholmes.billiards.turn.AdvStats.SubType.NO_DIRECT_SHOT;
import static com.brookmanholmes.billiards.turn.AdvStats.SubType.PARTIAL_HOOK;
import static com.brookmanholmes.billiards.turn.AdvStats.SubType.SHORT_T;

/**
 * Created by helios on 9/9/2016.
 */

public class AdvSafetyEspressoTest extends AdvStatsEspressoTest {
    // safeties
    static ITurn turn1 = turn().setAdvStats(safety(FULL_HOOK)).safety();
    static ITurn turn2 = turn().setAdvStats(safety(PARTIAL_HOOK)).safety();
    static ITurn turn3 = turn().setAdvStats(safety(LONG_T)).safety();
    static ITurn turn4 = turn().setAdvStats(safety(SHORT_T)).safety();
    static ITurn turn5 = turn().setAdvStats(safety(NO_DIRECT_SHOT)).safety();
    // safety misses
    static ITurn safetyMiss1 = turn().setAdvStats(safetyMiss(KICK_LONG, MISCUE, AdvStats.HowType.TOO_HARD, THICK)).safetyMiss();
    static ITurn safetyMiss2 = turn().setAdvStats(safetyMiss(KICK_SHORT, MISCUE, AdvStats.HowType.TOO_SOFT, THIN)).safetyMiss();

    @Override
    protected List<ITurn> getTurns() {
        return list();
    }

    @Test
    public void safeties() {
        mainTest(list(break0, turn1, turn2, turn3, turn4, turn5));
    }

    @Test
    public void safetyMisses() {
        mainTest(list(break0, safetyMiss1, safetyMiss2));
    }

}
