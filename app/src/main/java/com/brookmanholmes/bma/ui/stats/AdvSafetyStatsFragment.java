package com.brookmanholmes.bma.ui.stats;

import android.os.Bundle;
import android.widget.TextView;

import com.brookmanholmes.bma.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class AdvSafetyStatsFragment extends BaseAdvStatsFragment {
    @Bind(R.id.successfulSafetiesTitle) TextView safetyResults;
    @Bind(R.id.safetyErrorsTitle) TextView safetyErrorsTitle;
    @Bind(R.id.over) TextView overCut;
    @Bind(R.id.under) TextView underCut;
    @Bind(R.id.fast) TextView fast;
    @Bind(R.id.slow) TextView slow;
    @Bind(R.id.kickLong) TextView kickLong;
    @Bind(R.id.kickShort) TextView kickShort;
    @Bind(R.id.miscues) TextView miscues;


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
        miscues.setText(getString(R.string.title_miscues, StatsUtils.getMiscues(getContext(), stats)));

        if (statsLayout != null) {
            StatsUtils.setListOfSafetyStats(statsLayout, stats);
            safetyResults.setText(getString(R.string.title_safety_results, stats.size()));
            safetyErrorsTitle.setText(
                    getString(R.string.title_safety_errors,
                            StatsUtils.getFailedSafeties(getContext(), stats)));
        }
    }

    @Override List<String> getShotTypes() {
        List<String> list = new ArrayList<>();
        list.add("Safety");
        list.add("Safety error");
        return list;
    }

    @Override int getLayoutId() {
        return R.layout.fragment_adv_safety_stats;
    }
}
