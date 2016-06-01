package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public interface IMatch<T extends AbstractPlayer> {
    Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost, AdvStats advStats);

    T getPlayer();

    T getOpponent();

    String getLocation();

    String getCurrentPlayersName();

    String getNonCurrentPlayersName();

    int getTurnCount();

    boolean isRedoTurn();

    boolean isUndoTurn();

    Turn redoTurn();

    void undoTurn();

    long getMatchId();

    void setPlayerName(String newName);

    void setOpponentName(String newName);
}
