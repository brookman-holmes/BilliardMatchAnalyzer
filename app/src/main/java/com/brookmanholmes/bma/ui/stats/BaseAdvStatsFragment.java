package com.brookmanholmes.bma.ui.stats;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
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
    @Bind(R.id.parentView) LinearLayout statsLayout;
    @Bind(R.id.baseLayout) LinearLayout baseLayout;
    String playerName;
    String[] shotTypes;
    protected FilterStats task;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID, -1L);
        playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);
        shotTypes = getShotTypes();

        DatabaseAdapter db = new DatabaseAdapter(getContext());
        stats = matchId == -1L ?
                db.getAdvStats(playerName, shotTypes) :
                db.getAdvStats(matchId, playerName, shotTypes);
    }

    abstract void updateView();

    abstract String[] getShotTypes();

    @LayoutRes abstract int getLayoutId();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addListener(this);
        } else updateView();
    }

    @Override public void onDestroyView() {
        ButterKnife.unbind(this);
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeListener(this);
        }
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        task.cancel(true);
        super.onDestroy();
    }

    @Override
    public void setFilter(StatFilter filter) {
        if (task == null) {
            task = new FilterStats();
            task.execute(filter);
        }

        if (task.getStatus() != AsyncTask.Status.RUNNING) {
            task.cancel(true);
            task = new FilterStats();
            task.execute(filter);
        }
    }

    private class FilterStats extends AsyncTask<StatFilter, Void, List<AdvStats>> {
        @Override protected void onPostExecute(List<AdvStats> list) {
            if (!isCancelled() && isAdded())
                stats = list;

            if (!isCancelled() && isAdded())
                updateView();
        }

        @Override protected List<AdvStats> doInBackground(StatFilter... params) {
            return new DatabaseAdapter(getContext()).getAdvStats(playerName, shotTypes, params[0]);
        }
    }
}
