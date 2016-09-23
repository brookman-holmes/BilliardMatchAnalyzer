package com.brookmanholmes.bma.ui.matchinfo;


import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class MatchOverviewBinder extends BindingAdapter {
    String playerWinPct, opponentWinPct;

    int playerGamesWon, opponentGamesWon;
    int playerGamesPlayed, opponentGamesPlayed;

    String playerTsp, opponentTsp;

    int playerShotsSuccess, opponentShotsSuccess;
    int playerTotalShots, opponentTotalShots;

    String playerTotalFouls, opponentTotalFouls;

    String playerAggRating, opponentAggRating;

    public MatchOverviewBinder(AbstractPlayer player, AbstractPlayer opponent, String title) {
        playerWinPct = player.getWinPct();
        opponentWinPct = opponent.getWinPct();

        playerGamesWon = player.getWins();
        opponentGamesWon = opponent.getWins();
        playerGamesPlayed = player.getGameTotal();
        opponentGamesPlayed = opponent.getGameTotal();

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
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        playerWinPct = player.getWinPct();
        opponentWinPct = opponent.getWinPct();

        playerGamesWon = player.getWins();
        opponentGamesWon = opponent.getWins();
        playerGamesPlayed = player.getGameTotal();
        opponentGamesPlayed = opponent.getGameTotal();

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

    public String getPlayerWinPct() {
        return playerWinPct;
    }

    public String getOpponentWinPct() {
        return opponentWinPct;
    }

    public int getPlayerGamesWon() {
        return playerGamesWon;
    }

    public int getOpponentGamesWon() {
        return opponentGamesWon;
    }

    public int getPlayerGamesPlayed() {
        return playerGamesPlayed;
    }

    public int getOpponentGamesPlayed() {
        return opponentGamesPlayed;
    }

    public String getPlayerTsp() {
        return playerTsp;
    }

    public String getOpponentTsp() {
        return opponentTsp;
    }

    public int getPlayerShotsSuccess() {
        return playerShotsSuccess;
    }

    public int getOpponentShotsSuccess() {
        return opponentShotsSuccess;
    }

    public int getPlayerTotalShots() {
        return playerTotalShots;
    }

    public int getOpponentTotalShots() {
        return opponentTotalShots;
    }

    public String getPlayerTotalFouls() {
        return playerTotalFouls;
    }

    public String getOpponentTotalFouls() {
        return opponentTotalFouls;
    }

    public String getPlayerAggRating() {
        return playerAggRating;
    }

    public String getOpponentAggRating() {
        return opponentAggRating;
    }

    public String getBold() {
        return "bold";
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
}
