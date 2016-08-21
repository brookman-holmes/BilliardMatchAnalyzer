package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.List;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
interface IMatch<T extends AbstractPlayer> {
    ITurn createAndAddTurn(ITableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost, AdvStats advStats);

    ITurn createAndAddTurn(ITableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost);

    T getPlayer();

    T getOpponent();

    T getPlayer(int from, int to);

    T getOpponent(int from, int to);

    List<ITurn> getTurns();

    List<ITurn> getTurns(int from, int to);

    String getLocation();

    void setLocation(String location);

    String getCurrentPlayersName();

    String getNonCurrentPlayersName();

    void setNotes(String notes);

    int getTurnCount();

    boolean isRedoTurn();

    boolean isUndoTurn();

    ITurn redoTurn();

    void undoTurn();

    long getMatchId();

    void setPlayerName(String newName);

    void setOpponentName(String newName);
}
