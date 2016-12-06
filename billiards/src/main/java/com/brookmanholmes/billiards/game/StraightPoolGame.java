package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;

import java.util.List;

/**
 * Created by Brookman Holmes on 11/18/2016.
 */

class StraightPoolGame extends Game {
    StraightPoolGame(PlayerTurn turn) {
        super(GameType.STRAIGHT_POOL, turn, BreakType.WINNER, 999, 999);
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
        return newGame && turn.isFoul() && turn.getShootingBallsMade() == 0;
    }

    @Override
    void removeBallsFromTable(List<Integer> ballsToRemove) {
        // don't remove balls from the table because it shouldn't matter....
    }

    @Override
    public int[] getGhostBallsToWinGame() {
        return new int[0];
    }
}
