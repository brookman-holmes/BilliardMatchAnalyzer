package com.brookmanholmes.bma.ui.matchinfo;

import android.text.Html;
import android.text.Spanned;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.TurnEnd;

/**
 * Created by Brookman Holmes on 4/30/2016.
 */
class TurnStringAdapter {
    private final ITurn turn;
    private final String playerName;
    private final StringBuilder turnBuilder = new StringBuilder();

    public TurnStringAdapter(ITurn turn, AbstractPlayer player, String color) {
        this.turn = turn;

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
        return "<b><font color='#57000000'>" + val + "</font></b>";
    }

    private String formatVal(String val) {
        return "<b><font color='#57000000'>" + val + "</font></b>";
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

    private void buildAdvStats() {
        if (turn.getAdvStats().getShotType().equals("Break shot")) {
            for (String item : turn.getAdvStats().getWhyTypes()) {
                turnBuilder.append(" (")
                        .append(formatVal(item.toLowerCase()))
                        .append(")");
            }
        } else if (turn.getAdvStats().getShotType().equals("Safety error")) {
            appendHowAndWhy();
        } else if (turn.getAdvStats().getShotType().equals("Safety")) {
            turnBuilder.append(" (")
                    .append(formatVal(turn.getAdvStats().getShotSubtype().toLowerCase()))
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
                turnBuilder.append(formatVal(turn.getAdvStats().getShotSubtype().toLowerCase()));
            } else {
                turnBuilder.append(formatVal(turn.getAdvStats().getShotType().toLowerCase()));
            }

            appendHowAndWhy();
        }
    }

    private void appendHowAndWhy() {
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
