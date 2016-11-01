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

    public boolean showCard = true;

    SafetiesBinder(AbstractPlayer player, AbstractPlayer opponent, String title, boolean expanded, boolean showCard) {
        super(expanded);
        this.title = title;
        helpLayout = R.layout.dialog_help_safeties;

        playerSafetyPct = player.getSafetyPct();
        opponentSafetyPct = opponent.getSafetyPct();

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

        this.showCard = showCard;
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        playerSafetyPct = player.getSafetyPct();
        opponentSafetyPct = opponent.getSafetyPct();

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

    public boolean playerShootingBetter() {
        return Double.compare(
                Double.parseDouble(playerSafetyPct),
                Double.parseDouble(opponentSafetyPct)
        ) > 0;
    }

    public boolean opponentShootingBetter() {
        return Double.compare(
                Double.parseDouble(playerSafetyPct),
                Double.parseDouble(opponentSafetyPct)
        ) < 0;
    }

    public boolean playerFoulsLess() {
        return Integer.parseInt(playerSafetyFouls) < Integer.parseInt(opponentSafetyFouls);
    }

    public boolean opponentFoulsLess() {
        return Integer.parseInt(playerSafetyFouls) > Integer.parseInt(opponentSafetyFouls);
    }

    public boolean playerEscapesMore() {
        return Integer.parseInt(playerSafetyEscapes) > Integer.parseInt(opponentSafetyEscapes);
    }

    public boolean opponentEscapesMore() {
        return Integer.parseInt(playerSafetyEscapes) < Integer.parseInt(opponentSafetyEscapes);
    }

    public boolean playerReturnsMore() {
        return Integer.parseInt(playerSafetyReturns) > Integer.parseInt(opponentSafetiesReturns);
    }

    public boolean opponentReturnsMore() {
        return Integer.parseInt(playerSafetyReturns) < Integer.parseInt(opponentSafetiesReturns);
    }

    public boolean playerForcedMore() {
        return Integer.parseInt(opponentForcedFouls) > Integer.parseInt(playerForcedFouls);
    }

    public boolean opponentForcedMore() {
        return Integer.parseInt(opponentForcedFouls) < Integer.parseInt(playerForcedFouls);
    }
}
