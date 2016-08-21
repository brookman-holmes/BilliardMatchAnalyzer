package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableUtils;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.Arrays;

import static com.brookmanholmes.billiards.game.util.PlayerColor.OPEN;
import static com.brookmanholmes.billiards.game.util.PlayerColor.SOLIDS;
import static com.brookmanholmes.billiards.game.util.PlayerColor.STRIPES;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
class EightBallGame extends Game {
    private final static int GAME_BALL = 8;
    private final static int MAX_BALLS = 15;

    EightBallGame(PlayerTurn turn, BreakType breakType) {
        super(GameType.BCA_EIGHT_BALL, turn, breakType, MAX_BALLS, GAME_BALL);
    }

    EightBallGame(GameType gameType, PlayerTurn turn, BreakType breakType) throws InvalidGameTypeException {
        super(gameType, turn, breakType, MAX_BALLS, GAME_BALL);

        if (gameType != GameType.APA_EIGHT_BALL)
            throw new InvalidGameTypeException(gameType.name());
    }

    @Override boolean setAllowPush(ITurn turn) {
        return false;
    }

    @Override boolean setAllowTurnSkip(ITurn turn) {
        return false;
    }

    @Override boolean setAllowPlayerToBreakAgain(ITurn turn) {
        return turn.getTurnEnd() == TurnEnd.BREAK_MISS && turn.isFoul() && turn.getBallsToRemoveFromTable().contains(GAME_BALL);
    }

    @Override int getCurrentPlayersConsecutiveFouls() {
        return 0;
    }

    @Override PlayerColor setPlayerColor(ITurn turn) {
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

    @Override public int[] getGhostBallsToWinGame() {
        if (playerColor == SOLIDS) {
            ballsOnTable.removeAll(Arrays.asList(1,2,3,4,5,6,7));
        } else if (playerColor == STRIPES) {
            ballsOnTable.removeAll(Arrays.asList(9,10,11,12,13,14,15));
        }

        int[] ballsToWin = new int[ballsOnTable.size()];

        for (int i = 0; i < ballsOnTable.size(); i++) {
            ballsToWin[i] = ballsOnTable.get(i);
        }

        return ballsToWin;
    }
}
