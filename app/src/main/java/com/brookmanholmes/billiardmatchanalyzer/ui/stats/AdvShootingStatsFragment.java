package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvShootingStatsFragment extends BaseAdvStatsFragment {
    @Bind(R.id.over) TextView overCut;
    @Bind(R.id.under) TextView underCut;
    @Bind(R.id.left) TextView leftOfAim;
    @Bind(R.id.right) TextView rightOfAim;
    @Bind(R.id.shootingErrorTitle) TextView title;

    public static AdvShootingStatsFragment create(Bundle args) {
        AdvShootingStatsFragment frag = new AdvShootingStatsFragment();
        frag.setArguments(args);

        return frag;
    }

    public static AdvShootingStatsFragment create(String name) {
        AdvShootingStatsFragment frag = new AdvShootingStatsFragment();
        Bundle args = new Bundle();
        args.putString(AdvStatsDialog.ARG_PLAYER_NAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override String[] getShotTypes() {
        return getContext().getResources().getStringArray(R.array.shot_types);
    }

    @Override void updateView(View view) {
        StatsUtils.setLayoutWeights(StatsUtils.getHowAimErrors(getContext(), stats), leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getContext(), stats), overCut, underCut);

        title.setText(getString(R.string.title_shooting_errors, stats.size()));

        StatsUtils.updateGridOfMissReasons(view, StatsUtils.getFourMostCommonItems(stats));
    }

    @Override int getLayoutId() {
        return R.layout.container_adv_shooting_stats;
    }
}
