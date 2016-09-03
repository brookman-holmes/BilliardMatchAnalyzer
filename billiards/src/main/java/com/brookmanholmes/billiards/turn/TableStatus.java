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

    /**
     * Creates a new table of the correct size with all balls on the table
     *
     * @param gameType The type of game this table represents
     *                 {@link com.brookmanholmes.billiards.game.util.GameType}
     * @return A new table with all balls on it
     * @throws InvalidGameTypeException thrown when
     *                                  {@link com.brookmanholmes.billiards.game.util.GameType#AMERICAN_ROTATION} or
     *                                  {@link com.brookmanholmes.billiards.game.util.GameType#STRAIGHT_POOL} is selected because
     *                                  these games are not yet supported
     */
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

    /**
     * Creates a new table of the correct size with only the specified balls on it
     * @param gameType The type of game this table represents
     * @param ballsOnTable The balls which you want to remain on the table
     * @return A new table with only the balls in {@param ballsOnTable} on it
     * @throws InvalidGameTypeException thrown when
     * {@link com.brookmanholmes.billiards.game.util.GameType#AMERICAN_ROTATION} or
     * {@link com.brookmanholmes.billiards.game.util.GameType#STRAIGHT_POOL} is selected because
     * these games are not yet supported
     */
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

    @Override public List<BallStatus> getBallStatuses() {
        List<BallStatus> ballStatuses = new ArrayList<>();
        for (int i = 1; i <= size(); i++) {
            ballStatuses.add(table.get(i));
        }

        return ballStatuses;
    }

    /**
     * Removes balls in the list from the table
     * @param ballsToRemove integers representing each ball that you want to remove
     */
    public void removeBallsFromTable(int... ballsToRemove) {
        for (int ball : ballsToRemove) {
            if (table.get(ball) == ON_TABLE)
                table.put(ball, OFF_TABLE);
        }
    }

    @Override public void setBallTo(BallStatus status, int... balls) throws InvalidBallException {
        for (int ball : balls) {
            if (table.get(ball) == null) {
                throw new InvalidBallException();
            } else
                table.put(ball, status);
        }
    }

    @Override public int getDeadBalls() {
        return Collections.frequency(table.values(), DEAD)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD ? 1 : 0);
    }

    @Override public int getDeadBallsOnBreak() {
        return Collections.frequency(table.values(), DEAD_ON_BREAK)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_DEAD ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK ? 1 : 0);
    }

    @Override public int getBallsRemaining() {
        return Collections.frequency(table.values(), ON_TABLE);
    }

    @Override public int getBreakBallsMade() {
        return Collections.frequency(table.values(), MADE_ON_BREAK)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD ? 1 : 0);
    }

    @Override public int getShootingBallsMade() {
        return Collections.frequency(table.values(), MADE)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE ? 1 : 0);
    }

    @Override public boolean isGameBallMade() {
        return table.get(GAME_BALL) == MADE ||
                table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ||
                table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE;
    }

    @Override public boolean getGameBallMadeOnBreak() {
        return table.get(GAME_BALL) == MADE_ON_BREAK
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE;
    }

    @Override public boolean getGameBallMadeIllegally() {
        return table.get(GAME_BALL) == DEAD ||
                table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
    }

    @Override public BallStatus getBallStatus(int ball) throws InvalidBallException {
        if (table.get(ball) != null)
            return table.get(ball);
        else throw new InvalidBallException();
    }

    @Override public GameType getGameType() {
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
