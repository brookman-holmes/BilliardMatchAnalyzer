package com.brookmanholmes.billiards.game.util;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum BallStatus {
    ON_TABLE,
    OFF_TABLE,
    MADE,
    DEAD,
    MADE_ON_BREAK,
    DEAD_ON_BREAK,
    GAME_BALL_MADE_ON_BREAK,
    GAME_BALL_MADE_ON_BREAK_THEN_MADE,
    GAME_BALL_MADE_ON_BREAK_THEN_DEAD;

    @Override
    public String toString() {
        switch (this) {
            case ON_TABLE:
                return "OnTable";
            case OFF_TABLE:
                return "OffTable";
            case MADE:
                return "Made";
            case MADE_ON_BREAK:
                return "MadeOnBreak";
            case DEAD_ON_BREAK:
                return "DeadOnBreak";
            case GAME_BALL_MADE_ON_BREAK:
                return "GameBallMadeOnBreak";
            case GAME_BALL_MADE_ON_BREAK_THEN_DEAD:
                return "GameBallMadeOnBreakThenDead";
            case GAME_BALL_MADE_ON_BREAK_THEN_MADE:
                return "GameBallMadeOnBreakThenMade";
            default:
                throw new IllegalArgumentException();
        }
    }
}
