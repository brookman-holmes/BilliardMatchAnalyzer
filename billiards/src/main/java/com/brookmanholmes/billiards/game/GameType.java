package com.brookmanholmes.billiards.game;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum GameType {
    BCA_EIGHT_BALL,
    BCA_TEN_BALL,
    BCA_NINE_BALL,
    BCA_GHOST_EIGHT_BALL,
    BCA_GHOST_NINE_BALL,
    BCA_GHOST_TEN_BALL,
    STRAIGHT_POOL,
    AMERICAN_ROTATION,
    APA_NINE_BALL,
    APA_EIGHT_BALL,
    APA_GHOST_EIGHT_BALL,
    APA_GHOST_NINE_BALL,
    STRAIGHT_GHOST,
    EQUAL_OFFENSE,
    EQUAL_DEFENSE,
    ALL;

    public boolean isApa() {
        return name().contains("APA");
    }

    public boolean isBca() {
        return name().contains("BCA");
    }

    public boolean isSinglePlayer() {
        return name().contains("GHOST");
    }

    public boolean isApa8Ball() {
        return this.equals(APA_EIGHT_BALL) || this.equals(APA_GHOST_EIGHT_BALL);
    }

    public boolean isApa9Ball() {
        return this.equals(APA_NINE_BALL) || this.equals(APA_GHOST_NINE_BALL);
    }

    public boolean isStraightPool() {
        return this.equals(STRAIGHT_POOL) || this.equals(STRAIGHT_GHOST);
    }

    public boolean is9Ball() {
        return this.equals(APA_GHOST_NINE_BALL) || this.equals(BCA_GHOST_NINE_BALL)
                || this.equals(APA_NINE_BALL) || this.equals(BCA_NINE_BALL);
    }

    public boolean is10Ball() {
        return this.equals(BCA_TEN_BALL) || this.equals(BCA_GHOST_TEN_BALL);
    }

    public boolean is8Ball() {
        return isBca8Ball() || isApa8Ball();
    }

    public boolean isBca8Ball() {
        return this.equals(BCA_EIGHT_BALL) || this.equals(BCA_GHOST_EIGHT_BALL);
    }

    public boolean isBca9Ball() {
        return this.equals(BCA_NINE_BALL) || this.equals(BCA_GHOST_NINE_BALL);
    }

    public boolean isWinOnBreak() {
        return isApa() || is9Ball() || this.equals(ALL);
    }

    public boolean isWinEarly() {
        return is9Ball() || this.equals(BCA_TEN_BALL) || this.equals(ALL);
    }

    public int getMaxBalls() {
        switch (this) {
            case BCA_EIGHT_BALL:
            case BCA_GHOST_EIGHT_BALL:
            case APA_EIGHT_BALL:
            case APA_GHOST_EIGHT_BALL:
                return 15;
            case BCA_TEN_BALL:
            case BCA_GHOST_TEN_BALL:
                return 10;
            case BCA_NINE_BALL:
            case APA_NINE_BALL:
            case APA_GHOST_NINE_BALL:
            case BCA_GHOST_NINE_BALL:
                return 9;
            case STRAIGHT_POOL:
            case STRAIGHT_GHOST:
                return 999;
            case AMERICAN_ROTATION:
            case EQUAL_OFFENSE:
            case EQUAL_DEFENSE:
            case ALL:
                return 0;
            default:
                return 0;
        }
    }

    public int getGameBall() {
        switch (this) {
            case BCA_EIGHT_BALL:
            case BCA_GHOST_EIGHT_BALL:
            case APA_EIGHT_BALL:
            case APA_GHOST_EIGHT_BALL:
                return 8;
            case BCA_TEN_BALL:
            case BCA_GHOST_TEN_BALL:
                return 10;
            case BCA_NINE_BALL:
            case APA_NINE_BALL:
            case APA_GHOST_NINE_BALL:
            case BCA_GHOST_NINE_BALL:
                return 9;
            case STRAIGHT_POOL:
            case STRAIGHT_GHOST:
                return 999;
            case AMERICAN_ROTATION:
            case EQUAL_OFFENSE:
            case EQUAL_DEFENSE:
            case ALL:
                return 0;
            default:
                return 0;
        }
    }
}
