package com.brookmanholmes.billiardmatchanalyzer.ui.dialog;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;

/**
 * Created by Brookman Holmes on 3/23/2016.
 */
public abstract class GameStatusStringBuilder {
    private static final String ARG_PLAYER_NAME = "player name";
    private static final String ARG_OPPONENT_NAME = "opponent name";

    GameStatus status;

    String playerName;
    String opponentName;
    String gameType;
    String newGame;
    String turn;
    String breaker;
    String breakType;
    String ballsOnTable;

    GameStatusStringBuilder(Match<?> match) {
        this.status = match.getGameStatus();

        playerName = match.getPlayer().getName();
        opponentName = match.getOpponent().getName();

        gameType = "Game: " + status.gameType.toString();
        newGame = "New game: " + (status.newGame ? "yes" : "no");
        turn = "Current turn: " + getPlayerName(status.turn);
        breaker = getPlayerName(status.breaker) + (status.newGame ? " is breaking" : " broke this rack");
        breakType = getBreakType(status.breakType).toLowerCase();
        ballsOnTable = "Current balls on the table:\n" + status.ballsOnTable.toString();
    }

    public static String getMatchStatusString(Match<?> match) {
        GameStatusStringBuilder builder = new RotationGameStatusStringBuilder(match);

        return builder.createGameStatusString();
    }

    String getPlayerName(PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER)
            return playerName;
        else if (turn == PlayerTurn.OPPONENT)
            return opponentName;
        else throw new IllegalStateException("No such turn as: " + turn.name());
    }

    String getBreakType(BreakType breakType) {
        switch (breakType) {
            case PLAYER:
                return getPlayerName(PlayerTurn.PLAYER) + " always breaks";
            case OPPONENT:
                return getPlayerName(PlayerTurn.OPPONENT) + " always breaks";
            default:
                return breakType.toString() + " breaks";
        }
    }

    abstract String createGameStatusString();
}
