package com.brookmanholmes.billiardmatchanalyzer.adaptervh;

import android.view.View;

import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 3/2/2016.
 */
public class FooterViewHolder<T extends AbstractPlayer> extends BaseViewHolder<T> {
    public FooterViewHolder(View itemView) {
        super(itemView);
    }

    @Override public void bind(AbstractPlayer player, AbstractPlayer opponent) {

    }
}
