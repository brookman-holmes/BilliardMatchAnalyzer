package com.brookmanholmes.bma.ui.matchinfo;

import android.support.annotation.Nullable;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class SafetiesBinder extends BindingAdapter {
    public String playerSafetyPct = defaultPct, opponentSafetyPct = defaultPct;

    public int playerSafetiesMade = 0, opponentSafetiesMade = 0;
    public int playerSafeties = 0, opponentSafeties = 0;

    public String playerSafetyFouls = "0", opponentSafetyFouls = "0";

    public String playerSafetyEscapes = "0", opponentSafetyEscapes = "0";

    public String playerSafetyReturns = "0", opponentSafetiesReturns = "0";

    public String playerForcedFouls = "0", opponentForcedFouls = "0";

    SafetiesBinder(String title, boolean expanded, GameType gameType) {
        super(title, expanded, !gameType.isSinglePlayer());
        helpLayout = R.layout.dialog_help_safeties;
    }

    @Override
    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        playerSafetyPct = pctf.format(player.getSafetyPct());
        opponentSafetyPct = pctf.format(opponent.getSafetyPct());

        playerSafetiesMade = player.getSafetySuccesses();
        opponentSafetiesMade = opponent.getSafetySuccesses();
        playerSafeties = player.getSafetyAttempts();
        opponentSafeties = opponent.getSafetyAttempts();

        playerSafetyFouls = player.getSafetyFouls() + "";
        opponentSafetyFouls = opponent.getSafetyFouls() + "";

        playerSafetyEscapes = player.getSafetyEscapes() + "";
        opponentSafetyEscapes = opponent.getSafetyEscapes() + "";

        playerSafetyReturns = player.getSafetyReturns() + "";
        opponentSafetiesReturns = opponent.getSafetyReturns() + "";

        playerForcedFouls = player.getSafetyForcedErrors() + "";
        opponentForcedFouls = opponent.getSafetyForcedErrors() + "";

        notifyChange();
    }

    public int highlightSafeties() {
        return compare(playerSafetyPct, opponentSafetyPct);
    }

    public int highlightFouls() {
        return compare(playerSafetyFouls, opponentSafetyFouls) * -1;
    }

    public int highlightEscapes() {
        return compare(playerSafetyEscapes, opponentSafetyEscapes);
    }

    public int highlightReturns() {
        return compare(playerSafetyReturns, opponentSafetiesReturns);
    }

    public int highlightForcing() {
        return compare(playerForcedFouls, opponentForcedFouls) * -1;
    }
}
