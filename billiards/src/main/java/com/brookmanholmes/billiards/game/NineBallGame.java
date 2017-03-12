package com.brookmanholmes.billiards.game;

/**
 * Subclass of {@link com.brookmanholmes.billiards.game.RotationGame} that keeps track of the status
 * of a game of 9 ball (BCA rule set)
 * <p></p>Created by Brookman Holmes on 10/27/2015.
 */
class NineBallGame extends RotationGame {
    NineBallGame(PlayerTurn playerTurn, BreakType breakType) {
        super(GameType.BCA_NINE_BALL, playerTurn, breakType);
    }

    NineBallGame(GameType gameType, PlayerTurn playerTurn, BreakType breakType) throws InvalidGameTypeException {
        super(gameType, playerTurn, breakType);
    }

    NineBallGame(GameType gameType, PlayerTurn playerTurn, BreakType breakType, int maxAttemptsPerGame) throws InvalidGameTypeException {
        super(gameType, playerTurn, breakType, maxAttemptsPerGame);
    }

    @Override boolean winOnBreak() {
        return true;
    }
}
