package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TurnEnd;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Locale;

/**
 * Created by Brookman Holmes on 4/30/2016.
 */
public class TurnStringAdapter {
    Turn turn;
    AdvStats advStats;
    String playerName;
    AbstractPlayer player;
    StringBuilder turnBuilder = new StringBuilder();
    String mainColor, accentColor;
    String title;

    public TurnStringAdapter(Pair<Turn, AdvStats> data, AbstractPlayer player, PlayerTurn playerTurn) {
        this.turn = data.getLeft();
        this.advStats = data.getRight();
        this.player = player;

        if (playerTurn == PlayerTurn.PLAYER) {
            mainColor = "#2196F3";
            accentColor = "<font color='#2196F3'>";
        } else {
            mainColor = "#FF3D00";
            accentColor = "<font color='#FF3D00'>";
        }

        playerName = "<font color='" + mainColor + "'>" + player.getName() + "</font>";

        title = "%1$s <font color='";
        title += playerTurn == PlayerTurn.PLAYER ? "#2196F3" : "#FF3D00";
        title += "'>%2$s</font>";
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

    void buildTurn() {
        if (isBreakShot()) {
            turnBuilder.append(playerName);
            if (isBallMadeOnBreak()) {
                turnBuilder.append(" made ")
                        .append(accentColor)
                        .append(String.valueOf(turn.getBreakBallsMade()))
                        .append("</font>")
                        .append(turn.getBreakBallsMade() > 1 ? " balls" : " ball")
                        .append(" on the break");

                if (isBallMade()) {
                    turnBuilder.append(" and pocketed ")
                            .append(accentColor)
                            .append(String.valueOf(turn.getShootingBallsMade()))
                            .append("</font>")
                            .append(" more")
                            .append(turn.getShootingBallsMade() > 1 ? " balls" : " ball");
                }

                if (turn.getTurnEnd() == TurnEnd.GAME_WON) {
                    turnBuilder.append(" to win the game");
                } else if (turn.getTurnEnd() == TurnEnd.MISS) {
                    turnBuilder.append(" and then missed");
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY) {
                    turnBuilder.append(" and then played safe");
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) {
                    turnBuilder.append(" and then failed their safety attempt");
                } else if (turn.getTurnEnd() == TurnEnd.PUSH_SHOT) {
                    turnBuilder.append(" and then pushed");
                }

                if (turn.isScratch()) {
                    turnBuilder.append(" and fouled");
                }
            } else {
                if (turn.isScratch()) {
                    turnBuilder.append(" fouled on the break");
                } else {
                    turnBuilder.append(" broke dry");
                }
            }
        } else {
            if (isBallMade()) {
                turnBuilder.append(playerName)
                        .append(" pocketed ")
                        .append(accentColor)
                        .append(String.valueOf(turn.getShootingBallsMade()))
                        .append("</font>")
                        .append(turn.getShootingBallsMade() > 1 ? " balls" : " ball");

                if (turn.getTurnEnd() == TurnEnd.GAME_WON) {
                    turnBuilder.append(" to win the game");
                } else if (turn.getTurnEnd() == TurnEnd.MISS) {
                    turnBuilder.append(" and then missed");
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY) {
                    turnBuilder.append(" and then played safe");
                } else if (turn.getTurnEnd() == TurnEnd.SAFETY_ERROR) {
                    turnBuilder.append(" and then failed their safety attempt");
                }
            } else {
                turnBuilder.append(playerName);

                if (turn.getTurnEnd() == TurnEnd.MISS)
                    turnBuilder.append(" missed");
                else if (turn.getTurnEnd() == TurnEnd.SAFETY)
                    turnBuilder.append(" played safe");
                else if (turn.getTurnEnd() == TurnEnd.SAFETY_ERROR)
                    turnBuilder.append(" failed their safety attempt");
                else if (turn.getTurnEnd() == TurnEnd.SKIP_TURN)
                    turnBuilder.append(" gave the shot back");
            }

            if (turn.isScratch()) {
                turnBuilder.append(" and fouled");
            }
        }
    }

    Spanned getTurnString() {
        buildTurn();

        if (advStats != null) {
            buildAdvStats();
        }

        return Html.fromHtml(turnBuilder.toString());
    }

    void buildAdvStats() {
        Log.i("TurnStringAdapter", advStats.toString());
        if (advStats.getShotType().equals("Break shot")) {
            for (String item : advStats.getWhyTypes()) {
                turnBuilder.append(" (")
                        .append(accentColor)
                        .append(item.toLowerCase())
                        .append("</font>")
                        .append(")");
            }
        } else if (advStats.getShotType().equals("Safety error")) {
            appendHowAndWhy();
        } else if (advStats.getShotType().equals("Safety")) {
            turnBuilder.append(" (")
                    .append(accentColor)
                    .append(advStats.getShotSubtype().toLowerCase())
                    .append("</font>")
                    .append(")");
        } else { // this should be misses
            if (turn.getTurnEnd() != TurnEnd.GAME_WON)
                turnBuilder.append(" on a ");

            if (advStats.getAngles().size() > 0) {
                turnBuilder.append(advStats.getAngles().get(0));

                for (int i = 1; i < advStats.getAngles().size(); i++) {
                    turnBuilder.append(", ");
                    turnBuilder.append(advStats.getAngles().get(i));
                }
                turnBuilder.append(" ");
            }

            turnBuilder.append(" ");

            if (!advStats.getShotSubtype().equals("")) {
                turnBuilder.append(accentColor)
                        .append(advStats.getShotSubtype().toLowerCase())
                        .append("</font>");
            } else {
                turnBuilder.append(accentColor)
                        .append(advStats.getShotType().toLowerCase())
                        .append("</font> ");
            }

            appendHowAndWhy();
        }
    }

    void appendHowAndWhy() {
        // format hows
        if (advStats.getHowTypes().size() > 0) {
            turnBuilder.append("<br><br>How: ");
        }

        for (int i = 0; i < advStats.getHowTypes().size(); i++) {
            turnBuilder.append(advStats.getHowTypes().get(i));

            if (i != advStats.getHowTypes().size() - 1)
                turnBuilder.append(", ");
        }

        // format whys
        if (advStats.getWhyTypes().size() > 0) {
            turnBuilder.append("<br><br>Why: ");
        }

        for (int i = 0; i < advStats.getWhyTypes().size(); i++) {
            turnBuilder.append(advStats.getWhyTypes().get(i));

            if (i != advStats.getWhyTypes().size() - 1)
                turnBuilder.append(", ");
        }
    }

    Spanned getShootingStats() {
        return Html.fromHtml(String.format(Locale.getDefault(), title, "Shooting", player.getShootingPct()));
    }

    Spanned getBreakingStats() {
        return Html.fromHtml(String.format(Locale.getDefault(), title, "Breaking", player.getBreakPct()));
    }

    Spanned getSafetyStats() {
        return Html.fromHtml(String.format(Locale.getDefault(), title, "Safeties", player.getSafetyPct()));
    }
}
