package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/7/2015.
 */
public abstract class AbstractEightBallGameTest extends AbstractGameTest {

    @Test
    public void getPlayerColorReturnsPlayerIsSolids() {
        game.playerColor = PlayerColor.SOLIDS;

        assertThat(game.getCurrentPlayerColor(), is(PlayerColor.SOLIDS));
        game.turn = PlayerTurn.OPPONENT;
        assertThat(game.getCurrentPlayerColor(), is(PlayerColor.STRIPES));
    }

    @Test
    public void getPlayerColorReturnsPlayerIsStripes() {
        game.playerColor = PlayerColor.STRIPES;

        assertThat(game.getCurrentPlayerColor(), is(PlayerColor.STRIPES));
        game.turn = PlayerTurn.OPPONENT;
        assertThat(game.getCurrentPlayerColor(), is(PlayerColor.SOLIDS));
    }

    @Test
    public void getPlayerColorReturnsOpen() {
        assertThat(game.getCurrentPlayerColor(), is(PlayerColor.OPEN));
    }

    @Test
    public void setAllowPushReturnsFalse() {
        assertThat(game.setAllowPush(
                turn().madeBalls(1, 2, 3).miss()), is(false));
    }

    @Test
    public void setAllowTurnSkipReturnsFalse() {
        assertThat(game.setAllowTurnSkip(
                turn().madeBalls(1, 2, 3).miss()), is(false));
    }
}
