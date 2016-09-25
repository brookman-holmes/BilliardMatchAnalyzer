package com.brookmanholmes.bma.ui.stats;

import android.os.Bundle;

import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.List;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class AdvShootingStatsFragment2 extends BaseAdvStatsFragment2 {

    public static AdvShootingStatsFragment2 create(Bundle args) {
        AdvShootingStatsFragment2 frag = new AdvShootingStatsFragment2();
        frag.setArguments(args);

        return frag;
    }

    public static AdvShootingStatsFragment2 create(String name) {
        AdvShootingStatsFragment2 frag = new AdvShootingStatsFragment2();
        Bundle args = new Bundle();
        args.putString(AdvStatsDialog.ARG_PLAYER_NAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override
    String[] getShotTypes() {
        return AdvStats.ShotType.getShots();
    }

    @Override
    List<StatLineItem> getStatLineItems(List<AdvStats> stats) {
        return StatsUtils.getStats(getContext(), stats);
    }
}
