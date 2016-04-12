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
public class AdvSafetyStatsFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    private static final String[] choices = {"Too hard", "Too soft", "Too thick", "Too thin"};
    @Bind(R.id.successfulSafetiesTitle) TextView safetyResults;
    @Bind(R.id.safetyErrorsTitle) TextView safetyErrorsTitle;
    @Bind(R.id.spinner) MultiSelectionSpinner spinner1;
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

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseAdapter db = new DatabaseAdapter(getContext());
        db.open();

        long matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID);
        String playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);
        stats = db.getAdvStats(matchId, playerName, new String[]{"Safety", "Safety error"});
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_adv_safety_stats, container, false);
        ButterKnife.bind(this, view);

        updateView(view);

        spinner1.setListener(this);
        spinner1.setItems(new String[]{"Safe", "Open"});
        return view;
    }

    public void updateView(View view) {
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getContext(), stats), overCut, underCut);
        StatsUtils.setLayoutWeights(StatsUtils.getHowSpeedErrors(getContext(), stats), slow, fast);

        updateSafetyGrid(view);
    }

    private void updateSafetyGrid(View view) {
        List<StatsUtils.StatLineItem> statLineItems = StatsUtils.getSafetyStats(getContext(), stats);

        ((TextView) view.findViewById(R.id.tvFullHookCount)).setText(statLineItems.get(0).getCount());
        ((TextView) view.findViewById(R.id.tvFullHookPct)).setText(statLineItems.get(0).getPercentage());
        ((TextView) view.findViewById(R.id.tvPartialHookCount)).setText(statLineItems.get(1).getCount());
        ((TextView) view.findViewById(R.id.tvPartialHookPct)).setText(statLineItems.get(1).getPercentage());
        ((TextView) view.findViewById(R.id.tvLongTCount)).setText(statLineItems.get(2).getCount());
        ((TextView) view.findViewById(R.id.tvLongTPct)).setText(statLineItems.get(2).getPercentage());
        ((TextView) view.findViewById(R.id.tvShortTCount)).setText(statLineItems.get(3).getCount());
        ((TextView) view.findViewById(R.id.tvShortTPct)).setText(statLineItems.get(3).getPercentage());
        ((TextView) view.findViewById(R.id.tvDirectShotCount)).setText(statLineItems.get(4).getCount());
        ((TextView) view.findViewById(R.id.tvDirectShotPct)).setText(statLineItems.get(4).getPercentage());
        ((TextView) view.findViewById(R.id.tvOpenCount)).setText(statLineItems.get(5).getCount());
        ((TextView) view.findViewById(R.id.tvOpenPct)).setText(statLineItems.get(5).getPercentage());

        safetyErrorsTitle.setText("Safety errors (" + ((TextView) view.findViewById(R.id.tvOpenCount)).getText() + ")");
        safetyResults.setText("Safety results (" + stats.size() + ")");
    }

    @Override public void selectedIndices(List<Integer> indices) {

    }

    @Override public void selectedStrings(List<String> strings) {

    }
}