package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.player.Player;

import java.util.List;

/**
 * Created by Brookman Holmes on 3/2/2017.
 */

public interface PlayersListener {
    void updatePlayers(List<Player> players, List<Player> opponents);
}
