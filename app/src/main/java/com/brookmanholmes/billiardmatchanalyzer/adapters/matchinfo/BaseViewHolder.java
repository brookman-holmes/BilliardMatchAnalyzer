package com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 3/2/2016.
 */
public abstract class BaseViewHolder<T extends AbstractPlayer> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T player, T opponent);
}
