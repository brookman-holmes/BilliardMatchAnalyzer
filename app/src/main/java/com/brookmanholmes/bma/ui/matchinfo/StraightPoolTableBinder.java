package com.brookmanholmes.bma.ui.matchinfo;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.StraightPoolPlayer;

import java.util.List;

/**
 * Created by Brookman Holmes on 12/7/2016.
 */

public class StraightPoolTableBinder extends BindingAdapter {
    private static final String TAG = "StraightTableBinder";
    public int ballsRemaining;
    public String playerBallsMade, opponentBallsMade;
    PlayerTurn initialTurn = PlayerTurn.PLAYER;

    public StraightPoolTableBinder(AbstractPlayer player, AbstractPlayer opponent, boolean expanded) {
        super(expanded, player instanceof StraightPoolPlayer);
        ballsRemaining = 15 - ((player.getShootingBallsMade() + opponent.getShootingBallsMade()) % 14);
    }

    public void update(AbstractPlayer player, AbstractPlayer opponent) {
        ballsRemaining = 15 - ((player.getShootingBallsMade() + opponent.getShootingBallsMade()) % 14);
        notifyChange();
    }

    private void getBallsMadeThisRack(List<Integer> playerBalls, List<Integer> opponentBalls) {
        // TODO: 12/9/2016 this function is fucking dumb
    }
}
