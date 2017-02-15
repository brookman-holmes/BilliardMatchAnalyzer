package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.Player;

/**
 * Created by Brookman Holmes on 1/23/2017.
 */

public class WinIndicatorBinder extends BindingAdapter {
    public float playerMatchCompPct = 0, opponentMatchCompPct = 0;

    WinIndicatorBinder(Player player, Player opponent) {
        super(true, true);

        update(player, opponent);
    }

    public void update(Player player, Player opponent) {
        playerMatchCompPct = round(player.getMatchCompletionPct());
        opponentMatchCompPct = round(opponent.getMatchCompletionPct());

        notifyChange();
    }
}
