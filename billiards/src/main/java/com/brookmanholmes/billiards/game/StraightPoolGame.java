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
    void setConsecutiveFouls(ITurn turn) {
        // don't count breaking fouls in with consecutive fouls
        if (turn.isFoul() && turn.getShootingBallsMade() == 0 && !newGame) {
            if (this.turn == PlayerTurn.PLAYER)
                consecutivePlayerFouls++;
            else
                consecutiveOpponentFouls++;
        } else {
            if (this.turn == PlayerTurn.PLAYER)
                consecutivePlayerFouls = 0;
            else
                consecutiveOpponentFouls = 0;
        }


        // clear consecutive fouls after the third
        if (turn.isSeriousFoul()) {
            if (this.turn == PlayerTurn.PLAYER)
                consecutivePlayerFouls = 0;
            else consecutiveOpponentFouls = 0;
        }
    }

    @Override
    boolean isGameOver(ITurn turn) {
        return false;
    }

    @Override
    public int[] getGhostBallsToWinGame() {
        return new int[0];
    }
}
