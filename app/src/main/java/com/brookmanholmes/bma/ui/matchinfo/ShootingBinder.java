package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class ShootingBinder extends BindingAdapter {
    public String playerShootingPct, opponentShootingPct;

    public int playerShotsMade, opponentShotsMade;
    public int playerShots, opponentShots;

    public String playerAvg, opponentAvg;

    public String playerFouls, opponentFouls;

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
        helpLayout = R.layout.dialog_help_shooting;
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

    public boolean playerShootingBetter() {
        return Double.compare(
                Double.parseDouble(playerShootingPct),
                Double.parseDouble(opponentShootingPct)
        ) > 0;
    }

    public boolean opponentShootingBetter() {
        return Double.compare(
                Double.parseDouble(playerShootingPct),
                Double.parseDouble(opponentShootingPct)
        ) < 0;
    }

    public boolean playerAvgBetter() {
        return Double.compare(
                Double.parseDouble(playerAvg),
                Double.parseDouble(opponentAvg)
        ) > 0;
    }

    public boolean opponentAvgBetter() {
        return Double.compare(
                Double.parseDouble(playerAvg),
                Double.parseDouble(opponentAvg)
        ) < 0;
    }

    public boolean playerFoulsBetter() {
        return Double.compare(
                Double.parseDouble(playerFouls),
                Double.parseDouble(opponentFouls)
        ) < 0;
    }

    public boolean opponentFoulsBetter() {
        return Double.compare(
                Double.parseDouble(playerFouls),
                Double.parseDouble(opponentFouls)
        ) > 0;
    }
}
