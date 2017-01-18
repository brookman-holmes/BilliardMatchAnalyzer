package com.brookmanholmes.bma.ui.matchinfo;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

/**
 * Created by Brookman Holmes on 4/30/2016.
 */
class TurnStringAdapter {
    private final ITurn turn;
    private final String playerName;
    private final StringBuilder turnBuilder = new StringBuilder();
    private final Context context;

    TurnStringAdapter(Context context, ITurn turn, AbstractPlayer player, String color) {
        this.turn = turn;
        this.context = context;

        playerName = "<b><font color='" + color + "'>" + player.getName() + "</font></b>";
    }

    private boolean isBreakShot() {
        return turn.getBreakBallsMade() > 0 ||
                turn.getTurnEnd() == TurnEnd.BREAK_MISS;
    }

    private boolean isBallMadeOnBreak() {
        return turn.getBreakBallsMade() > 0;
    }

    private boolean isBallMade() {
        return turn.getShootingBallsMade() > 0;
    }

    private String formatVal(int val) {
        return "<b><font color='#de000000'>" + val + "</font></b>";
    }

    private void buildTurn() {
        if (isBreakShot()) {
            turnBuilder.append(playerName);
            if (isBallMadeOnBreak()) {
                turnBuilder.append(" made ")
                        .append(formatVal(turn.getBreakBallsMade()))
                        .append(turn.getBreakBallsMade() > 1 ? " balls" : " ball")
                        .append(" on the break");

                if (isBallMade()) {
                    turnBuilder.append(" and pocketed ")
                            .append(formatVal(turn.getShootingBallsMade()))
                            .append(" more")
                            .append(turn.getShootingBallsMade() > 1 ? " balls" : " ball");
                }

                if (turn.getTurnEnd() == TurnEnd.GAME_WON) {
                    turnBuilder.append(" to win the game");
                } else if (turn.getTurnEnd() == TurnEnd.MISS) {
                    turnBuilder.append(" and then missed");
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY) {
                    turnBuilder.append(" and then played safe");
                    if (turn.getAdvStats() != null && AdvStats.SubType.isSafety(turn.getAdvStats().getShotSubtype())) {
                        turnBuilder.append(" (<b>");
                        turnBuilder.append(context.getString(MatchDialogHelperUtils.convertSubTypeToStringRes(turn.getAdvStats().getShotSubtype())));
                        turnBuilder.append("</b>)");
                    }
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) {
                    turnBuilder.append(" and then failed their safety attempt");
                } else if (turn.getTurnEnd() == TurnEnd.PUSH_SHOT) {
                    turnBuilder.append(" and then pushed");
                }

                if (turn.isFoul()) {
                    turnBuilder.append(" and fouled");
                }
            } else {
                if (turn.isFoul()) {
                    turnBuilder.append(" fouled on the break");
                } else {
                    turnBuilder.append(" broke dry");
                }
            }
        } else {
            if (isBallMade()) {
                turnBuilder.append(playerName)
                        .append(" pocketed ")
                        .append(formatVal(turn.getShootingBallsMade()))
                        .append(turn.getShootingBallsMade() > 1 ? " balls" : " ball");

                if (turn.getTurnEnd() == TurnEnd.GAME_WON) {
                    turnBuilder.append(" to win the game");
                } else if (turn.getTurnEnd() == TurnEnd.MISS) {
                    turnBuilder.append(" and then missed");
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY) {
                    turnBuilder.append(" and then played safe");
                    if (turn.getAdvStats() != null) {
                        turnBuilder.append(" (<b>");
                        turnBuilder.append(context.getString(MatchDialogHelperUtils.convertSubTypeToStringRes(turn.getAdvStats().getShotSubtype())));
                        turnBuilder.append("</b>)");
                    }
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) {
                    turnBuilder.append(" and then failed their safety attempt");
                }
            } else {
                turnBuilder.append(playerName);

                switch (turn.getTurnEnd()) {
                    case SAFETY_ERROR:
                        turnBuilder.append(" failed their safety attempt");
                        break;
                    case MISS:
                        turnBuilder.append(" missed");
                        break;
                    case BREAK_MISS:
                        break;
                    case GAME_WON:
                        break;
                    case SAFETY:
                        turnBuilder.append(" played safe");
                        if (turn.getAdvStats() != null) {
                            turnBuilder.append(" (<b>");
                            turnBuilder.append(context.getString(MatchDialogHelperUtils.convertSubTypeToStringRes(turn.getAdvStats().getShotSubtype())));
                            turnBuilder.append("</b>)");
                        }
                        break;
                    case PUSH_SHOT:
                        turnBuilder.append(" pushed");
                        break;
                    case SKIP_TURN:
                        turnBuilder.append(" gave the shot back");
                        break;
                    case CURRENT_PLAYER_BREAKS_AGAIN:
                        turnBuilder.append(" chooses to break");
                        break;
                    case OPPONENT_BREAKS_AGAIN:
                        turnBuilder.append(" chooses their opponent to break");
                        break;
                    case CONTINUE_WITH_GAME:
                        turnBuilder.append(" chooses to continue shooting");
                        break;
                    case CHANGE_TURN:
                        break;
                    case ILLEGAL_BREAK:
                        break;
                }
            }

            if (turn.isFoul()) {
                turnBuilder.append(" and fouled");
            }
        }
    }

    Spanned getTurnString() {
        buildTurn();
        return Html.fromHtml(turnBuilder.toString());
    }
}
