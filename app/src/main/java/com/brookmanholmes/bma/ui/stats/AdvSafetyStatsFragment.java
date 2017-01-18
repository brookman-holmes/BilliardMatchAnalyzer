package com.brookmanholmes.bma.ui.stats;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.databinding.FragmentAdvSafetyStatsBinding;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICK_LONG;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICK_SHORT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.THICK;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.THIN;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.TOO_HARD;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.TOO_SOFT;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class AdvSafetyStatsFragment extends BaseAdvStatsFragment {
    @Bind(R.id.successfulSafetiesTitle)
    TextView safetyResults;
    @Bind(R.id.safetyErrorsTitle)
    TextView safetyErrorsTitle;
    @Bind(R.id.over)
    TextView overCut;
    @Bind(R.id.under)
    TextView underCut;
    @Bind(R.id.fast)
    TextView fast;
    @Bind(R.id.slow)
    TextView slow;
    @Bind(R.id.kickLong)
    TextView kickLong;
    @Bind(R.id.kickShort)
    TextView kickShort;
    @Bind(R.id.miscues)
    TextView miscues;

    SafetyStatBinder model;
    FragmentAdvSafetyStatsBinding binding;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_adv_safety_stats, container, false);
        ButterKnife.bind(this, binding.getRoot());

        model = new SafetyStatBinder(new ArrayList<StatLineItem>());
        binding.setSafety(model);

        return binding.getRoot();
    }

    @Override
    void updateView() {
        StatsUtils.setLayoutWeights(stats, THIN, THICK, overCut, underCut);
        StatsUtils.setLayoutWeights(stats, TOO_SOFT, TOO_HARD, slow, fast);
        StatsUtils.setLayoutWeights(stats, KICK_SHORT, KICK_LONG, kickShort, kickLong);

        model.update(StatsUtils.getSafetyStats(stats));

        miscues.setText(getString(R.string.title_miscues, StatsUtils.getMiscues(stats)));
        safetyResults.setText(getString(R.string.title_safety_results, stats.size()));
        safetyErrorsTitle.setText(getString(R.string.title_safety_errors, StatsUtils.getFailedSafeties(stats)));
    }

    @Override
    String[] getShotTypes() {
        return AdvStats.ShotType.getSafeties();
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_adv_safety_stats;
    }
}
