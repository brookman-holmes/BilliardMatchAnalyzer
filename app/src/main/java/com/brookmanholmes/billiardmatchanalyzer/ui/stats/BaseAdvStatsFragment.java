package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 6/3/2016.
 */
public abstract class BaseAdvStatsFragment extends Fragment implements Filterable {
    List<AdvStats> stats = new ArrayList<>();
    DatabaseAdapter db;
    String playerName;
    String[] shotTypes;
    long matchId;
    View view;
    boolean updateView = true;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID, -1L);
        playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);
        shotTypes = getShotTypes();

        db = new DatabaseAdapter(getContext());
        stats = matchId == -1L ?
                db.getAdvStats(playerName, shotTypes) :
                db.getAdvStats(matchId, playerName, shotTypes);
    }

    abstract void updateView(View view);

    abstract String[] getShotTypes();

    @LayoutRes abstract int getLayoutId();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateView(view);
        updateView = false;
    }

    @Override public void setFilter(StatFilter filter) {
        updateView = true;
    }
}
