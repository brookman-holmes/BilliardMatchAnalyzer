package com.brookmanholmes.bma.ui.stats;

import android.os.Bundle;

import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.List;

/**
 * Created by Brookman Holmes on 9/24/2016.
 */

public class AdvBreakingStatsFragment2 extends BaseAdvStatsFragment2 {

    public static AdvBreakingStatsFragment2 create(Bundle args) {
        AdvBreakingStatsFragment2 frag = new AdvBreakingStatsFragment2();
        frag.setArguments(args);

        return frag;
    }

    public static AdvBreakingStatsFragment2 create(String name) {
        AdvBreakingStatsFragment2 frag = new AdvBreakingStatsFragment2();
        Bundle args = new Bundle();
        args.putString(AdvStatsDialog.ARG_PLAYER_NAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override
    String[] getShotTypes() {
        return AdvStats.ShotType.getBreaks();
    }

    @Override
    List<StatLineItem> getStatLineItems(List<AdvStats> stats) {
        return StatsUtils.getStats(getContext(), stats);
    }
}
