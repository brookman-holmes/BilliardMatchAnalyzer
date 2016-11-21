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

    /**
     * Gets the number of points that the turn is worth (based on the APA 9 ball scoring system)
     * @param turn The turn to check
     * @return an integer between 0 and 10 that this turn is worth
     */
    public static int getPointsFromTurn(ITurn turn) {
        return turn.getBreakBallsMade()
                + turn.getShootingBallsMade()
                + (turn.getTurnEnd() == TurnEnd.GAME_WON ? 1 : 0);
    }

    @Override boolean setAllowPush(ITurn turn) {
        return false;
    }

    @Override boolean setAllowTurnSkip(ITurn turn) {
        return false;
    }

    @Override int getCurrentPlayersConsecutiveFouls() {
        return 0;
    }

    @Override void startNewGame(ITurn turn) {
        super.startNewGame(turn);
        allowPush = false;
    }
}
