package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;

/**
 * Created by Brookman Holmes on 11/8/2015.
 * A helper class that creates a turn with chaining methods for convenience
 */
public class TurnBuilder {
    private final int GAME_BALL;
    private final ITableStatus status;
    private boolean scratch = false;

    public TurnBuilder(GameType gameType) {
        status = TableStatus.newTable(gameType);
        switch (gameType) {
            case APA_EIGHT_BALL:
                GAME_BALL = 8;
                break;
            case BCA_EIGHT_BALL:
                GAME_BALL = 8;
                break;
            case APA_NINE_BALL:
                GAME_BALL = 9;
                break;
            case BCA_NINE_BALL:
                GAME_BALL = 9;
                break;
            case BCA_TEN_BALL:
                GAME_BALL = 10;
                break;
            default:
                GAME_BALL = 15;
        }
    }

    public TurnBuilder deadBalls(int... balls) {
        status.setBallTo(BallStatus.DEAD, balls);
        return this;
    }

    public TurnBuilder deadOnBreak(int... balls) {
        status.setBallTo(BallStatus.DEAD_ON_BREAK, balls);
        return this;
    }

    public TurnBuilder madeBalls(int... balls) {
        status.setBallTo(BallStatus.MADE, balls);
        return this;
    }

    public TurnBuilder gameBallMadeOnBreakAndThenMade() {
        status.setBallTo(BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE, GAME_BALL);
        return this;
    }

    public TurnBuilder gameBallMadeOnBreakAndThenDead() {
        status.setBallTo(BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD, GAME_BALL);
        return this;
    }

    public TurnBuilder breakBalls(int... balls) {
        status.setBallTo(BallStatus.MADE_ON_BREAK, balls);
        return this;
    }

    public TurnBuilder offTable(int... balls) {
        status.setBallTo(BallStatus.OFF_TABLE, balls);
        return this;
    }

    public TurnBuilder scratch() {
        scratch = true;
        return this;
    }

    public ITurn miss() {
        return new Turn(0, 0L, scratch, TurnEnd.MISS, status, false, null);
    }

    public ITurn win() {
        return new Turn(0, 0L, scratch, TurnEnd.GAME_WON, status, false, null);
    }

    public ITurn lose() {
        return new Turn(0, 0L, scratch, TurnEnd.MISS, status, true, null);
    }

    public ITurn safety() {
        return new Turn(0, 0L, scratch, TurnEnd.SAFETY, status, false, null);
    }

    public ITurn safetyMiss() {
        return new Turn(0, 0L, scratch, TurnEnd.SAFETY_ERROR, status, false, null);
    }

    public ITurn breakMiss() {
        return new Turn(0, 0L, scratch, TurnEnd.BREAK_MISS, status, false, null);
    }

    public ITurn push() {
        return new Turn(0, 0L, scratch, TurnEnd.PUSH_SHOT, status, false, null);
    }

    public ITurn skipTurn() {
        return new Turn(0, 0L, scratch, TurnEnd.SKIP_TURN, status, false, null);
    }

    public ITurn continueGame() {
        return new Turn(0, 0L, scratch, TurnEnd.CONTINUE_WITH_GAME, status, false, null);
    }

    public ITurn currentPlayerBreaks() {
        return new Turn(0, 0L, scratch, TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN, status, false, null);
    }

    public ITurn opposingPlayerBreaks() {
        return new Turn(0, 0L, scratch, TurnEnd.OPPONENT_BREAKS_AGAIN, status, false, null);
    }
}
