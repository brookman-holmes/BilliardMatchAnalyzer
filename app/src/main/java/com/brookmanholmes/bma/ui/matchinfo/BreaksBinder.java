package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class BreaksBinder extends BindingAdapter {
    public BreaksBinder(AbstractPlayer player, AbstractPlayer opponent, String title) {
        this.title = title;
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        notifyAll();
    }
}
