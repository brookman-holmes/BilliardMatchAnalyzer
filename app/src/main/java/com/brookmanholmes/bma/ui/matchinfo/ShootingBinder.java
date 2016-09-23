package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class ShootingBinder extends BindingAdapter {
    String playerShootingPct, opponentShootingPct;

    int playerShotsMade, opponentShotsMade;
    int playerShots, opponentShots;

    String playerAvg, opponentAvg;

    String playerFouls, opponentFouls;

    public ShootingBinder(AbstractPlayer player, AbstractPlayer opponent, String title) {
        playerShootingPct = player.getShootingPct();
        opponentShootingPct = opponent.getShootingPct();

        playerShotsMade = player.getShootingBallsMade();
        opponentShotsMade = opponent.getShootingBallsMade();
        playerShots = player.getShootingAttempts();
        opponentShots = opponent.getShootingAttempts();

        playerAvg = player.getAvgBallsTurn();
        opponentAvg = opponent.getAvgBallsTurn();

        playerFouls = player.getShootingFouls() + "";
        opponentFouls = opponent.getShootingFouls() + "";

        this.title = title;
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        playerShootingPct = player.getShootingPct();
        opponentShootingPct = opponent.getShootingPct();

        playerShotsMade = player.getShootingBallsMade();
        opponentShotsMade = opponent.getShootingBallsMade();
        playerShots = player.getShootingAttempts();
        opponentShots = opponent.getShootingAttempts();

        playerAvg = player.getAvgBallsTurn();
        opponentAvg = opponent.getAvgBallsTurn();

        playerFouls = player.getShootingFouls() + "";
        opponentFouls = opponent.getShootingFouls() + "";
        notifyChange();
    }

    public String getPlayerShootingPct() {
        return playerShootingPct;
    }

    public String getOpponentShootingPct() {
        return opponentShootingPct;
    }

    public int getPlayerShotsMade() {
        return playerShotsMade;
    }

    public int getOpponentShotsMade() {
        return opponentShotsMade;
    }

    public int getPlayerShots() {
        return playerShots;
    }

    public int getOpponentShots() {
        return opponentShots;
    }

    public String getPlayerAvg() {
        return playerAvg;
    }

    public String getOpponentAvg() {
        return opponentAvg;
    }

    public String getPlayerFouls() {
        return playerFouls;
    }

    public String getOpponentFouls() {
        return opponentFouls;
    }

    public boolean playerShootingBetter() {
        return Double.compare(
                Double.parseDouble(playerShootingPct),
                Double.parseDouble(opponentShootingPct)
        ) > 1;
    }

    public boolean opponentShootingBetter() {
        return Double.compare(
                Double.parseDouble(playerShootingPct),
                Double.parseDouble(opponentShootingPct)
        ) < 1;
    }

    public boolean playerAvgBetter() {
        return Double.compare(
                Double.parseDouble(playerAvg),
                Double.parseDouble(opponentAvg)
        ) < 1;
    }

    public boolean opponentAvgBetter() {
        return Double.compare(
                Double.parseDouble(playerAvg),
                Double.parseDouble(opponentAvg)
        ) < 1;
    }

    public boolean playerFoulsBetter() {
        return Double.compare(
                Double.parseDouble(playerFouls),
                Double.parseDouble(opponentFouls)
        ) < 1;
    }

    public boolean opponentFoulsBetter() {
        return Double.compare(
                Double.parseDouble(playerFouls),
                Double.parseDouble(opponentFouls)
        ) > 1;
    }
}
