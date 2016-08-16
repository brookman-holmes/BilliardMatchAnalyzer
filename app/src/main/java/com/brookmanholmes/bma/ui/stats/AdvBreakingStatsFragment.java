package com.brookmanholmes.bma.ui.stats;

import android.os.Bundle;
import android.widget.TextView;

import com.brookmanholmes.bma.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvBreakingStatsFragment extends BaseAdvStatsFragment {
    final static List<String> array = Arrays.asList(new String[]{"Break shot"});
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

    @Override void updateView() {
        StatsUtils.setLayoutWeights(StatsUtils.getHowAimErrors(getContext(), stats), leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(StatsUtils.getHowSpeedErrors(getContext(), stats), slow, fast);

        title.setText(getString(R.string.title_break_errors, stats.size()));

        StatsUtils.setListOfMissReasons(this.statsLayout, stats);
    }

    @Override List<String> getShotTypes() {
        return array;
    }

    @Override int getLayoutId() {
        return R.layout.fragment_adv_break_stats;
    }
}
