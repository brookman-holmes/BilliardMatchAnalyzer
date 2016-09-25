package com.brookmanholmes.bma.ui.stats;

import android.os.Bundle;

import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.List;

/**
 * Created by Brookman Holmes on 9/24/2016.
 */

public class AdvSafetyStatsFragment2 extends BaseAdvStatsFragment2 {

    public static AdvSafetyStatsFragment2 create(Bundle args) {
        AdvSafetyStatsFragment2 frag = new AdvSafetyStatsFragment2();
        frag.setArguments(args);

        return frag;
    }

    public static AdvSafetyStatsFragment2 create(String name) {
        AdvSafetyStatsFragment2 frag = new AdvSafetyStatsFragment2();
        Bundle args = new Bundle();
        args.putString(AdvStatsDialog.ARG_PLAYER_NAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override
    String[] getShotTypes() {
        return AdvStats.ShotType.getSafeties();
    }

    @Override
    List<StatLineItem> getStatLineItems(List<AdvStats> stats) {
        return StatsUtils.getSafetyStats(getContext(), stats);
    }
}
