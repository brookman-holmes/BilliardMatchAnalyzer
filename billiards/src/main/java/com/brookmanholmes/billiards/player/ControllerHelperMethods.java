package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.interfaces.EarlyWins;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreak;

import java.util.Collection;

/**
 * Created by Brookman Holmes on 1/31/2016.
 */
public class ControllerHelperMethods {
    private ControllerHelperMethods() {
    }

    public static <T extends AbstractPlayer & WinsOnBreak> void addWinOnBreak(T player) {
        player.addWinOnBreak();

    }

    public static <T extends AbstractPlayer & EarlyWins> void addEarlyWin(T player) {
        player.addEarlyWin();
    }

    public static <T extends AbstractPlayer> T getPlayerFromList(Collection<T> players, T blankPlayer) {
        for (T item : players) {
            blankPlayer.addPlayerStats(item);
        }

        return blankPlayer;
    }
}