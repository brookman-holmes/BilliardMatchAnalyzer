package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class SafetiesBinder extends BindingAdapter {
    public String playerSafetyPct, opponentSafetyPct;

    public int playerSafetiesMade, opponentSafetiesMade;
    public int playerSafeties, opponentSafeties;

    public String playerSafetyFouls, opponentSafetyFouls;

    public String playerSafetyEscapes, opponentSafetyEscapes;

    public String playerSafetyReturns, opponentSafetiesReturns;

    public String playerForcedFouls, opponentForcedFouls;

    SafetiesBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded, boolean showCard) {
        super(expanded, showCard);
        this.title = title;
        helpLayout = R.layout.dialog_help_safeties;

        update(player, opponent);
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
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
