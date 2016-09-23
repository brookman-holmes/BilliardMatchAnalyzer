package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/22/2016.
 */

public class SafetiesBinder extends BindingAdapter {
    String playerSafetyPct, opponentSafetyPct;

    int playerSafetiesMade, opponentSafetiesMade;
    int playerSafeties, opponentSafeties;

    String playerSafetyFouls, opponentSafetyFouls;

    String playerSafetyEscapes, opponentSafetyEscapes;

    String playerSafetyReturns, opponentSafetiesReturns;

    String playerForcedFouls, opponentForcedFouls;

    public SafetiesBinder(AbstractPlayer player, AbstractPlayer opponent, String title) {
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

    public String getPlayerSafetyPct() {
        return playerSafetyPct;
    }

    public String getOpponentSafetyPct() {
        return opponentSafetyPct;
    }

    public int getPlayerSafetiesMade() {
        return playerSafetiesMade;
    }

    public int getOpponentSafetiesMade() {
        return opponentSafetiesMade;
    }

    public int getPlayerSafeties() {
        return playerSafeties;
    }

    public int getOpponentSafeties() {
        return opponentSafeties;
    }

    public String getPlayerSafetyFouls() {
        return playerSafetyFouls;
    }

    public String getOpponentSafetyFouls() {
        return opponentSafetyFouls;
    }

    public String getPlayerSafetyEscapes() {
        return playerSafetyEscapes;
    }

    public String getOpponentSafetyEscapes() {
        return opponentSafetyEscapes;
    }

    public String getPlayerSafetyReturns() {
        return playerSafetyReturns;
    }

    public String getOpponentSafetiesReturns() {
        return opponentSafetiesReturns;
    }

    public String getPlayerForcedFouls() {
        return playerForcedFouls;
    }

    public String getOpponentForcedFouls() {
        return opponentForcedFouls;
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
