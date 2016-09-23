package com.brookmanholmes.bma.ui.stats;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseRecyclerFragment;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class AdvShootingStatsFragment2 extends BaseRecyclerFragment implements FilterListener {
    long matchId;
    String playerName;
    String[] shotTypes = AdvStats.ShotType.getShots();
    private GetFilteredStatsAsync task;
    private String shotType = "All", subType = "All", angle = "All";

    public static AdvShootingStatsFragment2 create(Bundle args) {
        AdvShootingStatsFragment2 frag = new AdvShootingStatsFragment2();
        frag.setArguments(args);

        return frag;
    }

    public static AdvShootingStatsFragment2 create(String name) {
        AdvShootingStatsFragment2 frag = new AdvShootingStatsFragment2();
        Bundle args = new Bundle();
        args.putString(AdvStatsDialog.ARG_PLAYER_NAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID, -1L);
        playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);

        DatabaseAdapter db = new DatabaseAdapter(getContext());
        List<AdvStats> stats = db.getAdvStats(matchId, playerName, shotTypes);

        adapter = new AdvStatsRecyclerAdapter(getContext(),
                stats,
                StatsUtils.getStats(getContext(), stats),
                this);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    private List<AdvStats> getFilteredStats(AdvStats... stats) {
        List<AdvStats> list = new ArrayList<>();

        for (AdvStats stat : stats) {
            if (isShotType(stat)) {
                if (isSubType(stat)) {
                    if (isAngle(stat))
                        list.add(stat);
                }
            }
        }

        return list;
    }

    public boolean isShotType(AdvStats stat) {
        // all has to go first otherwise it throws an illegal argument exception
        return shotType.equals(getString(R.string.all)) || stat.getShotType() == MatchDialogHelperUtils.convertStringToShotType(getContext(), shotType);
    }

    public boolean isSubType(AdvStats stat) {
        // all has to go first otherwise it throws an illegal argument exception
        return subType.equals(getString(R.string.all)) || stat.getShotSubtype() == MatchDialogHelperUtils.convertStringToSubType(getContext(), subType);
    }

    private boolean isAngle(AdvStats stat) {
        // all has to go first otherwise it throws an illegal argument exception
        return angle.equals(getString(R.string.all)) || stat.getAngles().contains(MatchDialogHelperUtils.convertStringToAngle(getContext(), angle));
    }

    @Override
    public void updateShotType(String shotType) {
        this.shotType = shotType;
        updateView();
    }

    @Override
    public void updateSubType(String subType) {
        this.subType = subType;
        updateView();
    }

    @Override
    public void updateAngle(String angle) {
        this.angle = angle;
        updateView();
    }

    private void updateView() {

        task = new GetFilteredStatsAsync();
        DatabaseAdapter db = new DatabaseAdapter(getContext());
        //task.execute(db.getAdvStats(matchId, playerName, shotTypes).toArray(new AdvStats[0]));
    }

    private class GetFilteredStatsAsync extends AsyncTask<AdvStats, Void, List[]> {
        @Override
        protected void onPostExecute(List[] results) {
            if (isAdded() && !isCancelled())
                ((AdvStatsRecyclerAdapter) adapter).update(results[0], results[1]);
        }

        @Override
        protected List[] doInBackground(AdvStats... stats) {
            List<AdvStats> filteredStats = getFilteredStats(stats);
            return new List[]{filteredStats, StatsUtils.getStats(getContext(), filteredStats)};
        }
    }
}
