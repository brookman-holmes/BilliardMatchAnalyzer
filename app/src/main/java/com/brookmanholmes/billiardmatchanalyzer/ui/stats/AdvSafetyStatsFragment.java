package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.utils.MultiSelectionSpinner;
import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvSafetyStatsFragment extends Fragment {
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
    List<AdvStats> stats = new ArrayList<>();

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

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseAdapter db = new DatabaseAdapter(getContext());

        long matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID);
        String playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);

        stats = matchId == -1L ? db.getAdvStats(playerName, new String[] {"Safety", "Safety error"}) : db.getAdvStats(matchId, playerName, new String[]{"Safety", "Safety error"});
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_adv_safety_stats, container, false);
        ButterKnife.bind(this, view);

        updateView(view);

        return view;
    }

    public void updateView(View view) {
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getContext(), stats), overCut, underCut);
        StatsUtils.setLayoutWeights(StatsUtils.getHowSpeedErrors(getContext(), stats), slow, fast);

        updateSafetyGrid(view);
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
