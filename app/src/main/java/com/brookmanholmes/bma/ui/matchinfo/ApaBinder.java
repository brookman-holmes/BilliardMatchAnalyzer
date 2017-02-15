package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by helios on 9/23/2016.
 */

public class ApaBinder extends BindingAdapter {
    public String playerRank, opponentRank;

    public int playerPoints, opponentPoints;
    public int playerPointsNeeded, opponentPointsNeeded;

    public String playerMatchPoints, opponentMatchPoints;

    public String playerDefenses, opponentDefenses;

    public String innings, deadBalls = "0";

    public boolean apa8Ball;

    ApaBinder(Player player, Player opponent, String title, boolean expanded, int innings) {
        super(expanded, player.getGameType().isApa());
        this.title = title;
        this.helpLayout = R.layout.dialog_help_apa;

        update(player, opponent, innings);

        if (player.getGameType().isApa8Ball()) {
            apa8Ball = true;
        } else if (player.getGameType().isApa9Ball()) {
            deadBalls = player.getDeadBalls() + "";
        }
    }

    public void update(Player player, Player opponent, int innings) {
        if (player.getGameType().isApa()) {
            playerRank = player.getRank() + "";
            opponentRank = opponent.getRank() + "";

            playerPoints = player.getPoints();
            opponentPoints = opponent.getPoints();
            playerPointsNeeded = player.getPointsNeeded();
            opponentPointsNeeded = opponent.getPointsNeeded();

            playerMatchPoints = player.getMatchPoints(opponent.getPoints()) + "";
            opponentMatchPoints = opponent.getMatchPoints(player.getPoints()) + "";

            playerDefenses = player.getSafetyAttempts() + "";
            opponentDefenses = opponent.getSafetyAttempts() + "";

            this.innings = innings + "";
        }

        if (player.getGameType().isApa9Ball()) {
            deadBalls = player.getDeadBalls() + "";
        }

        notifyChange();
    }
}
