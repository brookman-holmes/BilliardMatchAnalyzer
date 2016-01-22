package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.player.AmericanRotationPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
class AmericanRotationController extends PlayerController<AmericanRotationPlayer> {
    AmericanRotationController(Game game, String playerName, String opponentName) {
        super(game);
        player1 = new AmericanRotationPlayer(playerName);
        player2 = new AmericanRotationPlayer(opponentName);
    }
}
