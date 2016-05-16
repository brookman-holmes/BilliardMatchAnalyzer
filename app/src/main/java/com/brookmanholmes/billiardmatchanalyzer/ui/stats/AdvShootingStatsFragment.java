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
public class AdvShootingStatsFragment extends Fragment {
    List<AdvStats> stats = new ArrayList<>();
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

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseAdapter db = new DatabaseAdapter(getContext());

        long matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID, -1L);
        String playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);
        String[] shotTypes = getContext().getResources().getStringArray(R.array.shot_types);
        stats = matchId == -1L ? db.getAdvStats(playerName, shotTypes): db.getAdvStats(matchId, playerName, shotTypes); // get the stats by name or name and match id
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_adv_shooting_stats, container, false);
        ButterKnife.bind(this, view);

        updateView(view);
        return view;
    }

    public void updateView(View view) {
        StatsUtils.setLayoutWeights(StatsUtils.getHowAimErrors(getContext(), stats), leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getContext(), stats), overCut, underCut);

        title.setText(getString(R.string.title_shooting_errors, stats.size()));

        StatsUtils.updateGridOfMissReasons(view, StatsUtils.getFourMostCommonItems(stats));
    }
}
