package com.brookmanholmes.billiards.game;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum GameType {
    BCA_EIGHT_BALL(false, false, true),
    BCA_TEN_BALL(false, false, true),
    BCA_NINE_BALL(false, false, true),
    BCA_GHOST_EIGHT_BALL(true, false, true),
    BCA_GHOST_NINE_BALL(true, false, true),
    BCA_GHOST_TEN_BALL(true, false, true),
    STRAIGHT_POOL(false, false, false),
    AMERICAN_ROTATION(false, false, false),
    APA_NINE_BALL(false, true, false),
    APA_EIGHT_BALL(false, true, false),
    APA_GHOST_EIGHT_BALL(true, true, false),
    APA_GHOST_NINE_BALL(true, true, false);

    private final boolean ghostGame;
    private final boolean apa;
    private final boolean bca;

    GameType(final boolean ghostGame, final boolean apa, final boolean bca) {
        this.ghostGame = ghostGame;
        this.apa = apa;
        this.bca = bca;
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
}
