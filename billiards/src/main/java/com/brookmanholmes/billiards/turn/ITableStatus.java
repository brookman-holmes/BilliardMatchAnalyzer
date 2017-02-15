package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public interface ITableStatus extends Serializable {
    /**
     * Returns the number of balls of status
     * {@link BallStatus#MADE}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_MADE}
     * {@link BallStatus#GAME_BALL_DEAD_ON_BREAK_THEN_MADE}
     *
     * @return the number of balls made
     */
    int getShootingBallsMade();

    /**
     * Returns the number of balls of status
     * {@link BallStatus#DEAD}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_DEAD}
     * @return the number of dead shooting balls
     */
    int getDeadBalls();

    /**
     * Returns the number of balls of status
     * {@link BallStatus#DEAD_ON_BREAK}
     * {@link BallStatus#GAME_BALL_DEAD_ON_BREAK_THEN_MADE}
     * {@link BallStatus#GAME_BALL_DEAD_ON_BREAK_THEN_DEAD}
     * {@link BallStatus#GAME_BALL_DEAD_ON_BREAK}
     * @return the number of dead break balls
     */
    int getDeadBallsOnBreak();

    /**
     * Returns the number of balls with status
     * {@link BallStatus#MADE_ON_BREAK}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_MADE}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_DEAD}
     * @return the number of balls made on the break
     */
    int getBreakBallsMade();

    /**
     * Returns whether or not the game ball has status
     * {@link BallStatus#MADE_ON_BREAK}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_DEAD}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_MADE}
     * @return true if the game ball was legally pocketed on the break, false otherwise
     */
    boolean isGameBallMadeOnBreak();

    /**
     * Returns whether or not the game ball has status
     * {@link BallStatus#MADE}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_MADE}
     * {@link BallStatus#GAME_BALL_DEAD_ON_BREAK_THEN_MADE}
     * @return true if the game ball was legally pocketed, false otherwise
     */
    boolean isGameBallMade();

    /**
     * Return the number of balls of status
     * {@link BallStatus#ON_TABLE}
     * @return the number of balls on the table
     */
    int getBallsRemaining();

    /**
     * Retrieve the status of a ball
     *
     * @param ball the number of the ball you want
     * @return the current status of the ball in the form of
     * {@link BallStatus}
     * @throws InvalidBallException if the ball is not in the range of 1 to 9, 10, or 15 (depending
     *                              on the game type)
     */
    BallStatus getBallStatus(int ball) throws InvalidBallException;

    /**
     * Sets the status of certain balls on the table
     *
     * @param status The status that you want to set the balls to
     * @param balls  The balls to set to that status
     */
    void setBallTo(BallStatus status, int... balls);

    /**
     * Returns whether or not the game ball has status
     * {@link BallStatus#DEAD}
     * {@link BallStatus#GAME_BALL_MADE_ON_BREAK_THEN_DEAD}
     * @return true if the game ball was illegally pocketed, false otherwise
     */
    boolean isGameBallMadeIllegally();

    /**
     * Returns a list of BallStatuses, where each ball is represented in order by the list
     * i.e. 1 ball is at location 0, 8 ball is at location 7, etc.
     * @return A list of BallStatuses representing the state of the table
     */
    List<BallStatus> getBallStatuses();

    /**
     * Returns a list of balls that are to be removed from the table when this turn is added
     * to the current game where each integer represents that ball
     * @return A list of balls that are no longer on the table
     */
    List<Integer> getBallsToRemoveFromTable();

    /**
     * The number of balls that this game keeps track of
     * @return a number that is 9 (nine ball), 10 (ten ball), or 15 (eight ball)
     */
    int size();

    /**
     * The type of game that this interface is representing
     * @return an enum of type {@link GameType}
     */
    GameType getGameType();

    /**
     * The ball that decides if the game is over
     *
     * @return 8/9/10 depending on the game type
     */
    int getGameBall();
}
