package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Subclass of {@link com.brookmanholmes.billiards.game.NineBallGame} that keeps track of a game of
 * 9 ball with APA rules
 * <p></p>Created by Brookman Holmes on 10/27/2015.
 */
class ApaNineBallGame extends NineBallGame {
    ApaNineBallGame(PlayerTurn turn) {
        super(GameType.APA_NINE_BALL, turn, BreakType.WINNER);
        allowPush = false;
    }

    @Override
    boolean setAllowPush(ITurn turn) {
        return false;
    }

    @Override
    boolean setAllowTurnSkip(ITurn turn) {
        return false;
    }

    @Override
    int getCurrentPlayersConsecutiveFouls() {
        return 0;
    }

    @Override
    boolean isGameOver(ITurn turn) {
        return turn.getTurnEnd() == TurnEnd.GAME_WON;
    }

    @Override
    void startNewGame(ITurn turn) {
        super.startNewGame(turn);
        allowPush = false;
    }
}
