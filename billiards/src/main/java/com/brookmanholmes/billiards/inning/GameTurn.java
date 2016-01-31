package com.brookmanholmes.billiards.inning;

import java.util.List;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
public class GameTurn implements com.brookmanholmes.billiards.game.Turn {
    final TableStatus tableStatus;
    final TurnEnd turnEnd;
    final boolean scratch;
    final long matchId;
    final int inningNumber;

    public GameTurn(int inningNumber, long matchId, boolean scratch, TurnEnd turnEnd, TableStatus tableStatus) {
        this.inningNumber = inningNumber;
        this.matchId = matchId;
        this.scratch = scratch;
        this.turnEnd = turnEnd;
        this.tableStatus = tableStatus;
    }

    @Override
    public int getShootingBallsMade() {
        return tableStatus.getShootingBallsMade();
    }

    @Override
    public int getDeadBalls() {
        return tableStatus.getDeadBalls();
    }

    @Override
    public int getDeadBallsOnBreak() {
        return tableStatus.getDeadBallsOnBreak();
    }

    @Override
    public int getBreakBallsMade() {
        return tableStatus.getBreakBallsMade();
    }

    @Override
    public boolean isScratch() {
        return scratch;
    }

    @Override
    public List<Integer> getBallsToRemoveFromTable() {
        return tableStatus.getBallsThatAreOffTable();
    }

    @Override
    public TurnEnd getTurnEnd() {
        return turnEnd;
    }

    @Override
    public TableStatus getTableStatus() {
        return tableStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameTurn gameTurn = (GameTurn) o;

        if (scratch != gameTurn.scratch) return false;
        if (matchId != gameTurn.matchId) return false;
        if (inningNumber != gameTurn.inningNumber) return false;
        if (!tableStatus.equals(gameTurn.tableStatus)) return false;
        return turnEnd == gameTurn.turnEnd;

    }

    @Override
    public int hashCode() {
        int result = tableStatus.hashCode();
        result = 31 * result + turnEnd.hashCode();
        result = 31 * result + (scratch ? 1 : 0);
        result = 31 * result + (int) (matchId ^ (matchId >>> 32));
        result = 31 * result + inningNumber;
        return result;
    }

    @Override
    public String toString() {
        return "GameTurn{" +
                "tableStatus=" + tableStatus +
                "\n turnEnd=" + turnEnd +
                "\n scratch=" + scratch +
                "\n matchId=" + matchId +
                "\n inningNumber=" + inningNumber +
                '}';
    }
}
