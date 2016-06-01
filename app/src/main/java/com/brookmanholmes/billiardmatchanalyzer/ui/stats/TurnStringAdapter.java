package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.text.Html;
import android.text.Spanned;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 4/30/2016.
 */
public class TurnStringAdapter {
    Turn turn;
    String playerName;
    AbstractPlayer player;
    StringBuilder turnBuilder = new StringBuilder();
    String mainColor, accentColor;
    String title;

    public TurnStringAdapter(Turn turn, AbstractPlayer player) {
        this.turn = turn;
        this.player = player;

        mainColor = "#57000000";
        accentColor = "<font color='#57000000'>";

        playerName = "<font color='" + mainColor + "'>" + player.getName() + "</font>";

        title = "%1$s %2$s";
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
                else if (turn.getTurnEnd() == TurnEnd.PUSH_SHOT)
                    turnBuilder.append(" pushed");
            }

            if (turn.isFoul()) {
                turnBuilder.append(" and fouled");
            }
        }
    }

    Spanned getTurnString() {
        buildTurn();

        if (turn.getAdvStats() != null && !turn.getAdvStats().getShotType().equals("")) {
            buildAdvStats();
        }

        return Html.fromHtml(turnBuilder.toString());
    }

    void buildAdvStats() {
        if (turn.getAdvStats().getShotType().equals("Break shot")) {
            for (String item : turn.getAdvStats().getWhyTypes()) {
                turnBuilder.append(" (")
                        .append(accentColor)
                        .append(item.toLowerCase())
                        .append("</font>")
                        .append(")");
            }
        } else if (turn.getAdvStats().getShotType().equals("Safety error")) {
            appendHowAndWhy();
        } else if (turn.getAdvStats().getShotType().equals("Safety")) {
            turnBuilder.append(" (")
                    .append(accentColor)
                    .append(turn.getAdvStats().getShotSubtype().toLowerCase())
                    .append("</font>")
                    .append(")");
        } else { // this should be misses
            if (turn.getTurnEnd() != TurnEnd.GAME_WON)
                turnBuilder.append(" on a ");

            if (turn.getAdvStats().getAngles().size() > 0) {
                turnBuilder.append(turn.getAdvStats().getAngles().get(0));

                for (int i = 1; i < turn.getAdvStats().getAngles().size(); i++) {
                    turnBuilder.append(", ");
                    turnBuilder.append(turn.getAdvStats().getAngles().get(i));
                }
                turnBuilder.append(" ");
            }

            turnBuilder.append(" ");

            if (!turn.getAdvStats().getShotSubtype().equals("")) {
                turnBuilder.append(accentColor)
                        .append(turn.getAdvStats().getShotSubtype().toLowerCase())
                        .append("</font>");
            } else {
                turnBuilder.append(accentColor)
                        .append(turn.getAdvStats().getShotType().toLowerCase())
                        .append("</font> ");
            }

            appendHowAndWhy();
        }
    }

    void appendHowAndWhy() {
        // format hows
        if (turn.getAdvStats().getHowTypes().size() > 0) {
            turnBuilder.append("<br><br>How: ");
        }

        for (int i = 0; i < turn.getAdvStats().getHowTypes().size(); i++) {
            turnBuilder.append(turn.getAdvStats().getHowTypes().get(i));

            if (i != turn.getAdvStats().getHowTypes().size() - 1)
                turnBuilder.append(", ");
        }

        // format whys
        if (turn.getAdvStats().getWhyTypes().size() > 0) {
            turnBuilder.append("<br><br>Why: ");
        }

        for (int i = 0; i < turn.getAdvStats().getWhyTypes().size(); i++) {
            turnBuilder.append(turn.getAdvStats().getWhyTypes().get(i));

            if (i != turn.getAdvStats().getWhyTypes().size() - 1)
                turnBuilder.append(", ");
        }
    }
}
