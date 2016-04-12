package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.List;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvBreakingStatsFragment extends Fragment {
    List<AdvStats> stats;

    public static AdvBreakingStatsFragment create(Bundle args) {
        AdvBreakingStatsFragment frag = new AdvBreakingStatsFragment();
        frag.setArguments(args);

        return frag;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseAdapter db = new DatabaseAdapter(getContext());
        db.open();

        long matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID);
        String playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);
        stats = db.getAdvStats(matchId, playerName, new String[]{"Break shot"});
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.container_adv_break_stats, container, false);
    }
}
