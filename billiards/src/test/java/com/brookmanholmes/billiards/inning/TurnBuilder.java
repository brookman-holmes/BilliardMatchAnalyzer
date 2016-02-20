package com.brookmanholmes.billiards.inning;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;

/**
 * Created by Brookman Holmes on 11/8/2015.
 */
public class TurnBuilder {
    private TableStatus status;
    private boolean scratch = false;

    public TurnBuilder(GameType gameType) {
        status = TableStatus.newTable(gameType);
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
        status.setBallTo(BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE, status.GAME_BALL);
        return this;
    }

    public TurnBuilder gameBallMadeOnBreakAndThenDead() {
        status.setBallTo(BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD, status.GAME_BALL);
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

    public Turn miss() {
        return new GameTurn(0, 0L, scratch, TurnEnd.MISS, status);
    }

    public Turn win() {
        return new GameTurn(0, 0L, scratch, TurnEnd.GAME_WON, status);
    }

    public Turn lose() {
        return new GameTurn(0, 0L, scratch, TurnEnd.GAME_LOST, status);
    }

    public Turn safety() {
        return new GameTurn(0, 0L, scratch, TurnEnd.SAFETY, status);
    }

    public Turn safetyMiss() {
        return new GameTurn(0, 0L, scratch, TurnEnd.SAFETY_ERROR, status);
    }

    public Turn breakMiss() {
        return new GameTurn(0, 0L, scratch, TurnEnd.BREAK_MISS, status);
    }

    public Turn push() {
        return new GameTurn(0, 0L, scratch, TurnEnd.PUSH_SHOT, status);
    }

    public Turn skipTurn() {
        return new GameTurn(0, 0L, scratch, TurnEnd.SKIP_TURN, status);
    }

    public Turn continueGame() {
        return new GameTurn(0, 0L, scratch, TurnEnd.CONTINUE_WITH_GAME, status);
    }

    public Turn currentPlayerBreaks() {
        return new GameTurn(0, 0L, scratch, TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN, status);
    }

    public Turn opposingPlayerBreaks() {
        return new GameTurn(0, 0L, scratch, TurnEnd.OPPONENT_BREAKS_AGAIN, status);
    }
}
