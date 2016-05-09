package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 10/27/2015.
 */
class ApaNineBallGame extends NineBallGame {
    ApaNineBallGame() {
        super(GameType.APA_NINE_BALL, PlayerTurn.PLAYER, BreakType.WINNER);
        allowPush = false;
    }

    public static int getPointsFromTurn(com.brookmanholmes.billiards.turn.Turn turn) {
        return turn.getBreakBallsMade()
                + turn.getShootingBallsMade()
                + (turn.getTurnEnd() == TurnEnd.GAME_WON ? 1 : 0);
    }

    @Override boolean setAllowPush(com.brookmanholmes.billiards.turn.Turn turn) {
        return false;
    }

    @Override boolean setAllowTurnSkip(com.brookmanholmes.billiards.turn.Turn turn) {
        return false;
    }

    @Override int getCurrentPlayersConsecutiveFouls() {
        return 0;
    }

    @Override void startNewGame(com.brookmanholmes.billiards.turn.Turn turn) {
        super.startNewGame(turn);
        allowPush = false;
    }
}
