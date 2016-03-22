package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public interface MatchInterface<T extends AbstractPlayer> {
    Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost);

    T getPlayer();

    T getOpponent();

    String getLocation();

    String getCurrentPlayersName();

    int getTurnCount();

    boolean isRedoTurn();

    boolean isUndoTurn();

    Turn redoTurn();

    void undoTurn();

    long getMatchId();
}
