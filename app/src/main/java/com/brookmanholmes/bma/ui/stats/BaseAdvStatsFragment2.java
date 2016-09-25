package com.brookmanholmes.bma.ui.stats;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseRecyclerFragment;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;

import java.util.List;

/**
 * Created by Brookman Holmes on 9/24/2016.
 */

public abstract class BaseAdvStatsFragment2 extends BaseRecyclerFragment implements Filterable {
    long matchId;
    String playerName;
    FilterStatsTask task;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID, -1L);
        playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);

        DatabaseAdapter db = new DatabaseAdapter(getContext());
        List<AdvStats> stats = matchId == -1L ?
                db.getAdvStats(playerName, getShotTypes()) :
                db.getAdvStats(matchId, playerName, getShotTypes());

        adapter = new AdvStatsRecyclerAdapter(stats, getStatLineItems(stats));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addListener(this);
        }
    }

    @Override
    public void onDestroyView() {
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeListener(this);
        }
        super.onDestroyView();
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    abstract String[] getShotTypes();

    abstract List<StatLineItem> getStatLineItems(List<AdvStats> stats);

    @Override
    public void setFilter(StatFilter filter) {
        if (task == null) {
            task = new FilterStatsTask();
            task.execute(filter);
        }

        if (task.getStatus() != AsyncTask.Status.RUNNING) {
            task.cancel(true);
            task = new FilterStatsTask();
            task.execute(filter);
        }
    }

    private class FilterStatsTask extends AsyncTask<StatFilter, Void, List<AdvStats>> {
        @Override
        protected void onPostExecute(List<AdvStats> list) {
            if (!isCancelled() && isAdded())
                ((AdvStatsRecyclerAdapter) adapter).update(list, getStatLineItems(list));
        }

        @Override
        protected List<AdvStats> doInBackground(StatFilter... params) {
            return new DatabaseAdapter(getContext()).getAdvStats(playerName, getShotTypes(), params[0]);
        }
    }
}
