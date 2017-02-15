package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 12/6/2016.
 */

public class StraightPoolBinder extends BindingAdapter {
    private static final String TAG = "StraightPoolBinder";

    public int playerPoints = 0, opponentPoints = 0;
    public int playerPointsNeeded = 0, opponentPointsNeeded = 0;

    public String playerTsp = ".000", opponentTsp = ".000";

    public int playerShotsSuccess = 0, opponentShotsSuccess = 0;
    public int playerTotalShots = 0, opponentTotalShots = 0;

    public String playerAggRating = ".000", opponentAggRating = ".000";

    public String playerTotalFouls = "0", opponentTotalFouls = "0";

    public int ballsRemaining;

    StraightPoolBinder(Player player, Player opponent, String title, boolean expanded) {
        super(expanded, player.getGameType().isStraightPool());

        if (player.getGameType().isStraightPool()) {
            update(player, opponent);
        }

        this.title = title;
        this.helpLayout = R.layout.dialog_help_straight_pool;
    }

    public void update(Player player, Player opponent) {
        if (player.getGameType().isStraightPool()) {
            playerPoints = player.getPoints();
            opponentPoints = opponent.getPoints();
            playerPointsNeeded = player.getRank();
            opponentPointsNeeded = opponent.getRank();

            playerTsp = pctf.format(player.getTrueShootingPct());
            opponentTsp = pctf.format(opponent.getTrueShootingPct());

            playerShotsSuccess = player.getShotsSucceededOfAllTypes();
            opponentShotsSuccess = opponent.getShotsSucceededOfAllTypes();
            playerTotalShots = player.getShotAttemptsOfAllTypes();
            opponentTotalShots = opponent.getShotAttemptsOfAllTypes();

            playerTotalFouls = Integer.toString(player.getTotalFouls());
            opponentTotalFouls = Integer.toString(opponent.getTotalFouls());

            playerAggRating = pctf.format(player.getAggressivenessRating());
            opponentAggRating = pctf.format(opponent.getAggressivenessRating());

            ballsRemaining = 15 - ((player.getShootingBallsMade() + opponent.getShootingBallsMade()) % 14);

            notifyChange();
        }
    }

    public int highlightShooting() {
        return compare(playerTsp, opponentTsp);
    }

    public int highlightFouls() {
        return compare(playerTotalFouls, opponentTotalFouls) * -1;
    }
}
