package com.brookmanholmes.billiards.game;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum GameType {
    BCA_EIGHT_BALL(false, false, true, false),
    BCA_TEN_BALL(false, false, true, false),
    BCA_NINE_BALL(false, false, true, false),
    BCA_GHOST_EIGHT_BALL(true, false, true, false),
    BCA_GHOST_NINE_BALL(true, false, true, false),
    BCA_GHOST_TEN_BALL(true, false, true, false),
    STRAIGHT_POOL(false, false, false, false),
    AMERICAN_ROTATION(false, false, false, false),
    APA_NINE_BALL(false, true, false, false),
    APA_EIGHT_BALL(false, true, false, false),
    APA_GHOST_EIGHT_BALL(true, true, false, false),
    APA_GHOST_NINE_BALL(true, true, false, false),
    STRAIGHT_GHOST(false, false, false, true),// is not classified as a ghost game because there are no turns by the second player
    EQUAL_OFFENSE(false, false, false, true),// is not classified as a ghost game because there are no turns by the second player
    EQUAL_DEFENSE(false, false, false, false), ALL(false, false, false, false);

    private final boolean ghostGame;
    private final boolean apa;
    private final boolean bca;
    private final boolean isSinglePlayer;

    GameType(final boolean ghostGame, final boolean apa, final boolean bca, final boolean singlePlayer) {
        this.ghostGame = ghostGame;
        this.apa = apa;
        this.bca = bca;
        this.isSinglePlayer = singlePlayer;
    }

    public boolean isGhostGame() {
        return ghostGame;
    }

    public boolean isApa() {
        return apa;
    }

    public boolean isBca() {
        return bca;
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
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
        return this.equals(BCA_TEN_BALL);
    }

    public boolean isBca8Ball() {
        return this.equals(BCA_EIGHT_BALL) || this.equals(BCA_GHOST_EIGHT_BALL);
    }

    public boolean isBca9Ball() {
        return this.equals(BCA_NINE_BALL) || this.equals(BCA_GHOST_NINE_BALL);
    }

    public boolean isWinOnBreak() {
        return isApa() || is9Ball();
    }

    public boolean isWinEarly() {
        return is9Ball() || this.equals(BCA_TEN_BALL);
    }
}
