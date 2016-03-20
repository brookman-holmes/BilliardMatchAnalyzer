package com.brookmanholmes.billiards.turn;

import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.PlayerColor;

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
    public static int getStripesMadeOnBreak(List<BallStatus> ballStatuses) {
        return getColorMadeOnBreak(ballStatuses, STRIPES);
    }

    public static int getSolidsMadeOnBreak(List<BallStatus> ballStatuses) {

        return getColorMadeOnBreak(ballStatuses, SOLIDS);
    }

    public static int getSolidsMade(List<BallStatus> ballStatuses) {

        return getColorMade(ballStatuses, SOLIDS);
    }

    public static int getStripesMade(List<BallStatus> ballStatuses) {

        return getColorMade(ballStatuses, STRIPES);
    }

    public static int getSolidsRemaining(List<BallStatus> ballStatuses) {

        return getColorRemaining(ballStatuses, SOLIDS);
    }

    public static int getStripesRemaining(List<BallStatus> ballStatuses) {

        return getColorRemaining(ballStatuses, STRIPES);
    }

    private static int getColorMadeOnBreak(List<BallStatus> ballStatuses, PlayerColor colorToCount) {
        return Collections.frequency(getBallStatusOfColor(ballStatuses, colorToCount), MADE_ON_BREAK);
    }

    private static int getColorRemaining(List<BallStatus> ballStatuses, PlayerColor colorToCount) {
        return Collections.frequency(getBallStatusOfColor(ballStatuses, colorToCount), ON_TABLE);
    }

    private static int getColorMade(List<BallStatus> ballStatuses, PlayerColor colorToCount) {
        return Collections.frequency(getBallStatusOfColor(ballStatuses, colorToCount), MADE);
    }

    private static List<BallStatus> getBallStatusOfColor(List<BallStatus> ballStatuses, PlayerColor colorToChoose) {
        int from, to;
        if (colorToChoose == SOLIDS) {
            from = 0;
            to = 7;
        } else if (colorToChoose == STRIPES) {
            from = 8;
            to = 15;
        } else
            return Collections.emptyList();

        return ballStatuses.subList(from, to);
    }
}
