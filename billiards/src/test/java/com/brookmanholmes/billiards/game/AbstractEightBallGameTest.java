package com.brookmanholmes.billiards.game;

import org.junit.Test;

import java.util.Arrays;

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

    @Override int[] getAllBallsOnTable() {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8};
    }

    @Override int[] getBallsOnTableAfterRemoval() {
        return new int[]{2, 3, 4, 5, 6, 8};
    }

    @Override void removeBalls() {
        game.ballsOnTable.removeAll(Arrays.asList(1, 7, 9, 10, 11, 12, 13, 14, 15));
    }
}
