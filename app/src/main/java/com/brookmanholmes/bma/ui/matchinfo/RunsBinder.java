package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class RunsBinder extends BindingAdapter {

    public RunsBinder(AbstractPlayer player, AbstractPlayer opponent, String title) {
        this.title = title;
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        notifyChange();
    }
}
