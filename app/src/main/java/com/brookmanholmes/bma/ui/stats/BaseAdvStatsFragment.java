package com.brookmanholmes.bma.ui.stats;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.ui.BaseDialogFragment;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 6/3/2016.
 */
public abstract class BaseAdvStatsFragment extends BaseDialogFragment implements TurnListener {
    private static final String TAG = "BaseAdvStatsFrag";

    List<AdvStats> stats = new ArrayList<>();

    @LayoutRes
    abstract int getLayoutId();

    abstract AdvStats.ShotType[] getShotTypes();
    abstract void updateView();

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
            ((PlayerProfileActivity) getActivity()).addAdvStatListener(this);
        } else if (getParentFragment() instanceof AdvStatsDialog) {
            ((AdvStatsDialog) getParentFragment()).addAdvStatListener(this);
        } else
            throw new IllegalStateException("Must be attached to either AdvStatsDialog or PlayerProfileActivity");
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        if (getActivity() instanceof PlayerProfileActivity) {
            ((PlayerProfileActivity) getActivity()).removeAdvStatListener(this);
        } else if (getParentFragment() instanceof AdvStatsDialog) {
            ((AdvStatsDialog) getParentFragment()).removeAdvStatListener(this);
        }

        super.onDestroyView();
    }

    @Override
    public void setAdvStats(Collection<ITurn> turns) {
        stats.clear();
        for (ITurn turn : turns) {
            if (ArrayUtils.contains(getShotTypes(), turn.getAdvStats().getShotType())) {
                stats.add(turn.getAdvStats());
            }
        }
        updateView();
    }
}
