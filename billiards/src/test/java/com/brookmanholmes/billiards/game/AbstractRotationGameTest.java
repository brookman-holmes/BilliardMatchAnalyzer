package com.brookmanholmes.billiards.game;

import com.brookmanholmes.billiards.turn.ITurn;

import org.junit.Test;

import java.util.Arrays;

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
        game.startNewGame(turn().breakBalls(4).madeBalls(1, 2, 3, game.gameType.getGameBall()).win());

        assertThat(game.allowPush, is(true));
    }

    @Test
    public void allowPushIsTrueAfterDryBreak() {
        ITurn turn = turn().breakMiss();

        game.addTurn(turn);

        assertThat(game.allowPush, is(true));
    }

    @Override int[] getAllBallsOnTable() {
        if (game.gameType == GameType.BCA_NINE_BALL || game.gameType == GameType.APA_NINE_BALL)
            return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        else return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }

    @Override int[] getBallsOnTableAfterRemoval() {
        if (game.gameType == GameType.BCA_NINE_BALL || game.gameType == GameType.APA_NINE_BALL)
            return new int[]{2, 3, 4, 5, 6, 7, 9};
        else return new int[]{2, 3, 4, 5, 6, 7, 8, 10};
    }

    @Override void removeBalls() {
        game.ballsOnTable.removeAll(Arrays.asList(1, (game.gameType.getGameBall() - 1)));
    }
}
