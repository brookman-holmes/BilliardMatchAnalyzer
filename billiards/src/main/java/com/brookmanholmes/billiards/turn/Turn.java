package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
public class Turn implements ITurn {
    private final ITableStatus tableStatus;
    private final TurnEnd turnEnd;
    private final boolean scratch;
    private final boolean gameLost;
    private final AdvStats advStats;

    public Turn(TurnEnd turnEnd, ITableStatus tableStatus, boolean scratch, boolean isGameLost, AdvStats advStats) {
        this.scratch = scratch;
        this.turnEnd = turnEnd;
        this.tableStatus = tableStatus;
        this.gameLost = isGameLost;
        this.advStats = advStats;
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
    public boolean isFoul() {
        return scratch;
    }

    @Override
    public List<Integer> getBallsToRemoveFromTable() {
        return tableStatus.getBallsToRemoveFromTable();
    }

    @Override
    public TurnEnd getTurnEnd() {
        return turnEnd;
    }

    @Override
    public boolean isGameBallMadeOnBreak() {
        return tableStatus.isGameBallMadeOnBreak();
    }

    @Override
    public boolean isGameBallMade() {
        return tableStatus.isGameBallMade();
    }

    @Override
    public int getBallsRemaining() {
        return tableStatus.getBallsRemaining();
    }

    @Override
    public BallStatus getBallStatus(int ball) {
        return tableStatus.getBallStatus(ball);
    }

    @Override
    public boolean isGameBallMadeIllegally() {
        return tableStatus.isGameBallMadeIllegally();
    }

    // TODO: 8/26/2016 add test for this method
    @Override
    public void setBallTo(BallStatus status, int... balls) {
        tableStatus.setBallTo(status, balls);
    }

    @Override
    public boolean isGameLost() {
        return gameLost;
    }

    @Override
    public List<BallStatus> getBallStatuses() {
        List<BallStatus> ballStatuses = new ArrayList<>();

        for (int ball = 1; ball <= tableStatus.size(); ball++) {
            ballStatuses.add(tableStatus.getBallStatus(ball));
        }

        return ballStatuses;
    }

    @Override
    public AdvStats getAdvStats() {
        return advStats;
    }

    @Override
    public int size() {
        return tableStatus.size();
    }

    @Override
    public GameType getGameType() {
        return tableStatus.getGameType();
    }

    @Override
    public int getGameBall() {
        return tableStatus.getGameBall();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Turn turn = (Turn) o;

        if (scratch != turn.scratch) return false;
        if (gameLost != turn.gameLost) return false;
        if (!tableStatus.equals(turn.tableStatus)) return false;
        if (turnEnd != turn.turnEnd) return false;
        return advStats.equals(turn.advStats);

    }

    @Override
    public int hashCode() {
        int result = tableStatus.hashCode();
        result = 31 * result + turnEnd.hashCode();
        result = 31 * result + (scratch ? 1 : 0);
        result = 31 * result + (gameLost ? 1 : 0);
        result = 31 * result + advStats.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Turn{" +
                "tableStatus=" + tableStatus +
                "\n turnEnd=" + turnEnd +
                "\n foul=" + scratch +
                '}';
    }
}
