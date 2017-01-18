package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;

/**
 * Created by Brookman Holmes on 1/5/2017.
 */

class EqualOffense extends Game {
    EqualOffense() {
        super(GameType.EQUAL_OFFENSE, PlayerTurn.PLAYER, BreakType.WINNER, 15, 15);
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
        return PlayerColor.OPEN;
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
