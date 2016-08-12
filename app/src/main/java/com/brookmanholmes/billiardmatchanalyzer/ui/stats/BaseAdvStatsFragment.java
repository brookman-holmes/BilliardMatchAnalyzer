package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.brookmanholmes.billiardmatchanalyzer.MyApplication;
import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 6/3/2016.
 */
public abstract class BaseAdvStatsFragment extends Fragment implements Filterable {
    List<AdvStats> stats = new ArrayList<>();
    DatabaseAdapter db;
    String playerName;
    @Bind(R.id.parentView) LinearLayout statsLayout;
    List<String> shotTypes;
    long matchId;
    boolean updateView = true;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID, -1L);
        playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);
        shotTypes = getShotTypes();

        db = new DatabaseAdapter(getContext());
        stats = matchId == -1L ?
                db.getAdvStats(playerName, shotTypes.toArray(new String[shotTypes.size()])) :
                db.getAdvStats(matchId, playerName, shotTypes.toArray(new String[shotTypes.size()]));
    }

    abstract void updateView();

    abstract List<String> getShotTypes();

    @LayoutRes abstract int getLayoutId();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateView();
        updateView = false;
    }

    @Override public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
    }

    @Override public void setFilter(StatFilter filter) {
        updateView = true;
    }
}
