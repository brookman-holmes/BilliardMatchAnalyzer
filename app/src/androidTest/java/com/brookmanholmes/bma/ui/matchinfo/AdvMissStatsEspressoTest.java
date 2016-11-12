package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.turn.ITurn;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.turn.AdvStats.HowType.BANK_LONG;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.BANK_SHORT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.CURVE_EARLY;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.CURVE_LATE;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICK_LONG;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICK_SHORT;

/**
 * Created by helios on 9/9/2016.
 */

public class AdvMissStatsEspressoTest extends AdvStatsEspressoTest {
    // bank shots
    static ITurn bank1 = turn().setAdvStats(bank(BANK_LONG)).miss();
    static ITurn bank2 = turn().setAdvStats(bank(BANK_SHORT)).miss();
    // kick shots
    static ITurn kick1 = turn().setAdvStats(kick(KICK_LONG)).miss();
    static ITurn kick2 = turn().setAdvStats(kick(KICK_SHORT)).miss();
    // masse shots
    static ITurn masse1 = turn().setAdvStats(masse(CURVE_EARLY)).miss();
    static ITurn masse2 = turn().setAdvStats(masse(CURVE_LATE)).miss();

    @Override
    protected List<ITurn> getTurns() {
        return Arrays.asList();
    }

    @Test
    public void kicks() {
        mainTest(list(break0, kick1, kick2));
    }

    @Test
    public void masses() {
        mainTest(list(break0, masse1, masse2));
    }

    @Test
    public void banks() {
        mainTest(list(break0, bank1, bank2));
    }

    @Test
    public void caroms() {

    }

    @Test
    public void combos() {

    }

    @Test
    public void jumps() {

    }

    @Test
    public void straightShots() {

    }

    @Test
    public void cuts() {

    }

    @Test
    public void whys() {

    }


}
