package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.brookmanholmes.billiards.game.BallStatus.DEAD;
import static com.brookmanholmes.billiards.game.BallStatus.DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_DEAD_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_DEAD;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_MADE;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
import static com.brookmanholmes.billiards.game.BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
import static com.brookmanholmes.billiards.game.BallStatus.MADE;
import static com.brookmanholmes.billiards.game.BallStatus.MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.BallStatus.OFF_TABLE;
import static com.brookmanholmes.billiards.game.BallStatus.ON_TABLE;

/**
 * Created by Brookman Holmes on 10/25/2015.
 */
final public class TableStatus implements ITableStatus, Serializable {
    private final int GAME_BALL;
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
     * @param gameType The type of game this table represents
     * {@link GameType}
     * @return A new table with all balls on it
     * @throws InvalidGameTypeException thrown when
     * {@link GameType#AMERICAN_ROTATION} or
     * {@link GameType#STRAIGHT_POOL} is selected because
     * these games are not yet supported
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
            case BCA_GHOST_EIGHT_BALL:
                return new TableStatus(15, 8, gameType);
            case BCA_GHOST_NINE_BALL:
                return new TableStatus(9, 9, gameType);
            case BCA_GHOST_TEN_BALL:
                return new TableStatus(10, 10, gameType);
            case APA_GHOST_EIGHT_BALL:
                return new TableStatus(15, 8, gameType);
            case APA_GHOST_NINE_BALL:
                return new TableStatus(9, 9, gameType);
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
     * {@link GameType#AMERICAN_ROTATION} or
     * {@link GameType#STRAIGHT_POOL} is selected because
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
            case BCA_GHOST_EIGHT_BALL:
                return new TableStatus(15, 8, gameType, ballsOnTable);
            case BCA_GHOST_NINE_BALL:
                return new TableStatus(9, 9, gameType, ballsOnTable);
            case BCA_GHOST_TEN_BALL:
                return new TableStatus(10, 10, gameType, ballsOnTable);
            case APA_GHOST_EIGHT_BALL:
                return new TableStatus(15, 8, gameType, ballsOnTable);
            case APA_GHOST_NINE_BALL:
                return new TableStatus(9, 9, gameType, ballsOnTable);
            default:
                throw new InvalidGameTypeException(gameType.name());
        }
    }

    @Override
    public List<Integer> getBallsToRemoveFromTable() {
        List<Integer> ballsOffTable = new ArrayList<>();

        for (int ball : table.keySet()) {
            if (table.get(ball) != ON_TABLE)
                ballsOffTable.add(ball);
        }

        return ballsOffTable;
    }

    @Override
    public List<BallStatus> getBallStatuses() {
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

    @Override
    public int getGameBall() {
        return GAME_BALL;
    }

    @Override
    public void setBallTo(BallStatus status, int... balls) throws InvalidBallException {
        for (int ball : balls) {
            if (table.get(ball) == null) {
                throw new InvalidBallException();
            } else
                table.put(ball, status);
        }
    }

    @Override
    public int getDeadBalls() {
        return Collections.frequency(table.values(), DEAD)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD ? 1 : 0);
    }

    @Override
    public int getDeadBallsOnBreak() {
        return Collections.frequency(table.values(), DEAD_ON_BREAK)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_DEAD ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK ? 1 : 0);
    }

    @Override
    public int getBallsRemaining() {
        return Collections.frequency(table.values(), ON_TABLE);
    }

    @Override
    public int getBreakBallsMade() {
        return Collections.frequency(table.values(), MADE_ON_BREAK)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD ? 1 : 0);
    }

    @Override
    public int getShootingBallsMade() {
        return Collections.frequency(table.values(), MADE)
                + (table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ? 1 : 0)
                + (table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE ? 1 : 0);
    }

    @Override
    public boolean isGameBallMade() {
        return table.get(GAME_BALL) == MADE ||
                table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE ||
                table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK_THEN_MADE;
    }

    @Override
    public boolean isGameBallMadeOnBreak() {
        return table.get(GAME_BALL) == MADE_ON_BREAK
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD
                || table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_MADE;
    }

    @Override
    public boolean isGameBallMadeIllegally() {
        return table.get(GAME_BALL) == DEAD ||
                table.get(GAME_BALL) == GAME_BALL_MADE_ON_BREAK_THEN_DEAD ||
                table.get(GAME_BALL) == GAME_BALL_DEAD_ON_BREAK;
    }

    @Override
    public BallStatus getBallStatus(int ball) throws InvalidBallException {
        if (table.get(ball) != null)
            return table.get(ball);
        else throw new InvalidBallException();
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }

    @Override
    public int size() {
        return table.size();
    }

    /**
     * Places balls into the table with the given status from 1 to {@param size} (inclusive)
     * @param size The highest ball that goes on the table
     * @param statusOfBalls The status of all the balls you are putting on the table
     */
    private void setupTable(int size, BallStatus statusOfBalls) {
        for (int i = 1; i <= size; i++) {
            table.put(i, statusOfBalls);
        }
    }


    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        inputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableStatus that = (TableStatus) o;

        if (GAME_BALL != that.GAME_BALL) return false;
        return table.equals(that.table);

    }

    @Override
    public int hashCode() {
        int result = table.hashCode();
        result = 31 * result + GAME_BALL;
        return result;
    }

    @Override
    public String toString() {
        return "TableStatus{" +
                "table=" + table.toString() +
                ", GAME_BALL=" + GAME_BALL +
                '}';
    }
}
