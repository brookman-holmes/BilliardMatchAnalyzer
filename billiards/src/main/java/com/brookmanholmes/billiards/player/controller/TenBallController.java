package com.brookmanholmes.billiards.player.controller;

import com.brookmanholmes.billiards.game.Game;
import com.brookmanholmes.billiards.player.TenBallPlayer;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class TenBallController extends PlayerController<TenBallPlayer> {
    public TenBallController(Game game, String playerName, String opponentName) {
        super(game);
        player1 = new TenBallPlayer(playerName);
        player2 = new TenBallPlayer(opponentName);
    }
}
