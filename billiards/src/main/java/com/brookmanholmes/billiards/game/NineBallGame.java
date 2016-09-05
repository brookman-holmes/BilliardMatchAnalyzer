package com.brookmanholmes.billiards.game;

/**
 * Subclass of {@link com.brookmanholmes.billiards.game.RotationGame} that keeps track of the status
 * of a game of 9 ball (BCA rule set)
 * <p></p>Created by Brookman Holmes on 10/27/2015.
 */
class NineBallGame extends RotationGame {
    private final static int GAME_BALL = 9;
    private final static int MAX_BALLS = 9;

    NineBallGame(PlayerTurn playerTurn, BreakType breakType) {
        super(GameType.BCA_NINE_BALL, playerTurn, breakType, MAX_BALLS, GAME_BALL);
    }

    NineBallGame(GameType gameType, PlayerTurn playerTurn, BreakType breakType) throws InvalidGameTypeException {
        super(gameType, playerTurn, breakType, MAX_BALLS, GAME_BALL);

        if (gameType != GameType.APA_NINE_BALL)
            throw new InvalidGameTypeException(gameType.name());
    }

    @Override boolean winOnBreak() {
        return true;
    }
}
