package com.brookmanholmes.billiards.match;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITableStatus;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.Date;
import java.util.List;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
interface IMatch<T extends AbstractPlayer> {
    /**
     * Creates and adds a new {@link com.brookmanholmes.billiards.turn.ITurn} to the match
     * with the specified arguments
     * @param tableStatus The status of the table for the new turn
     * @param turnEnd How the turn ended
     * @param foul Whether or not the turn resulted in a foul
     * @param isGameLost Whether or not the turn resulted in losing the game
     * @param advStats The advanced stats for the turn
     * @return A new turn with the specified arguments
     */
    ITurn createAndAddTurn(ITableStatus tableStatus, TurnEnd turnEnd, boolean foul, boolean isGameLost, AdvStats advStats);

    /**
     * Creates and adds a new {@link com.brookmanholmes.billiards.turn.ITurn} to the match
     * with the specified arguments
     * @param tableStatus The status of the table for the new turn
     * @param turnEnd How the turn ended
     * @param foul Whether or not the turn resulted in a foul
     * @param isGameLost Whether or not the turn resulted in losing the game
     * @return A new turn with the specified arguments
     */
    ITurn createAndAddTurn(ITableStatus tableStatus, TurnEnd turnEnd, boolean foul, boolean isGameLost);

    /**
     * Adds a turn to the match, adds turns to the list of stored turns, inserts a turn for the ghost
     * (if applicable) and sets whether the match is over or not
     * @param turn The turn being added to the match
     */
    void addTurn(ITurn turn);

    /**
     * Getter for the player
     * @return The stats of the player for the whole match
     */
    T getPlayer();

    /**
     * Getter for the opponent
     * @return The stats of the opponent for the whole match
     */
    T getOpponent();

    /**
     * Retrieve player's stats within the range given of turns
     * @param from low endpoint (inclusive) of the turns
     * @param to  high endpoint (exclusive) of the turns
     * @return An player with only the stats from within the range of turns given
     */
    T getPlayer(int from, int to);

    /**
     * Retrieve opponent's stats within the range given of turns
     * @param from low endpoint (inclusive) of the turns
     * @param to  high endpoint (exclusive) of the turns
     * @return An opponent with only the stats from within the range of turns given
     */
    T getOpponent(int from, int to);

    /**
     * Getter for all the turns added to the match
     * @return A list of turns that have been added to the match
     */
    List<ITurn> getTurns();

    /**
     * Get a list of turns in this match between the specified points
     * @param from low endpoint (inclusive) of the list
     * @param to high endpoint (exclusive) of the list
     * @return A list of turns in the match within the range
     */
    List<ITurn> getTurns(int from, int to);

    /**
     * Getter for the current game status
     * @return The current game status
     */
    GameStatus getGameStatus();

    /**
     * Returns the game status at a given point in time
     * @param turn The turn number you want to find the game status for
     * @return The game status at the given point in time
     */
    GameStatus getGameStatus(int turn);

    /**
     * Getter for the location of the match
     * @return The current location of the match
     */
    String getLocation();

    /**
     * Getter for the notes attached to the match
     * @return The notes for this match
     */
    String getNotes();

    /**
     * Sets the id of the match
     * @param matchId The new id for the match
     */
    void setMatchId(long matchId);

    /**
     * Sets the location of the match
     * @param location The new location for this match
     */
    void setLocation(String location);

    /**
     * Getter for the player's name who's currently shooting
     * @return Either the Player's name or the Opponent's name, depending on the turn
     */
    String getCurrentPlayersName();

    /**
     * Getter for the player's name who's not currently shooting
     * @return Either the Player's name or the Opponent's name, depending on the turn
     */
    String getNonCurrentPlayersName();

    /**
     * Sets the notes for the match
     * @param notes The new notes you would like to set for this match
     */
    void setNotes(String notes);

    /**
     * Getter for the number of turns in the match
     * @return The number of turns in the match
     */
    int getTurnCount();

    /**
     * Returns whether the last undone turn can be redone
     * @return True if you can redo the last undone turn, false otherwise
     */
    boolean isRedoTurn();

    /**
     * Returns whether the last turn added can be undone (which is always true unless there are no
     * more turns to undo)
     * @return True if you can undo the last turn, false otherwise
     */
    boolean isUndoTurn();

    /**
     * Redoes the last turn and returns what it was
     * @return The turn that was redone
     */
    ITurn redoTurn();

    /**
     * Undo the last turn
     */
    void undoTurn();

    /**
     * Getter for the id of the match
     * @return the id of the match
     */
    long getMatchId();

    /**
     * Sets the player's name
     * @param newName The new name of the player
     */
    void setPlayerName(String newName);

    /**
     * Sets the opponent's name
     * @param newName The new name of the opponent
     */
    void setOpponentName(String newName);

    /**
     * Returns the level of detail being kept for this match
     * @return The level of detail being kept for this match
     */
    Match.StatsDetail getStatDetailLevel();

    /**
     * Getter for the date this match was created on
     * @return The date this match was created on
     */
    Date getCreatedOn();

    /**
     * Returns whether the match is over
     * @return True if the match is over, false otherwise
     */
    boolean isMatchOver();
}
