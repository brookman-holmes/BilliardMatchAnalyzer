package com.brookmanholmes.billiardmatchanalyzer.ui.dialog;

import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;

/**
 * Created by Brookman Holmes on 3/23/2016.
 */
class RotationGameStatusStringBuilder extends GameStatusStringBuilder {
    String allowPush, allowTurnSkip, consecutivePlayerFouls, consecutiveOpponentFouls;

    RotationGameStatusStringBuilder(Match<?> match) {
        super(match);

        allowPush = "Allow " + getPlayerName(status.turn) + " to push out? " + (status.allowPush ? "yes" : "no");
        allowTurnSkip = "Allow " + getPlayerName(status.turn) + " to skip their turn? " + (status.allowTurnSkip ? "yes" : "no");
        consecutivePlayerFouls = getPlayerName(PlayerTurn.PLAYER) + " is on " + status.consecutivePlayerFouls + " fouls";
        consecutiveOpponentFouls = getPlayerName(PlayerTurn.OPPONENT) + " is on " + status.consecutiveOpponentFouls + " fouls";
    }

    @Override String createGameStatusString() {
        return gameType
                + ", " + breakType
                + "\n" + turn
                + "\n" + breaker
                + "\n" + newGame
                + "\n" + allowPush
                + "\n" + allowTurnSkip
                + "\n" + consecutivePlayerFouls
                + "\n" + consecutiveOpponentFouls
                + "\n" + ballsOnTable;
    }
}
