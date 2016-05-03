package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 4/30/2016.
 */
public class TurnStringAdapter {
    Turn turn;
    String playerName;
    AbstractPlayer player;
    StringBuilder builder = new StringBuilder();
    String mainColor, accentColor;

    public TurnStringAdapter(Turn turn, AbstractPlayer player, PlayerTurn playerTurn) {
        this.turn = turn;
        this.player = player;
        if (playerTurn == PlayerTurn.PLAYER) {
            mainColor = "#2196F3";
            accentColor = "<font color='#2196F3'>";
        } else {
            mainColor = "#FF3D00";
            accentColor = "<font color='#FF3D00'>";
        }

        playerName = "<font color='" + mainColor + "'>" + player.getName() + "</font>";
    }

    boolean isBreakShot() {
        return turn.getBreakBallsMade() > 0 ||
                turn.getTurnEnd() == TurnEnd.BREAK_MISS;
    }

    boolean isBallMadeOnBreak() {
        return turn.getBreakBallsMade() > 0;
    }

    boolean isBallMade() {
        return turn.getShootingBallsMade() > 0;
    }

    String getString() {
        if (isBreakShot()) {
            builder.append(playerName);
            if (isBallMadeOnBreak()) {
                builder.append(" made ")
                        .append(accentColor)
                        .append(String.valueOf(turn.getBreakBallsMade()))
                        //.append(String.format(Locale.getDefault(), " (%1$d/%2$d)", player.getBreakSuccesses(), player.getBreakAttempts()))
                        .append("</font>")
                        .append(turn.getBreakBallsMade() > 1 ? " balls" : " ball")
                        .append(" on the break");

                if (isBallMade()) {
                    builder.append(" and pocketed ")
                            .append(accentColor)
                            .append(String.valueOf(turn.getShootingBallsMade()))
                            //.append(String.format(Locale.getDefault(), " (%1$s)", player.getShootingPct()))
                            .append("</font>")
                            .append(" more")
                            .append(turn.getShootingBallsMade() > 1 ? " balls" : " ball");

                    if (turn.getTurnEnd() == TurnEnd.GAME_WON) {
                        builder.append(" to win the game");
                    } else if (turn.getTurnEnd() == TurnEnd.MISS) {
                        builder.append(" and then missed");
                    } else if (turn.getTurnEnd() == TurnEnd.SAFETY) {
                        builder.append(" and then played safe");
                    } else if (turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) {
                        builder.append(" and then failed their safety attempt");
                    }
                }

                if (turn.isScratch()) {
                    builder.append(" and fouled");
                }
            } else {
                if (turn.isScratch()) {
                    builder.append(" fouled on the break");
                } else {
                    builder.append(" broke dry");
                }
            }
        } else {
            if (isBallMade()) {
                builder.append(playerName)
                        .append(" pocketed ")
                        .append(accentColor)
                        .append(String.valueOf(turn.getShootingBallsMade()))
                        //.append(String.format(Locale.getDefault(), " (%1$s)", player.getShootingPct()))
                        .append("</font>")
                        .append(turn.getShootingBallsMade() > 1 ? " balls" : " ball");

                if (turn.getTurnEnd() == TurnEnd.GAME_WON) {
                    builder.append(" to win the game");
                } else if (turn.getTurnEnd() == TurnEnd.MISS) {
                    builder.append(" and then missed");
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY) {
                    builder.append(" and then played safe");
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) {
                    builder.append(" and then failed their safety attempt");
                }
            } else {
                builder.append(playerName);

                if (turn.getTurnEnd() == TurnEnd.MISS)
                    builder.append(" missed");
                else if (turn.getTurnEnd() == TurnEnd.SAFETY)
                    builder.append(" played safe");
                else if (turn.getTurnEnd() == TurnEnd.SAFETY_ERROR)
                    builder.append(" failed their safety attempt");
            }

            if (turn.isScratch()) {
                builder.append(" and fouled");
            }
        }

        return builder.toString();
    }
}
