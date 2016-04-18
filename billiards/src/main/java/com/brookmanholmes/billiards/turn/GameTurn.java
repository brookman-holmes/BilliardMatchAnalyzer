package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
public class GameTurn implements Turn {
    final TableStatus tableStatus;
    final TurnEnd turnEnd;
    final boolean scratch;
    final long matchId;
    final int inningNumber;
    final boolean gameLost;
    final AdvStats advStats;

    public GameTurn(int inningNumber, long matchId, boolean scratch, TurnEnd turnEnd, TableStatus tableStatus, boolean isGameLost) {
        this.inningNumber = inningNumber;
        this.matchId = matchId;
        this.scratch = scratch;
        this.turnEnd = turnEnd;
        this.tableStatus = tableStatus;
        this.gameLost = isGameLost;
        advStats = null;
    }

    public GameTurn(int inningNumber, long matchId, boolean scratch, TurnEnd turnEnd, TableStatus tableStatus, boolean isGameLost, AdvStats advStats) {
        this.inningNumber = inningNumber;
        this.matchId = matchId;
        this.scratch = scratch;
        this.turnEnd = turnEnd;
        this.tableStatus = tableStatus;
        this.gameLost = isGameLost;
        this.advStats = advStats;
    }

    @Override public int getShootingBallsMade() {
        return tableStatus.getShootingBallsMade();
    }

    @Override public int getDeadBalls() {
        return tableStatus.getDeadBalls();
    }

    @Override public int getDeadBallsOnBreak() {
        return tableStatus.getDeadBallsOnBreak();
    }

    @Override public int getBreakBallsMade() {
        return tableStatus.getBreakBallsMade();
    }

    @Override public boolean isScratch() {
        return scratch;
    }

    @Override public List<Integer> getBallsToRemoveFromTable() {
        return tableStatus.getBallsThatAreOffTable();
    }

    @Override public TurnEnd getTurnEnd() {
        return turnEnd;
    }

    @Override public boolean getGameBallMadeOnBreak() {
        return tableStatus.getGameBallMadeOnBreak();
    }

    @Override public boolean getGameBallMade() {
        return tableStatus.getGameBallMade();
    }

    @Override public int getBallsRemaining() {
        return tableStatus.getBallsRemaining();
    }

    @Override public BallStatus getBallStatus(int ball) {
        return tableStatus.getBallStatus(ball);
    }

    @Override public boolean getGameBallMadeIllegally() {
        return tableStatus.getGameBallMadeIllegally();
    }

    @Override public boolean isGameLost() {
        return gameLost;
    }

    @Override public List<BallStatus> getBallStatuses() {
        List<BallStatus> ballStatuses = new ArrayList<>();

        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            ballStatuses.add(tableStatus.getBallStatus(ball));
        }

        return ballStatuses;
    }

    @Override public int size() {
        return tableStatus.size();
    }

    @Override public GameType getGameType() {
        return tableStatus.getGameType();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameTurn gameTurn = (GameTurn) o;

        if (scratch != gameTurn.scratch) return false;
        if (matchId != gameTurn.matchId) return false;
        if (inningNumber != gameTurn.inningNumber) return false;
        if (gameLost != gameTurn.gameLost) return false;
        if (!tableStatus.equals(gameTurn.tableStatus)) return false;
        if (turnEnd != gameTurn.turnEnd) return false;
        return advStats.equals(gameTurn.advStats);

    }

    @Override public int hashCode() {
        int result = tableStatus.hashCode();
        result = 31 * result + turnEnd.hashCode();
        result = 31 * result + (scratch ? 1 : 0);
        result = 31 * result + (int) (matchId ^ (matchId >>> 32));
        result = 31 * result + inningNumber;
        result = 31 * result + (gameLost ? 1 : 0);
        result = 31 * result + advStats.hashCode();
        return result;
    }

    @Override public String toString() {
        return "GameTurn{" +
                "tableStatus=" + tableStatus +
                "\n turnEnd=" + turnEnd +
                "\n foul=" + scratch +
                "\n matchId=" + matchId +
                "\n inningNumber=" + inningNumber +
                '}';
    }
}
