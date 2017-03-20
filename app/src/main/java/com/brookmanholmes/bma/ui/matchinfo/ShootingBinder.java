package com.brookmanholmes.bma.ui.matchinfo;

import android.support.annotation.Nullable;
import android.util.Log;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class ShootingBinder extends BindingAdapter {
    public String playerShootingPct = "0", opponentShootingPct = "0";

    public int playerShotsMade = 0, opponentShotsMade = 0;
    public int playerShots = 0, opponentShots = 0;

    public String playerAvg = defaultAvg, opponentAvg = defaultAvg;

    public String playerFouls = "0", opponentFouls = "0";

    ShootingBinder(String title, boolean expanded, GameType gameType) {
        super(title, expanded, !(gameType.isStraightPool()));
        helpLayout = R.layout.dialog_help_shooting;
    }

    ShootingBinder(String title, boolean expanded, boolean showCard) {
        super(title, expanded, showCard);
    }

    @Override
    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        playerShootingPct = pctf.format(player.getShootingPct());
        opponentShootingPct = pctf.format(opponent.getShootingPct());

        playerShotsMade = player.getShootingBallsMade();
        Log.i(TAG, "update: " + player.getShootingBallsMade());
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
