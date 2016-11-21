package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;

/**
 * Created by Brookman Holmes on 11/18/2016.
 */

class StraightPoolGame extends Game {
    StraightPoolGame(PlayerTurn turn) {
        super(GameType.STRAIGHT_POOL, turn, BreakType.WINNER, 15, 0);
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
    PlayerColor setPlayerColor(ITurn turn) {
        return null;
    }

    @Override
    boolean setAllowPlayerToBreakAgain(ITurn turn) {
        return false;
    }

    @Override
    public int[] getGhostBallsToWinGame() {
        return new int[0];
    }
}
