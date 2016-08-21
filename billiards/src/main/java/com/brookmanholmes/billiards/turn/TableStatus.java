package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.brookmanholmes.billiards.game.util.BallStatus.DEAD;
import static com.brookmanholmes.billiards.game.util.BallStatus.DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.util.BallStatus.GAME_BALL_DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.util.BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_DEAD;
import static com.brookmanholmes.billiards.game.util.BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_MADE;
import static com.brookmanholmes.billiards.game.util.BallStatus.GAME_BALL_MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.util.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
import static com.brookmanholmes.billiards.game.util.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
import static com.brookmanholmes.billiards.game.util.BallStatus.MADE;
import static com.brookmanholmes.billiards.game.util.BallStatus.MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.util.BallStatus.OFF_TABLE;
import static com.brookmanholmes.billiards.game.util.BallStatus.ON_TABLE;

/**
 * Created by Brookman Holmes on 10/25/2015.
 */
final public class TableStatus implements ITableStatus {
    final int GAME_BALL;
    private final GameType gameType;
    final private Map<Integer, BallStatus> table;

    TableStatus(int size, int gameBall, GameType gameType) throws InvalidBallException {
        if (gameBall > size)
            throw new InvalidBallException("Game ball (" + gameBall
                    + ") is out of range (1-" + size + ")");

        GAME_BALL = gameBall;
        this.gameType = gameType;
        table = new HashMap<>(size);
        setupTable(size, ON_TABLE);
    }

    private TableStatus(int size, int gameBall, GameType gameType, List<Integer> ballsOnTable) throws InvalidBallException {
        if (!ballsOnTable.contains(gameBall))
            throw new InvalidBallException("Game ball (" + gameBall + ") " +
                    "not on table, balls on table:" + ballsOnTable.toString());

        this.GAME_BALL = gameBall;
        this.gameType = gameType;
        table = new HashMap<>(size);
        setupTable(size, OFF_TABLE);

        for (int ball : ballsOnTable) {
            table.put(ball, ON_TABLE);
        }
    }

    public static TableStatus newTable(GameType gameType) throws InvalidGameTypeException {
        switch (gameType) {
            case APA_EIGHT_BALL:
                return new TableStatus(15, 8, gameType);
            case BCA_EIGHT_BALL:
                return new TableStatus(15, 8, gameType);
            case APA_NINE_BALL:
                return new TableStatus(9, 9, gameType);
            case BCA_NINE_BALL:
                return new TableStatus(9, 9, gameType);
            case BCA_TEN_BALL:
                return new TableStatus(10, 10, gameType);
            default:
                throw new InvalidGameTypeException(gameType.name());
        }
    }

    public static TableStatus newTable(GameType gameType, List<Integer> ballsOnTable) throws InvalidGameTypeException {
        switch (gameType) {
            case APA_EIGHT_BALL:
                return new TableStatus(15, 8, gameType, ballsOnTable);
            case BCA_EIGHT_BALL:
                return new TableStatus(15, 8, gameType, ballsOnTable);
            case APA_NINE_BALL:
                return new TableStatus(9, 9, gameType, ballsOnTable);
            case BCA_NINE_BALL:
                return new TableStatus(9, 9, gameType, ballsOnTable);
            case BCA_TEN_BALL:
                return new TableStatus(10, 10, gameType, ballsOnTable);
            default:
                throw new InvalidGameTypeException(gameType.name());
        }
    }

    @Override public List<Integer> getBallsToRemoveFromTable() {
        List<Integer> ballsOffTable = new ArrayList<>();

        for (int ball : table.keySet()) {
            if (table.get(ball) != ON_TABLE)
                ballsOffTable.add(ball);
        }

        return ballsOffTable;
    }

    public List<BallStatus> getBallStatuses() {
        List<BallStatus> ballStatuses = new ArrayList<>();
        for (int i = 1; i <= size(); i++) {
            ballStatuses.add(table.get(i));
        }

        return ballStatuses;
    }

    public void removeBallsFromTable(List<Integer> ballsToRemove) {
        for (int ball : ballsToRemove) {
            if (table.get(ball) == ON_TABLE)
                table.put(ball, OFF_TABLE);
        }
    }

    public void setBallTo(BallStatus status, int... i) throws InvalidBallException {
        for (int ball : i) {
            if (table.get(ball) == null) {
                throw new InvalidBallException();
            } else
                table.put(ball, status);
        }
    }

    public int getDeadBalls() {
        return Collections.frequency(table.values(), DEAD)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD ? 1 : 0);
    }

    public int getDeadBallsOnBreak() {
        return Collections.frequency(table.values(), DEAD_ON_BREAK)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_DEAD ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK ? 1 : 0);
    }

    public int getBallsRemaining() {
        return Collections.frequency(table.values(), ON_TABLE);
    }

    public int getBreakBallsMade() {
        return Collections.frequency(table.values(), MADE_ON_BREAK)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD ? 1 : 0);
    }

    public int getShootingBallsMade() {
        return Collections.frequency(table.values(), MADE)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE ? 1 : 0);
    }

    public boolean isGameBallMade() {
        return table.get(GAME_BALL) == MADE ||
                table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ||
                table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE;
    }

    public boolean getGameBallMadeOnBreak() {
        return table.get(GAME_BALL) == MADE_ON_BREAK
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE;
    }

    public boolean getGameBallMadeIllegally() {
        BallStatus status = table.get(GAME_BALL);

        return status == DEAD || status == GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
    }

    public BallStatus getBallStatus(int ball) throws InvalidBallException {
        if (table.get(ball) != null)
            return table.get(ball);
        else throw new InvalidBallException();
    }

    public GameType getGameType() {
        return gameType;
    }

    public int size() {
        return table.size();
    }

    private void setupTable(int size, BallStatus statusOfBalls) {
        for (int i = 1; i <= size; i++) {
            table.put(i, statusOfBalls);
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableStatus that = (TableStatus) o;

        if (GAME_BALL != that.GAME_BALL) return false;
        return table.equals(that.table);

    }

    @Override public int hashCode() {
        int result = table.hashCode();
        result = 31 * result + GAME_BALL;
        return result;
    }

    @Override public String toString() {
        return "TableStatus{" +
                "table=" + table.toString() +
                ", GAME_BALL=" + GAME_BALL +
                '}';
    }


}
