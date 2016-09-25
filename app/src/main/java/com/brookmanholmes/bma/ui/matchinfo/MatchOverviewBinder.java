package com.brookmanholmes.bma.ui.matchinfo;


import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.CompPlayer;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class MatchOverviewBinder extends BindingAdapter {
    public String playerWinPct, opponentWinPct;

    public int playerGamesWon, opponentGamesWon;
    public int playerGamesPlayed, opponentGamesPlayed;

    public String playerTsp, opponentTsp;

    public int playerShotsSuccess, opponentShotsSuccess;
    public int playerTotalShots, opponentTotalShots;

    public String playerTotalFouls, opponentTotalFouls;

    public String playerAggRating, opponentAggRating;

    public boolean apaTitle = false;

    public MatchOverviewBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded) {
        if (useGameTotal(player, opponent)) {
            apaTitle = true;
        }

        playerWinPct = player.getWinPct();
        opponentWinPct = opponent.getWinPct();

        playerGamesWon = player.getWins();
        opponentGamesWon = opponent.getWins();

        if (useGameTotal(player, opponent)) {
            playerGamesPlayed = player.getGameTotal();
            opponentGamesPlayed = opponent.getGameTotal();
        } else {
            playerGamesPlayed = player.getRank();
            opponentGamesPlayed = opponent.getRank();
        }

        playerTsp = player.getTrueShootingPct();
        opponentTsp = opponent.getTrueShootingPct();

        playerShotsSuccess = player.getShotsSucceededOfAllTypes();
        opponentShotsSuccess = opponent.getShotsSucceededOfAllTypes();
        playerTotalShots = player.getShotAttemptsOfAllTypes();
        opponentTotalShots = opponent.getShotAttemptsOfAllTypes();

        playerTotalFouls = Integer.toString(player.getTotalFouls());
        opponentTotalFouls = Integer.toString(opponent.getTotalFouls());

        playerAggRating = player.getAggressivenessRating();
        opponentAggRating = opponent.getAggressivenessRating();

        this.title = title;
        helpLayout = R.layout.dialog_help_match_overview;

        visible = expanded;
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        playerWinPct = player.getWinPct();
        opponentWinPct = opponent.getWinPct();

        playerGamesWon = player.getWins();
        opponentGamesWon = opponent.getWins();

        if (useGameTotal(player, opponent)) {
            playerGamesPlayed = player.getGameTotal();
            opponentGamesPlayed = opponent.getGameTotal();
        } else {
            playerGamesPlayed = player.getRank();
            opponentGamesPlayed = opponent.getRank();
        }

        playerTsp = player.getTrueShootingPct();
        opponentTsp = opponent.getTrueShootingPct();

        playerShotsSuccess = player.getShotsSucceededOfAllTypes();
        opponentShotsSuccess = opponent.getShotsSucceededOfAllTypes();
        playerTotalShots = player.getShotAttemptsOfAllTypes();
        opponentTotalShots = opponent.getShotAttemptsOfAllTypes();

        playerTotalFouls = Integer.toString(player.getTotalFouls());
        opponentTotalFouls = Integer.toString(opponent.getTotalFouls());

        playerAggRating = player.getAggressivenessRating();
        opponentAggRating = opponent.getAggressivenessRating();

        notifyChange();
    }

    public boolean playerShootingBetter() {
        return Double.compare(
                Double.parseDouble(playerTsp),
                Double.parseDouble(opponentTsp)
        ) > 0;
    }

    public boolean opponentShootingBetter() {
        return Double.compare(
                Double.parseDouble(playerTsp),
                Double.parseDouble(opponentTsp)
        ) < 0;
    }

    public boolean opponentFoulsMore() {
        return Integer.parseInt(playerTotalFouls) > Integer.parseInt(opponentTotalFouls);
    }

    public boolean playerFoulsMore() {
        return Integer.parseInt(playerTotalFouls) < Integer.parseInt(opponentTotalFouls);
    }

    private boolean useGameTotal(AbstractPlayer player, AbstractPlayer opponent) {
        return (player instanceof ApaEightBallPlayer && opponent instanceof ApaEightBallPlayer) ||
                (player instanceof ApaNineBallPlayer && opponent instanceof ApaNineBallPlayer) ||
                (player instanceof CompPlayer && opponent instanceof CompPlayer);
    }
}
