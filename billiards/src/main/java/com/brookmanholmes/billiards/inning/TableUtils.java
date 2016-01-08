package com.brookmanholmes.billiards.inning;

import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.PlayerColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.brookmanholmes.billiards.game.util.BallStatus.MADE;
import static com.brookmanholmes.billiards.game.util.BallStatus.MADE_ON_BREAK;
import static com.brookmanholmes.billiards.game.util.BallStatus.ON_TABLE;
import static com.brookmanholmes.billiards.game.util.PlayerColor.SOLIDS;
import static com.brookmanholmes.billiards.game.util.PlayerColor.STRIPES;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public class TableUtils {
    private static TableStatus table;

    public static int getStripesMadeOnBreak(TableStatus table) {
        setTable(table);
        return getColorMadeOnBreak(STRIPES);
    }

    public static int getSolidsMadeOnBreak(TableStatus table) {
        setTable(table);
        return getColorMadeOnBreak(SOLIDS);
    }

    public static int getSolidsMade(TableStatus table) {
        setTable(table);
        return getColorMade(SOLIDS);
    }

    public static int getStripesMade(TableStatus table) {
        setTable(table);
        return getColorMade(STRIPES);
    }

    public static int getSolidsRemaining(TableStatus table) {
        setTable(table);
        return getColorRemaining(SOLIDS);
    }

    public static int getStripesRemaining(TableStatus table) {
        setTable(table);
        return getColorRemaining(STRIPES);
    }

    private static int getColorMadeOnBreak(PlayerColor colorToCount) {
        return Collections.frequency(getBallStatusOfColor(colorToCount), MADE_ON_BREAK);
    }

    private static int getColorRemaining(PlayerColor colorToCount) {
        return Collections.frequency(getBallStatusOfColor(colorToCount), ON_TABLE);
    }

    private static int getColorMade(PlayerColor colorToCount) {
        return Collections.frequency(getBallStatusOfColor(colorToCount), MADE);
    }

    private static void setTable(TableStatus tableStatus) {
        table = tableStatus;
    }

    private static List<BallStatus> getBallStatusOfColor(PlayerColor colorToChoose) {
        int from, to;
        if (colorToChoose == SOLIDS) {
            from = 0;
            to = 7;
        } else if (colorToChoose == STRIPES) {
            from = 8;
            to = 15;
        } else return Collections.emptyList();

        return getBallStatuses().subList(from, to);
    }

    private static List<BallStatus> getBallStatuses() {
        List<BallStatus> ballStatuses = new ArrayList<>(15);
        for (int i = 1; i <= table.size(); i++) {
            ballStatuses.add(table.getBallStatus(i));
        }

        return ballStatuses;
    }
}
