package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

/**
 * Created by Brookman Holmes on 10/27/2015.
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
