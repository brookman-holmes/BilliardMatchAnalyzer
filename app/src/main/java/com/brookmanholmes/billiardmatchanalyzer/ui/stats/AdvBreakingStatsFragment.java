package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvBreakingStatsFragment extends BaseAdvStatsFragment {
    final static String[] array = new String[]{"Break shot"};
    @Bind(R.id.tvBreakErrorsTitle) TextView title;
    @Bind(R.id.left) TextView leftOfAim;
    @Bind(R.id.right) TextView rightOfAim;
    @Bind(R.id.fast) TextView fast;
    @Bind(R.id.slow) TextView slow;


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

    @Override void updateView(View view) {
        StatsUtils.setLayoutWeights(StatsUtils.getHowAimErrors(getContext(), stats), leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(StatsUtils.getHowSpeedErrors(getContext(), stats), slow, fast);

        title.setText(getString(R.string.title_break_errors, stats.size()));

        StatsUtils.updateGridOfMissReasons((GridLayout) view.findViewById(R.id.grid), StatsUtils.getFourMostCommonItems(stats));
    }

    @Override String[] getShotTypes() {
        return array;
    }

    @Override int getLayoutId() {
        return R.layout.container_adv_break_stats;
    }
}
