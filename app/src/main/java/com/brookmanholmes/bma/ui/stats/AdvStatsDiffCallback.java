package com.brookmanholmes.bma.ui.stats;

import android.support.v7.util.DiffUtil;

import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.List;

/**
 * Created by Brookman Holmes on 9/24/2016.
 */

public class AdvStatsDiffCallback extends DiffUtil.Callback {
    private List oldList;
    private List newList;

    public AdvStatsDiffCallback(List<AdvStats> oldList, List<AdvStats> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
