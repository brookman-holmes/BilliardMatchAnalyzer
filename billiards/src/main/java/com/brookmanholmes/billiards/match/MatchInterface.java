package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public interface MatchInterface<T extends AbstractPlayer> {
    Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch);

    T getPlayer();

    T getOpponent();

    String getLocation();

    String getCurrentPlayersName();

    int getTurnCount();
}
