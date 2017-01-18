package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;

/**
 * Created by Brookman Holmes on 1/5/2017.
 */

class EqualDefense extends Game {
    EqualDefense(PlayerTurn turn) {
        super(GameType.EQUAL_DEFENSE, turn, BreakType.WINNER, 2, 2);
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
