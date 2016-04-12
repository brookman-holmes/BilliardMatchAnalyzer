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

    @Override public String toString() {
        switch (this) {
            case ON_TABLE:
                return "On Table";
            case OFF_TABLE:
                return "Off Table";
            case DEAD:
                return "Dead";
            case MADE:
                return "Made";
            case MADE_ON_BREAK:
                return "Made On Break";
            case DEAD_ON_BREAK:
                return "Dead On Break";
            case GAME_BALL_MADE_ON_BREAK:
                return "Game Ball Made On Break";
            case GAME_BALL_MADE_ON_BREAK_THEN_DEAD:
                return "Game Ball Made On Break Then Dead";
            case GAME_BALL_MADE_ON_BREAK_THEN_MADE:
                return "Game Ball Made On Break Then Made";
            default:
                throw new IllegalArgumentException("Illegal argument: " + this.name());
        }
    }
}
