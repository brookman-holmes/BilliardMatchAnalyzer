package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.IApa;
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

    ApaBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded, int innings) {
        super(expanded, player instanceof IApa);
        this.title = title;
        this.helpLayout = R.layout.dialog_help_apa;

        update(player, opponent, innings);

        if (player instanceof ApaEightBallPlayer) {
            apa8Ball = true;
        } else if (player instanceof ApaNineBallPlayer) {
            deadBalls = ((ApaNineBallPlayer) player).getDeadBalls() + "";
        }
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent, int innings) {
        if (player instanceof IApa && opponent instanceof IApa) {
            playerRank = player.getRank() + "";
            opponentRank = opponent.getRank() + "";

            playerPoints = ((IApa) player).getPoints();
            opponentPoints = ((IApa) opponent).getPoints();
            playerPointsNeeded = ((IApa) player).getPointsNeeded();
            opponentPointsNeeded = ((IApa) opponent).getPointsNeeded();

            playerMatchPoints = ((IApa) player).getMatchPoints(((IApa) opponent).getPoints()) + "";
            opponentMatchPoints = ((IApa) opponent).getMatchPoints(((IApa) player).getPoints()) + "";

            playerDefenses = player.getSafetyAttempts() + "";
            opponentDefenses = opponent.getSafetyAttempts() + "";

            this.innings = innings + "";
        }

        if (player instanceof ApaNineBallPlayer) {
            deadBalls = ((ApaNineBallPlayer) player).getDeadBalls() + "";
        }

        notifyChange();
    }
}
