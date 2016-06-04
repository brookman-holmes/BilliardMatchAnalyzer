package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvSafetyStatsFragment extends BaseAdvStatsFragment {
    final static String[] array = new String[]{"Safety", "Safety error"};
    private static final int FULL_HOOK = 0,
            PARTIAL_HOOK = 1,
            LONG_T = 2,
            SHORT_T = 3,
            NO_DIRECT_SHOT = 4,
            OPEN = 5;

    @Bind(R.id.successfulSafetiesTitle) TextView safetyResults;
    @Bind(R.id.safetyErrorsTitle) TextView safetyErrorsTitle;
    @Bind(R.id.over) TextView overCut;
    @Bind(R.id.under) TextView underCut;
    @Bind(R.id.fast) TextView fast;
    @Bind(R.id.slow) TextView slow;


    public static AdvSafetyStatsFragment create(Bundle args) {
        AdvSafetyStatsFragment frag = new AdvSafetyStatsFragment();
        frag.setArguments(args);

        return frag;
    }

    public static AdvSafetyStatsFragment create(String name) {
        AdvSafetyStatsFragment frag = new AdvSafetyStatsFragment();
        Bundle args = new Bundle();
        args.putString(AdvStatsDialog.ARG_PLAYER_NAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override void updateView(View view) {
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getContext(), stats), overCut, underCut);
        StatsUtils.setLayoutWeights(StatsUtils.getHowSpeedErrors(getContext(), stats), slow, fast);

        updateSafetyGrid(view);
    }

    @Override String[] getShotTypes() {
        return array;
    }

    @Override int getLayoutId() {
        return R.layout.container_adv_safety_stats;
    }


    private void updateSafetyGrid(View view) {
        List<StatsUtils.StatLineItem> statLineItems = StatsUtils.getSafetyStats(getContext(), stats);

        ((TextView) view.findViewById(R.id.tvFullHookCount)).setText(statLineItems.get(FULL_HOOK).getCount());
        ((TextView) view.findViewById(R.id.tvFullHookPct)).setText(statLineItems.get(FULL_HOOK).getPercentage());
        ((TextView) view.findViewById(R.id.tvPartialHookCount)).setText(statLineItems.get(PARTIAL_HOOK).getCount());
        ((TextView) view.findViewById(R.id.tvPartialHookPct)).setText(statLineItems.get(PARTIAL_HOOK).getPercentage());
        ((TextView) view.findViewById(R.id.tvLongTCount)).setText(statLineItems.get(LONG_T).getCount());
        ((TextView) view.findViewById(R.id.tvLongTPct)).setText(statLineItems.get(LONG_T).getPercentage());
        ((TextView) view.findViewById(R.id.tvShortTCount)).setText(statLineItems.get(SHORT_T).getCount());
        ((TextView) view.findViewById(R.id.tvShortTPct)).setText(statLineItems.get(SHORT_T).getPercentage());
        ((TextView) view.findViewById(R.id.tvDirectShotCount)).setText(statLineItems.get(NO_DIRECT_SHOT).getCount());
        ((TextView) view.findViewById(R.id.tvDirectShotPct)).setText(statLineItems.get(NO_DIRECT_SHOT).getPercentage());
        ((TextView) view.findViewById(R.id.tvOpenCount)).setText(statLineItems.get(OPEN).getCount());
        ((TextView) view.findViewById(R.id.tvOpenPct)).setText(statLineItems.get(OPEN).getPercentage());

        safetyErrorsTitle.setText(getString(R.string.title_safety_errors, statLineItems.get(OPEN).getCount()));
        safetyResults.setText(getString(R.string.title_safety_results, stats.size()));
    }
}
