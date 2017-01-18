package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;
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

    ShootingBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded) {
        super(expanded, !(player instanceof StraightPoolPlayer));
        update(player, opponent);
        this.title = title;
        helpLayout = R.layout.dialog_help_shooting;
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        playerShootingPct = pctf.format(player.getShootingPct());
        opponentShootingPct = pctf.format(opponent.getShootingPct());

        playerShotsMade = player.getShootingBallsMade();
        opponentShotsMade = opponent.getShootingBallsMade();
        playerShots = player.getShootingAttempts();
        opponentShots = opponent.getShootingAttempts();

        playerAvg = avgf.format(player.getAvgBallsTurn());
        opponentAvg = avgf.format(opponent.getAvgBallsTurn());

        playerFouls = player.getShootingFouls() + "";
        opponentFouls = opponent.getShootingFouls() + "";
        notifyChange();
    }

    public int highlightShooting() {
        return compare(playerShootingPct, opponentShootingPct);
    }

    public int highlightAvg() {
        return compare(playerAvg, opponentAvg);
    }

    public int highlightFouls() {
        return compare(playerFouls, opponentFouls) * -1;
    }
}
