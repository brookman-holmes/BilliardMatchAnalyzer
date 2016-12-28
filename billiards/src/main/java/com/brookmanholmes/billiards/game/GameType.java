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
    STRAIGHT_GHOST(false, false, false, true); // is not classified as a ghost game because there are no turns by the second player

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
}
