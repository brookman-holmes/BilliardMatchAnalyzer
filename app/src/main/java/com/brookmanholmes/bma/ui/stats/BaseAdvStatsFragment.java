package com.brookmanholmes.bma.ui.stats;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseDialogFragment;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 6/3/2016.
 */
public abstract class BaseAdvStatsFragment extends BaseDialogFragment implements Filterable {
    private static final String TAG = "BaseAdvStatsFrag";

    List<AdvStats> stats = new ArrayList<>();
    String playerName;
    String[] shotTypes;
    private DatabaseAdapter db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID, -1L);
        playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);
        shotTypes = getShotTypes();

        db = new DatabaseAdapter(getContext());

        stats = matchId == -1L ?
                db.getAdvStats(playerName, shotTypes) :
                db.getAdvStats(matchId, playerName, shotTypes);
    }

    @LayoutRes
    abstract int getLayoutId();
    abstract void updateView();
    abstract String[] getShotTypes();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).addListener(this);
        } else updateView();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeListener(this);
        }
        super.onDestroyView();
    }

    @Override
    public void setFilter(StatFilter filter) {
        db.getAdvStats(playerName, shotTypes, filter);
        updateView();
    }
}
