package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvSafetyStatsFragment extends BaseAdvStatsFragment {
    final static List<String> array = Arrays.asList(new String[]{"Safety", "Safety error"});

    @Bind(R.id.successfulSafetiesTitle) TextView safetyResults;
    @Bind(R.id.safetyErrorsTitle) TextView safetyErrorsTitle;
    @Bind(R.id.over) TextView overCut;
    @Bind(R.id.under) TextView underCut;
    @Bind(R.id.fast) TextView fast;
    @Bind(R.id.slow) TextView slow;
    @Bind(R.id.kickLong) TextView kickLong;
    @Bind(R.id.kickShort) TextView kickShort;


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

    @Override void updateView() {
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getContext(), stats), overCut, underCut);
        StatsUtils.setLayoutWeights(StatsUtils.getHowSpeedErrors(getContext(), stats), slow, fast);
        StatsUtils.setLayoutWeights(StatsUtils.getHowKickErrors(getContext(), stats), kickShort, kickLong);

        if (statsLayout != null) {
            StatsUtils.setListOfSafetyStats(statsLayout, stats);
            safetyResults.setText(getString(R.string.title_safety_results, stats.size()));
            safetyErrorsTitle.setText(
                    getString(R.string.title_safety_errors,
                            StatsUtils.getFailedSafeties(getContext(), stats)));
        }
    }

    @Override List<String> getShotTypes() {
        return array;
    }

    @Override int getLayoutId() {
        return R.layout.fragment_adv_safety_stats;
    }
}
