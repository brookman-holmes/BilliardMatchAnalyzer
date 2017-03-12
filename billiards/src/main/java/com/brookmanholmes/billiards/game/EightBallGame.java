package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableUtils;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.Arrays;

import static com.brookmanholmes.billiards.game.PlayerColor.OPEN;
import static com.brookmanholmes.billiards.game.PlayerColor.SOLIDS;
import static com.brookmanholmes.billiards.game.PlayerColor.STRIPES;

/**
 * Subclass of {@link com.brookmanholmes.billiards.game.Game} that keeps track of the status of a
 * game of 8 ball (BCA rule set)
 * <p></p>Created by Brookman Holmes on 10/26/2015.
 */
class EightBallGame extends Game {
    EightBallGame(PlayerTurn turn, BreakType breakType) {
        super(GameType.BCA_EIGHT_BALL, turn, breakType);
    }

    EightBallGame(GameType gameType, PlayerTurn turn, BreakType breakType) throws InvalidGameTypeException {
        super(gameType, turn, breakType);
    }

    EightBallGame(GameType gameType, PlayerTurn turn, BreakType breakType, int maxAttemptsPerGame) {
        super(gameType, turn, breakType, maxAttemptsPerGame);
    }

    /**
     * Changes the input from a PlayerColor to the correct PlayerColor based on the turn and returns
     * it
     * @param currentPlayerColor The player color that the current player should be
     * @return The player color of the Player
     */
    PlayerColor convertCurrentPlayerColorToPlayerColor(PlayerColor currentPlayerColor) {
        if (currentPlayerColor == OPEN)
            return OPEN;

        if (turn == PlayerTurn.PLAYER)
            return currentPlayerColor;
        else {
            if (currentPlayerColor == SOLIDS)
                return STRIPES;
            else return SOLIDS;
        }
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
        if (playerColor == OPEN) {
            if (TableUtils.getSolidsMade(turn.getBallStatuses()) > 0) {
                return convertCurrentPlayerColorToPlayerColor(SOLIDS);
            } else if (TableUtils.getStripesMade(turn.getBallStatuses()) > 0) {
                return convertCurrentPlayerColorToPlayerColor(STRIPES);
            } else
                return OPEN;
        } else
            return playerColor;
    }

    @Override
    boolean setAllowPlayerToBreakAgain(ITurn turn) {
        return turn.getTurnEnd() == TurnEnd.BREAK_MISS
                && turn.isFoul()
                && turn.getBallsToRemoveFromTable().contains(gameType.getGameBall());
    }

    @Override
    public int[] getGhostBallsToWinGame() {
        if (playerColor == SOLIDS) {
            ballsOnTable.removeAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        } else {
            ballsOnTable.removeAll(Arrays.asList(9, 10, 11, 12, 13, 14, 15));
        }

        int[] ballsToWin = new int[ballsOnTable.size()];

        for (int i = 0; i < ballsOnTable.size(); i++) {
            ballsToWin[i] = ballsOnTable.get(i);
        }

        return ballsToWin;
    }

    @Override
    int getCurrentPlayersConsecutiveFouls() {
        return 0;
    }
}
