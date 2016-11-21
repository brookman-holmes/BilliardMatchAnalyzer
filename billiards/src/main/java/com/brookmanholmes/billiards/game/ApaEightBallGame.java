package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;

import java.util.List;

import static com.brookmanholmes.billiards.game.PlayerColor.OPEN;
import static com.brookmanholmes.billiards.game.PlayerColor.SOLIDS;
import static com.brookmanholmes.billiards.game.PlayerColor.STRIPES;
import static com.brookmanholmes.billiards.turn.TableUtils.getSolidsMade;
import static com.brookmanholmes.billiards.turn.TableUtils.getSolidsMadeOnBreak;
import static com.brookmanholmes.billiards.turn.TableUtils.getStripesMade;
import static com.brookmanholmes.billiards.turn.TableUtils.getStripesMadeOnBreak;

/**
 * Subclass of {@link com.brookmanholmes.billiards.game.EightBallGame} that keeps track of the
 * status of a game of 8 ball (APA rule set)
 * <p></p>Created by Brookman Holmes on 10/27/2015.
 */
class ApaEightBallGame extends EightBallGame {
    ApaEightBallGame(PlayerTurn turn) {
        super(GameType.APA_EIGHT_BALL, turn, BreakType.WINNER);
    }

    @Override
    public GameType getGameType() {
        return GameType.APA_EIGHT_BALL;
    }

    @Override
    PlayerColor setPlayerColor(ITurn turn) {
        List<BallStatus> ballStatuses = turn.getBallStatuses(); // makes stuff more readable

        if (newGame) {
            if (getSolidsMadeOnBreak(ballStatuses) == getStripesMadeOnBreak(ballStatuses)) {
                return getColorMade(ballStatuses);
            } else if (getSolidsMadeOnBreak(ballStatuses) > getStripesMadeOnBreak(ballStatuses)) {
                return convertCurrentPlayerColorToPlayerColor(SOLIDS);
            } else if (getSolidsMadeOnBreak(ballStatuses) < getStripesMadeOnBreak(ballStatuses)) {
                return convertCurrentPlayerColorToPlayerColor(STRIPES);
            } else
                throw new IllegalArgumentException("reach end of if statement that I shouldn't"); // this will never happen
        } else {
            if (playerColor == OPEN) {
                return getColorMade(ballStatuses);
            } else
                return playerColor;
        }
    }

    PlayerColor getColorMade(List<BallStatus> ballStatuses) {
        if (getSolidsMade(ballStatuses) > getStripesMade(ballStatuses)) {
            return convertCurrentPlayerColorToPlayerColor(SOLIDS);
        } else if (getSolidsMade(ballStatuses) < getStripesMade(ballStatuses)) {
            return convertCurrentPlayerColorToPlayerColor(STRIPES);
        } else
            return OPEN;
    }

    @Override
    boolean winOnBreak() {
        return true;
    }
}
