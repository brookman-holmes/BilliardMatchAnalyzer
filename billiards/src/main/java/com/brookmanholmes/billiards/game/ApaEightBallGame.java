package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TableUtils;

/**
 * Subclass of {@link com.brookmanholmes.billiards.game.EightBallGame} that keeps track of the
 * status of a game of 8 ball (APA rule set)
 * <p></p>Created by Brookman Holmes on 10/27/2015.
 */
class ApaEightBallGame extends EightBallGame {
    ApaEightBallGame() {
        super(GameType.APA_EIGHT_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
    }

    @Override public GameType getGameType() {
        return GameType.APA_EIGHT_BALL;
    }

    @Override PlayerColor setPlayerColor(ITurn turn) {
        if (newGame && turn.getBreakBallsMade() > 0) {
            if (TableUtils.getSolidsMadeOnBreak(turn.getBallStatuses()) == TableUtils.getStripesMadeOnBreak(turn.getBallStatuses())) {
                return super.setPlayerColor(turn);
            } else if (TableUtils.getSolidsMadeOnBreak(turn.getBallStatuses()) > 0) {
                return convertCurrentPlayerColorToPlayerColor(PlayerColor.SOLIDS);
            } else if (TableUtils.getStripesMadeOnBreak(turn.getBallStatuses()) > 0) {
                return convertCurrentPlayerColorToPlayerColor(PlayerColor.STRIPES);
            } else return PlayerColor.OPEN; // this will never happen
        } else
            return super.setPlayerColor(turn);
    }

    @Override boolean winOnBreak() {
        return true;
    }
}
