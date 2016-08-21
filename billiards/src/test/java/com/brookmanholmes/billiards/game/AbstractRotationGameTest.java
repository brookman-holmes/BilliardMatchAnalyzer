package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brookman Holmes on 11/6/2015.
 */
public abstract class AbstractRotationGameTest extends AbstractGameTest {
    @Test
    public void setAllowTurnSkipReturnsTrueAfterPushShot() {
        ITurn turn = turn().push();

        assertThat(game.setAllowTurnSkip(turn), is(true));

        game.addTurn(turn);

        assertThat(game.allowTurnSkip, is(true));
    }

    @Test
    public void allowPushIsTrueAfterNewGame() {
        game.startNewGame(turn().breakBalls(4).madeBalls(1, 2, 3, game.GAME_BALL).win());

        assertThat(game.allowPush, is(true));
    }

    @Test
    public void allowPushIsTrueAfterDryBreak() {
        ITurn turn = turn().breakMiss();

        game.addTurn(turn);

        assertThat(game.allowPush, is(true));
    }
}
