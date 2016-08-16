package com.brookmanholmes.bma.adaptervh;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 3/2/2016.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(AbstractPlayer player, AbstractPlayer opponent);
}
