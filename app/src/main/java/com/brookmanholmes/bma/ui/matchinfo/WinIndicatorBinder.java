package com.brookmanholmes.bma.ui.matchinfo;

import android.support.annotation.Nullable;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.player.Player;

/**
 * Created by Brookman Holmes on 1/23/2017.
 */

public class WinIndicatorBinder extends BindingAdapter {
    public float playerMatchCompPct = 0, opponentMatchCompPct = 0;

    WinIndicatorBinder() {
        super("", true, true);
    }

    @Override
    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        playerMatchCompPct = round(player.getMatchCompletionPct());
        opponentMatchCompPct = round(opponent.getMatchCompletionPct());

        notifyChange();
    }
}
