package com.brookmanholmes.bma.ui.view;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Brookman Holmes on 12/28/2016.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    @ColorInt
    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(itemView.getContext(), color);
    }
}
