package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;

/**
 * Created by Brookman Holmes on 11/8/2015.
 * A helper class that creates a turn with chaining methods for convenience
 */
public class TurnBuilder {
    private final int GAME_BALL;
    private final ITableStatus status;
    private boolean foul = false;
    private boolean seriousFoul = false;
    private AdvStats advStats = null;

    /**
     * Creates a builder for creating a new turn to add to a match
     * @param gameType The type of game that this turn is being added to
     */
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

    /**
     * Sets status of balls to {@link BallStatus#DEAD}
     * @param balls the balls which you want to be set to dead
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder deadBalls(int... balls) {
        status.setBallTo(BallStatus.DEAD, balls);
        return this;
    }

    /**
     * Sets status of balls to {@link BallStatus#DEAD_ON_BREAK}
     * @param balls the balls which you want to be set to dead on the break
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder deadOnBreak(int... balls) {
        status.setBallTo(BallStatus.DEAD_ON_BREAK, balls);
        return this;
    }

    /**
     * Sets status of balls to {@link BallStatus#MADE}
     * @param balls the balls which you want to be set to made
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder madeBalls(int... balls) {
        status.setBallTo(BallStatus.MADE, balls);
        return this;
    }

    /**
     * Sets status of game ball to {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_MADE}
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder gameBallMadeOnBreakAndThenMade() {
        status.setBallTo(BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE, GAME_BALL);
        return this;
    }

    /**
     * Sets status of game ball to {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_DEAD}
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder gameBallMadeOnBreakAndThenDead() {
        status.setBallTo(BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD, GAME_BALL);
        return this;
    }

    /**
     * Sets status of balls to {@link BallStatus#MADE_ON_BREAK}
     * @param balls the balls which you want to be set to made on the break
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder breakBalls(int... balls) {
        status.setBallTo(BallStatus.MADE_ON_BREAK, balls);
        return this;
    }

    /**
     * Sets status of balls to {@link BallStatus#OFF_TABLE}
     * @param balls the balls which you want to be set to off the table
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder offTable(int... balls) {
        status.setBallTo(BallStatus.OFF_TABLE, balls);
        return this;
    }

    /**
     * Sets that you fouled
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder fouled() {
        foul = true;
        return this;
    }

    /**
     * Sets the advanced stats for this turn
     * @param advStats The {@link com.brookmanholmes.billiards.turn.AdvStats} that you would like to
     *                 add to this turn
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder setAdvStats(AdvStats advStats) {
        this.advStats = advStats;
        return this;
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#MISS} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn miss() {
        return new Turn(TurnEnd.MISS, status, foul, seriousFoul, advStats);
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#MISS} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn win() {
        return new Turn(TurnEnd.GAME_WON, status, foul, seriousFoul, advStats);
    }

    /**
     * Sets the this object o have a serious foul
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#MISS} as the turn end and that a serious foul
     * was committed
     * @return This instance of {@link com.brookmanholmes.billiards.turn.TurnBuilder} for chaining purposes
     */
    public TurnBuilder seriousFoul() {
        seriousFoul = true;
        return this;
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#SAFETY} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn safety() {
        return new Turn(TurnEnd.SAFETY, status, foul, seriousFoul, advStats);
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#SAFETY_ERROR} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn safetyMiss() {
        return new Turn(TurnEnd.SAFETY_ERROR, status, foul, seriousFoul, advStats);
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#BREAK_MISS} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn breakMiss() {
        return new Turn(TurnEnd.BREAK_MISS, status, foul, seriousFoul, advStats);
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#PUSH_SHOT} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn push() {
        return new Turn(TurnEnd.PUSH_SHOT, status, foul, seriousFoul, advStats);
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#SKIP_TURN} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn skipTurn() {
        return new Turn(TurnEnd.SKIP_TURN, status, foul, seriousFoul, advStats);
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#CONTINUE_WITH_GAME} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn continueGame() {
        return new Turn(TurnEnd.CONTINUE_WITH_GAME, status, foul, seriousFoul, advStats);
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#CURRENT_PLAYER_BREAKS_AGAIN} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn currentPlayerBreaks() {
        return new Turn(TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN, status, foul, seriousFoul, advStats);
    }

    /**
     * Creates a new turn with the arguments supplied to this builder with
     * {@link com.brookmanholmes.billiards.turn.TurnEnd#OPPONENT_BREAKS_AGAIN} as the turn end
     * @return A new instance of {@link com.brookmanholmes.billiards.turn.ITurn}
     */
    public ITurn opposingPlayerBreaks() {
        return new Turn(TurnEnd.OPPONENT_BREAKS_AGAIN, status, foul, seriousFoul, advStats);
    }
}
