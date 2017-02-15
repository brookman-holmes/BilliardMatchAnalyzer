package com.brookmanholmes.bma.ui.stats;

import android.os.Bundle;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.view.HowMissLayout;

import butterknife.Bind;

import static com.brookmanholmes.billiards.turn.AdvStats.HowType.AIM_LEFT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.AIM_RIGHT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.TOO_HARD;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.TOO_SOFT;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvBreakingStatsFragment extends BaseAdvStatsFragment {
    @Bind(R.id.tvBreakErrorsTitle)
    TextView title;

    @Bind(R.id.hmlSpeed)
    HowMissLayout speed;
    @Bind(R.id.hmlAim)
    HowMissLayout aim;

    @Bind(R.id.miscues)
    TextView miscues;

    public static AdvBreakingStatsFragment create(Bundle args) {
        AdvBreakingStatsFragment frag = new AdvBreakingStatsFragment();
        frag.setArguments(args);

        return frag;
    }

    public static AdvBreakingStatsFragment create(String name) {
        AdvBreakingStatsFragment frag = new AdvBreakingStatsFragment();
        Bundle args = new Bundle();
        args.putString(AdvStatsDialog.ARG_PLAYER_NAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override
    void updateView() {
        speed.setWeights(StatsUtils.getHowError(stats, TOO_SOFT, TOO_HARD));
        aim.setWeights(StatsUtils.getHowError(stats, AIM_LEFT, AIM_RIGHT));
        miscues.setText(getString(R.string.title_miscues, StatsUtils.getMiscues(stats)));
        title.setText(getString(R.string.title_break_errors, stats.size()));
    }

    @Override
    String[] getShotTypes() {
        return AdvStats.ShotType.getBreaks();
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_adv_break_stats;
    }
}
