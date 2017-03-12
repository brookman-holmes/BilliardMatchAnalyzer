package com.brookmanholmes.bma.ui.matchinfo;

import android.support.annotation.Nullable;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by helios on 9/23/2016.
 */

public class ApaBinder extends BindingAdapter {
    public String playerRank = "0", opponentRank = "0";

    public int playerPoints, opponentPoints;
    public int playerPointsNeeded, opponentPointsNeeded;

    public String playerMatchPoints = "0", opponentMatchPoints = "0";

    public String playerDefenses = "0", opponentDefenses = "0";

    public String innings = "0", deadBalls = "0";

    public boolean apa8Ball;

    ApaBinder(String title, boolean expanded, GameType gameType) {
        super(title, expanded, gameType.isApa());
        this.helpLayout = R.layout.dialog_help_apa;
    }

    @Override
    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        if (player.getGameType().isApa8Ball()) {
            apa8Ball = true;
        } else if (player.getGameType().isApa9Ball()) {
            deadBalls = player.getDeadBalls() + "";
        }

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

            if (gameStatus != null)
                this.innings = gameStatus.innings + "";
        }

        if (player.getGameType().isApa9Ball()) {
            deadBalls = player.getDeadBalls() + "";
        }

        notifyChange();
    }
}
